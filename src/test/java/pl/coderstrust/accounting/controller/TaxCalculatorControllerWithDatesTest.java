package pl.coderstrust.accounting.controller;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static pl.coderstrust.accounting.helpers.CompanyProvider.COMPANY_DRUKPOL;
import static pl.coderstrust.accounting.helpers.CompanyProvider.COMPANY_DRUTEX;
import static pl.coderstrust.accounting.helpers.CompanyProvider.COMPANY_WASBUD;
import static pl.coderstrust.accounting.helpers.InvoiceProvider.INVOICE_DRUTEX_LINK_2016;
import static pl.coderstrust.accounting.helpers.InvoiceProvider.INVOICE_DRUTEX_SPAN_CLAMP_SUPPORT_2018;
import static pl.coderstrust.accounting.helpers.InvoiceProvider.INVOICE_WASBUD_SPAN_CLAMP_2017;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import pl.coderstrust.accounting.helpers.InsuranceProvider;
import pl.coderstrust.accounting.helpers.RestTestHelper;
import pl.coderstrust.accounting.logic.CompanyService;
import pl.coderstrust.accounting.logic.InsuranceService;
import pl.coderstrust.accounting.logic.InvoiceService;
import pl.coderstrust.accounting.security.Account;
import pl.coderstrust.accounting.security.AccountRepository;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TaxCalculatorControllerWithDatesTest {

  private static final String TAX_CALCULATOR_SERVICE_PATH = "/taxcalculatorDates";
  private static final LocalDate START_DATE = LocalDate.of(2013, 1, 10);
  private static final LocalDate END_DATE = LocalDate.of(2018, 12, 31);
  private static final String URL = String.format("/?startDate=%1$s&endDate=%2$s", START_DATE, END_DATE);

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper mapper;

  @Autowired
  private TaxCalculatorControllerWithDates taxCalculatorControllerWithDates;

  @Autowired
  private InvoiceService invoiceService;

  @Autowired
  private InsuranceService insuranceService;

  @Autowired
  private CompanyService companyService;

  @Autowired
  private AccountRepository accountRepository;

  private RestTestHelper restTestHelper;

  @PostConstruct
  public void postConstruct() {
    restTestHelper = new RestTestHelper(mockMvc);
  }

  @Before
  public void beforeMethod() {
    accountRepository.save(new Account("user", "password"));
    insuranceService.clearDatabase();
    invoiceService.clearDatabase();
    companyService.clearDatabase();
  }

  @Test
  public void contexLoads() {
    assertThat(taxCalculatorControllerWithDates, is(notNullValue()));
  }

  @Test
  @WithMockUser
  public void getIncome() throws Exception {
    //given
    restTestHelper.callRestServiceToAddInvoiceAndReturnId(INVOICE_DRUTEX_SPAN_CLAMP_SUPPORT_2018);

    //when

    //then
    MvcResult result = mockMvc
        .perform(get(TAX_CALCULATOR_SERVICE_PATH + "/income/" + COMPANY_DRUTEX.getNip() + URL))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", is("786.00")))
        .andReturn();
  }

  @Test
  @WithMockUser
  public void shouldGetTaxDue() throws Exception {
    //given
    restTestHelper.callRestServiceToAddInvoiceAndReturnId(INVOICE_DRUTEX_SPAN_CLAMP_SUPPORT_2018);
    restTestHelper.callRestServiceToAddInvoiceAndReturnId(INVOICE_DRUTEX_LINK_2016);

    //when

    //then
    MvcResult result = mockMvc
        .perform(get(TAX_CALCULATOR_SERVICE_PATH + "/TaxDue/" + COMPANY_DRUTEX.getNip() + URL))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", is("173.15")))
        .andReturn();
  }

  @Test
  @WithMockUser
  public void shouldGetTaxIncluded() throws Exception {
    //given
    restTestHelper.callRestServiceToAddInvoiceAndReturnId(INVOICE_WASBUD_SPAN_CLAMP_2017);
    restTestHelper.callRestServiceToAddInvoiceAndReturnId(INVOICE_DRUTEX_LINK_2016);

    //when

    //then
    MvcResult result = mockMvc
        .perform(get(TAX_CALCULATOR_SERVICE_PATH + "/TaxIncluded/" + COMPANY_DRUKPOL.getNip() + URL))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", is("115.92")))
        .andReturn();
  }

  @Test
  @WithMockUser
  public void shouldGetCosts() throws Exception {
    //given
    restTestHelper.callRestServiceToAddInvoiceAndReturnId(INVOICE_WASBUD_SPAN_CLAMP_2017);
    restTestHelper.callRestServiceToAddInvoiceAndReturnId(INVOICE_DRUTEX_LINK_2016);

    //when

    //then
    MvcResult result = mockMvc
        .perform(get(TAX_CALCULATOR_SERVICE_PATH + "/Costs/" + COMPANY_WASBUD.getNip() + URL))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", is("70.20")))
        .andReturn();
  }

  @Test
  @WithMockUser
  public void shouldGetProfit() throws Exception {
    //given
    restTestHelper.callRestServiceToAddInvoiceAndReturnId(INVOICE_WASBUD_SPAN_CLAMP_2017);
    restTestHelper.callRestServiceToAddInvoiceAndReturnId(INVOICE_DRUTEX_LINK_2016);

    //when

    //then
    MvcResult result = mockMvc
        .perform(get(TAX_CALCULATOR_SERVICE_PATH + "/Profit/" + COMPANY_WASBUD.getNip() + URL))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", is("433.80")))
        .andReturn();
  }

  @Test
  @WithMockUser
  public void shouldGetVatPayable() throws Exception {
    //given
    restTestHelper.callRestServiceToAddInvoiceAndReturnId(INVOICE_WASBUD_SPAN_CLAMP_2017);
    restTestHelper.callRestServiceToAddInvoiceAndReturnId(INVOICE_DRUTEX_LINK_2016);

    //when

    //then
    MvcResult result = mockMvc
        .perform(get(TAX_CALCULATOR_SERVICE_PATH + "/VatPayable/" + COMPANY_WASBUD.getNip() + URL))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", is("111.01")))
        .andReturn();
  }

  @Test
  @WithMockUser
  public void shouldReturnFinalIncomeTaxes() throws Exception {
    //given
    for (int i = 0; i < 200; i++) {
      restTestHelper.callRestServiceToAddInvoiceAndReturnId(INVOICE_WASBUD_SPAN_CLAMP_2017);
    }
    insuranceService.saveInsurance(COMPANY_WASBUD.getNip(), InsuranceProvider.HEALTH_INSURANCE2);
    insuranceService.saveInsurance(COMPANY_WASBUD.getNip(), InsuranceProvider.HEALTH_INSURANCE3);
    insuranceService.saveInsurance(COMPANY_WASBUD.getNip(), InsuranceProvider.PENSION_INSURANCE3);

    //when

    //then
    MvcResult result = mockMvc
        .perform(get(TAX_CALCULATOR_SERVICE_PATH + "/FinalIncomeTaxes/" + COMPANY_WASBUD.getNip() + URL))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", is("18798.41")))
        .andReturn();
  }

  @Test
  @WithMockUser
  public void getIncomTaxesDetails() throws Exception {
    //given
    for (int i = 0; i < 200; i++) {
      restTestHelper.callRestServiceToAddInvoiceAndReturnId(INVOICE_WASBUD_SPAN_CLAMP_2017);
    }
    insuranceService.saveInsurance(COMPANY_WASBUD.getNip(), InsuranceProvider.HEALTH_INSURANCE2);
    insuranceService.saveInsurance(COMPANY_WASBUD.getNip(), InsuranceProvider.HEALTH_INSURANCE3);
    insuranceService.saveInsurance(COMPANY_WASBUD.getNip(), InsuranceProvider.PENSION_INSURANCE3);

    Map<String, BigDecimal> expected = new LinkedHashMap<>();
    expected.put("Income: ", BigDecimal.valueOf(100800.00).setScale(2, RoundingMode.HALF_UP));
    expected.put("Costs: ", BigDecimal.valueOf(0.00).setScale(2, RoundingMode.HALF_UP));
    expected.put("Income - Costs: ", BigDecimal.valueOf(100800.00).setScale(2, RoundingMode.HALF_UP));
    expected.put("Pension insurance: ", BigDecimal.valueOf(600.00).setScale(2, RoundingMode.HALF_UP));
    expected.put("Income - Costs - Pension Insurance: ", BigDecimal.valueOf(100200.00).setScale(2, RoundingMode.HALF_UP));
    expected.put("Tax Base: ", BigDecimal.valueOf(100200.00).setScale(2, RoundingMode.HALF_UP));
    expected.put("Income Tax: ", BigDecimal.valueOf(20090.08).setScale(2, RoundingMode.HALF_UP));
    expected.put("Health insurance: ", BigDecimal.valueOf(1500.00).setScale(2, RoundingMode.HALF_UP));
    expected.put("Health Insurance For Reducing: ", BigDecimal.valueOf(1291.67).setScale(2, RoundingMode.HALF_UP));
    expected.put("Income tax - health insurance: ", BigDecimal.valueOf(18798.41).setScale(2, RoundingMode.HALF_UP));
    expected.put("Final Income Tax Value: ", BigDecimal.valueOf(18798.41).setScale(2, RoundingMode.HALF_UP));

    //when

    //then
    String response = mockMvc
        .perform(get(TAX_CALCULATOR_SERVICE_PATH + "/IncomeTaxesDetails/" + COMPANY_WASBUD.getNip() + URL))
        .andDo(print())
        .andExpect(status().isOk())
        .andReturn().getResponse().getContentAsString();
    Map<String, BigDecimal> actual = mapper.readValue(response, new TypeReference<Map<String, BigDecimal>>() {
    });

    assertThat(actual, is(equalTo(expected)));
  }
}