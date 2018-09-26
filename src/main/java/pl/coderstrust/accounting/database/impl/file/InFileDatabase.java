package pl.coderstrust.accounting.database.impl.file;

import java.util.List;
import java.util.stream.Collectors;
import pl.coderstrust.accounting.configuration.JacksonProvider;
import pl.coderstrust.accounting.database.Database;
import pl.coderstrust.accounting.database.impl.file.helpers.FileHelper;
import pl.coderstrust.accounting.database.impl.file.helpers.IndexHelper;
import pl.coderstrust.accounting.database.impl.file.helpers.ItemConverter;
import pl.coderstrust.accounting.model.ItemsForDatabase;

public class InFileDatabase<T extends ItemsForDatabase> implements Database<T> {


  private FileHelper<T> fileHelper;
  private ItemConverter<T> itemConverter;
  private IndexHelper<T> indexHelper;


  public InFileDatabase(FileHelper<T> fileHelper, ItemConverter<T> itemConverter, IndexHelper<T> indexHelper) {
    this.fileHelper = fileHelper;
    this.itemConverter = itemConverter;
    this.indexHelper = indexHelper;
  }

  @Override
  public int saveItem(T item) {
    int id = indexHelper.getNextId();
    T itemCopy = (T) item.makeDeepCopy(item);
    itemCopy.setId(id);
    fileHelper.writeItem(itemConverter.convertItemToJson(itemCopy));
    return id;
  }

  @Override
  public List<T> getItems() {
    return itemConverter.convertListOfStringsToListOfItems(fileHelper.readLines());
  }

  @Override
  public void updateItemById(int id, T item) {
    List<T> itemList = getAllItemsExceptInvoiceWithSpecifiedId(id);
    T itemCopy = (T) item.makeDeepCopy(item);
    itemCopy.setId(id);
    itemList.add(itemCopy);
    fileHelper.replaceFileContent(itemConverter.convertListOfItemsToListOfStrings(itemList));
  }

  @Override
  public void removeItemById(int id) {
    List<T> itemList = getAllItemsExceptInvoiceWithSpecifiedId(id);
    fileHelper.replaceFileContent(itemConverter.convertListOfItemsToListOfStrings(itemList));
  }

  private List<T> getAllItemsExceptInvoiceWithSpecifiedId(int id) {
    return getItems()
        .stream()
        .filter(n -> n.getId() != id)
        .collect(Collectors.toList());
  }

  @Override
  public void clearDatabase() {
    fileHelper.clearDatabaseFile();
    indexHelper.clearIdFile();
  }
}