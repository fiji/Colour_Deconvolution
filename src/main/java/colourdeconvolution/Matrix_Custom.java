package colourdeconvolution;

import java.awt.Rectangle;

import ij.gui.PolygonRoi;
import ij.gui.Roi;
import ij.process.ImageProcessor;

public class Matrix_Custom extends MatrixTransformation{
  
  public Matrix_Custom()
  {
    myStain=STAINING_USER_VALUES;
    init();
  }
  
  public Matrix_Custom(Roi[] rois, ImageProcessor ip)
  {
    this();
    
    int p;
    double log255=Math.log(255.0);
    double [] rgbOD = new double[3];
    
    rgbOD[0]=0;
    rgbOD[1]=0;
    rgbOD[2]=0;
    
    for (int c=0; c<3; c++)
    {
      Roi roi = rois[c];
      if (roi instanceof PolygonRoi)
      {
        PolygonRoi polygon = (PolygonRoi)roi;
        Rectangle bounds = roi.getBounds();
        int w = roi.getBounds().width;
        int h = roi.getBounds().height;
        int n = polygon.getNCoordinates();
        int[] x = polygon.getXCoordinates();
        int[] y = polygon.getYCoordinates();

        for (int i = 0; i < n; i++)
        {
          p=ip.getPixel(bounds.x + x[i],bounds.y + y[i]);
          rgbOD[0] = rgbOD[0]+ (-((255.0*Math.log(((double)((p & 0xff0000)>>16) +1)/255.0))/log255));
          rgbOD[1] = rgbOD[1]+ (-((255.0*Math.log(((double)((p & 0x00ff00)>> 8) +1)/255.0))/log255));
          rgbOD[2] = rgbOD[2]+ (-((255.0*Math.log(((double)((p & 0x0000ff))     +1)/255.0))/log255));
        }
        rgbOD[0] = rgbOD[0] / (w*h);
        rgbOD[1] = rgbOD[1] / (w*h);
        rgbOD[2] = rgbOD[2] / (w*h);

      
        MODx[c]=rgbOD[0];
        MODy[c]=rgbOD[1];
        MODz[c]=rgbOD[2];
      }
    }
  }

}