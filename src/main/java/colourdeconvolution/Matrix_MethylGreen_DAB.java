package colourdeconvolution;

public class Matrix_MethylGreen_DAB  extends MatrixTransformation{
  
  public Matrix_MethylGreen_DAB()
  {
    myStain=STAINING_METHYL_GREEN_DAB;
    init();
    // MG matrix (GL)
    MODx[0]= 0.98003;
    MODy[0]= 0.144316;
    MODz[0]= 0.133146;
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