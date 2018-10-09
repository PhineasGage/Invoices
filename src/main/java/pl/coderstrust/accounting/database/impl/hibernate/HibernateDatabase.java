package pl.coderstrust.accounting.database.impl.hibernate;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.springframework.data.repository.CrudRepository;
import pl.coderstrust.accounting.database.Database;
import pl.coderstrust.accounting.model.ItemsForDatabase;

public class HibernateDatabase<T extends ItemsForDatabase> implements Database {

  private CrudRepository<T, Integer> repository;

  public HibernateDatabase(CrudRepository<T, Integer> repository) {
    this.repository = repository;
  }

  @Override
  public int saveItem(ItemsForDatabase item) {
    return repository.save((T) item).getId();
  }

  @Override
  public List<T> getItems() {
    Iterable<T> items = repository.findAll();
    return StreamSupport.stream(items.spliterator(), false).collect(Collectors.toList());
  }

  @Override
  public void updateItemById(int id, ItemsForDatabase item) {
    if (repository.existsById(id)) {
      item.setId(id);
      repository.save((T) item);
    } else {
      throw new IllegalArgumentException("Such id does not exist");
    }
  }

  @Override
  public void removeItemById(int id) {
    if (repository.existsById(id)) {
      repository.deleteById(id);
    } else {
      throw new IllegalArgumentException("Such id does not exist");
    }
  }

  @Override
  public void clearDatabase() {
    repository.deleteAll();
  }
}