package colourdeconvolution;

public class Matrix_CMY extends MatrixTransformation{
  
  public Matrix_CMY()
  {
    myStain=STAINING_CMY;
    init();
    //C
    MODx[0]= 1.0;
    MODy[0]= 0.0;
    MODz[0]= 0.0;
    //M
    MODx[1]= 0.0;
    MODy[1]= 1.0;
    MODz[1]= 0.0;
    //Y
    MODx[2]= 0.0;
    MODy[2]= 0.0;
    MODz[2]= 1.0;
  }
}