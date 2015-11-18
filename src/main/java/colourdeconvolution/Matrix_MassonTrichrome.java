package colourdeconvolution;

public class Matrix_MassonTrichrome extends MatrixTransformation{
  
  public Matrix_MassonTrichrome()
  {
    myStain=STAINING_MASSON_TRICHROME;
    init();
    // GL Methyl blue
    MODx[0]=0.7995107;
    MODy[0]=0.5913521;
    MODz[0]=0.10528667;
    // GL Ponceau Fuchsin has 2 hues, really this is only approximate
    MODx[1]=0.09997159;
    MODy[1]=0.73738605;
    MODz[1]=0.6680326;
    // Zero matrix
    MODx[2]= 0.0;
    MODy[2]= 0.0;
    MODz[2]= 0.0;
    // GL Iron Haematoxylin, but this does not seem to work well because it gets confused with the other 2 components
//    MODx[2]=0.6588232;
//    MODy[2]=0.66414213;
//    MODz[2]=0.3533655;
  }
}