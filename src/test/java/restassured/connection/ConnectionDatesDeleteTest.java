package restassured.connection;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.Is.is;

import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import restassured.Data;

public class ConnectionDatesDeleteTest implements Data {

  private List<String> nipNumbers = Arrays
      .asList(invoiceDrutex_2018.getBuyer().getNip(), invoiceDrutex_2018.getSeller().getNip(), invoiceDrutex_2016.getBuyer().getNip(),
          invoiceDrutex_2016.getSeller().getNip(), invoiceTranspol_2016.getSeller().getNip(), invoiceTranspol_2016.getBuyer().getNip(),
          invoiceWasbud_2017.getBuyer().getNip(), invoiceWasbud_2017.getSeller().getNip(), invoiceWasbud_2018.getSeller().getNip(),
          invoiceWasbud_2018.getBuyer().getNip());

  @Test
  public void whenRequestGetInvoicesThen200() {
    given()
        .when()
        .get(invoicesUrl)
        .then()
        .statusCode(200);
  }

  //  TODO after merge - id problem
  //  @Test
  //  public void whenRequestGetSimpleInvoiceWithCorrectId_Then200() {
  //    given()
  //        .pathParam("id", 0)
  //        .when()
  //        .get(invoicesUrl + "/{id}")
  //        .then()
  //        .statusCode(200);
  //  }


  //  @Test
  //  public void whenRequestGetSimpleInvoiceWithBadId_Then404() {
  //    given()
  //        .pathParam("id", 100)
  //        .when()
  //        .get(invoicesUrl + "/{id}")
  //        .then()
  //        .statusCode(404);
  //  }

  @Test
  public void whenRequestTaxCalculatorGetIncome_Then200() {
    for (String nip : nipNumbers) {
      given()
          .when()
          .get(taxCalculatorUrl + "/income/" + nip)
          .then()
          .statusCode(200);
    }
  }

  @Test
  public void whenRequestTaxCalculatorTaxDue_Then200() {
    for (String nip : nipNumbers) {
      given()
          .when()
          .get(taxCalculatorUrl + "/TaxDue/" + nip)
          .then()
          .statusCode(200);
    }
  }

  @Test
  public void whenRequestTaxCalculatorCosts_Then200() {
    for (String nip : nipNumbers) {
      given()
          .when()
          .get(taxCalculatorUrl + "/Costs/" + nip)
          .then()
          .statusCode(200);
    }
  }

  @Test
  public void whenRequestTaxCalculatorVatPayable_Then200() {
    for (String nip : nipNumbers) {
      given()
          .when()
          .get(taxCalculatorUrl + "/VatPayable/" + nip)
          .then()
          .statusCode(200);
    }
  }

  @Test
  public void whenRequestTaxCalculatorGetProfit_Then200() {
    for (String nip : nipNumbers) {
      given()
          .when()
          .get(taxCalculatorUrl + "/VatPayable/" + nip)
          .then()
          .statusCode(200);
    }
  }


  //  TODO after merge - id problem
  //  @Test
  //  public void whenRequestPutInvoiceWithCorrectID_Then200() {
  //    given()
  //        .contentType(ContentType.JSON)
  //        .body(invoiceDrutex_2018).pathParam("id", 2)
  //        .when()
  //        .put(invoicesUrl + "/{id}")
  //        .then()
  //        .statusCode(200);
  //  }


  //  TODO after merge - id problem

  //  @Test
  //  public void whenRequestDeleteInvoiceCorrectID_Then200() {
  //    given()
  //        .pathParam("id", 0)
  //        .when()
  //        .delete(invoicesUrl + "/{id}")
  //        .then().statusCode(200);
  //  }


  @Test
  public void whenRequestDeleteInvoiceWithBadID_Then404() {
    given()
        .pathParam("id", 100).when()
        .delete(invoicesUrl + "/{id}")
        .then()
        .statusCode(404);
  }

  private String path = invoicesUrl + "/dates?startDate=2017-03-01&endDate=2018-08-09";

  @Test
  public void getInvoicesByIssueDateRange() {
    given().when()
        .get(path)
        .then()
        .statusCode(200);
  }

  //  @Test
  //  public void getInvoicesByIssueDateRange_Size() {
  //    given()
  //        .when()
  //        .get(path)
  //        .then()
  //        .body("$.size()", is(4));
  //  }

  @Test
  public void getInvoicesByIssueDateRange_hasItems() {
    given()
        .when()
        .get(path)
        .then()
        .body("salePlace", hasItems("Radomsko", "Krakow", "Grudziadz"));
  }

  @Test
  public void getInvoicesByIssueDateRange_notHasItem() {
    given()
        .when()
        .get(path)
        .then()
        .body("salePlace", not(hasItems("Bydgoszcz")));
  }

  @Test
  public void verifyIdentifierOfFirstInvoice() {
    given()
        .when()
        .get(path)
        .then()
        .body("[0].identifier", is("ss999"));
  }

  @Test
  public void verifyIdentifierOfSecondInvoice() {
    given()
        .when()
        .get(path)
        .then()
        .body("[1].identifier", is("1/2018"));
  }

  @Test
  public void verifyIdentifierOfThirdInvoice() {
    given()
        .when()
        .get(path)
        .then()
        .body("[2].identifier", is("2/2018"));
  }
}