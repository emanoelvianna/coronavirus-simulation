package com.coronavirus.core.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ReadFiles {

  public static void main(String[] args) {
    try {
      String line = null;
      BufferedWriter writer = new BufferedWriter(new FileWriter(new File("output/d_dem_n.txt")));
      FileReader fileReader = new FileReader(new File("<PATH FILE ASCGRID>"));
      BufferedReader reader = new BufferedReader(fileReader);
      // skip the next lines as they contain irrelevant metadata
      for (int i = 0; i < 6; i++) {
        writer.write(reader.readLine().toString());
        writer.newLine();
      }
      while ((line = reader.readLine()) != null) {
        String[] tokens = line.split("\\s+");
        for (int i = 0; i < 60; i++) {
          writer.write(tokens[i] + " ");
        }
        writer.newLine();
      }
      fileReader.close();
      reader.close();
      writer.flush();
      writer.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
