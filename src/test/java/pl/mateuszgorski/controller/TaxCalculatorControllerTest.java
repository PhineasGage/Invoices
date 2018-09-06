package pl.coderstrust.accounting.controller;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static pl.coderstrust.accounting.helpers.CompanyProvider.COMPANY_DRUKPOL;
import static pl.coderstrust.accounting.helpers.CompanyProvider.COMPANY_DRUTEX;
import static pl.coderstrust.accounting.helpers.CompanyProvider.COMPANY_WASBUD;
import static pl.coderstrust.accounting.helpers.InvoiceProvider.INVOICE_DRUTEX_LINK_2016;
import static pl.coderstrust.accounting.helpers.InvoiceProvider.INVOICE_DRUTEX_SPAN_CLAMP_SUPPORT_2018;
import static pl.coderstrust.accounting.helpers.InvoiceProvider.INVOICE_WASBUD_SPAN_CLAMP_2017;

import javax.annotation.PostConstruct;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import pl.coderstrust.accounting.helpers.RestTestHelper;
import pl.coderstrust.accounting.logic.InvoiceService;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TaxCalculatorControllerTest {

  private static final String TAX_CALCULATOR_SERVICE_PATH = "/taxcalculator";

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private InvoiceService invoiceService;

  @Autowired
  private TaxCalculatorController taxCalculatorController;

  private RestTestHelper restTestHelper;

  @PostConstruct
  public void postConstruct() {
    restTestHelper = new RestTestHelper(mockMvc);
  }

  @Before
  public void beforeMethod() {
    invoiceService.clearDatabase();
  }

  @Test
  public void contexLoads() {
    assertThat(taxCalculatorController, is(notNullValue()));
  }

  @Test
  public void shouldGetIncome() throws Exception {
    //given
    restTestHelper.callRestServiceToAddInvoiceAndReturnId(INVOICE_DRUTEX_SPAN_CLAMP_SUPPORT_2018);

    //when

    //then
    MvcResult result = mockMvc
        .perform(get(TAX_CALCULATOR_SERVICE_PATH + "/income/" + COMPANY_DRUTEX.getNip()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", is(138.0)))
        .andReturn();
  }

  @Test
  public void shouldGetTaxDue() throws Exception {
    //given
    restTestHelper.callRestServiceToAddInvoiceAndReturnId(INVOICE_DRUTEX_SPAN_CLAMP_SUPPORT_2018);
    restTestHelper.callRestServiceToAddInvoiceAndReturnId(INVOICE_DRUTEX_LINK_2016);

    //when

    //then
    MvcResult result = mockMvc
        .perform(get(TAX_CALCULATOR_SERVICE_PATH + "/TaxDue/" + COMPANY_DRUTEX.getNip()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", is(22.314)))
        .andReturn();
  }

  @Test
  public void shouldGetTaxIncluded() throws Exception {
    //given
    restTestHelper.callRestServiceToAddInvoiceAndReturnId(INVOICE_WASBUD_SPAN_CLAMP_2017);
    restTestHelper.callRestServiceToAddInvoiceAndReturnId(INVOICE_DRUTEX_LINK_2016);

    //when

    //then
    MvcResult result = mockMvc
        .perform(get(TAX_CALCULATOR_SERVICE_PATH + "/TaxIncluded/" + COMPANY_DRUKPOL.getNip()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", is(10.332)))
        .andReturn();
  }

  @Test
  public void shouldGetCosts() throws Exception {
    //given
    restTestHelper.callRestServiceToAddInvoiceAndReturnId(INVOICE_WASBUD_SPAN_CLAMP_2017);
    restTestHelper.callRestServiceToAddInvoiceAndReturnId(INVOICE_DRUTEX_LINK_2016);

    //when

    //then
    MvcResult result = mockMvc
        .perform(get(TAX_CALCULATOR_SERVICE_PATH + "/Costs/" + COMPANY_WASBUD.getNip()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", is(70.2)))
        .andReturn();
  }

  @Test
  public void shouldGetProfit() throws Exception {
    //given
    restTestHelper.callRestServiceToAddInvoiceAndReturnId(INVOICE_WASBUD_SPAN_CLAMP_2017);
    restTestHelper.callRestServiceToAddInvoiceAndReturnId(INVOICE_DRUTEX_LINK_2016);

    //when

    //then
    MvcResult result = mockMvc
        .perform(get(TAX_CALCULATOR_SERVICE_PATH + "/Profit/" + COMPANY_WASBUD.getNip()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", is(-19.8)))
        .andReturn();
  }

  @Test
  public void shouldGetVatPayable() throws Exception {
    //given
    restTestHelper.callRestServiceToAddInvoiceAndReturnId(INVOICE_WASBUD_SPAN_CLAMP_2017);
    restTestHelper.callRestServiceToAddInvoiceAndReturnId(INVOICE_DRUTEX_LINK_2016);

    //when

    //then
    MvcResult result = mockMvc
        .perform(get(TAX_CALCULATOR_SERVICE_PATH + "/VatPayable/" + COMPANY_WASBUD.getNip()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", is(5.418)))
        .andReturn();
  }
}