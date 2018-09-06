package pl.mateuszgorski.database.impl.file;

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
  public int saveInvoice(Invoice invoice) {
    int id = indexHelper.getIdAndSaveToFile();
    Invoice internalInvoice = new Invoice(invoice);
    internalInvoice.setId(id);
    fileHelper.writeInvoice(invoiceConverter.convertInvoiceToJson(internalInvoice));
    return id;
  }

  @Override
  public List<Invoice> getInvoices() {
    return invoiceConverter.convertListOfStringsToListOfInvoices(fileHelper.readLines());
  }

  @Override
  public void updateInvoiceById(int id, Invoice invoice) {
    List<Invoice> invoiceList = getAllInvoicesExceptInvoiceWithSpecifiedId(id);
    Invoice internalInvoice = new Invoice(invoice);
    internalInvoice.setId(id);
    invoiceList.add(internalInvoice);
    fileHelper.replaceFileContent(invoiceConverter.convertListOfInvoicesToListOfStrings(invoiceList));
  }

  @Override
  public void removeInvoiceById(int id) {
    List<Invoice> invoiceList = getAllInvoicesExceptInvoiceWithSpecifiedId(id);
    fileHelper.replaceFileContent(invoiceConverter.convertListOfInvoicesToListOfStrings(invoiceList));
  }

  private List<Invoice> getAllInvoicesExceptInvoiceWithSpecifiedId(int id) {
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