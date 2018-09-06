package pl.coderstrust.accounting.database.impl.file.helpers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class IndexHelper {

  private File currentIdFile;
  private int id;

  public IndexHelper(@Value("${idFilePath}") String path) {
    currentIdFile = new File(path);
    id = generateId();
  }

  private int generateId() {
    if (currentIdFile.exists()) {
      try (Scanner scanner = new Scanner(currentIdFile)) {
        if (scanner.hasNextInt()) {
          id = scanner.nextInt();
        }
      } catch (FileNotFoundException exception) {
        throw new RuntimeException("idFile not found", exception);
      }
    }
    return getIdAndSaveToFile(1);
  }

  public int getIdAndSaveToFile() {
    writeToFile(String.valueOf(id));
    return id++;
  }

  public int getIdAndSaveToFile(int id) {
    writeToFile(String.valueOf(id));
    return id++;
  }

  public void clearIdFile() {
    writeToFile("1");
    id = 0;
  }

  private void writeToFile(String string) {
    try (PrintWriter printWriter = new PrintWriter(currentIdFile.getName())) {
      printWriter.print(string);
    } catch (FileNotFoundException exception) {
      throw new RuntimeException("idFile not found", exception);
    }

  }
}