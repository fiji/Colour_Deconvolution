package colourdeconvolution;

public class Matrix_Giemsa extends MatrixTransformation{
  
  public Matrix_Giemsa()
  {
    myStain=STAINING_GIEMSA;
    init();
    // GL  Methylene Blue and Eosin
    MODx[0]= 0.834750233;
    MODy[0]= 0.513556283;
    MODz[0]= 0.196330403;
    // GL Eos matrix
    MODx[1]= 0.092789;
    MODy[1]= 0.954111;
    MODz[1]= 0.283111;
    // Zero matrix
    MODx[2]= 0.0;
    MODy[2]= 0.0;
    MODz[2]= 0.0;
  }
}