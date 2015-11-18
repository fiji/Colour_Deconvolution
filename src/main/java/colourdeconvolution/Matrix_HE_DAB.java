package colourdeconvolution;

public class Matrix_HE_DAB extends MatrixTransformation{
  
  public Matrix_HE_DAB()
  {
    myStain=STAINING_H_E_DAB;
    init();
    // Haem matrix
    MODx[0]= 0.650;
    MODy[0]= 0.704;
    MODz[0]= 0.286;
    // Eos matrix
    MODx[1]= 0.072;
    MODy[1]= 0.990;
    MODz[1]= 0.105;
    // DAB matrix
    MODx[2]= 0.268;
    MODy[2]= 0.570;
    MODz[2]= 0.776;
  }
}