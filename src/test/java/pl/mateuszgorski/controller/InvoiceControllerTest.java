package pl.mateuszgorski.controller;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static pl.mateuszgorski.helpers.InvoiceProvider.INVOICE_BLANK_BUYER_CITY;
import static pl.mateuszgorski.helpers.InvoiceProvider.INVOICE_BLANK_IDENTIFIER;
import static pl.mateuszgorski.helpers.InvoiceProvider.INVOICE_BYDGOSZCZ_2018;
import static pl.mateuszgorski.helpers.InvoiceProvider.INVOICE_CHELMNO_2016;
import static pl.mateuszgorski.helpers.InvoiceProvider.INVOICE_KRAKOW_2018;
import static pl.mateuszgorski.helpers.InvoiceProvider.INVOICE_RADOMSKO_2018;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import pl.mateuszgorski.logic.InvoiceService;
import pl.mateuszgorski.model.Invoice;
import pl.mateuszgorski.configuration.JacksonProvider;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class InvoiceControllerTest {

  private static final String INVOICE_SERVICE_PATH = "/invoices";
  private static final MediaType JSON_CONTENT_TYPE = MediaType.APPLICATION_JSON_UTF8;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private InvoiceController invoiceController;

  @Autowired
  private InvoiceService invoiceService;

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
    int firstResponse = callRestServiceToAddInvoiceAndReturnId(INVOICE_KRAKOW_2018); // TODO if not needed why do you add?
    int secondResponse = callRestServiceToAddInvoiceAndReturnId(INVOICE_BYDGOSZCZ_2018);

    String savedInvoice = mockMvc
        .perform(get(INVOICE_SERVICE_PATH + "/" + secondResponse))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(secondResponse)))
        .andExpect(jsonPath("$.identifier", is(INVOICE_BYDGOSZCZ_2018.getIdentifier())))
        .andExpect(jsonPath("$.salePlace", is(INVOICE_BYDGOSZCZ_2018.getSalePlace())))
        .andExpect(jsonPath("$.buyer.name", is(INVOICE_BYDGOSZCZ_2018.getBuyer().getName())))
        .andExpect(jsonPath("$.buyer.nip", is(INVOICE_BYDGOSZCZ_2018.getBuyer().getNip())))
        .andExpect(jsonPath("$.buyer.street", is(INVOICE_BYDGOSZCZ_2018.getBuyer().getStreet())))
        .andExpect(jsonPath("$.buyer.postalCode", is(INVOICE_BYDGOSZCZ_2018.getBuyer().getPostalCode())))
        .andExpect(jsonPath("$.buyer.discount", is(INVOICE_BYDGOSZCZ_2018.getBuyer().getDiscount().doubleValue())))
        .andExpect(jsonPath("$.seller.name", is(INVOICE_BYDGOSZCZ_2018.getSeller().getName())))
        .andExpect(jsonPath("$.seller.nip", is(INVOICE_BYDGOSZCZ_2018.getSeller().getNip())))
        .andExpect(jsonPath("$.seller.street", is(INVOICE_BYDGOSZCZ_2018.getSeller().getStreet())))
        .andExpect(jsonPath("$.seller.postalCode", is(INVOICE_BYDGOSZCZ_2018.getSeller().getPostalCode())))
        .andExpect(jsonPath("$.seller.discount", is(INVOICE_BYDGOSZCZ_2018.getSeller().getDiscount().doubleValue())))
        .andExpect(jsonPath("$.entries[0].description", is(INVOICE_BYDGOSZCZ_2018.getEntries().get(0).getDescription())))
        .andExpect(jsonPath("$.entries[0].netPrice", is(INVOICE_BYDGOSZCZ_2018.getEntries().get(0).getNetPrice().intValue())))
        .andExpect(jsonPath("$.entries[0].vatRate", is(INVOICE_BYDGOSZCZ_2018.getEntries().get(0).getVatRate().toString())))
        .andExpect(jsonPath("$.entries[0].quantity", is(INVOICE_BYDGOSZCZ_2018.getEntries().get(0).getQuantity().intValue())))
        .andReturn().getResponse().getContentAsString();
  }

  @Test
  public void shouldReturnErrorMessageCorrespondingToIncorrectInvoiceField() throws Exception {
    mockMvc.perform(
        post(INVOICE_SERVICE_PATH)
            .content(convertToJson(INVOICE_BLANK_BUYER_CITY))
            .contentType(JSON_CONTENT_TYPE)
    )
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$[0]", is("Buyer city not found")));
  }

  @Test
  public void getInvoices() throws Exception {
    int firstResponse = callRestServiceToAddInvoiceAndReturnId(INVOICE_KRAKOW_2018); // TODO if not needed why do you add?
    int secondResponse = callRestServiceToAddInvoiceAndReturnId(INVOICE_CHELMNO_2016); // TODO if not needed why do you add?
    int thirdResponse = callRestServiceToAddInvoiceAndReturnId(INVOICE_BYDGOSZCZ_2018);

    mockMvc
        .perform(get(INVOICE_SERVICE_PATH))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(3))) // TODO add assertions to all 3 :) HINT - you can use helper method with parameter
        .andExpect(jsonPath("$[2].id", is(thirdResponse)))
        .andExpect(jsonPath("$[2].identifier", is(INVOICE_BYDGOSZCZ_2018.getIdentifier())))
        .andExpect(jsonPath("$[2].salePlace", is(INVOICE_BYDGOSZCZ_2018.getSalePlace())))
        .andExpect(jsonPath("$[2].buyer.name", is(INVOICE_BYDGOSZCZ_2018.getBuyer().getName())))
        .andExpect(jsonPath("$[2].buyer.nip", is(INVOICE_BYDGOSZCZ_2018.getBuyer().getNip())))
        .andExpect(jsonPath("$[2].buyer.street", is(INVOICE_BYDGOSZCZ_2018.getBuyer().getStreet())))
        .andExpect(jsonPath("$[2].buyer.postalCode", is(INVOICE_BYDGOSZCZ_2018.getBuyer().getPostalCode())))
        .andExpect(jsonPath("$[2].buyer.discount", is(INVOICE_BYDGOSZCZ_2018.getBuyer().getDiscount().doubleValue())))
        .andExpect(jsonPath("$[2].seller.name", is(INVOICE_BYDGOSZCZ_2018.getSeller().getName())))
        .andExpect(jsonPath("$[2].seller.nip", is(INVOICE_BYDGOSZCZ_2018.getSeller().getNip())))
        .andExpect(jsonPath("$[2].seller.street", is(INVOICE_BYDGOSZCZ_2018.getSeller().getStreet())))
        .andExpect(jsonPath("$[2].seller.postalCode", is(INVOICE_BYDGOSZCZ_2018.getSeller().getPostalCode())))
        .andExpect(jsonPath("$[2].seller.discount", is(INVOICE_BYDGOSZCZ_2018.getSeller().getDiscount().doubleValue())))
        .andExpect(jsonPath("$[2].entries[0].description", is(INVOICE_BYDGOSZCZ_2018.getEntries().get(0).getDescription())))
        .andExpect(jsonPath("$[2].entries[0].netPrice", is(INVOICE_BYDGOSZCZ_2018.getEntries().get(0).getNetPrice().intValue())))
        .andExpect(jsonPath("$[2].entries[0].vatRate", is(INVOICE_BYDGOSZCZ_2018.getEntries().get(0).getVatRate().toString())))
        .andExpect(jsonPath("$[2].entries[0].quantity", is(INVOICE_BYDGOSZCZ_2018.getEntries().get(0).getQuantity().intValue())));
  }

  @Test
  public void getInvoicesByIssueDateRange() throws Exception {
    int firstResponse = callRestServiceToAddInvoiceAndReturnId(INVOICE_KRAKOW_2018); // TODO if not needed why do you assign to varialble?
    int secondResponse = callRestServiceToAddInvoiceAndReturnId(INVOICE_CHELMNO_2016);
    int thirdResponse = callRestServiceToAddInvoiceAndReturnId(INVOICE_BYDGOSZCZ_2018); // TODO if not needed why do you assign to varialble?

    LocalDate startDate = LocalDate.of(2015, 4, 12);
    LocalDate endDate = LocalDate.of(2017, 4, 12);

    String url = String.format("/dates?startDate=%1$s&endDate=%2$s", startDate, endDate); // TODO what is "$s" doing?

    mockMvc
        .perform(
            get(INVOICE_SERVICE_PATH + url))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1))) // TODO those assertions are duplicates in each test -
        // write helper method taking id and Invoice to assert
        .andExpect(jsonPath("$[0].id", is((secondResponse))))
        .andExpect(jsonPath("$[0].identifier", is(INVOICE_CHELMNO_2016.getIdentifier())))
        .andExpect(jsonPath("$[0].salePlace", is(INVOICE_CHELMNO_2016.getSalePlace())))
        .andExpect(jsonPath("$[0].buyer.name", is(INVOICE_CHELMNO_2016.getBuyer().getName())))
        .andExpect(jsonPath("$[0].buyer.nip", is(INVOICE_CHELMNO_2016.getBuyer().getNip())))
        .andExpect(jsonPath("$[0].buyer.street", is(INVOICE_CHELMNO_2016.getBuyer().getStreet())))
        .andExpect(jsonPath("$[0].buyer.postalCode", is(INVOICE_CHELMNO_2016.getBuyer().getPostalCode())))
        .andExpect(jsonPath("$[0].buyer.discount", is(INVOICE_CHELMNO_2016.getBuyer().getDiscount().doubleValue())))
        .andExpect(jsonPath("$[0].seller.name", is(INVOICE_CHELMNO_2016.getSeller().getName())))
        .andExpect(jsonPath("$[0].seller.nip", is(INVOICE_CHELMNO_2016.getSeller().getNip())))
        .andExpect(jsonPath("$[0].seller.street", is(INVOICE_CHELMNO_2016.getSeller().getStreet())))
        .andExpect(jsonPath("$[0].seller.postalCode", is(INVOICE_CHELMNO_2016.getSeller().getPostalCode())))
        .andExpect(jsonPath("$[0].seller.discount", is(INVOICE_CHELMNO_2016.getSeller().getDiscount().doubleValue())))
        .andExpect(jsonPath("$[0].entries[0].description", is(INVOICE_CHELMNO_2016.getEntries().get(0).getDescription())))
        .andExpect(jsonPath("$[0].entries[0].netPrice", is(INVOICE_CHELMNO_2016.getEntries().get(0).getNetPrice().intValue())))
        .andExpect(jsonPath("$[0].entries[0].vatRate", is(INVOICE_CHELMNO_2016.getEntries().get(0).getVatRate().toString())))
        .andExpect(jsonPath("$[0].entries[0].quantity", is(INVOICE_CHELMNO_2016.getEntries().get(0).getQuantity().intValue())));
  }

  @Test
  public void getSingleInvoice() throws Exception {
    int idResponse = callRestServiceToAddInvoiceAndReturnId(INVOICE_KRAKOW_2018);

    mockMvc // TODO as above - use helper method to do assertions
        .perform(get(INVOICE_SERVICE_PATH + "/" + idResponse))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(idResponse)))
        .andExpect(jsonPath("$.identifier", is(INVOICE_KRAKOW_2018.getIdentifier())))
        .andExpect(jsonPath("$.salePlace", is(INVOICE_KRAKOW_2018.getSalePlace())))
        .andExpect(jsonPath("$.buyer.name", is(INVOICE_KRAKOW_2018.getBuyer().getName())))
        .andExpect(jsonPath("$.buyer.nip", is(INVOICE_KRAKOW_2018.getBuyer().getNip())))
        .andExpect(jsonPath("$.buyer.street", is(INVOICE_KRAKOW_2018.getBuyer().getStreet())))
        .andExpect(jsonPath("$.buyer.postalCode", is(INVOICE_KRAKOW_2018.getBuyer().getPostalCode())))
        .andExpect(jsonPath("$.buyer.discount", is(INVOICE_KRAKOW_2018.getBuyer().getDiscount().doubleValue())))
        .andExpect(jsonPath("$.seller.name", is(INVOICE_KRAKOW_2018.getSeller().getName())))
        .andExpect(jsonPath("$.seller.nip", is(INVOICE_KRAKOW_2018.getSeller().getNip())))
        .andExpect(jsonPath("$.seller.street", is(INVOICE_KRAKOW_2018.getSeller().getStreet())))
        .andExpect(jsonPath("$.seller.postalCode", is(INVOICE_KRAKOW_2018.getSeller().getPostalCode())))
        .andExpect(jsonPath("$.seller.discount", is(INVOICE_KRAKOW_2018.getSeller().getDiscount().doubleValue())))
        .andExpect(jsonPath("$.entries[0].description", is(INVOICE_KRAKOW_2018.getEntries().get(0).getDescription())))
        .andExpect(jsonPath("$.entries[0].netPrice", is(INVOICE_KRAKOW_2018.getEntries().get(0).getNetPrice().intValue())))
        .andExpect(jsonPath("$.entries[0].vatRate", is(INVOICE_KRAKOW_2018.getEntries().get(0).getVatRate().toString())))
        .andExpect(jsonPath("$.entries[0].quantity", is(INVOICE_KRAKOW_2018.getEntries().get(0).getQuantity().intValue())));
  }

  @Test
  public void shouldReturnErrorCausedByNotExistingId() throws Exception {
    mockMvc
        .perform(get(INVOICE_SERVICE_PATH + "/0"))
        .andExpect(status().isNotFound());
  }

  @Test
  public void updateInvoice() throws Exception {
    int invoiceId = callRestServiceToAddInvoiceAndReturnId(INVOICE_RADOMSKO_2018);

    mockMvc
        .perform(put(INVOICE_SERVICE_PATH + "/" + invoiceId)
            .contentType(JSON_CONTENT_TYPE)
            .content(convertToJson(INVOICE_BYDGOSZCZ_2018)))
        .andExpect(status().isOk());

    mockMvc // TODO as above - use helper method to do assertions
        .perform(get(INVOICE_SERVICE_PATH + "/" + invoiceId))
        .andExpect(status().isOk())
        .andExpect(content().contentType(JSON_CONTENT_TYPE))
        .andExpect(jsonPath("$.id", is(invoiceId)))
        .andExpect(jsonPath("$.identifier", is(INVOICE_BYDGOSZCZ_2018.getIdentifier())))
        .andExpect(jsonPath("$.salePlace", is(INVOICE_BYDGOSZCZ_2018.getSalePlace())))
        .andExpect(jsonPath("$.buyer.name", is(INVOICE_BYDGOSZCZ_2018.getBuyer().getName())))
        .andExpect(jsonPath("$.buyer.nip", is(INVOICE_BYDGOSZCZ_2018.getBuyer().getNip())))
        .andExpect(jsonPath("$.buyer.street", is(INVOICE_BYDGOSZCZ_2018.getBuyer().getStreet())))
        .andExpect(jsonPath("$.buyer.postalCode", is(INVOICE_BYDGOSZCZ_2018.getBuyer().getPostalCode())))
        .andExpect(jsonPath("$.buyer.discount", is(INVOICE_BYDGOSZCZ_2018.getBuyer().getDiscount().doubleValue())))
        .andExpect(jsonPath("$.seller.name", is(INVOICE_BYDGOSZCZ_2018.getSeller().getName())))
        .andExpect(jsonPath("$.seller.nip", is(INVOICE_BYDGOSZCZ_2018.getSeller().getNip())))
        .andExpect(jsonPath("$.seller.street", is(INVOICE_BYDGOSZCZ_2018.getSeller().getStreet())))
        .andExpect(jsonPath("$.seller.postalCode", is(INVOICE_BYDGOSZCZ_2018.getSeller().getPostalCode())))
        .andExpect(jsonPath("$.seller.discount", is(INVOICE_BYDGOSZCZ_2018.getSeller().getDiscount().doubleValue())))
        .andExpect(jsonPath("$.entries[0].description", is(INVOICE_BYDGOSZCZ_2018.getEntries().get(0).getDescription())))
        .andExpect(jsonPath("$.entries[0].netPrice", is(INVOICE_BYDGOSZCZ_2018.getEntries().get(0).getNetPrice().intValue())))
        .andExpect(jsonPath("$.entries[0].vatRate", is(INVOICE_BYDGOSZCZ_2018.getEntries().get(0).getVatRate().toString())))
        .andExpect(jsonPath("$.entries[0].quantity", is(INVOICE_BYDGOSZCZ_2018.getEntries().get(0).getQuantity().intValue())));
  }

  @Test
  public void shouldReturnErrorCausedByNotExistingIdPassedToUpdate() throws Exception {
    mockMvc
        .perform(put(INVOICE_SERVICE_PATH + "/0")
            .content(convertToJson(INVOICE_CHELMNO_2016))
            .contentType(JSON_CONTENT_TYPE))
        .andExpect(status().isNotFound());
  } // TODO - no such test for delete? :)

  @Test // TODO test name says that error is because update method is not valid - is that true? :)
  public void shouldReturnErrorCausedByNotValidInvoiceUpdateMethod() throws Exception {
    int invoiceId = callRestServiceToAddInvoiceAndReturnId(INVOICE_KRAKOW_2018);

    mockMvc
        .perform(put(INVOICE_SERVICE_PATH + "/" + invoiceId)
            .contentType(JSON_CONTENT_TYPE)
            .content(convertToJson(INVOICE_BLANK_IDENTIFIER)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$[0]", is("Identifier not found")));
  }

  @Test
  public void removeInvoiceById() throws Exception {
    int firstResponse = callRestServiceToAddInvoiceAndReturnId(INVOICE_KRAKOW_2018);
    int secondResponse = callRestServiceToAddInvoiceAndReturnId(INVOICE_CHELMNO_2016); // TODO why do you assign to variable if not used?
    int thirdResponse = callRestServiceToAddInvoiceAndReturnId(INVOICE_BYDGOSZCZ_2018); // TODO why do you assign to variable if not used?

    mockMvc
        .perform(delete(INVOICE_SERVICE_PATH + "/" + firstResponse))
        .andExpect(status().isOk());

    mockMvc
        .perform(delete(INVOICE_SERVICE_PATH + "/5")) // TODO that should be separate test
        .andExpect(status().isNotFound());

    mockMvc
        .perform(get(INVOICE_SERVICE_PATH))
        .andExpect(jsonPath("$", hasSize(2)));
  }

  private int callRestServiceToAddInvoiceAndReturnId(Invoice invoice) throws Exception {
    String response = mockMvc
        .perform(post(INVOICE_SERVICE_PATH)
            .content(convertToJson(invoice))
            .contentType(JSON_CONTENT_TYPE))
        .andExpect(status().isOk())
        .andReturn()
        .getResponse().getContentAsString();
    return Integer.valueOf(response);
  }

  private String convertToJson(Object object) {
    try {
      return JacksonProvider.getObjectMapper().writeValueAsString(object);
    } catch (JsonProcessingException exception) {
      exception.printStackTrace();
    }
    return "";
  }
}