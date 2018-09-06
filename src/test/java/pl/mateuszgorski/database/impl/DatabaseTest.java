package pl.coderstrust.accounting.database.impl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static pl.coderstrust.accounting.helpers.InvoiceProvider.INVOICE_DRUTEX_LINK_2016;
import static pl.coderstrust.accounting.helpers.InvoiceProvider.INVOICE_DRUTEX_SPAN_CLAMP_SUPPORT_2018;
import static pl.coderstrust.accounting.helpers.InvoiceProvider.INVOICE_WASBUD_SPAN_CLAMP_2017;


import java.util.List;
import org.junit.Test;
import pl.coderstrust.accounting.database.Database;
import pl.coderstrust.accounting.helpers.InvoiceAssertionHelper;
import pl.coderstrust.accounting.model.Invoice;

public abstract class DatabaseTest {

  private InvoiceAssertionHelper invoiceAssertion = new InvoiceAssertionHelper();

  protected abstract Database getDatabase();


  @Test
  public void shouldSaveInvoices() throws Exception {
    //given
    Database database = getDatabase();

    //when
    int actualId = database.saveInvoice(INVOICE_DRUTEX_SPAN_CLAMP_SUPPORT_2018);
    List<Invoice> invoices = database.getInvoices();
    Invoice savedInvoice = getInvoiceById(actualId, invoices);

    //then
    assertThat(invoices.size(), is(1));
    invoiceAssertion.assertInvoice(actualId, INVOICE_DRUTEX_SPAN_CLAMP_SUPPORT_2018, savedInvoice);
  }

  @Test
  public void shouldReturnCollectionsOfInvoices() throws Exception {
    //given
    Database database = getDatabase();

    //when
    int actualIdA = database.saveInvoice(INVOICE_DRUTEX_SPAN_CLAMP_SUPPORT_2018);
    int actualIdB = database.saveInvoice(INVOICE_WASBUD_SPAN_CLAMP_2017);
    List<Invoice> invoices = database.getInvoices();
    Invoice savedInvoiceA = getInvoiceById(actualIdA, invoices);
    Invoice savedInvoiceB = getInvoiceById(actualIdB, invoices);

    //then
    assertThat(database.getInvoices().size(), is(2));
    invoiceAssertion.assertInvoice(actualIdA, INVOICE_DRUTEX_SPAN_CLAMP_SUPPORT_2018, savedInvoiceA);
    invoiceAssertion.assertInvoice(actualIdB, INVOICE_WASBUD_SPAN_CLAMP_2017, savedInvoiceB);
  }

  @Test
  public void shouldRemoveInvoices() throws Exception {
    //given
    Database database = getDatabase();
    int idA = database.saveInvoice(INVOICE_DRUTEX_SPAN_CLAMP_SUPPORT_2018);
    int idB = database.saveInvoice(INVOICE_WASBUD_SPAN_CLAMP_2017);

    //when
    database.removeInvoiceById(idA);
    List<Invoice> invoices = database.getInvoices();
    Invoice savedInvoiceB = getInvoiceById(idB, invoices);

    //then
    assertThat(database.getInvoices().size(), is(1));
    invoiceAssertion.assertInvoice(idB, INVOICE_WASBUD_SPAN_CLAMP_2017, savedInvoiceB);
  }

  @Test
  public void shouldUpdateInvoice() throws Exception {
    //given
    Database database = getDatabase();
    int returnedId = database.saveInvoice(INVOICE_WASBUD_SPAN_CLAMP_2017);

    //when
    database.updateInvoiceById(returnedId, INVOICE_DRUTEX_LINK_2016);
    List<Invoice> invoices = database.getInvoices();
    Invoice savedInvoice = getInvoiceById(returnedId, invoices);

    //then
    assertThat(database.getInvoices().size(), is(1));
    invoiceAssertion.assertInvoice(returnedId, INVOICE_DRUTEX_LINK_2016, savedInvoice);
  }

  public Invoice getInvoiceById(int id, List<Invoice> invoices) {
    return invoices
        .stream()
        .filter(invoice -> invoice.getId() == id)
        .findAny()
        .get();
  }
}