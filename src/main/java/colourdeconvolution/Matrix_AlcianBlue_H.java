package colourdeconvolution;

public class Matrix_AlcianBlue_H extends MatrixTransformation{
  
  public Matrix_AlcianBlue_H()
  {
    myStain=STAINING_ALCIAN_BLUE_H;
    init();
    // GL Alcian Blue matrix
    MODx[0]= 0.874622;
    MODy[0]= 0.457711;
    MODz[0]= 0.158256;
    // GL Haematox after PAS matrix
    MODx[1]= 0.552556;
    MODy[1]= 0.7544;
    MODz[1]= 0.353744;
    // Zero matrix
    MODx[2]= 0.0;
    MODy[2]= 0.0;
    MODz[2]= 0.0;
  }
}