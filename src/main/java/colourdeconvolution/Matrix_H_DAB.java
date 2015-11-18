package colourdeconvolution;

public class Matrix_H_DAB extends MatrixTransformation{
  
  public Matrix_H_DAB()
  {
    myStain=STAINING_H_DAB;
    init();
    // 3,3-diamino-benzidine tetrahydrochloride
    // Haem matrix
    MODx[0]= 0.650;
    MODy[0]= 0.704;
    MODz[0]= 0.286;
    // DAB matrix
    MODx[1]= 0.268;
    MODy[1]= 0.570;
    MODz[1]= 0.776;
    // Zero matrix
    MODx[2]= 0.0;
    MODy[2]= 0.0;
    MODz[2]= 0.0;
  }
}