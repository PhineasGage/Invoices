package pl.mateuszgorski.database;

import java.io.IOException;
import java.util.List;
import pl.mateuszgorski.model.Invoice;

public interface Database {

  int saveInvoice(Invoice invoice) throws IOException; // TODO can use do something with this exception?

  List<Invoice> getInvoices() throws IOException;

  void updateInvoice(int id, Invoice invoice) throws IOException;

  void removeInvoiceById(int id) throws IOException; // TODO you remove byId but update without id - be consistent please :)

  void clearDatabase();
}