package pl.coderstrust.accounting.database.impl.memory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import pl.coderstrust.accounting.database.Database;
import pl.coderstrust.accounting.model.ItemsForDatabase;

public class InMemoryDatabase<T extends ItemsForDatabase> implements Database<T> {

  private Map<Integer, T> items = new HashMap<>();
  private int id = 0;

  private int getNextId() {
    return id++;
  }

  @Override
  public int saveItem(T item) {
    T copy = (T) item.makeDeepCopy(item);
    copy.setId(getNextId());
    items.put(copy.getId(), copy);
    return copy.getId();
  }

  @Override
  public List<T> getItems() {
    return new ArrayList<>(items.values());
  }

  @Override
  public void updateItemById(int id, T item) {
    T copy = (T) item.makeDeepCopy(item);
    items.put(id, copy);
  }

  @Override
  public void removeItemById(int id) {
    items.remove(id);
  }

  @Override
  public void clearDatabase() {
    items.clear();
    id = 0;
  }
}