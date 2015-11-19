package colourdeconvolution;
public class Matrix_FeulgenLightGreen extends MatrixTransformation{
  
  public Matrix_FeulgenLightGreen()
  {
    myStain=STAINING_FEULGEN_LIGHT_GREEN;
    init();
    //GL Feulgen & light green
    //Feulgen
    MODx[0]= 0.46420921;
    MODy[0]= 0.83008335;
    MODz[0]= 0.30827187;
    // light green
    MODx[1]= 0.94705542;
    MODy[1]= 0.25373821;
    MODz[1]= 0.19650764;
    // Zero matrix
    MODx[2]= 0.0; // 0.0010000
    MODy[2]= 0.0; // 0.47027777
    MODz[2]= 0.0; //0.88235928
  }
}