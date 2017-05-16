public class Sorting {
  public int[][] things; //int[i][0]
  public String[] nameThings;
  public int pright = -1;
  public int pleft = -1;
  
  Sorting(int[][] things, String[] nameThings, int start, int end) {
    this.things = things;
    this.nameThings = nameThings;
    QuickSort(start, end);
  }
  
  public void QuickSort(int i, int j) {
    if (i < j) { /* ukuran T > 1 */
      Divide(i,j); /* partisi menjadi T[i..k] dan T[l..j] */
      QuickSort(i, pright); 
      /* mengurutkan T[i..k] dengan Quick Sort left - pright*/ 
      QuickSort(pleft, j); 
      /* mengurutkan T[l..j] dengan Quick Sort pleft - right*/ 
    }   
  }
  
  public void Divide(int left, int right) {
    int pivot, tempInt;
    String tempString;
    pivot = things[(left+right)/2][1]; 
    /* pivot adalah elemen tengah */
    pleft = left;
    pright = right;
    do
    {
      while (things[pleft][1] > pivot) {
        pleft++;
      }
      /* things[pleft][1] <= pivot */
      while (things[pright][1] < pivot) {
        pright--;
      }
      /* things[pright][1] >= pivot */
      if (pleft <= pright) {
      /* menukar things[pleft][1] dengan things[pright][1] */
        for (int i = 0; i < 3; i++) {
          tempInt = things[pleft][i];
          things[pleft][i] = things[pright][i];
          things[pright][i] = tempInt;
        }
        tempString = nameThings[pleft];
        nameThings[pleft] = nameThings[pright];
        nameThings[pright] = tempString;
        /* menentukan awal pemindaian berikutnya */
        pleft++;
        pright--;
      }
    }
    while (pleft <= pright);
  }
  
  
}