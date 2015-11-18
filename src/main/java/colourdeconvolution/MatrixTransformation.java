package colourdeconvolution;
import java.awt.Color;
import java.awt.Font;
import java.awt.image.IndexColorModel;

import ij.IJ;
import ij.ImagePlus;
import ij.ImageStack;
import ij.gui.NewImage;
import ij.gui.Roi;
import ij.process.ImageProcessor;

/**
 * This abstract class contains all the library methods to perform image deconvolution based on some pre-stain or custom vector.
 * E.gg, to perfom an H&E DAB deconvolution, do the following:
 * <code>
 * boolean showMatrix=false; //To not display the Matrix used to do the deconvolution in a popup at the end of the process
 * boolean hideLegend=true; //To not display the deconvolution legend in a popup at the end of the process
 * ImagePlus imp = WindowManager.getCurrentImage();
 * MatrixTransformation mt = new Matrix_HE_DAB(); // Create a new MatrixTransformation corresponding to your stain.
 * //Compute the Deconvolution images and return a Stack array of three 8-bit images.
 * ImageStack[] stacks = mt.compute(showMatrix, hideLegend, imp);
 * Then if you want to display them:
 * new ImagePlus(title+"-(Colour_1)",stack[0]).show();
 * new ImagePlus(title+"-(Colour_2)",stack[1]).show();
 * new ImagePlus(title+"-(Colour_3)",stack[2]).show();
 * </code>
 * 
 * If you want to use your own Matrix :
 * <code>
 * boolean showMatrix=false; //To not display the Matrix used to do the deconvolution in a popup at the end of the process
 * boolean hideLegend=true; //To not display the deconvolution legend in a popup at the end of the process
 * mt = new Matrix_Custom();// Create a new Matrix_Custom
 * //Populate the Transformation Matrix
 * mt.getMODx()[0]=0.650;
 * mt.getMODy()[0]=0.704;
 * mt.getMODz()[0]=0.286;
 * mt.getMODx()[1]=0.072;
 * mt.getMODy()[1]=0.990;
 * mt.getMODz()[1]=0.105;
 * mt.getMODx()[2]=0.268;
 * mt.getMODy()[2]=0.570;
 * mt.getMODz()[2]=0.776; // Create a new MatrixTransformation corresponding to your stain.
 * //Compute the Deconvolution images and return a Stack array of three 8-bit images.
 * ImageStack[] stacks = mt.compute(showMatrix, hideLegend, imp);
 * //Then if you want to display them:
 * new ImagePlus(title+"-(Colour_1)",stack[0]).show();
 * new ImagePlus(title+"-(Colour_2)",stack[1]).show();
 * new ImagePlus(title+"-(Colour_3)",stack[2]).show();
 * </code>
 * If you want to perform it from a ROIs, you need to have 3 ROIs, one for each Colour:
 * boolean showMatrix=false; //To not display the Matrix used to do the deconvolution in a popup at the end of the process
 * boolean hideLegend=true; //To not display the deconvolution legend in a popup at the end of the process
 * // Create a new Custom Matrix Transformation and give 3 ROIs, one for each Colour
 * mt = new Matrix_Custom(rois, imp.getProcessor);// Create a new Matrix_Custom and populate the Transformation Matrix from the ROIs
 * //Compute the Deconvolution images and return a Stack array of three 8-bit images. 
 * ImageStack[] stacks = mt.compute(showMatrix, hideLegend, imp);
 * Then if you want to display them:
 * new ImagePlus(title+"-(Colour_1)",stack[0]).show();
 * new ImagePlus(title+"-(Colour_2)",stack[1]).show();
 * new ImagePlus(title+"-(Colour_3)",stack[2]).show();
 * 
 * @author Benjamin Pavie
 *
 */
public abstract class MatrixTransformation {

  public static final String [] STAIN_ARRAY={"From ROI", "H&E", "H&E 2","H DAB", "Feulgen Light Green", "Giemsa", "FastRed FastBlue DAB", "Methyl Green DAB",
    "H&E DAB", "H AEC","Azan-Mallory","Masson Trichrome","Alcian blue & H","H PAS","Brilliant_Blue","RGB","CMY", "User values"};
  public static String STAINING_FROM_ROI               = STAIN_ARRAY[0];
  public static String STAINING_H_E                    = STAIN_ARRAY[1];
  public static String STAINING_H_E2                   = STAIN_ARRAY[2];
  public static String STAINING_H_DAB                  = STAIN_ARRAY[3];
  public static String STAINING_FEULGEN_LIGHT_GREEN    = STAIN_ARRAY[4];
  public static String STAINING_GIEMSA                 = STAIN_ARRAY[5];
  public static String STAINING_FAST_RED_FAST_BLUE_DAB = STAIN_ARRAY[6];
  public static String STAINING_METHYL_GREEN_DAB       = STAIN_ARRAY[7];
  public static String STAINING_H_E_DAB                = STAIN_ARRAY[8];
  public static String STAINING_H_AEC                  = STAIN_ARRAY[9];
  public static String STAINING_AZAN_MALLORY           = STAIN_ARRAY[10];
  public static String STAINING_MASSON_TRICHROME       = STAIN_ARRAY[11];
  public static String STAINING_ALCIAN_BLUE_H          = STAIN_ARRAY[12];
  public static String STAINING_H_PAS                  = STAIN_ARRAY[13];
  public static String STAINING_BRILLIANT_BLUE         = STAIN_ARRAY[14];
  public static String STAINING_RGB                    = STAIN_ARRAY[15];
  public static String STAINING_CMY                    = STAIN_ARRAY[16];
  public static String STAINING_USER_VALUES            = STAIN_ARRAY[17];
  
  double[] MODx, MODy, MODz;
  String myStain;
  private double [] cosx = new double[3];
  private double [] cosy = new double[3];
  private double [] cosz = new double[3];

  public void init()
  {
    MODx=new double[3];
    MODy=new double[3];
    MODz=new double[3];
  }
  
  public double[] getMODx() {
  return MODx;
  }
  
  public void setMODx(double[] mODx) {
    MODx = mODx;
  }
  
  public double[] getMODy() {
    return MODy;
  }
  
  public void setMODy(double[] mODy) {
    MODy = mODy;
  }
  
  public double[] getMODz() {
    return MODz;
  }
  
  public void setMODz(double[] mODz) {
    MODz = mODz;
  }
  
  /**
   * Compute the Deconvolution images and display them
   * 
   * @param doIshow: Show or not the matrix in a popup
   * @param hideLegend: Hide or not the legend in a popup
   * @param imp : The ImagePlus that will be deconvolved. RGB only.
   */
  public void computeAndShow(boolean doIshow, boolean hideLegend, ImagePlus imp)
  {
    String title = imp.getTitle();
    ImageStack[] stack = compute(doIshow, hideLegend, imp);
    new ImagePlus(title+"-(Colour_1)",stack[0]).show();
    new ImagePlus(title+"-(Colour_2)",stack[1]).show();
    new ImagePlus(title+"-(Colour_3)",stack[2]).show();
  }

  /**
   * Compute the Deconvolution images and return a Stack array of three 8-bit images.
   * If the specimen is stained with a 2 colour scheme (such as H & E) the 3rd image
   * represents the complimentary of the first two colours (i.e. green)
   * @param doIshow: Show or not the matrix in a popup
   * @param hideLegend: Hide or not the legend in a popup
   * @param imp : The ImagePlus that will be deconvolved. RGB only.
   * @return a Stack array of three 8-bit images
   */
  public ImageStack[] compute(boolean doIshow, boolean hideLegend, ImagePlus imp)
  {
    ImageStack stack = imp.getStack();
    int width = stack.getWidth();
    int height = stack.getHeight();
    double [] len = new double[3];
    double [] q = new double[9];
    byte [] rLUT = new byte[256];
    byte [] gLUT = new byte[256];
    byte [] bLUT = new byte[256];
    double leng, A, V, C, log255=Math.log(255.0);
    
    
    // Start
    for (int i=0; i<3; i++){
      // Normalise vector length
      cosx[i]=cosy[i]=cosz[i]=0.0;
      len[i]=Math.sqrt(MODx[i]*MODx[i] + MODy[i]*MODy[i] + MODz[i]*MODz[i]);
      if (len[i] != 0.0){
        cosx[i]= MODx[i]/len[i];
        cosy[i]= MODy[i]/len[i];
        cosz[i]= MODz[i]/len[i];
      }
    }
    
    // Translation Matrix
    if (cosx[1]==0.0){ //2nd colour is unspecified
      if (cosy[1]==0.0){
        if (cosz[1]==0.0){
          cosx[1]=cosz[0];
          cosy[1]=cosx[0];
          cosz[1]=cosy[0];
        }
      }
    }
    
    if (cosx[2]==0.0){ // 3rd colour is unspecified
      if (cosy[2]==0.0){
        if (cosz[2]==0.0){
          if ((cosx[0]*cosx[0] + cosx[1]*cosx[1])> 1){
            if (doIshow)
              IJ.log("Colour_3 has a negative R component.");
            cosx[2]=0.0;
          }
          else
            cosx[2]=Math.sqrt(1.0-(cosx[0]*cosx[0])-(cosx[1]*cosx[1]));

          if ((cosy[0]*cosy[0] + cosy[1]*cosy[1])> 1){
            if (doIshow)
              IJ.log("Colour_3 has a negative G component.");
            cosy[2]=0.0;
          }
          else {
            cosy[2]=Math.sqrt(1.0-(cosy[0]*cosy[0])-(cosy[1]*cosy[1]));
          }

          if ((cosz[0]*cosz[0] + cosz[1]*cosz[1])> 1){
            if (doIshow)
              IJ.log("Colour_3 has a negative B component.");
            cosz[2]=0.0;
          }
          else {
            cosz[2]=Math.sqrt(1.0-(cosz[0]*cosz[0])-(cosz[1]*cosz[1]));
          }
        }
      }
    }

    leng=Math.sqrt(cosx[2]*cosx[2] + cosy[2]*cosy[2] + cosz[2]*cosz[2]);
    cosx[2]= cosx[2]/leng;
    cosy[2]= cosy[2]/leng;
    cosz[2]= cosz[2]/leng;

    for (int i=0; i<3; i++){
      if (cosx[i] == 0.0) cosx[i] = 0.001;
      if (cosy[i] == 0.0) cosy[i] = 0.001;
      if (cosz[i] == 0.0) cosz[i] = 0.001;
    }
    if (!hideLegend) {
      showLegend(myStain);
    }
    if (doIshow){
      showMatrix(myStain);
    }
    
  // Matrix inversion
    A = cosy[1] - cosx[1] * cosy[0] / cosx[0];
    V = cosz[1] - cosx[1] * cosz[0] / cosx[0];
    C = cosz[2] - cosy[2] * V/A + cosx[2] * (V/A * cosy[0] / cosx[0] - cosz[0] / cosx[0]);
    q[2] = (-cosx[2] / cosx[0] - cosx[2] / A * cosx[1] / cosx[0] * cosy[0] / cosx[0] + cosy[2] / A * cosx[1] / cosx[0]) / C;
    q[1] = -q[2] * V / A - cosx[1] / (cosx[0] * A);
    q[0] = 1.0 / cosx[0] - q[1] * cosy[0] / cosx[0] - q[2] * cosz[0] / cosx[0];
    q[5] = (-cosy[2] / A + cosx[2] / A * cosy[0] / cosx[0]) / C;
    q[4] = -q[5] * V / A + 1.0 / A;
    q[3] = -q[4] * cosy[0] / cosx[0] - q[5] * cosz[0] / cosx[0];
    q[8] = 1.0 / C;
    q[7] = -q[8] * V / A;
    q[6] = -q[7] * cosy[0] / cosx[0] - q[8] * cosz[0] / cosx[0];

    // Initialize 3 output colour stacks
    ImageStack[] outputstack = new ImageStack[3];
    for (int i=0; i<3; i++){
      for (int j=0; j<256; j++) { //LUT[1]
        //if (cosx[i] < 0)
        //  rLUT[255-j]=(byte)(255.0 + (double)j * cosx[i]);
        //else
          rLUT[255-j]=(byte)(255.0 - (double)j * cosx[i]);

        //if (cosy[i] < 0)
        //  gLUT[255-j]=(byte)(255.0 + (double)j * cosy[i]);
        //else
          gLUT[255-j]=(byte)(255.0 - (double)j * cosy[i]);

        //if (cosz[i] < 0)
        //  bLUT[255-j]=(byte)(255.0 + (double)j * cosz[i]);
        ///else
          bLUT[255-j]=(byte)(255.0 - (double)j * cosz[i]);
      }
      IndexColorModel cm = new IndexColorModel(8, 256, rLUT, gLUT, bLUT);
      outputstack[i] = new ImageStack(width, height, cm);
    }

    // Translate ------------------
    int imagesize = width * height;
    int modulo = imagesize/60;
    for (int imagenum=1; imagenum<=stack.getSize(); imagenum++) {
      int[] pixels = (int[])stack.getPixels(imagenum);
      String label = stack.getSliceLabel(imagenum);
      byte[][] newpixels = new byte[3][];
      newpixels[0] = new byte[imagesize];
      newpixels[1] = new byte[imagesize];
      newpixels[2] = new byte[imagesize];

      for (int j=0;j<imagesize;j++){
        if (j % modulo == 0)
           IJ.showProgress(j, imagesize);    //show progress bar, quicker than calling it every time.
         // Log transform the RGB data
        int R = (pixels[j] & 0xff0000)>>16;
        int G = (pixels[j] & 0x00ff00)>>8 ;
        int B = (pixels[j] & 0x0000ff);
        double Rlog = -((255.0*Math.log(((double)R+1)/255.0))/log255);
        double Glog = -((255.0*Math.log(((double)G+1)/255.0))/log255);
        double Blog = -((255.0*Math.log(((double)B+1)/255.0))/log255);
        for (int i=0; i<3; i++){
          // Rescale to match original paper values
          double Rscaled = Rlog * q[i*3];
          double Gscaled = Glog * q[i*3+1];
          double Bscaled = Blog * q[i*3+2];
          double output = Math.exp(-((Rscaled + Gscaled + Bscaled) - 255.0) * log255 / 255.0);
          if(output>255) output=255;
          newpixels[i][j]=(byte)(0xff&(int)(Math.floor(output+.5)));
        }
      }
       // Add new values to output images
      outputstack[0].addSlice(label,newpixels[0]);
      outputstack[1].addSlice(label,newpixels[1]);
      outputstack[2].addSlice(label,newpixels[2]);
    }
     IJ.showProgress(1);
     
     return outputstack;    
  }
  
  private void showLegend(String myStain)
  {

    ImagePlus imp0 = NewImage.createRGBImage("Colour Deconvolution", 350, 65, 1, 0);
    ImageProcessor ip0 = imp0.getProcessor();
    ip0.setFont(new Font("Monospaced", Font.BOLD, 11));
    ip0.setAntialiasedText(true);
    ip0.setColor(Color.black);
    ip0.moveTo(10,15);
    ip0.drawString("Colour deconvolution: "+myStain);
    ip0.setFont(new Font("Monospaced", Font.PLAIN, 10));

    for (int i=0; i<3; i++){
      ip0.setRoi(10,18+ i*15, 14, 14); 
      ip0.setColor( 
        (((255 -(int)(255.0* cosx[i])) & 0xff)<<16)+
        (((255 -(int)(255.0* cosy[i])) & 0xff)<<8 )+
        (((255 -(int)(255.0* cosz[i])) & 0xff)    ));
      ip0.fill();
      ip0.setFont(new Font("Monospaced", Font.PLAIN, 10));
      ip0.setAntialiasedText(true);
      ip0.setColor(Color.black);
      ip0.moveTo(27,32+ i*15);
      ip0.drawString("Colour_"+(i+1)+" R:"+(float)cosx[i]+", G:"+(float)cosy[i]+", B:"+(float)cosz[i] );
    }
    imp0.show();
    imp0.updateAndDraw();
  }
  
  private void showMatrix(String myStain)
  {
    IJ.log( myStain +" Vector Matrix ---");
    for (int i=0; i<3; i++){
      IJ.log("Colour["+(i+1)+"]:\n"+
      "  R"+(i+1)+": "+ (float) MODx[i] +"\n"+
      "  G"+(i+1)+": "+ (float) MODy[i] +"\n"+
      "  B"+(i+1)+": "+ (float) MODz[i] +"\n \n");
    }
    
    IJ.log( myStain +" Java code ---");
    IJ.log("\t\tif (myStain.equals(\"New_Stain\")){");
    IJ.log("\t\t// This is the New_Stain");
    for (int i=0; i<3; i++){
      IJ.log("\t\t\tMODx["+i+"]="+ (float) cosx[i] +";\n"+
        "\t\t\tMODy["+i+"]="+ (float) cosy[i] +";\n"+
        "\t\t\tMODz["+i+"]="+ (float) cosz[i] +";\n\n");
    }
    IJ.log("}");
  }
  
}
