package pl.mateuszgorski.logic;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import pl.mateuszgorski.database.Database;
import pl.mateuszgorski.model.Invoice;

@Service
public class InvoiceService {

  private Database database;

  public InvoiceService(Database database) {
    this.database = database;
  }

  public int saveInvoice(Invoice invoice) throws IOException { // TODO those exceptions are not needed now :)
    if (invoice == null) {
      throw new IllegalArgumentException("Invoice cannot be null");
    }
    return database.saveInvoice(invoice);
  }

  public List<Invoice> getInvoices() throws IOException {
    return database.getInvoices();
  }

  public Optional<Invoice> getInvoiceById(int id) throws IOException {
    return database.getInvoices()
        .stream()
        .filter(invoice -> invoice.getId() == id)
        .findAny();
  }

  public List<Invoice> getInvoicesByIssueDate(LocalDate startDate, LocalDate endDate)
      throws IOException {
    return database.getInvoices()
        .stream()
        .filter(n -> n.getIssueDate().isAfter(startDate))
        .filter(n -> n.getIssueDate().isBefore(endDate))
        .collect(Collectors.toList());
  }

  public void updateInvoice(int id, Invoice invoice) throws IOException {
    Optional<Invoice> invoiceFromDatabase = getInvoiceById(id);

    if (!invoiceFromDatabase.isPresent()) {
      throw new IllegalStateException("Invoice with id: " + id + " does not exist");
    }

    database.updateInvoice(id, invoice);
  }

  public void removeInvoiceById(int id) throws IOException {
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