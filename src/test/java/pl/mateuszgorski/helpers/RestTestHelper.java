package pl.mateuszgorski.helpers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static pl.mateuszgorski.configuration.JacksonProvider.getObjectMapper;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pl.mateuszgorski.database.impl.file.helpers.InvoiceConverter;
import pl.mateuszgorski.model.Invoice;

public class RestTestHelper {

  protected MockMvc mockMvc;

  public RestTestHelper(MockMvc mockMvc) {
    this.mockMvc = mockMvc;
  }

  private static final String INVOICE_SERVICE_PATH = "/invoices";
  private static final MediaType JSON_CONTENT_TYPE = MediaType.APPLICATION_JSON_UTF8;
  InvoiceConverter invoiceConverter = new InvoiceConverter(getObjectMapper());

  public int callRestServiceToAddInvoiceAndReturnId(Invoice invoice) throws Exception {
    String response =
        mockMvc
            .perform(post(INVOICE_SERVICE_PATH)
                .content(invoiceConverter.convertInvoiceToJson(invoice))
                .contentType(JSON_CONTENT_TYPE))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();
    return Integer.parseInt(response);
  }

  public Invoice callRestServiceToReturnInvoiceById(int id) throws Exception {
    String invoiceJson = mockMvc.perform(get(INVOICE_SERVICE_PATH + "/" + id))
        .andReturn()
        .getResponse()
        .getContentAsString();
    return invoiceConverter.convertJsonToInvoice(invoiceJson);
  }
}