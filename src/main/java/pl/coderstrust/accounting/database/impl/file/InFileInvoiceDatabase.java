package pl.coderstrust.accounting.database.impl.file;

import java.util.List;
import java.util.stream.Collectors;
import pl.coderstrust.accounting.database.impl.file.helpers.FileHelper;
import pl.coderstrust.accounting.database.impl.file.helpers.IndexHelper;
import pl.coderstrust.accounting.database.impl.file.helpers.ItemConverter;
import pl.coderstrust.accounting.model.Company;
import pl.coderstrust.accounting.model.Invoice;
import pl.coderstrust.accounting.model.ItemsForDatabase;

public class InFileInvoiceDatabase<T extends ItemsForDatabase> extends InFileDatabase<T> {

  private FileHelper<T> fileHelper;
  private ItemConverter<T> itemConverter;
  private IndexHelper<T> indexHelper;
  private InFileCompanyDatabase<Company> inFileCompanyDatabase;
  Company companyObject = new Company();


  public InFileInvoiceDatabase(FileHelper fileHelper,
                               ItemConverter itemConverter,
                               IndexHelper indexHelper,
                               InFileCompanyDatabase inFileCompanyDatabase
  ) {
    this.fileHelper = fileHelper;
    this.itemConverter = itemConverter;
    this.indexHelper = indexHelper;
    this.inFileCompanyDatabase = inFileCompanyDatabase;
  }

  @Override
  public int saveItem(T item) {
    T itemCopy = (T) item.makeDeepCopy(item);
    int id = indexHelper.getNextId();
    itemCopy.setId(id);
    itemCopy = setCorrectIdToBuyerAndSellerOnInvoice(itemCopy);
    fileHelper.writeItem(itemConverter.convertItemToJson(itemCopy));
    return id;
  }

  private T setCorrectIdToBuyerAndSellerOnInvoice(T item) {
    Company buyer = companyObject.makeDeepCopy(((Invoice) item).getBuyer());
    Company seller = companyObject.makeDeepCopy(((Invoice) item).getSeller());
    int buyerId = inFileCompanyDatabase.getIndexHelper().getNextId();
    int sellerId = inFileCompanyDatabase.getIndexHelper().getNextId();
    buyer.setId(buyerId);
    seller.setId(sellerId);
    inFileCompanyDatabase.saveItem(buyer);
    inFileCompanyDatabase.saveItem(seller);
    ((Invoice) item).setBuyer(buyer);
    ((Invoice) item).setSeller(seller);
    return item;
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
    itemCopy = setCorrectIdToBuyerAndSellerOnInvoice(itemCopy);
    itemList.add(itemCopy);
    fileHelper.replaceFileContent(itemConverter.convertListOfItemsToListOfStrings(itemList));
  }

  @Override
  public void removeItemById(int id) {
    List<T> itemList = getAllItemsExceptInvoiceWithSpecifiedId(id);
    fileHelper.replaceFileContent(itemConverter.convertListOfItemsToListOfStrings(itemList));
  }

  @Override
  public void clearDatabase() {
    fileHelper.clearDatabaseFile();
    indexHelper.clearIdFile();
  }

  private List<T> getAllItemsExceptInvoiceWithSpecifiedId(int id) {
    return getItems()
        .stream()
        .filter(n -> n.getId() != id)
        .collect(Collectors.toList());
  }
}