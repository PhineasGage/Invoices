package pl.mateuszgorski.database;

import java.util.List;
import pl.mateuszgorski.model.Invoice;

public interface Database {

  int saveInvoice(Invoice invoice);

  List<Invoice> getInvoices();


  void updateInvoiceById(int id, Invoice invoice);

  void removeInvoiceById(int id);


  void clearDatabase();
}