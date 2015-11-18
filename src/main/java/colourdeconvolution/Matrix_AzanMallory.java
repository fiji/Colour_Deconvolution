package colourdeconvolution;

public class Matrix_AzanMallory extends MatrixTransformation{
  
  public Matrix_AzanMallory()
  {
    myStain=STAINING_AZAN_MALLORY;
    init();
    //Azocarmine and Aniline Blue (AZAN)
    // GL Blue matrix Anilline Blue
    MODx[0]= .853033;
    MODy[0]= .508733;
    MODz[0]= .112656;
    // GL Red matrix Azocarmine
    MODx[1]=0.09289875;
    MODy[1]=0.8662008;
    MODz[1]=0.49098468;
    //GL  Orange matrix Orange-G
    MODx[2]=0.10732849;
    MODy[2]=0.36765403;
    MODz[2]=0.9237484;
  }
}