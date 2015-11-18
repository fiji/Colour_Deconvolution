package colourdeconvolution;

public class Matrix_HE extends MatrixTransformation{
  
  public Matrix_HE()
  {
    init();
    myStain=STAINING_H_E;
    MODx[0]= 0.644211; //0.650;
    MODy[0]= 0.716556; //0.704;
    MODz[0]= 0.266844; //0.286;
    // GL Eos matrix
    MODx[1]= 0.092789; //0.072;
    MODy[1]= 0.954111; //0.990;
    MODz[1]= 0.283111; //0.105;
    // Zero matrix
    MODx[2]= 0.0;
    MODy[2]= 0.0;
    MODz[2]= 0.0;
  }
}
