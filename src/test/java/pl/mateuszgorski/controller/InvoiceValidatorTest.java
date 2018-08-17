package pl.mateuszgorski.controller;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.MockitoAnnotations.initMocks;
import static pl.mateuszgorski.helpers.InvoiceProvider.INVOICE_BAD_DISCOUNT_VALUE;
import static pl.mateuszgorski.helpers.InvoiceProvider.INVOICE_BLANK_BUYER_CITY;
import static pl.mateuszgorski.helpers.InvoiceProvider.INVOICE_BLANK_BUYER_NAME;
import static pl.mateuszgorski.helpers.InvoiceProvider.INVOICE_BLANK_BUYER_NIP;
import static pl.mateuszgorski.helpers.InvoiceProvider.INVOICE_BLANK_BUYER_POSTAL_CODE;
import static pl.mateuszgorski.helpers.InvoiceProvider.INVOICE_BLANK_BUYER_STREET;
import static pl.mateuszgorski.helpers.InvoiceProvider.INVOICE_BLANK_ENTRY_DESCRIPTION;
import static pl.mateuszgorski.helpers.InvoiceProvider.INVOICE_BLANK_ENTRY_NET_PRICE;
import static pl.mateuszgorski.helpers.InvoiceProvider.INVOICE_BLANK_ENTRY_QUNTITY;
import static pl.mateuszgorski.helpers.InvoiceProvider.INVOICE_BLANK_ENTRY_VAT_RATE;
import static pl.mateuszgorski.helpers.InvoiceProvider.INVOICE_BLANK_IDENTIFIER;
import static pl.mateuszgorski.helpers.InvoiceProvider.INVOICE_BLANK_ISSUE_DATE;
import static pl.mateuszgorski.helpers.InvoiceProvider.INVOICE_BLANK_SALE_DATE;
import static pl.mateuszgorski.helpers.InvoiceProvider.INVOICE_BLANK_SALE_PLACE;
import static pl.mateuszgorski.helpers.InvoiceProvider.INVOICE_BLANK_SELLER_CITY;
import static pl.mateuszgorski.helpers.InvoiceProvider.INVOICE_BLANK_SELLER_NAME;
import static pl.mateuszgorski.helpers.InvoiceProvider.INVOICE_BLANK_SELLER_NIP;
import static pl.mateuszgorski.helpers.InvoiceProvider.INVOICE_BLANK_SELLER_POSTAL_CODE;
import static pl.mateuszgorski.helpers.InvoiceProvider.INVOICE_BLANK_SELLER_STREET;
import static pl.mateuszgorski.helpers.InvoiceProvider.INVOICE_EMPTY_ENTRIES;

import java.util.List;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import pl.mateuszgorski.model.Company;
import pl.mateuszgorski.model.Invoice;

@RunWith(JUnitParamsRunner.class)
public class InvoiceValidatorTest {

  @InjectMocks
  private InvoiceValidator invoiceValidator;

  @Mock
  private Invoice invoice; // TODO not used

  private Company company; // TODO not used

  @Before
  public void setup() {
    initMocks(this);
  }

  @Parameters(method = "param")
  @Test
  public void shouldCheckIfReturnedValidationMessageCorrespondsToIncompleteInvoices(Invoice invoice, String message) {
    //when
    List<String> actualTest = invoiceValidator.validate(invoice);

    //then
    assertThat(actualTest, hasItem(message));
  }

  private Object[] param() {
    return new Object[]{
        new Object[]{INVOICE_BLANK_SALE_PLACE, "Sale place not found"},
        new Object[]{INVOICE_BLANK_IDENTIFIER, "Identifier not found"},
        new Object[]{INVOICE_EMPTY_ENTRIES, "Entries not found"},
        new Object[]{INVOICE_BLANK_SALE_DATE, "Sale date not found"},
        new Object[]{INVOICE_BLANK_ISSUE_DATE, "Issue date not found"},
        new Object[]{INVOICE_BLANK_BUYER_NAME, "Buyer name not found"},
        new Object[]{INVOICE_BLANK_BUYER_NIP, "Buyer nip not found"},
        new Object[]{INVOICE_BLANK_BUYER_STREET, "Buyer street not found"},
        new Object[]{INVOICE_BLANK_BUYER_POSTAL_CODE, "Buyer postal code not found"},
        new Object[]{INVOICE_BLANK_BUYER_CITY, "Buyer city not found"},
        new Object[]{INVOICE_BLANK_SELLER_NAME, "Seller name not found"},
        new Object[]{INVOICE_BLANK_SELLER_NIP, "Seller nip not found"},
        new Object[]{INVOICE_BLANK_SELLER_STREET, "Seller street not found"},
        new Object[]{INVOICE_BLANK_SELLER_POSTAL_CODE, "Seller postal code not found"},
        new Object[]{INVOICE_BLANK_SELLER_CITY, "Seller city not found"},
        new Object[]{INVOICE_BLANK_ENTRY_DESCRIPTION, "Entry description not found"},
        new Object[]{INVOICE_BLANK_ENTRY_NET_PRICE, "Net price for entry not found"},
        new Object[]{INVOICE_BLANK_ENTRY_VAT_RATE, "Vat rate for entry not found"},
        new Object[]{INVOICE_BLANK_ENTRY_QUNTITY, "Quantity for entry not found"},
        new Object[]{INVOICE_BAD_DISCOUNT_VALUE, "Bad value of discount"},
    };
  }
}
