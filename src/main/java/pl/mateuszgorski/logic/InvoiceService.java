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

  private Database database;

  public InvoiceService(Database database) {
    this.database = database;
  }

  public int saveInvoice(Invoice invoice) {
    if (invoice == null) {
      throw new IllegalArgumentException("Invoice cannot be null");
    }
    return database.saveInvoice(invoice);
  }

  public List<Invoice> getInvoices() {
    return database.getInvoices();
  }

  public Optional<Invoice> getInvoiceById(int id) {
    return database.getInvoices()
        .stream()
        .filter(invoice -> invoice.getId() == id)
        .findAny();
  }

  public List<Invoice> getInvoicesByIssueDate(LocalDate startDate, LocalDate endDate) {
    return database.getInvoices()
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

    database.updateInvoiceById(id, invoice);
  }

  public void removeInvoiceById(int id) {
    Optional<Invoice> invoiceFromDatabase = getInvoiceById(id);

    if (!invoiceFromDatabase.isPresent()) {
      throw new IllegalStateException("Invoice with id: " + id + " does not exist");
    }

    database.removeInvoiceById(id);
  }

  public void clearDatabase() {
    database.clearDatabase();
  }
}