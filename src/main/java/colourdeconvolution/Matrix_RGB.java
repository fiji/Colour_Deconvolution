package colourdeconvolution;

public class Matrix_RGB extends MatrixTransformation{
  
  public Matrix_RGB()
  {
    myStain=STAINING_RGB;
    init();
    //R
    MODx[0]= 0.0;
    MODy[0]= 1.0;
    MODz[0]= 1.0;
    //G
    MODx[1]= 1.0;
    MODy[1]= 0.0;
    MODz[1]= 1.0;
    //B
    MODx[2]= 1.0;
    MODy[2]= 1.0;
    MODz[2]= 0.0;
  }
}