package pl.coderstrust.accounting.helpers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static pl.coderstrust.accounting.configuration.JacksonProvider.getObjectMapper;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pl.coderstrust.accounting.database.impl.file.helpers.ItemConverter;
import pl.coderstrust.accounting.model.Company;
import pl.coderstrust.accounting.model.Insurance;
import pl.coderstrust.accounting.model.Invoice;

public class RestTestHelper {

  protected MockMvc mockMvc;

  public RestTestHelper(MockMvc mockMvc) {
    this.mockMvc = mockMvc;
  }

  private static final String INVOICE_SERVICE_PATH = "/invoices";
  private static final String COMPANY_SERVICE_PATH = "/companies";
  private static final String INSURANCE_SERVICE_PATH = "/insurance/";

  private static final MediaType JSON_CONTENT_TYPE = MediaType.APPLICATION_JSON_UTF8;
  ItemConverter invoiceConverter = new ItemConverter(getObjectMapper(), Invoice.class);
  ItemConverter companyConverter = new ItemConverter(getObjectMapper(), Company.class);
  ItemConverter insuranceConverter = new ItemConverter(getObjectMapper(), Insurance.class);


  public int callRestServiceToAddInvoiceAndReturnId(Invoice invoice) throws Exception {
    String response =
        mockMvc
            .perform(post(INVOICE_SERVICE_PATH)
                .content(invoiceConverter.convertItemToJson(invoice))
                .contentType(JSON_CONTENT_TYPE))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();
    return Integer.parseInt(response);
  }

  public Invoice callRestServiceToReturnInvoiceById(int id) throws Exception {
    StringBuilder requestPath = new StringBuilder();
    requestPath.append(INVOICE_SERVICE_PATH).append("/").append(id);
    String invoiceJson = mockMvc.perform(get(requestPath.toString()))
        .andReturn()
        .getResponse()
        .getContentAsString();
    return (Invoice) invoiceConverter.convertJsonToItem(invoiceJson);
  }

  public int callRestServiceToAddCompanyAndReturnId(Company company) throws Exception {
    String response =
        mockMvc
            .perform(post(COMPANY_SERVICE_PATH)
                .content(companyConverter.convertItemToJson(company))
                .contentType(JSON_CONTENT_TYPE))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();
    return Integer.parseInt(response);
  }

  public Company callRestServiceToReturnCompanyById(int id) throws Exception {
    StringBuilder requestPath = new StringBuilder();
    requestPath.append(COMPANY_SERVICE_PATH).append("/").append(id);
    String companyJson = mockMvc.perform(get(requestPath.toString()))
        .andReturn()
        .getResponse()
        .getContentAsString();
    return (Company) companyConverter.convertJsonToItem(companyJson);
  }

  public int callRestServiceToAddInsurance(String nip, Insurance insurance) throws Exception {
    StringBuilder urlTemplate = new StringBuilder();
    urlTemplate.append(INSURANCE_SERVICE_PATH).append("add/").append(nip);
    String response =
        mockMvc
            .perform(post(urlTemplate.toString())
                .content(insuranceConverter.convertItemToJson(insurance))
                .contentType(JSON_CONTENT_TYPE))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();
    return Integer.parseInt(response);
  }

  public Insurance callRestServiceToReturnInsuranceById(int id) throws Exception {
    StringBuilder requestPath = new StringBuilder();
    requestPath.append(INSURANCE_SERVICE_PATH).append(id).append("/company");
    String insuranceJson = mockMvc.perform(get(requestPath.toString()))
        .andReturn()
        .getResponse()
        .getContentAsString();
    return (Insurance) insuranceConverter.convertJsonToItem(insuranceJson);
  }

  public String insurancesByCompanyRequest(String nip) throws Exception {
    return mockMvc
        .perform(get(INSURANCE_SERVICE_PATH + "company/" + nip))
        .andExpect(status().isOk())
        .andReturn()
        .getResponse()
        .getContentAsString();
  }
}