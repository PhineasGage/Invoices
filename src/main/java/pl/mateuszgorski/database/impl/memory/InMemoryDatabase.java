package pl.mateuszgorski.database.impl.memory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;
import pl.mateuszgorski.database.Database;
import pl.mateuszgorski.model.Invoice;

@Repository
public class InMemoryDatabase implements Database {

  private Map<Integer, Invoice> invoices = new HashMap<>();
  private int id = 0;

  private int getNextId() {
    return id++;
  }

  @Override
  public int saveInvoice(Invoice invoice) {
    //invoice.setId(getNextId()); // TODO you cannot modify object you received - good example is Invoice provider -
    // with each test you are changing the invoice so other tests are failing... There is also other aspect of that
    // security - you keep in your collection the same object someone has reference too and he can manipulate this object :)
    // Please correct workaround below to do deep copy (not shallow one as now) - best do copy constructor in class
    Invoice internalInvoice = new Invoice();
    internalInvoice.setId(getNextId());
    internalInvoice.setBuyer(invoice.getBuyer());
    internalInvoice.setSeller(invoice.getSeller());
    internalInvoice.setIdentifier(invoice.getIdentifier());
    internalInvoice.setIssueDate(invoice.getIssueDate());
    internalInvoice.setEntries(invoice.getEntries());
    internalInvoice.setSaleDate(invoice.getSaleDate());
    internalInvoice.setSalePlace(invoice.getSalePlace());
    invoices.put(internalInvoice.getId(), internalInvoice);
    return internalInvoice.getId();
  }

  @Override
  public List<Invoice> getInvoices() {
    return new ArrayList<>(invoices.values());
  }

  @Override
  public void updateInvoice(int id, Invoice invoice) {
    //invoice.setId(id); // TODO you cannot modify object you received - good example is Invoice provider -
    // with each test you are changing the invoice so other tests are failing... There is also other aspect of that
    // security - you keep in your collection the same object someone has reference too and he can manipulate this object :)
    // Please correct workaround below to do deep copy (not shallow one as now) - best do copy constructor in class

    Invoice internalInvoice = new Invoice();
    internalInvoice.setId(id);
    internalInvoice.setBuyer(invoice.getBuyer());
    internalInvoice.setSeller(invoice.getSeller());
    internalInvoice.setIdentifier(invoice.getIdentifier());
    internalInvoice.setIssueDate(invoice.getIssueDate());
    internalInvoice.setEntries(invoice.getEntries());
    internalInvoice.setSaleDate(invoice.getSaleDate());
    internalInvoice.setSalePlace(invoice.getSalePlace());
    invoices.put(id, internalInvoice);
  }

  @Override
  public void removeInvoiceById(int id) {
    invoices.remove(id);
  }

  @Override
  public void clearDatabase() {
    invoices.clear();
    id = 0;
  }
}