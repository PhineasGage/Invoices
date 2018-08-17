package pl.mateuszgorski.database.impl.file.helpers;

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

  public IndexHelper(@Value("${idFilePath}") String path) { // TODO you don't inject from property file - ${} missing :)
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
        throw new RuntimeException("idFile not found"); // TODO - exception chaining
      }
    }
    return 1; // TODO shouldn't you create file in such case ?
  }

  public int getIdAndSaveToFile() {
    id++;
    writeToFile(String.valueOf(id));
    return id;
  }

  public void clearIdFile() {
    writeToFile(""); // TODO I would like it more if we write to file initial value instead of empty string - e.g. 1
    id = 0;
  }

  void writeToFile(String string) { // TODO why not private?
    try (PrintWriter printWriter = new PrintWriter(currentIdFile.getName())) {
      printWriter.print(string);
    } catch (FileNotFoundException exception) {
      throw new RuntimeException("idFile not found"); // TODO exception chaining
    }

  }
}