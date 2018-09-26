package pl.coderstrust.accounting.database.impl.file;

import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import pl.coderstrust.accounting.configuration.DatabaseProvider;
import pl.coderstrust.accounting.database.Database;
import pl.coderstrust.accounting.database.impl.DatabaseTest;
import pl.coderstrust.accounting.database.impl.hibernate.InvoiceRepository;
import pl.coderstrust.accounting.model.Invoice;

public class InFileDatabaseTest extends DatabaseTest {

  InvoiceRepository invoiceRepository;

  JdbcTemplate jdbcTemplate;

  private InFileDatabase<Invoice> inFileDatabase = (InFileDatabase) DatabaseProvider.provideInvoiceDatabase(
      "InFileDatabase",
      "src/main/resources/",
      invoiceRepository,
      jdbcTemplate);

  @Override
  protected Database getDatabase() {
    return inFileDatabase;
  }

  @Before
  public void beforeMethod() {
    inFileDatabase.clearDatabase();
  }
}