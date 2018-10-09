package pl.coderstrust.accounting.database.impl.file;

import java.util.List;
import java.util.stream.Collectors;
import pl.coderstrust.accounting.database.Database;
import pl.coderstrust.accounting.database.impl.file.helpers.FileHelper;
import pl.coderstrust.accounting.database.impl.file.helpers.IndexHelper;
import pl.coderstrust.accounting.database.impl.file.helpers.ItemConverter;
import pl.coderstrust.accounting.model.ItemsForDatabase;

public abstract class InFileDatabase<T extends ItemsForDatabase> implements Database<T> {

  @Override
  public abstract int saveItem(T item);


  @Override
  public abstract List<T> getItems();



  @Override
  public abstract void updateItemById(int id, T item);


  @Override
  public abstract void removeItemById(int id);


  @Override
  public abstract void clearDatabase();
}