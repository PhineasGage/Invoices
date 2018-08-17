package pl.mateuszgorski.database.impl.file;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import pl.mateuszgorski.database.Database;
import pl.mateuszgorski.database.impl.file.helpers.FileHelper;
import pl.mateuszgorski.database.impl.file.helpers.IndexHelper;
import pl.mateuszgorski.database.impl.file.helpers.InvoiceConverter;
import pl.mateuszgorski.model.Invoice;

@Primary
@Repository
public class InFileDatabase implements Database {

  private FileHelper fileHelper;
  private InvoiceConverter invoiceConverter;
  private IndexHelper indexHelper;

  public InFileDatabase(FileHelper fileHelper, InvoiceConverter invoiceConverter, IndexHelper indexHelper) {
    this.fileHelper = fileHelper;
    this.invoiceConverter = invoiceConverter;
    this.indexHelper = indexHelper;
  }

  @Override
  public int saveInvoice(Invoice invoice) throws IOException { // TODO don't declare unused exception :)
    int id = indexHelper.getIdAndSaveToFile();
    invoice.setId(id); // TODO you should not modify objects you receive
    fileHelper.writeInvoice(invoiceConverter.convertInvoiceToJson(invoice));
    return id;
  }

  @Override
  public List<Invoice> getInvoices() {
    return fileHelper.lines() // TODO this method convert stream to list and then you convert back - can you optimize?
        .stream()
        .map(invoiceConverter::convertJsonToInvoice)
        .collect(Collectors.toList()); // TODO invoice converter can convert list of invoices to strings, why cannot opposite?
  }

  @Override
  public void updateInvoice(int id, Invoice invoice) throws IOException {
    List<Invoice> invoiceList = getAllInvoicesExceptWithSpecifiedId(id);
    invoice.setId(id); // TODO you should not modify received object
    invoiceList.add(invoice);
    fileHelper.replaceFileContent(invoiceConverter.convertListOfInvoicesToJsons(invoiceList));
  }

  @Override
  public void removeInvoiceById(int id) throws IOException {
    // TODO this method and this above are almist identical but organised different way e.g. invoiceList should be variable here
    List<String> jsonList = invoiceConverter.convertListOfInvoicesToJsons(getAllInvoicesExceptWithSpecifiedId(id));
    fileHelper.replaceFileContent(jsonList);
  }

  private List<Invoice> getAllInvoicesExceptWithSpecifiedId(int id) { // TODO except invoice with ...
    return getInvoices()
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