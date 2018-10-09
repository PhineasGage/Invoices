package restassured.actualisationofdataandtaxes.invoices;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.core.Is.is;

import com.jayway.restassured.http.ContentType;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import org.junit.BeforeClass;
import org.junit.Test;
import pl.coderstrust.accounting.model.Invoice;
import restassured.Data;

public class PostMethodTest implements Data {

  private static List<Invoice> invoices = Arrays.asList(invoiceWasbud_2018, invoiceDrutex_2016, invoiceDrutex_2018, invoiceWasbud_2017,
      invoiceTranspol_2016);

  @BeforeClass
  public static void prepareInvoicesToTest() {
    for (Invoice invoice : invoices) {
      String id = given()
          .contentType(ContentType.JSON)
          .body(invoice)
          .post(invoicesUrl).thenReturn().body().print();
      invoice.setId(Integer.valueOf(id));
    }
  }

  @Test
  public void verifyAllFieldsOfInvoiceWhenPostAndThenGet() {
    for (int j = 0; j < invoices.size(); j++) {
      int id = invoices.get(j).getId();
      String identifier = invoices.get(j).getIdentifier();
      String issueDate = invoices.get(j).getIssueDate().toString();
      String saleDate = invoices.get(j).getSaleDate().toString();
      String salePlace = invoices.get(j).getSalePlace();

      String buyerName = invoices.get(j).getBuyer().getName();
      String buyerNip = invoices.get(j).getBuyer().getNip();
      String buyerStreet = invoices.get(j).getBuyer().getStreet();
      String buyerPostalCode = invoices.get(j).getBuyer().getPostalCode();
      String buyerCity = invoices.get(j).getBuyer().getCity();
      String buyerDiscount = invoices.get(j).getBuyer().getDiscount().toString();

      String sellerName = invoices.get(j).getSeller().getName();
      String sellerNip = invoices.get(j).getSeller().getNip();
      String sellerStreet = invoices.get(j).getSeller().getStreet();
      String sellerPostalCode = invoices.get(j).getSeller().getPostalCode();
      String sellerCity = invoices.get(j).getSeller().getCity();
      String sellerDiscount = invoices.get(j).getSeller().getDiscount().toString();

      int entriesSize = invoices.get(j).getEntries().size();

      given().when().get(invoicesUrl).then()
          .body("[" + j + "].id", is(id))
          .body("[" + j + "].identifier", is(identifier))
          .body("[" + j + "].issueDate", is(issueDate))
          .body("[" + j + "].saleDate", is(saleDate))
          .body("[" + j + "].salePlace", is(salePlace))

          .body("[" + j + "].buyer.name", is(buyerName))
          .body("[" + j + "].buyer.nip", is(buyerNip))
          .body("[" + j + "].buyer.street", is(buyerStreet))
          .body("[" + j + "].buyer.postalCode", is(buyerPostalCode))
          .body("[" + j + "].buyer.city", is(buyerCity))
          .body("[" + j + "].buyer.discount", is(buyerDiscount))

          .body("[" + j + "].seller.name", is(sellerName))
          .body("[" + j + "].seller.nip", is(sellerNip))
          .body("[" + j + "].seller.street", is(sellerStreet))
          .body("[" + j + "].seller.postalCode", is(sellerPostalCode))
          .body("[" + j + "].seller.city", is(sellerCity))
          .body("[" + j + "].seller.discount", is(sellerDiscount))

          .body("[" + j + "].entries.size()", is(entriesSize));

      for (int i = 0; i < invoices.get(j).getEntries().size(); i++) {
        given().when().get(invoicesUrl).then()
            .body("[" + j + "].entries[" + i + "].description", is(invoices.get(j).getEntries().get(i).getDescription()))
            .body("[" + j + "].entries[" + i + "].netPrice", is(invoices.get(j).getEntries().get(i).getNetPrice().toString()))
            .body("[" + j + "].entries[" + i + "].vatRate",
                is(invoices.get(j).getEntries().get(i).getVatRate().setScale(2, BigDecimal.ROUND_HALF_UP).toString()))
            .body("[" + j + "].entries[" + i + "].quantity", is(invoices.get(j).getEntries().get(i).getQuantity().toString()));
      }
    }
  }

  @Test
  public void verifySizeOfInvoicesArray() {
    given()
        .when()
        .get(invoicesUrl)
        .then()
        .body("$.size()", is(invoices.size()));
  }
}