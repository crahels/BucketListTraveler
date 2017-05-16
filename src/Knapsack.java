import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;

public class Knapsack {
  public String[] str;
  public String[] nameThings;
  public int[][] things;
  public int[][] thingsProcess;
  public int[][] optimumPrev;
  public int[][] optimumNext;
  public int[][] tableSolution;
  public int[][] ever;
  public int neffEver = -1;
  public int length;
  public int weightMax;
  public Scanner src;
  public boolean same;
  public int iterator;
  
  public JFrame frame;
  public JPanel panel;
  public JLabel[] arrLabel;
  public JTextField[] arrTextField;
  public JLabel judul1;
  public JLabel judul2;
  public JLabel weight;
  public JTextField weightField;
  public JLabel[] arrSolution;
  
  //public KnapsackView view; 
  public Knapsack(String fileName) {
    readExternalFile(fileName);
    initialize();
  }
  
  // prosedur untuk membaca file
  public void readExternalFile(String fileName) {
    try {
      ReadFile file = new ReadFile(fileName);
      str = file.openFile();
      length = str.length;
    } catch (IOException e) {
      System.out.println(e.getMessage());
    }  
  }
  
  //prosedur untuk menginisialisasi nilai variabel
  public void initialize() {
    frame = new JFrame("Traveler's Bucket List");
    
    panel = new JPanel();
    panel.setBackground(new Color(255, 218, 185));
    panel.setPreferredSize(new Dimension(1000,1000));
    panel.setLayout(null);
    
    judul1 = new JLabel("Traveler's Bucket List");
    judul1.setFont(new Font("NanumGothic", Font.BOLD, 40));
    judul1.setBounds(300, -60, 500, 200);
    panel.add(judul1);
    
    judul2 = new JLabel("Fill the items' priority. Choose 0 - 5.");
    judul2.setFont(new Font("L M Mono Prop10", Font.PLAIN, 25));
    judul2.setBounds(280, 70, 500, 20);
    
    weight = new JLabel("Maximum weight (g):");
    weight.setFont(new Font("NanumGothic", Font.PLAIN, 20));
    weight.setBounds(50, 120, 300, 30);
    panel.add(weight);
    
    weightField = new JTextField();
    weightField.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
    weightField.setFont(new Font("NanumGothic", Font.PLAIN, 20));
    weightField.setBounds(400, 120, 200, 30);
    panel.add(weightField);
    
    JButton submit = new JButton("Submit");
    submit.setFont(new Font("URW Gothic L", Font.PLAIN, 15));
    submit.setBackground(new Color(175, 238, 238));
    submit.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
    submit.setBounds(400, 200, 150, 40);
    submit.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        weightMax = Integer.parseInt(weightField.getText());
        tableSolution = new int[weightMax + 1][2];
        optimumPrev = new int[weightMax + 1][length + 11];
        optimumNext = new int[weightMax + 1][length + 11];
        for (int i = 0; i < weightMax + 1; i++) {
          for (int j = 0; j < length + 1; j++) {
            optimumPrev[i][j] = 0;
          }
        }
        
        ever = new int [length + 11][length + 11];
        
        things = new int [length + 10][3];
        //things[i][0]: weight
        //things[i][1]: profit
        //things[i][2]: chosen or not
        thingsProcess = new int [length + 10][3];
        //thingsProcess[i][2]: indeks asal
        Scanner sc;
        nameThings = new String [length + 10];
        for (int i = 0; i < length; i++) {
          sc = new Scanner(str[i]);
          nameThings[i] = sc.next();
          things[i][0] = sc.nextInt();
          sc.close();
        }
        panel.removeAll();
        frame.revalidate();
        frame.repaint();
        userInteraction();
      }
    });
    panel.add(submit);
    
    frame.getContentPane().add(panel);
    frame.pack();
    frame.setVisible(true);
  }
  
  //prosedur untuk print hasil baca file eksternal
  public void print() {
    for (int i = 0; i < length; i++) {
      System.out.print(nameThings[i] + " ");
      for (int j = 0; j < 2; j++) {
        System.out.print(things[i][j] + " ");
      }
      System.out.println();
    }
  }
  
  //prosedur untuk menerima pilihan barang yang akan dibawa oleh user
  public void userInteraction() {
    panel.add(judul1);
    panel.add(judul2);
    
    boolean resize = false;
    int offset = 130;
    int offsetX1 = 40;
    int offsetX2 = 300;
    arrLabel = new JLabel[length];
    arrTextField = new JTextField[length];
    for (int i = 0; i < length; i++) {
      arrLabel[i] = new JLabel(nameThings[i] + ":");
      arrLabel[i].setFont(new Font("NanumGothic", Font.PLAIN, 20));
      arrLabel[i].setBounds(offsetX1, offset, 200, 30);
      panel.add(arrLabel[i]);
      
      arrTextField[i] = new JTextField();
      arrTextField[i].setFont(new Font("NanumGothic", Font.PLAIN, 20));
      arrTextField[i].setBounds(offsetX2, offset, 200, 30);
      panel.add(arrTextField[i]);
      offset += 40;
      if (offset >= 700) {
        resize = true;
        offset = 130;
        offsetX1 = 520;
        offsetX2 = 750;
      }
    }
    if (resize) {
      offset = 730;
    }
    JLabel addLabel = new JLabel("Additional items:"); 
    addLabel.setFont(new Font("NanumGothic", Font.PLAIN, 20));
    addLabel.setBounds(40, offset, 200, 30);
    panel.add(addLabel);
    
    JLabel addLabel2 = new JLabel("<name,weight(g),priority>"); 
    addLabel2.setFont(new Font("NanumGothic", Font.PLAIN, 20));
    addLabel2.setBounds(40, offset + 40, 300, 30);
    panel.add(addLabel2);
    
    JTextArea addItem = new JTextArea();
    addItem.setFont(new Font("NanumGothic", Font.PLAIN, 20));
    addItem.setBounds(300, offset, 200, 100);
    
    JScrollPane sp = new JScrollPane(addItem);
    sp.setBounds(300, offset, 200, 100);
    offset += 110;
    panel.add(sp);
    
    JButton submit = new JButton("Submit");
    submit.setFont(new Font("URW Gothic L", Font.PLAIN, 15));
    submit.setBackground(new Color(175, 238, 238));
    submit.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
    submit.setBounds(400, offset + 30, 150, 40);
    submit.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        for (int i = 0; i < length; i++) {
          things[i][1] = Integer.parseInt(arrTextField[i].getText());
          if (things[i][1] == 0) {
            things[i][2] = 0;
          }
          else {
            things[i][2] = 1;
          } 
        }
        
        BufferedReader reader = new BufferedReader(new StringReader(addItem.getText()));
        int count = 0;
        try {
          while (reader.readLine() != null) {
            count++;
          }
        } catch (IOException e1) {
          e1.printStackTrace();
        }
        
        reader = new BufferedReader(new StringReader(addItem.getText()));
        Scanner sc;
        try {
          for (int i = 0; i < count; i++) {
            sc = new Scanner(reader.readLine());
            sc.useDelimiter(",");
            nameThings[length + i] = sc.next();
            things[length + i][0] = sc.nextInt();
            things[length + i][1] = sc.nextInt();
            if (things[length + i][1] == 0) {
              things[length + i][2] = 0;
            }
            else {
              things[length + i][2] = 1;
            } 
          }
        } catch (IOException e1) {
          e1.printStackTrace();
        }
        length += count;
        
        //sorting berdasarkan prioritas dari yang terbesar
        Sorting s = new Sorting(things, nameThings, 0, length - 1);
        System.out.println("Items");
        for (int i = 0; i < length; i++) {
          System.out.println(nameThings[i] + " priority: " + things[i][1] + " weight: " + things[i][0]);
        }
        System.out.println();
        
        int inc = -1;
        for (int i = 0; i < length; i++) {
          if (things[i][2] == 1) {
            inc++;
            for (int j = 0; j < 2; j++) {
              thingsProcess[inc][j] = things[i][j];
            }
            thingsProcess[inc][2] = i;
          }
        }
        length = inc + 1;
        arrSolution = new JLabel[length];
        
        panel.removeAll();
        frame.revalidate();
        frame.repaint();
        calculation();
      }
    });
    panel.add(submit);
    frame.revalidate();
    frame.repaint();
  }
  
  public boolean isEver(int[] check) {
    boolean everBool = false;
    int i = 0;
    int j;
    while ((i <= neffEver) && (!everBool)) {
      everBool = true;
      j = 1;
      while ((j < length + 1) && (everBool)) {
        if (ever[i][j] != check[j]) {
          everBool = false;
        }
        else {
          j++;
        }
      }
      if (!everBool) {
        i++;
      }
    }
    if (!everBool) {
      neffEver++;
      for (int k = 1; k < length + 1; k++) {
        ever[neffEver][k] = check[k];
      }
    }
    return everBool;
  }
  
  //prosedur untuk membangkitkan tabel tahapan-tahapan dari persoalan
  public void calculation() {
    //System.out.println("Solution Table:");
    for (int i = 0; i < length; i++) {
      for (int j = 0; j < weightMax + 1; j++) {
        tableSolution[j][0] = optimumPrev[j][0];
        int temp = j - thingsProcess[i][0]; 
        if (temp < 0) {
          tableSolution[j][1] = -1;
        }
        else {
          tableSolution[j][1] = thingsProcess[i][1] + optimumPrev[temp][0];
        }
        
        int indexMax = maximum(tableSolution[j][0], tableSolution[j][1]);
        optimumNext[j][0] = tableSolution[j][indexMax];
        if (indexMax == 0) {
          for (int k = 1; k < length + 1; k++) {
            optimumNext[j][k] = optimumPrev[j][k];
          }
        }
        else {
          for (int k = 1; k < length + 1; k++) {
            if (k == i + 1) {
              optimumNext[j][k] = 1;
            }
            else {
              optimumNext[j][k] = optimumPrev[temp][k];
            }
          }
        }
      }
      
      printTable();
      for (int j = 0; j < weightMax + 1; j++) {
        for (int k = 0; k < length + 1; k++) {
          optimumPrev[j][k] = optimumNext[j][k];
        }
      }
    }
    conclusion();
  }

  //prosedur untuk mencetak setiap tahap pada knapsack dalam bentuk tabel solusi
  public void printTable() {
    for (int i = 0; i < weightMax + 1; i++) {
      System.out.print(i + "\t");
      for (int j = 0; j < 2; j++) {
        System.out.print(tableSolution[i][j] + "\t");
      }
      for (int j = 0; j < length + 1; j++) {
        System.out.print(optimumNext[i][j] + "\t");
      }
      System.out.println();
    }
    System.out.println();
  }
  
  //prosedur untuk menyimpulkan barang-barang yang harus dipilih
  public void conclusion() {
    panel.add(judul1);
    int max = optimumNext[weightMax][0];
    same = true;
    int i = weightMax;
    int offset = 120;
    if (optimumNext[i][0] == max) {
      if (!isEver(optimumNext[i])) {
        JLabel totalPrio = new JLabel("Maximum total priority: " + optimumNext[i][0]);
        totalPrio.setFont(new Font("NanumGothic", Font.BOLD, 25));
        totalPrio.setBounds(50, offset, 400, 30);
        offset += 40;
        panel.add(totalPrio);
        
        JLabel bring = new JLabel("Things you should bring:");
        bring.setFont(new Font("NanumGothic", Font.BOLD, 25));
        bring.setBounds(50, offset, 300, 30);
        offset += 40;
        panel.add(bring);
        
        int inc = 0;
        for (int j = 1; j < length + 1; j++) {
          if (optimumNext[i][j] == 1) {
            arrSolution[inc] = new JLabel(++inc + ". " + nameThings[thingsProcess[j - 1][2]] + ": " + thingsProcess[j - 1][0] + " g");
            arrSolution[inc - 1].setFont(new Font("NanumGothic", Font.PLAIN, 25));
            arrSolution[inc - 1].setBounds(50, offset, 300, 30);
            offset += 40;
            panel.add(arrSolution[inc - 1]);
          }
        }
      }
      i--;
    }
    
    JLabel message = new JLabel("There is no more option.");
    message.setFont(new Font("NanumGothic", Font.BOLD, 25));
    message.setBounds(50, 100, 400, 30);
    
    iterator = i;
    JButton next = new JButton("Next Option");
    next.setFont(new Font("URW Gothic L", Font.PLAIN, 15));
    next.setBackground(new Color(175, 238, 238));
    next.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
    next.setBounds(400, offset, 150, 40);
    next.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if ((iterator >= 0) && (same)) {
          panel.removeAll();
          frame.revalidate();
          frame.repaint();
          panel.add(judul1);
          int offs = 120;
          if (optimumNext[iterator][0] == max) {
            while ((iterator >= 0) && (optimumNext[iterator][0] == max) && isEver(optimumNext[iterator])) {
              iterator--;
            }
            if ((iterator >= 0) && (optimumNext[iterator][0] == max)) {
              JLabel totalPrio = new JLabel("Maximum total priority: " + optimumNext[iterator][0]);
              totalPrio.setFont(new Font("NanumGothic", Font.BOLD, 25));
              totalPrio.setBounds(50, offs, 400, 30);
              offs += 40;
              panel.add(totalPrio);
              
              JLabel bring = new JLabel("Things you should bring:");
              bring.setFont(new Font("NanumGothic", Font.BOLD, 25));
              bring.setBounds(50, offs, 300, 30);
              offs += 40;
              panel.add(bring);
              
              int inc = 0;
              for (int j = 1; j < length + 1; j++) {
                if (optimumNext[iterator][j] == 1) {
                  arrSolution[inc] = new JLabel(++inc + ". " + nameThings[thingsProcess[j - 1][2]] + ": " + thingsProcess[j - 1][0] + " g");
                  arrSolution[inc - 1].setFont(new Font("NanumGothic", Font.PLAIN, 25));
                  arrSolution[inc - 1].setBounds(50, offs, 300, 30);
                  offs += 40;
                  panel.add(arrSolution[inc - 1]);
                }
              }
              next.setBounds(400, offs, 150, 40);
              panel.add(next);
              iterator--;
            }
            else {
              same = false;
              panel.add(message);
            }
          }
          else {
            same = false;
            panel.add(message);
          }
        }
      }
    });
    panel.add(next);
  }
  
  //prosedur untuk mencari nilai maksimum
  public int maximum(int a, int b) {
    if (a > b) {
      return 0;
    }
    else {
      return 1;
    }
  }
  
  //prosedur untuk mengembalikan scanner
  public Scanner getScanner() {
    return src;
  }
  
  //main
  public static void main (String[] args) {
    Knapsack k = new Knapsack("src/input.txt");
  }
}