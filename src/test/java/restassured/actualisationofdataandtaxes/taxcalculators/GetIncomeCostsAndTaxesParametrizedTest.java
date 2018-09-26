package restassured.actualisationofdataandtaxes.taxcalculators;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;
import restassured.Data;

@RunWith(JUnitParamsRunner.class)
public class GetIncomeCostsAndTaxesParametrizedTest implements Data {

  private String pathIncome = taxCalculatorUrl + "/income/";

  private void verifyFinancialData(String path, String expectedValue) {
    given()
        .when()
        .get(path)
        .then()
        .body(containsString(expectedValue));
  }

  @Test
  @Parameters(method = "dataForTestingIncome")
  public void verifyIncomeByNipNumber(String path, String income) {
    verifyFinancialData(path, income);
  }

  private Object[] dataForTestingIncome() {
    return new Object[]{
        new Object[]{pathIncome + invoiceTranspol_2016.getSeller().getNip(), "96.60"},
        new Object[]{pathIncome + invoiceDrutex_2016.getSeller().getNip(), "208.20"},
        new Object[]{pathIncome + invoiceDrutex_2018.getSeller().getNip(), "208.20"},
        new Object[]{pathIncome + invoiceWasbud_2017.getSeller().getNip(), "112.80"},
        new Object[]{pathIncome + invoiceWasbud_2018.getSeller().getNip(), "112.80"},
        new Object[]{pathIncome + invoiceTranspol_2016.getBuyer().getNip(), "0"},
        new Object[]{pathIncome + invoiceDrutex_2016.getBuyer().getNip(), "112.80"},
        new Object[]{pathIncome + invoiceDrutex_2018.getBuyer().getNip(), "96.60"},
        new Object[]{pathIncome + invoiceWasbud_2017.getBuyer().getNip(), "0"},
        new Object[]{pathIncome + invoiceWasbud_2018.getBuyer().getNip(), "208.20"}
    };
  }

  private String pathCosts = taxCalculatorUrl + "/Costs/";

  @Test
  @Parameters(method = "dataForTestingCosts")
  public void verifyCostsByNipNumber(String path, String costs) {
    verifyFinancialData(path, costs);
  }

  private Object[] dataForTestingCosts() {
    return new Object[]{
        new Object[]{pathCosts + invoiceTranspol_2016.getSeller().getNip(), "138.00"},
        new Object[]{pathCosts + invoiceDrutex_2016.getSeller().getNip(), "62.40"},
        new Object[]{pathCosts + invoiceDrutex_2018.getSeller().getNip(), "62.40"},
        new Object[]{pathCosts + invoiceWasbud_2017.getSeller().getNip(), "70.20"},
        new Object[]{pathCosts + invoiceWasbud_2018.getSeller().getNip(), "70.20"},
        new Object[]{pathCosts + invoiceTranspol_2016.getBuyer().getNip(), "147.00"},
        new Object[]{pathCosts + invoiceDrutex_2016.getBuyer().getNip(), "70.20"},
        new Object[]{pathCosts + invoiceDrutex_2018.getBuyer().getNip(), "138.00"},
        new Object[]{pathCosts + invoiceWasbud_2017.getBuyer().getNip(), "147.00"},
        new Object[]{pathCosts + invoiceWasbud_2018.getBuyer().getNip(), "62.40"}
    };
  }

  private String pathTaxDue = taxCalculatorUrl + "/TaxDue/";

  @Test
  @Parameters(method = "dataForTestingTaxDue")
  public void verifyTaxDueByNipNumber(String path, String taxDue) {
    verifyFinancialData(path, taxDue);
  }

  private Object[] dataForTestingTaxDue() {
    return new Object[]{
        new Object[]{pathTaxDue + invoiceTranspol_2016.getSeller().getNip(), "13.44"},
        new Object[]{pathTaxDue + invoiceDrutex_2016.getSeller().getNip(), "24.11"},
        new Object[]{pathTaxDue + invoiceDrutex_2018.getSeller().getNip(), "24.11"},
        new Object[]{pathTaxDue + invoiceWasbud_2017.getSeller().getNip(), "15.96"},
        new Object[]{pathTaxDue + invoiceWasbud_2018.getSeller().getNip(), "15.96"},
        new Object[]{pathTaxDue + invoiceTranspol_2016.getBuyer().getNip(), "0"},
        new Object[]{pathTaxDue + invoiceDrutex_2016.getBuyer().getNip(), "15.96"},
        new Object[]{pathTaxDue + invoiceDrutex_2018.getBuyer().getNip(), "13.44"},
        new Object[]{pathTaxDue + invoiceWasbud_2017.getBuyer().getNip(), "0"},
        new Object[]{pathTaxDue + invoiceWasbud_2018.getBuyer().getNip(), "24.11"}
    };
  }

  private String pathTaxIncluded = taxCalculatorUrl + "/TaxIncluded/";

  @Test
  @Parameters(method = "dataForTestingTaxIncluded")
  public void verifyTaxIncludedByNipNumber(String path, String taxIncluded) {
    verifyFinancialData(path, taxIncluded);
  }

  private Object[] dataForTestingTaxIncluded() {
    return new Object[]{
        new Object[]{pathTaxIncluded + invoiceTranspol_2016.getSeller().getNip(), "19.20"},
        new Object[]{pathTaxIncluded + invoiceDrutex_2016.getSeller().getNip(), "4.37"},
        new Object[]{pathTaxIncluded + invoiceDrutex_2018.getSeller().getNip(), "4.37"},
        new Object[]{pathTaxIncluded + invoiceWasbud_2017.getSeller().getNip(), "4.91"},
        new Object[]{pathTaxIncluded + invoiceWasbud_2018.getSeller().getNip(), "4.91"},
        new Object[]{pathTaxIncluded + invoiceTranspol_2016.getBuyer().getNip(), "25.03"},
        new Object[]{pathTaxIncluded + invoiceDrutex_2016.getBuyer().getNip(), "4.91"},
        new Object[]{pathTaxIncluded + invoiceDrutex_2018.getBuyer().getNip(), "19.20"},
        new Object[]{pathTaxIncluded + invoiceWasbud_2017.getBuyer().getNip(), "25.03"},
        new Object[]{pathTaxIncluded + invoiceWasbud_2018.getBuyer().getNip(), "4.37"}
    };
  }

  private String pathVatPayable = taxCalculatorUrl + "/VatPayable/";

  @Test
  @Parameters(method = "dataForTestingVatPayable")
  public void verifyVatPayableByNip(String path, String vatPayable) {
    verifyFinancialData(path, vatPayable);
  }

  private Object[] dataForTestingVatPayable() {
    return new Object[]{
        new Object[]{pathVatPayable + invoiceTranspol_2016.getSeller().getNip(), "-5.76"},
        new Object[]{pathVatPayable + invoiceDrutex_2016.getSeller().getNip(), "19.75"},
        new Object[]{pathVatPayable + invoiceDrutex_2018.getSeller().getNip(), "19.75"},
        new Object[]{pathVatPayable + invoiceWasbud_2017.getSeller().getNip(), "11.05"},
        new Object[]{pathVatPayable + invoiceWasbud_2018.getSeller().getNip(), "11.05"},
        new Object[]{pathVatPayable + invoiceTranspol_2016.getBuyer().getNip(), "0"},
        new Object[]{pathVatPayable + invoiceDrutex_2016.getBuyer().getNip(), "11.05"},
        new Object[]{pathVatPayable + invoiceDrutex_2018.getBuyer().getNip(), "-5.76"},
        new Object[]{pathVatPayable + invoiceWasbud_2017.getBuyer().getNip(), "0"},
        new Object[]{pathVatPayable + invoiceWasbud_2018.getBuyer().getNip(), "19.75"}
    };
  }
}