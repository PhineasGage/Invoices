package pl.coderstrust.accounting.controller;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static pl.coderstrust.accounting.helpers.CompanyProvider.COMPANY_DRUKPOL;
import static pl.coderstrust.accounting.helpers.CompanyProvider.COMPANY_DRUTEX;
import static pl.coderstrust.accounting.helpers.CompanyProvider.COMPANY_WASBUD;
import static pl.coderstrust.accounting.helpers.InsuranceProvider.HEALTH_INSURANCE2;
import static pl.coderstrust.accounting.helpers.InsuranceProvider.HEALTH_INSURANCE3;
import static pl.coderstrust.accounting.helpers.InsuranceProvider.PENSION_INSURANCE3;
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
import pl.coderstrust.accounting.logic.CompanyService;
import pl.coderstrust.accounting.logic.InsuranceService;
import pl.coderstrust.accounting.logic.InvoiceService;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TaxCalculatorControllerTest {

  private static final String TAX_CALCULATOR_SERVICE_PATH = "/taxcalculator";

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private TaxCalculatorController taxCalculatorController;

  @Autowired
  private InvoiceService invoiceService;

  @Autowired
  private InsuranceService insuranceService;

  @Autowired
  private CompanyService companyService;

  private RestTestHelper restTestHelper;

  @PostConstruct
  public void postConstruct() {
    restTestHelper = new RestTestHelper(mockMvc);
  }

  @Before
  public void beforeMethod() {
    insuranceService.clearDatabase();
    invoiceService.clearDatabase();
    companyService.clearDatabase();
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
        .andExpect(jsonPath("$", is("786.00")))
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
        .andExpect(jsonPath("$", is("173.15")))
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
        .andExpect(jsonPath("$", is("115.92")))
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
        .andExpect(jsonPath("$", is("70.20")))
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
        .andExpect(jsonPath("$", is("433.80")))
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
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", is("111.01")))
        .andReturn();
  }

  @Test
  public void shouldReturnFinalIncomeTaxes() throws Exception {
    //given
    for (int i = 0; i < 200; i++) {
      restTestHelper.callRestServiceToAddInvoiceAndReturnId(INVOICE_WASBUD_SPAN_CLAMP_2017);
    }
    insuranceService.saveInsurance(COMPANY_WASBUD.getNip(), HEALTH_INSURANCE2);
    insuranceService.saveInsurance(COMPANY_WASBUD.getNip(), HEALTH_INSURANCE3);
    insuranceService.saveInsurance(COMPANY_WASBUD.getNip(), PENSION_INSURANCE3);

    //when

    //then
    MvcResult result = mockMvc
        .perform(get(TAX_CALCULATOR_SERVICE_PATH + "/FinalIncomeTaxes/" + COMPANY_WASBUD.getNip()))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", is("18798.41")))
        .andReturn();
  }
}