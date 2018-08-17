package pl.mateuszgorski.database.impl.file;


import org.junit.Before;
import pl.mateuszgorski.configuration.JacksonProvider;
import pl.mateuszgorski.database.Database;
import pl.mateuszgorski.database.impl.DatabaseTest;
import pl.mateuszgorski.database.impl.file.helpers.FileHelper;
import pl.mateuszgorski.database.impl.file.helpers.IndexHelper;
import pl.mateuszgorski.database.impl.file.helpers.InvoiceConverter;


public class InFileDatabaseTest extends DatabaseTest {

  private FileHelper fileHelper = new FileHelper("invoicesTest.json");
  private IndexHelper indexHelper = new IndexHelper("currentIdFileTest.json");

  private InvoiceConverter invoiceConverter = new InvoiceConverter(JacksonProvider.getObjectMapper());
  private InFileDatabase inFileDatabase = new InFileDatabase(fileHelper, invoiceConverter, indexHelper);

  @Override
  protected Database getDatabase() {
    return inFileDatabase;
  }

  @Before
  public void beforeMethod() {
    inFileDatabase.clearDatabase();
  }
}