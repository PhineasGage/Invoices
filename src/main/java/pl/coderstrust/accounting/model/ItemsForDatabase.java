package pl.coderstrust.accounting.model;

public interface ItemsForDatabase<T> {

  int getId();

  void setId(int id);

  T makeDeepCopy(T item);
}
