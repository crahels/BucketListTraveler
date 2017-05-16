import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ReadFile {
  private String path;  
  public ReadFile(String path) {
    this.path = path;
  }
  // fungsi yang membaca file eksternal dan memasukannya ke dalam suatu array bertipe String
  public String[] openFile() throws IOException {
    FileReader fr = new FileReader(path);
    BufferedReader textReader = new BufferedReader(fr);
    int numberOfLines = readLines();
    String[] textData = new String[numberOfLines];
    for (int i = 0; i < numberOfLines; i++) {
      textData[i] = textReader.readLine();
    }
    textReader.close();
    return textData;
  }
  // fungsi yang membaca jumlah baris dalam file eksternal dan mengembalikan jumlah baris
  public int readLines() throws IOException {
    FileReader fileToRead = new FileReader(path);
    BufferedReader bf = new BufferedReader(fileToRead);
    int numberOfLines = 0;
    while (bf.readLine() != null) {
      numberOfLines++;
    }
    bf.close();
    return numberOfLines;
  }
}