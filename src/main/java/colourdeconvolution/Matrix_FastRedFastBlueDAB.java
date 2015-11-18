package colourdeconvolution;

public class Matrix_FastRedFastBlueDAB extends MatrixTransformation{
  
  public Matrix_FastRedFastBlueDAB()
  {
    myStain=STAINING_FAST_RED_FAST_BLUE_DAB;
    init();
    // Fast red
    MODx[0]= 0.21393921;
    MODy[0]= 0.85112669;
    MODz[0]= 0.47794022;
    // Fast blue
    MODx[1]= 0.74890292;
    MODy[1]= 0.60624161;
    MODz[1]= 0.26731082;
    // DAB
    MODx[2]= 0.268;
    MODy[2]= 0.570;
    MODz[2]= 0.776;
  }
}