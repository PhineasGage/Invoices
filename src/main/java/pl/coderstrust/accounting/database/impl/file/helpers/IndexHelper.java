package pl.coderstrust.accounting.database.impl.file.helpers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import pl.coderstrust.accounting.model.ItemsForDatabase;


public class IndexHelper<T extends ItemsForDatabase> {

  private Class<T> classObject;
  private File currentIdFile;
  private int id;

  public IndexHelper(Class<T> itemForDatabase, String path) {
    this.classObject = itemForDatabase;
    currentIdFile = new File(path + "Id.txt");
    id = generateId();
  }

  private int generateId() {
    try {
      currentIdFile.createNewFile();
    } catch (IOException ioException) {
      throw new RuntimeException("cannot create Idfile", ioException);
    }
    try (Scanner scanner = new Scanner(currentIdFile)) {
      if (scanner.hasNextInt()) {
        return scanner.nextInt();
      } else {
        return 1;
      }
    } catch (FileNotFoundException fileNotFoundexception) {
      throw new RuntimeException("cannot create file", fileNotFoundexception);
    }
  }

  public int getNextId() {
    writeToFile(String.valueOf(id));
    return id++;
  }

  public void clearIdFile() {
    writeToFile("1");
    id = 0;
  }

  private void writeToFile(String string) {
    try (PrintWriter printWriter = new PrintWriter(currentIdFile)) {
      printWriter.print(string);
    } catch (FileNotFoundException exception) {
      throw new RuntimeException("idFile not found", exception);
    }
  }
}