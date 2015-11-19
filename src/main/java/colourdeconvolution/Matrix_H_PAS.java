package colourdeconvolution;

public class Matrix_H_PAS extends MatrixTransformation{
  
  public Matrix_H_PAS()
  {
    myStain=STAINING_H_PAS;
    init();
    // GL Haem matrix
    MODx[0]= 0.644211; //0.650;
    MODy[0]= 0.716556; //0.704;
    MODz[0]= 0.266844; //0.286;
    // GL PAS matrix
    MODx[1]= 0.175411;
    MODy[1]= 0.972178;
    MODz[1]= 0.154589;
    // Zero matrix
    MODx[2]= 0.0;
    MODy[2]= 0.0;
    MODz[2]= 0.0;
  }
}