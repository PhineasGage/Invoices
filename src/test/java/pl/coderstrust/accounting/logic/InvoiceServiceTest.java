package pl.coderstrust.accounting.logic;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static pl.coderstrust.accounting.helpers.InvoiceProvider.INVOICE_DRUTEX_LINK_2016;
import static pl.coderstrust.accounting.helpers.InvoiceProvider.INVOICE_DRUTEX_SPAN_CLAMP_SUPPORT_2018;
import static pl.coderstrust.accounting.helpers.InvoiceProvider.INVOICE_TRANSPOL_SPAN_CLAMP_SUPPORT_2016;
import static pl.coderstrust.accounting.helpers.InvoiceProvider.INVOICE_WASBUD_LINK_2018;
import static pl.coderstrust.accounting.helpers.InvoiceProvider.INVOICE_WASBUD_SPAN_CLAMP_2017;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import pl.coderstrust.accounting.database.Database;
import pl.coderstrust.accounting.model.Invoice;

@RunWith(MockitoJUnitRunner.class)
public class InvoiceServiceTest {

  @Rule
  public ExpectedException expectedEx = ExpectedException.none();

  @Mock
  private Database databaseMock;

  @InjectMocks
  private InvoiceService invoiceService;

  @Test
  public void shouldSaveInvoice() {
    //given
    when(databaseMock.saveItem(INVOICE_DRUTEX_SPAN_CLAMP_SUPPORT_2018)).thenReturn(INVOICE_DRUTEX_SPAN_CLAMP_SUPPORT_2018.getId());

    //when
    int id = invoiceService.saveInvoice(INVOICE_DRUTEX_SPAN_CLAMP_SUPPORT_2018);

    //then
    assertThat(id, is(equalTo(INVOICE_DRUTEX_SPAN_CLAMP_SUPPORT_2018.getId())));
    verify(databaseMock).saveItem(INVOICE_DRUTEX_SPAN_CLAMP_SUPPORT_2018);
  }

  @Test
  public void shouldGetCollectionOfInvoices() {
    //given
    List<Invoice> invoices = new ArrayList<>();
    invoices.add(INVOICE_DRUTEX_LINK_2016);
    invoices.add(INVOICE_TRANSPOL_SPAN_CLAMP_SUPPORT_2016);
    invoices.add(INVOICE_WASBUD_SPAN_CLAMP_2017);

    when(databaseMock.getItems()).thenReturn(invoices);

    //when
    List<Invoice> actual = invoiceService.getInvoices();

    //then
    verify(databaseMock).getItems();
    assertThat(actual, is(invoices));
    assertThat(actual, hasItems(INVOICE_DRUTEX_LINK_2016, INVOICE_TRANSPOL_SPAN_CLAMP_SUPPORT_2016, INVOICE_WASBUD_SPAN_CLAMP_2017));
  }

  @Test
  public void shouldUpdateInvoice() {
    //given
    List<Invoice> invoices = new ArrayList<>();
    invoices.add(INVOICE_TRANSPOL_SPAN_CLAMP_SUPPORT_2016);
    invoices.add(INVOICE_WASBUD_SPAN_CLAMP_2017);

    int id = INVOICE_TRANSPOL_SPAN_CLAMP_SUPPORT_2016.getId();
    when(databaseMock.getItems()).thenReturn(invoices);

    //when
    invoiceService.updateInvoice(id, INVOICE_DRUTEX_SPAN_CLAMP_SUPPORT_2018);

    //then
    verify(databaseMock).updateItemById(id, INVOICE_DRUTEX_SPAN_CLAMP_SUPPORT_2018);
  }

  @Test
  public void shouldRemoveInvoiceById() {
    //given
    List<Invoice> invoices = new ArrayList<>();
    invoices.add(INVOICE_DRUTEX_SPAN_CLAMP_SUPPORT_2018);
    invoices.add(INVOICE_WASBUD_SPAN_CLAMP_2017);

    int id = INVOICE_WASBUD_SPAN_CLAMP_2017.getId();

    when(databaseMock.getItems()).thenReturn(invoices);

    //when
    invoiceService.removeInvoiceById(id);

    //then
    verify(databaseMock).removeItemById(id);
  }

  @Test
  public void shouldReturnInvoiceById() {
    //given
    List<Invoice> invoices = new ArrayList<>();
    invoices.add(INVOICE_DRUTEX_LINK_2016);
    invoices.add(INVOICE_TRANSPOL_SPAN_CLAMP_SUPPORT_2016);
    invoices.add(INVOICE_WASBUD_SPAN_CLAMP_2017);

    int id = INVOICE_DRUTEX_LINK_2016.getId();

    when(databaseMock.getItems()).thenReturn(invoices);

    //when
    Optional<Invoice> actual = invoiceService.getInvoiceById(id);
    Invoice expected = INVOICE_DRUTEX_LINK_2016;

    //then
    assertTrue(actual.isPresent());
    assertThat(actual.get(), is(expected));
  }

  @Test
  public void shouldReturnInvoicesByIssueDate() {
    //given
    List<Invoice> invoices = new ArrayList<>();
    invoices.add(INVOICE_DRUTEX_SPAN_CLAMP_SUPPORT_2018);
    invoices.add(INVOICE_WASBUD_LINK_2018);
    invoices.add(INVOICE_WASBUD_SPAN_CLAMP_2017);
    when(databaseMock.getItems()).thenReturn(invoices);

    //when
    LocalDate fromDate = LocalDate.of(2018, 4, 12);
    LocalDate toDate = LocalDate.of(2018, 6, 25);
    List<Invoice> actual = invoiceService.getInvoicesByIssueDate(fromDate, toDate);

    //then
    assertThat(actual.size(), is(2));
    assertThat(actual, hasItems(INVOICE_DRUTEX_SPAN_CLAMP_SUPPORT_2018, INVOICE_WASBUD_LINK_2018));
  }

  @Test
  public void shouldThrowExceptionCausedByMissingInvoiceWithProvidedIdForUpdate() {
    //given
    int id = 0;
    expectedEx.expect(IllegalStateException.class);
    expectedEx.expectMessage("Invoice with id: " + id + " does not exist");

    //when
    invoiceService.updateInvoice(0, INVOICE_WASBUD_SPAN_CLAMP_2017);
  }

  @Test
  public void shouldThrowExceptionCausedByMissingInvoiceWithProvidedIdToRemove() {
    //given
    int id = 0;
    expectedEx.expect(IllegalStateException.class);
    expectedEx.expectMessage("Invoice with id: " + id + " does not exist");

    //when
    invoiceService.removeInvoiceById(0);
  }

  @Test
  public void shouldThrowExceptionCausedByNullInvoice() {
    //given
    Invoice invoice = null;
    expectedEx.expect(IllegalArgumentException.class);
    expectedEx.expectMessage("Invoice cannot be null");

    //when
    invoiceService.saveInvoice(invoice);
  }
}