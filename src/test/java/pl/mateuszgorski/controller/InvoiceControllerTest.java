package pl.coderstrust.accounting.controller;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static pl.coderstrust.accounting.helpers.InvoiceProvider.INVOICE_BLANK_BUYER_CITY;
import static pl.coderstrust.accounting.helpers.InvoiceProvider.INVOICE_BLANK_IDENTIFIER;
import static pl.coderstrust.accounting.helpers.InvoiceProvider.INVOICE_DRUTEX_LINK_2016;
import static pl.coderstrust.accounting.helpers.InvoiceProvider.INVOICE_DRUTEX_SPAN_CLAMP_SUPPORT_2018;
import static pl.coderstrust.accounting.helpers.InvoiceProvider.INVOICE_TRANSPOL_SPAN_CLAMP_SUPPORT_2016;
import static pl.coderstrust.accounting.helpers.InvoiceProvider.INVOICE_WASBUD_LINK_2018;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.List;
import javax.annotation.PostConstruct;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import pl.coderstrust.accounting.database.impl.file.helpers.InvoiceConverter;
import pl.coderstrust.accounting.helpers.InvoiceAssertionHelper;
import pl.coderstrust.accounting.helpers.RestTestHelper;
import pl.coderstrust.accounting.logic.InvoiceService;
import pl.coderstrust.accounting.model.Invoice;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class InvoiceControllerTest {

  private static final String INVOICE_SERVICE_PATH = "/invoices";
  private static final MediaType JSON_CONTENT_TYPE = MediaType.APPLICATION_JSON_UTF8;

  @Autowired
  private ObjectMapper mapper;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private InvoiceController invoiceController;

  @Autowired
  private InvoiceService invoiceService;

  @Autowired
  private InvoiceConverter invoiceConverter;

  private RestTestHelper restTestHelper;

  private InvoiceAssertionHelper invoiceAssertion = new InvoiceAssertionHelper();

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
    assertThat(invoiceController, is(notNullValue()));
  }

  @Test
  public void shouldCheckSaveInvoiceRequest() throws Exception {
    int idResponse = restTestHelper.callRestServiceToAddInvoiceAndReturnId(INVOICE_DRUTEX_LINK_2016);
    Invoice savedInvoice = restTestHelper.callRestServiceToReturnInvoiceById(idResponse);

    invoiceAssertion.assertInvoice(idResponse, INVOICE_DRUTEX_LINK_2016, savedInvoice);
  }

  @Test
  public void shouldReturnErrorMessageCorrespondingToIncorrectInvoiceField() throws Exception {
    mockMvc.perform(
        post(INVOICE_SERVICE_PATH)
            .content(invoiceConverter.convertInvoiceToJson(INVOICE_BLANK_BUYER_CITY))
            .contentType(JSON_CONTENT_TYPE)
    )
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$[0]", is("Buyer city not found")));
  }

  @Test
  public void getInvoices() throws Exception {
    int firstResponse = restTestHelper.callRestServiceToAddInvoiceAndReturnId(INVOICE_DRUTEX_SPAN_CLAMP_SUPPORT_2018);
    int secondResponse = restTestHelper.callRestServiceToAddInvoiceAndReturnId(INVOICE_TRANSPOL_SPAN_CLAMP_SUPPORT_2016);
    int thirdResponse = restTestHelper.callRestServiceToAddInvoiceAndReturnId(INVOICE_DRUTEX_LINK_2016);
    mockMvc
        .perform(get(INVOICE_SERVICE_PATH))
        .andDo(print()).andExpect(status()
        .isOk())
        .andExpect(jsonPath("$", hasSize(3)));
    Invoice firstSavedInvoice = restTestHelper.callRestServiceToReturnInvoiceById(firstResponse);
    Invoice secondSavedInvoice = restTestHelper.callRestServiceToReturnInvoiceById(secondResponse);
    Invoice thirdSavedInvoice = restTestHelper.callRestServiceToReturnInvoiceById(thirdResponse);
    invoiceAssertion.assertInvoice(firstResponse, INVOICE_DRUTEX_SPAN_CLAMP_SUPPORT_2018, firstSavedInvoice);
    invoiceAssertion.assertInvoice(secondResponse, INVOICE_TRANSPOL_SPAN_CLAMP_SUPPORT_2016, secondSavedInvoice);
    invoiceAssertion.assertInvoice(thirdResponse, INVOICE_DRUTEX_LINK_2016, thirdSavedInvoice);
  }

  @Test
  public void getInvoicesByIssueDateRange() throws Exception {
    restTestHelper.callRestServiceToAddInvoiceAndReturnId(INVOICE_DRUTEX_SPAN_CLAMP_SUPPORT_2018);
    int idResponseA = restTestHelper.callRestServiceToAddInvoiceAndReturnId(INVOICE_TRANSPOL_SPAN_CLAMP_SUPPORT_2016);
    int idResponseB = restTestHelper.callRestServiceToAddInvoiceAndReturnId(INVOICE_DRUTEX_LINK_2016);

    LocalDate startDate = LocalDate.of(2015, 4, 12);
    LocalDate endDate = LocalDate.of(2017, 4, 12);

    String url = String
        .format("/dates?startDate=%1$s&endDate=%2$s", startDate, endDate);

    String jsonString = mockMvc
        .perform(get(INVOICE_SERVICE_PATH + url))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(2)))
        .andReturn()
        .getResponse()
        .getContentAsString();
    List<Invoice> invoices = mapper.readValue(jsonString, new TypeReference<List<Invoice>>() {
    });
    invoiceAssertion
        .assertInvoice(idResponseA, INVOICE_TRANSPOL_SPAN_CLAMP_SUPPORT_2016, restTestHelper.callRestServiceToReturnInvoiceById(idResponseA));
    invoiceAssertion.assertInvoice(idResponseB, INVOICE_DRUTEX_LINK_2016, restTestHelper.callRestServiceToReturnInvoiceById(idResponseB));
  }

  @Test
  public void getSingleInvoice() throws Exception {
    int idResponse = restTestHelper.callRestServiceToAddInvoiceAndReturnId(INVOICE_DRUTEX_SPAN_CLAMP_SUPPORT_2018);
    Invoice firstSavedInvoice = restTestHelper.callRestServiceToReturnInvoiceById(idResponse);

    invoiceAssertion.assertInvoice(idResponse, INVOICE_DRUTEX_SPAN_CLAMP_SUPPORT_2018, firstSavedInvoice);
  }

  @Test
  public void shouldReturnErrorCausedByNotExistingId() throws Exception {
    mockMvc
        .perform(get(INVOICE_SERVICE_PATH + "/0"))
        .andExpect(status().isNotFound());
  }

  @Test
  public void updateInvoiceById() throws Exception {
    int idResponse = restTestHelper.callRestServiceToAddInvoiceAndReturnId(INVOICE_WASBUD_LINK_2018);

    mockMvc
        .perform(put(INVOICE_SERVICE_PATH + "/" + idResponse)
            .contentType(JSON_CONTENT_TYPE)
            .content(invoiceConverter.convertInvoiceToJson(INVOICE_DRUTEX_LINK_2016)))
        .andExpect(status().isOk());
    Invoice firstSavedInvoice = restTestHelper.callRestServiceToReturnInvoiceById(idResponse);

    invoiceAssertion.assertInvoice(idResponse, INVOICE_DRUTEX_LINK_2016, firstSavedInvoice);
  }

  @Test
  public void shouldReturnErrorCausedByNotExistingIdPassedToUpdate() throws Exception {
    mockMvc
        .perform(put(INVOICE_SERVICE_PATH + "/0")
            .content(invoiceConverter.convertInvoiceToJson(INVOICE_TRANSPOL_SPAN_CLAMP_SUPPORT_2016))
            .contentType(JSON_CONTENT_TYPE))
        .andExpect(status().isNotFound());
  }

  @Test
  public void shouldReturnErrorCausedByNotValidInvoiceIdentifier() throws Exception {
    int invoiceId = restTestHelper.callRestServiceToAddInvoiceAndReturnId(INVOICE_DRUTEX_SPAN_CLAMP_SUPPORT_2018);

    mockMvc
        .perform(put(INVOICE_SERVICE_PATH + "/" + invoiceId)
            .contentType(JSON_CONTENT_TYPE)
            .content(invoiceConverter.convertInvoiceToJson(INVOICE_BLANK_IDENTIFIER)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$[0]", is("Identifier not found")));
  }

  @Test
  public void removeInvoiceById() throws Exception {
    int firstResponse = restTestHelper.callRestServiceToAddInvoiceAndReturnId(INVOICE_DRUTEX_SPAN_CLAMP_SUPPORT_2018);
    int secondResponse = restTestHelper.callRestServiceToAddInvoiceAndReturnId(INVOICE_TRANSPOL_SPAN_CLAMP_SUPPORT_2016);
    int thirdResponse = restTestHelper.callRestServiceToAddInvoiceAndReturnId(INVOICE_DRUTEX_LINK_2016);

    mockMvc
        .perform(delete(INVOICE_SERVICE_PATH + "/" + firstResponse))
        .andExpect(status().isOk());

    mockMvc
        .perform(get(INVOICE_SERVICE_PATH))
        .andExpect(jsonPath("$", hasSize(2)));

    Invoice secondSavedInvoice = restTestHelper.callRestServiceToReturnInvoiceById(secondResponse);
    Invoice thirdSavedInvoice = restTestHelper.callRestServiceToReturnInvoiceById(thirdResponse);

    invoiceAssertion.assertInvoice(secondResponse, INVOICE_TRANSPOL_SPAN_CLAMP_SUPPORT_2016, secondSavedInvoice);
    invoiceAssertion.assertInvoice(thirdResponse, INVOICE_DRUTEX_LINK_2016, thirdSavedInvoice);
  }

  @Test
  public void shouldReturnErrorCausedByNotExistingIdPassedToDeleteRequest() throws Exception {
    mockMvc
        .perform(delete(INVOICE_SERVICE_PATH + "/0"))
        .andExpect(status().isNotFound());
  }
}