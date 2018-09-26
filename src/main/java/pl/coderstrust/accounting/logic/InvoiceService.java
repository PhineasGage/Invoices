package pl.coderstrust.accounting.logic;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import pl.coderstrust.accounting.database.Database;
import pl.coderstrust.accounting.model.Invoice;

@Service
public class InvoiceService {

  private Database<Invoice> database;

  public InvoiceService(Database<Invoice> database) {
    this.database = database;
  }

  public int saveInvoice(Invoice invoice) {
    if (invoice == null) {
      throw new IllegalArgumentException("Invoice cannot be null");
    }
    return database.saveItem(invoice);
  }

  public List<Invoice> getInvoices() {
    return database.getItems();
  }

  public Optional<Invoice> getInvoiceById(int id) {
    return database.getItems()
        .stream()
        .filter(invoice -> invoice.getId() == id)
        .findAny();
  }

  public List<Invoice> getInvoicesByIssueDate(LocalDate startDate, LocalDate endDate) {
    return database.getItems()
        .stream()
        .filter(n -> n.getIssueDate().isAfter(startDate))
        .filter(n -> n.getIssueDate().isBefore(endDate))
        .collect(Collectors.toList());
  }

  public void updateInvoice(int id, Invoice invoice) {
    Optional<Invoice> invoiceFromDatabase = getInvoiceById(id);

    if (!invoiceFromDatabase.isPresent()) {
      throw new IllegalStateException("Invoice with id: " + id + " does not exist");
    }

    database.updateItemById(id, invoice);
  }

  public void removeInvoiceById(int id) {
    Optional<Invoice> invoiceFromDatabase = getInvoiceById(id);

    if (!invoiceFromDatabase.isPresent()) {
      throw new IllegalStateException("Invoice with id: " + id + " does not exist");
    }

    database.removeItemById(id);
  }

  public List<Invoice> getAllInvoicesForSpecificCompany(String nip) {
    return database.getItems()
        .stream()
        .filter(n -> n.getSeller().getNip().equals(nip))
        .collect(Collectors.toList());
  }

  public Optional<Invoice> getSpecificInvoiceForSpecifiedCompany(String nip, int invoiceId) {
    return database.getItems()
        .stream()
        .filter(n -> n.getId() == invoiceId)
        .filter(n -> n.getSeller().getNip().equals(nip))
        .findAny();
  }

  public void clearDatabase() {
    database.clearDatabase();
  }
}