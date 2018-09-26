package restassured.actualisationofdataandtaxes.invoicesnotcompleted;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jayway.restassured.http.ContentType;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;
import pl.coderstrust.accounting.configuration.JacksonProvider;
import pl.coderstrust.accounting.helpers.InvoiceProvider;
import pl.coderstrust.accounting.model.Invoice;
import restassured.Data;

@RunWith(JUnitParamsRunner.class)
public class IncompleteInvoicesParametrizedTest implements Data {

  @Test
  @Parameters(method = "dataForTesting")
  public void verifyMessageIfIncompleteInvoice(Invoice invoice, String message)
      throws JsonProcessingException {
    given()
        .contentType(ContentType.JSON)
        .body(JacksonProvider.getObjectMapper().writeValueAsString(invoice))
        .post(invoicesUrl)
        .then()
        .body(containsString(message));
  }

  public Object[] dataForTesting() {
    return new Object[]{
        new Object[]{InvoiceProvider.INVOICE_BLANK_SALE_PLACE, "Sale place not found"},
        new Object[]{InvoiceProvider.INVOICE_BLANK_IDENTIFIER, "Identifier not found"},
        new Object[]{InvoiceProvider.INVOICE_EMPTY_ENTRIES, "Entries not found"},
        new Object[]{InvoiceProvider.INVOICE_BLANK_SALE_DATE, "Sale date not found"},
        new Object[]{InvoiceProvider.INVOICE_BLANK_ISSUE_DATE, "Issue date not found"},
        new Object[]{InvoiceProvider.INVOICE_BLANK_BUYER_NAME, "Buyer name not found"},
        new Object[]{InvoiceProvider.INVOICE_BLANK_BUYER_NIP, "Buyer NIP is required but was not provided"},
        new Object[]{InvoiceProvider.INVOICE_BLANK_BUYER_NIP, "Buyer NIP is not according to pattern"},
        new Object[]{InvoiceProvider.INVOICE_BLANK_BUYER_STREET, "Buyer street not found"},
        new Object[]{InvoiceProvider.INVOICE_BLANK_BUYER_POSTAL_CODE, "Buyer postal code not found"},
        new Object[]{InvoiceProvider.INVOICE_BLANK_BUYER_CITY, "Buyer city not found"},
        new Object[]{InvoiceProvider.INVOICE_BLANK_SELLER_NAME, "Seller name not found"},
        new Object[]{InvoiceProvider.INVOICE_BLANK_SELLER_NIP, "Seller NIP is required but was not provided"},
        new Object[]{InvoiceProvider.INVOICE_BLANK_SELLER_NIP, "Seller NIP is not according to pattern"},
        new Object[]{InvoiceProvider.INVOICE_BLANK_SELLER_STREET, "Seller street not found"},
        new Object[]{InvoiceProvider.INVOICE_BLANK_SELLER_POSTAL_CODE, "Seller postal code not found"},
        new Object[]{InvoiceProvider.INVOICE_BLANK_SELLER_CITY, "Seller city not found"},
        new Object[]{InvoiceProvider.INVOICE_BLANK_ENTRY_DESCRIPTION, "Entry description not found"},
        new Object[]{InvoiceProvider.INVOICE_BLANK_ENTRY_NET_PRICE, "Net price for entry not found"},
        new Object[]{InvoiceProvider.INVOICE_BLANK_ENTRY_VAT_RATE, "Vat rate for entry not found"},
        new Object[]{InvoiceProvider.INVOICE_BLANK_ENTRY_QUNTITY, "Quantity for entry not found"},
        new Object[]{InvoiceProvider.INVOICE_BAD_DISCOUNT_VALUE, "Discount must be less than 1 and greater than or equal to 0"},
    };
  }
}