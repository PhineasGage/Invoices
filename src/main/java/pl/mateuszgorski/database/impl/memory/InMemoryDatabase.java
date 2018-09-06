package pl.coderstrust.accounting.database.impl.memory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;
import pl.coderstrust.accounting.database.Database;
import pl.coderstrust.accounting.model.Invoice;


@Repository
public class InMemoryDatabase implements Database {

  private Map<Integer, Invoice> invoices = new HashMap<>();
  private int id = 0;

  private int getNextId() {
    return id++;
  }

  @Override
  public int saveInvoice(Invoice invoice) {
    Invoice internalInvoice = new Invoice(invoice);
    internalInvoice.setId(getNextId());
    invoices.put(internalInvoice.getId(), internalInvoice);
    return internalInvoice.getId();
  }

  @Override
  public List<Invoice> getInvoices() {
    return new ArrayList<>(invoices.values());
  }

  @Override
  public void updateInvoiceById(int id, Invoice invoice) {
    Invoice internalInvoice = new Invoice(invoice);
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