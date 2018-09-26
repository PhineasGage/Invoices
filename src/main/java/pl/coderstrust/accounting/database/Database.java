package pl.coderstrust.accounting.database;

import java.util.List;
import pl.coderstrust.accounting.model.Invoice;
import pl.coderstrust.accounting.model.ItemsForDatabase;

public interface Database<T extends ItemsForDatabase> {

  int saveItem(T item);

  List<T> getItems();

  void updateItemById(int id, T item);

  void removeItemById(int id);

  void clearDatabase();
}