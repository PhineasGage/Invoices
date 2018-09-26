package pl.coderstrust.accounting.database.impl.file.helpers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import pl.coderstrust.accounting.model.ItemsForDatabase;

public class FileHelper<T extends ItemsForDatabase> {

  private Class classObject;
  private File databaseFile;

  public FileHelper(Class<T> classObject, String path) {
    this.classObject = classObject;
    databaseFile = new File(path + ".json");
  }

  public void writeItem(String string) {
    try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(databaseFile.getPath(), true))) {
      bufferedWriter.write(string);
      bufferedWriter.newLine();
    } catch (IOException exception) {
      throw new RuntimeException("Unable to write to a file", exception);
    }
  }

  public List<String> readLines() {
    if (!databaseFile.exists()) {
      return new ArrayList<>();
    }
    try {
      return Files.lines(databaseFile.toPath()).collect(Collectors.toList());
    } catch (IOException exception) {
      throw new RuntimeException("Unable to read a file", exception);
    }
  }

  public boolean replaceFileContent(List<String> items) {
    File tempFile = new File(databaseFile.getName() + ".tmp");
    try {
      Files.copy(databaseFile.toPath(), tempFile.toPath());
    } catch (IOException exception) {
      throw new RuntimeException("cannot copy temporary file content", exception);
    }
    clearDatabaseFile();
    for (String json : items) {
      writeItem(json);
    }
    return tempFile.delete();
  }

  public void clearDatabaseFile() {
    try (PrintWriter printWriter = new PrintWriter(databaseFile)) {
      printWriter.print("");
    } catch (FileNotFoundException exception) {
      throw new RuntimeException("File was not found", exception);
    }
  }
}