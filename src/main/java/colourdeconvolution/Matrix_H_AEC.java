package colourdeconvolution;

public class Matrix_H_AEC extends MatrixTransformation{
  
  public Matrix_H_AEC()
  {
    myStain=STAINING_H_AEC;
    init();
    // 3-amino-9-ethylcarbazole
    // Haem matrix
    MODx[0]= 0.650;
    MODy[0]= 0.704;
    MODz[0]= 0.286;
    // AEC matrix
    MODx[1]= 0.2743;
    MODy[1]= 0.6796;
    MODz[1]= 0.6803;
    // Zero matrix
    MODx[2]= 0.0;
    MODy[2]= 0.0;
    MODz[2]= 0.0;
  }
}