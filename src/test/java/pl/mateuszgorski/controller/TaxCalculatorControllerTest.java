package pl.mateuszgorski.controller;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static pl.mateuszgorski.helpers.CompanyProvider.COMPANY_DRUKPOL;
import static pl.mateuszgorski.helpers.CompanyProvider.COMPANY_DRUTEX;
import static pl.mateuszgorski.helpers.CompanyProvider.COMPANY_WASBUD;
import static pl.mateuszgorski.helpers.InvoiceProvider.INVOICE_BYDGOSZCZ_2018;
import static pl.mateuszgorski.helpers.InvoiceProvider.INVOICE_GRUDZIADZ_2017;
import static pl.mateuszgorski.helpers.InvoiceProvider.INVOICE_KRAKOW_2018;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import pl.mateuszgorski.logic.InvoiceService;
import pl.mateuszgorski.model.Invoice;
import pl.mateuszgorski.configuration.JacksonProvider;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TaxCalculatorControllerTest {

  private static final String TAX_CALCULATOR_SERVICE_PATH = "/taxcalculator";
  private static final String INVOICE_SERVICE_PATH = "/invoices";
  private static final MediaType JSON_CONTENT_TYPE = MediaType.APPLICATION_JSON_UTF8;

  @Autowired
  private InvoiceService invoiceService;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private TaxCalculatorController taxCalculatorController;

  @Before
  public void beforeMethod() {
    invoiceService.clearDatabase();
  }

  @Test
  public void contexLoads() throws Exception {
    assertThat(taxCalculatorController, is(notNullValue()));
  }

  @Test
  public void shouldGetIncome() throws Exception {
    //given
    callRestServiceToAddInvoiceAndReturnId(INVOICE_KRAKOW_2018);

    //when

    //then
    MvcResult result = mockMvc
        .perform(get(TAX_CALCULATOR_SERVICE_PATH + "/Income/" + COMPANY_DRUTEX.getNip()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", is(138.0)))
        .andReturn();
  }

  @Test
  public void shouldGetTaxDue() throws Exception {
    //given
    callRestServiceToAddInvoiceAndReturnId(INVOICE_KRAKOW_2018);
    callRestServiceToAddInvoiceAndReturnId(INVOICE_BYDGOSZCZ_2018);

    //when

    //then
    MvcResult result = mockMvc
        .perform(get(TAX_CALCULATOR_SERVICE_PATH + "/TaxDue/" + COMPANY_DRUTEX.getNip()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", is(19.2)))
        .andReturn();
  }

  @Test
  public void shouldGetTaxIncluded() throws Exception {
    //given
    callRestServiceToAddInvoiceAndReturnId(INVOICE_GRUDZIADZ_2017);
    callRestServiceToAddInvoiceAndReturnId(INVOICE_BYDGOSZCZ_2018);

    //when

    //then
    MvcResult result = mockMvc
        .perform(get(TAX_CALCULATOR_SERVICE_PATH + "/TaxIncluded/" + COMPANY_DRUKPOL.getNip()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", is(11.592)))
        .andReturn();
  }

  @Test
  public void shouldGetCosts() throws Exception {
    //given
    callRestServiceToAddInvoiceAndReturnId(INVOICE_GRUDZIADZ_2017);
    callRestServiceToAddInvoiceAndReturnId(INVOICE_BYDGOSZCZ_2018);

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
    callRestServiceToAddInvoiceAndReturnId(INVOICE_GRUDZIADZ_2017);
    callRestServiceToAddInvoiceAndReturnId(INVOICE_BYDGOSZCZ_2018);

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
    callRestServiceToAddInvoiceAndReturnId(INVOICE_GRUDZIADZ_2017);
    callRestServiceToAddInvoiceAndReturnId(INVOICE_BYDGOSZCZ_2018);

    //when

    //then
    MvcResult result = mockMvc
        .perform(get(TAX_CALCULATOR_SERVICE_PATH + "/VatPayable/" + COMPANY_WASBUD.getNip()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", is(11.592)))
        .andReturn();
  }

  // TODO return value is never used - maybe you can simplify the method
  // TODO duplicate with InvoiceControllerTest - maybe you can move it to some kind of helper? :)
  private int callRestServiceToAddInvoiceAndReturnId(Invoice invoice) throws Exception {
    String response =
        mockMvc
            .perform(post(INVOICE_SERVICE_PATH)
                .content(convertToJson(invoice))
                .contentType(JSON_CONTENT_TYPE))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse().getContentAsString();
    return Integer.parseInt(response);
  }

  private String convertToJson(Object object) {
    try {
      return JacksonProvider.getObjectMapper().writeValueAsString(object);
    } catch (JsonProcessingException exception) {
      exception.printStackTrace();
    }
    return null; // TODO maybe "" ? :)
  }
}