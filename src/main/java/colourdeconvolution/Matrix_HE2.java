package colourdeconvolution;

public class Matrix_HE2  extends MatrixTransformation{
  
  public Matrix_HE2()
  {
    myStain=STAINING_H_E2;
    init();
    // GL Haem matrix
    MODx[0]= 0.49015734;
    MODy[0]= 0.76897085;
    MODz[0]= 0.41040173;
    // GL Eos matrix
    MODx[1]= 0.04615336;
    MODy[1]= 0.8420684;
    MODz[1]= 0.5373925;
    // Zero matrix
    MODx[2]= 0.0;
    MODy[2]= 0.0;
    MODz[2]= 0.0;
  }
}