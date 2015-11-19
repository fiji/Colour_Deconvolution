package colourdeconvolution;

import ij.IJ;
import ij.ImageJ;
import ij.ImagePlus;
import ij.WindowManager;
import ij.gui.GenericDialog;
import ij.gui.ImageCanvas;
import ij.gui.ImageWindow;
import ij.plugin.PlugIn;
import ij.process.ImageProcessor;

import java.awt.Point;

public class Colour_Deconvolution implements PlugIn {

// G.Landini at bham ac uk
// 30/Mar/2004 released
// 03/Apr/2004 resolved ROI exiting
// 07/Apr/2004 added Methyl Green DAB vectors
// 08/Jul/2004 shortened the code
// 01/Aug/2005 added fast red/blue/DAB vectors
// 02/Nov/2005 changed code to work with image stacks (DLC - dchao at fhcrc org)
// 02/Nov/2005 changed field names so user-defined colours can be set within  macros (DLC - dchao at fhcrc org)
// 04/Feb/2007 1.3 disable popup menu when right clicking
// 23/May/2009 added Feulgen-light green vectors
//14/Apr/2010 v 1.4 added Giemsa vector (Methylene blue & eosin) 
//      the images are now names "title"-(Colour_1) etc so there are not clash of names when using [ ]
//      the log window now prints the java code of the translation matrix to include new vectors in the plugin.
//      added "Hide legend" option
// 22/Jun/2010 v 1.5 added Masson Trichrome vector (Methyl blue & Ponceau Fuchsin only (this does not have Iron Haematoxylin vector!)
//      fixed bug: check for 0 components before hiding legend (otherwise there was no image shown if legent hidden) 
//
// 26/Mar/2011 v.1.6 added Brilliant_Blue stain for intraoral plaque images
//
// 03/Aug/2011 v1.7 added progress bar (thanks to Oskari Jaaskelainen),
//      added warning about immunostains.
//
// B.Pavie benjamin.pavie@cme.vib-kuleuven.be 
// 17/Oct/2015 v 1.8 Refactorized the code into multiple classes and made some public method to generate the result
//                   ImageStacks without displaying them so it can be called within scripts
//
// This plugin implements stain separation using the colour deconvolution
// method described in:
//
//     Ruifrok AC, Johnston DA. Quantification of histochemical
//     staining by color deconvolution. Analytical & Quantitative
//     Cytology & Histology 2001; 23: 291-299.
//
// The code is based on "Color separation-30", a macro for NIH Image kindly provided
// by A.C. Ruifrok. Thanks Arnout!
//
// The plugin assumes images generated by color subtraction (i.e. light-absorbing dyes
// such as those used in bright field histology or ink on printed paper) but the dyes
// should not be neutral grey.
//
// I strongly suggest to read the paper reference above to understand how to determine
// new vectors and how the whole procedure works.
//
// The plugin works correctly when the background is neutral (white to light grey), 
// so background subtraction and colour correction must be applied to the images before 
// processing.
//
// The plugin provides a number of "built in" stain vectors some of which were determined
// experimentally in our lab (marked GL), but you may have to determine your own vectors to
// provide a more accurate stain separation, depending on the stains and methods you use.
// Ideally, vector determination should be done on slides stained with only one colour
// at a time (using the "From ROI" interactive option).
//
// The plugin takes an RGB image and returns three 8-bit images. If the specimen is
// stained with a 2 colour scheme (such as H & E) the 3rd image represents the
// complimentary of the first two colours (i.e. green).
//
// Please be *very* careful about how to interpret the results of colour deconvolution
// when analysing histological images.
// Most staining methods are not stochiometric and so optical density of the chromogen
// may not correlate well with the *quantity* of the reactants.
// This means that optical density of the colour may not be a good indicator of
// the amount of material stained.
//
// Read the paper!
//  
  
  public void run(String arg) {
    ImagePlus imp = WindowManager.getCurrentImage();
    if (imp==null){
      IJ.error("No image!");
      return;
    }
    if (imp.getBitDepth()!=24){
      IJ.error("RGB image needed.");
      return;
    }

    GenericDialog gd = new GenericDialog("Colour Deconvolution 1.7", IJ.getInstance());
    gd.addMessage("Warning: This plugin is not suitable to quantify\n the intensity of immunostained slides because\n immunostains are not stoichiometric.");
    gd.addChoice("Vectors", MatrixTransformation.STAIN_ARRAY, MatrixTransformation.STAIN_ARRAY[0]);
    gd.addCheckbox("Show matrices",false);
    gd.addCheckbox("Hide legend",false);

    gd.showDialog();
    if (gd.wasCanceled())
      return;
    String myStain = gd.getNextChoice();
    boolean doIshow = gd.getNextBoolean();
    boolean hideLegend = gd.getNextBoolean();
    MatrixTransformation mt = null; 
    
    // stains are defined after this line
    if (myStain.equals(MatrixTransformation.STAINING_H_E))
      mt = new Matrix_HE();
    else if (myStain.equals(MatrixTransformation.STAINING_H_E2))
      mt = new Matrix_HE2();
    else if(myStain.equals(MatrixTransformation.STAINING_H_DAB))
      mt = new Matrix_H_DAB();
    else if(myStain.equals(MatrixTransformation.STAINING_FEULGEN_LIGHT_GREEN))
      mt = new Matrix_FeulgenLightGreen();
    else if(myStain.equals(MatrixTransformation.STAINING_GIEMSA))
      mt = new Matrix_Giemsa();
    else if(myStain.equals(MatrixTransformation.STAINING_FAST_RED_FAST_BLUE_DAB))
      mt = new Matrix_FastRedFastBlueDAB();
    else if(myStain.equals(MatrixTransformation.STAINING_METHYL_GREEN_DAB))
      mt = new Matrix_MethylGreen_DAB();
    else if(myStain.equals(MatrixTransformation.STAINING_H_E_DAB))
      mt = new Matrix_HE_DAB();
    else if(myStain.equals(MatrixTransformation.STAINING_H_AEC))
      mt = new Matrix_H_AEC();
    else if(myStain.equals(MatrixTransformation.STAINING_AZAN_MALLORY))
      mt = new Matrix_AzanMallory();
    else if(myStain.equals(MatrixTransformation.STAINING_MASSON_TRICHROME))
      mt = new Matrix_MassonTrichrome();
    else if(myStain.equals(MatrixTransformation.STAINING_ALCIAN_BLUE_H))
      mt = new Matrix_AlcianBlue_H();
    else if(myStain.equals(MatrixTransformation.STAINING_H_PAS))
      mt = new Matrix_H_PAS();
    else if(myStain.equals(MatrixTransformation.STAINING_BRILLIANT_BLUE))
      mt = new Matrix_BrilliantBlue();
    else if(myStain.equals(MatrixTransformation.STAINING_RGB))
      mt = new Matrix_RGB();
    else if(myStain.equals(MatrixTransformation.STAINING_CMY))
      mt = new Matrix_CMY();

    if (myStain.equals(MatrixTransformation.STAINING_USER_VALUES)){
      GenericDialog gd2 = new GenericDialog("User values", IJ.getInstance());
      gd2.addMessage("Colour[1]");
      gd2.addNumericField("[R1]", 0, 5);
      gd2.addNumericField("[G1]", 0, 5);
      gd2.addNumericField("[B1]", 0, 5);
      gd2.addMessage("Colour[2]");
      gd2.addNumericField("[R2]", 0, 5);
      gd2.addNumericField("[G2]", 0, 5);
      gd2.addNumericField("[B2]", 0, 5);
      gd2.addMessage("Colour[3]");
      gd2.addNumericField("[R3]", 0, 5);
      gd2.addNumericField("[G3]", 0, 5);
      gd2.addNumericField("[B3]", 0, 5);

      gd2.showDialog();
      if (gd2.wasCanceled())
        return;
      mt = new Matrix_Custom();
      mt.getMODx()[0]=gd2.getNextNumber();
      mt.getMODy()[0]=gd2.getNextNumber();
      mt.getMODz()[0]=gd2.getNextNumber();
      mt.getMODx()[1]=gd2.getNextNumber();
      mt.getMODy()[1]=gd2.getNextNumber();
      mt.getMODz()[1]=gd2.getNextNumber();
      mt.getMODx()[2]=gd2.getNextNumber();
      mt.getMODy()[2]=gd2.getNextNumber();
      mt.getMODz()[2]=gd2.getNextNumber();
    }

    if (myStain.equals("From ROI")){
      mt = new Matrix_Custom();
      IJ.runMacro("setOption('DisablePopupMenu', true)");
      double [] rgbOD = new double[3];
      for (int i=0; i<3; i++){
        getmeanRGBODfromROI(i, rgbOD, imp);
        mt.getMODx()[i]=rgbOD[0];
        mt.getMODy()[i]=rgbOD[1];
        mt.getMODz()[i]=rgbOD[2];
      }

      IJ.runMacro("setOption('DisablePopupMenu', false)");

    }
    
    mt.computeAndShow(doIshow, hideLegend, imp);

  }
  

  void getmeanRGBODfromROI(int i, double [] rgbOD, ImagePlus imp){
    //get a ROI and its mean optical density. GL
    int [] xyzf = new int [4]; //[0]=x, [1]=y, [2]=z, [3]=flags
    int x1, y1, x2, y2, h=0, w=0, px=0, py=0, x, y,p;
    double log255=Math.log(255.0);
    ImageProcessor ip = imp.getProcessor();
    int mw = ip.getWidth()-1;
    int mh = ip.getHeight()-1;

    IJ.showMessage("Select ROI for Colour_"+(i+1)+".\n \n(Right-click to end)");
    getCursorLoc( xyzf, imp );
    while ((xyzf[3] & 4) !=0){  //trap until right released
      getCursorLoc( xyzf, imp );
      IJ.wait(20);
    }

    while (((xyzf[3] & 16) == 0) && ((xyzf[3] & 4) ==0)) { //trap until one is pressed
      getCursorLoc( xyzf, imp );
      IJ.wait(20);
    }

    rgbOD[0]=0;
    rgbOD[1]=0;
    rgbOD[2]=0;

    if ((xyzf[3] & 4) == 0){// right was not pressed, but left (ROI) was
      x1=xyzf[0];
      y1=xyzf[1];
      //IJ.write("first point x:" + x1 + "  y:" + y1);
      x2=x1;  y2=y1;
      while ((xyzf[3] & 4) == 0){//until right pressed
        getCursorLoc( xyzf, imp );
        if (xyzf[0]!=x2 || xyzf[1]!=y2) {
          if (xyzf[0]<0) xyzf[0]=0;
          if (xyzf[1]<0) xyzf[1]=0;
          if (xyzf[0]>mw) xyzf[0]=mw;
          if (xyzf[1]>mh) xyzf[1]=mh;
          x2=xyzf[0]; y2=xyzf[1];
          w=x2-x1+1;
          h=y2-y1+1;
          if (x2<x1) {px=x2;  w=(x1-x2)+1;} else px=x1;
          if (y2<y1) {py=y2;  h=(y1-y2)+1;} else py=y1;
          IJ.makeRectangle(px, py, w, h);
        }
        IJ.wait(20);
      }
      while ((xyzf[3] & 16) !=0){  //trap until left released
        getCursorLoc( xyzf, imp );
        IJ.wait(20);
      }

      for (x=px;x<(px+w);x++){
        for(y=py;y<(py+h);y++){
          p=ip.getPixel(x,y);
          // rescale to match original paper values
          rgbOD[0] = rgbOD[0]+ (-((255.0*Math.log(((double)((p & 0xff0000)>>16)+1)/255.0))/log255));
          rgbOD[1] = rgbOD[1]+ (-((255.0*Math.log(((double)((p & 0x00ff00)>> 8) +1)/255.0))/log255));
          rgbOD[2] = rgbOD[2]+ (-((255.0*Math.log(((double)((p & 0x0000ff))        +1)/255.0))/log255));
        }
      }
      rgbOD[0] = rgbOD[0] / (w*h);
      rgbOD[1] = rgbOD[1] / (w*h);
      rgbOD[2] = rgbOD[2] / (w*h);
    }
    IJ.run("Select None");
  }


  void getCursorLoc(int [] xyzf, ImagePlus imp ) {
    ImageWindow win = imp.getWindow();
    ImageCanvas ic = win.getCanvas();
    Point p = ic.getCursorLoc();
    xyzf[0]=p.x;
    xyzf[1]=p.y;
    xyzf[2]=imp.getCurrentSlice()-1;
    xyzf[3]=ic.getModifiers();
  }
  
  public static void main(String[] args) {
    // Set the plugins.dir property to make the plugin appear in the Plugins menu
    Class<?> clazz = Colour_Deconvolution.class;
    String url = clazz.getResource("/" + clazz.getName().replace('.', '/') + ".class").toString();
    String pluginsDir = url.substring(5, url.length() - clazz.getName().length() - 6);
    System.setProperty("plugins.dir", pluginsDir);

    // Start ImageJ
    new ImageJ();

    // Open the Clown sample
    ImagePlus image = IJ.openImage("http://imagej.net/images/clown.jpg");
    image.show();

    // Run the plugin
    IJ.runPlugIn(clazz.getName(), "");
  }

}

