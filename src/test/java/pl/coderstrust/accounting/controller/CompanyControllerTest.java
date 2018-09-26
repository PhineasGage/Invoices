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
import static pl.coderstrust.accounting.helpers.CompanyProvider.COMPANY_BLANK_CITY;
import static pl.coderstrust.accounting.helpers.CompanyProvider.COMPANY_BLANK_NAME;
import static pl.coderstrust.accounting.helpers.CompanyProvider.COMPANY_DRUKPOL;
import static pl.coderstrust.accounting.helpers.CompanyProvider.COMPANY_DRUTEX;
import static pl.coderstrust.accounting.helpers.CompanyProvider.COMPANY_TRANSPOL;
import static pl.coderstrust.accounting.helpers.CompanyProvider.COMPANY_WASBUD;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import pl.coderstrust.accounting.configuration.JacksonProvider;
import pl.coderstrust.accounting.database.impl.file.helpers.ItemConverter;
import pl.coderstrust.accounting.helpers.CompanyAssertion;
import pl.coderstrust.accounting.helpers.RestTestHelper;
import pl.coderstrust.accounting.logic.CompanyService;
import pl.coderstrust.accounting.model.Company;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class CompanyControllerTest {

  private static final String COMPANY_SERVICE_PATH = "/companies";
  private static final MediaType JSON_CONTENT_TYPE = MediaType.APPLICATION_JSON_UTF8;

  @Autowired
  private ObjectMapper mapper;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  CompanyValidator companyValidator;

  @Autowired
  private CompanyService companyService;

  ItemConverter<Company> itemConverter = new ItemConverter<Company>(JacksonProvider.getObjectMapper(), Company.class);

  private CompanyController companyController = new CompanyController(companyService, companyValidator);

  private RestTestHelper restTestHelper;

  private CompanyAssertion companyAssertion = new CompanyAssertion();

  @PostConstruct
  public void postConstruct() {
    restTestHelper = new RestTestHelper(mockMvc);
  }

  @Before
  public void beforeMethod() {
    companyService.clearDatabase();
  }

  @Test
  public void contexLoads() {
    assertThat(companyController, is(notNullValue()));
  }

  @Test
  public void shouldCheckSaveSaveCompanyRequest() throws Exception {
    int idResponse = restTestHelper.callRestServiceToAddCompanyAndReturnId(COMPANY_DRUTEX);
    Company savedCompany = restTestHelper.callRestServiceToReturnCompanyById(idResponse);

    companyAssertion.assertCompany(idResponse, COMPANY_DRUTEX, savedCompany);
  }

  @Test
  public void shouldReturnErrorMessageCorrespondingToIncorrectCompanyField() throws Exception {
    mockMvc.perform(
        post(COMPANY_SERVICE_PATH)
            .content(itemConverter.convertItemToJson(COMPANY_BLANK_CITY))
            .contentType(JSON_CONTENT_TYPE)
    )
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$[0]", is(" city not found")));
  }

  @Test
  public void getAllCompanies() throws Exception {
    int firstResponse = restTestHelper.callRestServiceToAddCompanyAndReturnId(COMPANY_DRUTEX);
    int secondResponse = restTestHelper.callRestServiceToAddCompanyAndReturnId(COMPANY_WASBUD);
    int thirdResponse = restTestHelper.callRestServiceToAddCompanyAndReturnId(COMPANY_TRANSPOL);
    mockMvc
        .perform(get(COMPANY_SERVICE_PATH))
        .andDo(print()).andExpect(status()
        .isOk())
        .andExpect(jsonPath("$", hasSize(3)));
    Company firstSavedInvoice = restTestHelper.callRestServiceToReturnCompanyById(firstResponse);
    Company secondSavedInvoice = restTestHelper.callRestServiceToReturnCompanyById(secondResponse);
    Company thirdSavedInvoice = restTestHelper.callRestServiceToReturnCompanyById(thirdResponse);
    companyAssertion.assertCompany(firstResponse, COMPANY_DRUTEX, firstSavedInvoice);
    companyAssertion.assertCompany(secondResponse, COMPANY_WASBUD, secondSavedInvoice);
    companyAssertion.assertCompany(thirdResponse, COMPANY_TRANSPOL, thirdSavedInvoice);
  }

  @Test
  public void getsSingleCompanyById() throws Exception {
    int idResponse = restTestHelper.callRestServiceToAddCompanyAndReturnId(COMPANY_DRUKPOL);
    Company savedCompany = restTestHelper.callRestServiceToReturnCompanyById(idResponse);

    companyAssertion.assertCompany(idResponse, COMPANY_DRUKPOL, savedCompany);
  }

  @Test
  public void shouldReturnErrorCausedByNotExistingId() throws Exception {
    mockMvc
        .perform(get(COMPANY_SERVICE_PATH + "/0"))
        .andExpect(status().isNotFound());
  }

  @Test
  public void updateCompanyById() throws Exception {
    int idResponse = restTestHelper.callRestServiceToAddCompanyAndReturnId(COMPANY_TRANSPOL);
    int idResponse2 = restTestHelper.callRestServiceToAddCompanyAndReturnId(COMPANY_DRUTEX);

    mockMvc
        .perform(put(COMPANY_SERVICE_PATH + "/" + idResponse2)
            .contentType(JSON_CONTENT_TYPE)
            .content(itemConverter.convertItemToJson(COMPANY_WASBUD)))
        .andExpect(status().isOk());
    Company savedCompany = restTestHelper.callRestServiceToReturnCompanyById(idResponse2);

    companyAssertion.assertCompany(idResponse2, COMPANY_WASBUD, savedCompany);
  }

  @Test
  public void shouldReturnErrorCausedByNotExistingIdPassedToUpdate() throws Exception {
    mockMvc
        .perform(put(COMPANY_SERVICE_PATH + "/0")
            .content(itemConverter.convertItemToJson(COMPANY_WASBUD))
            .contentType(JSON_CONTENT_TYPE))
        .andExpect(status().isNotFound());
  }

  @Test
  public void removeCompanyById() throws Exception {
    int firstResponse = restTestHelper.callRestServiceToAddCompanyAndReturnId(COMPANY_DRUTEX);
    int secondResponse = restTestHelper.callRestServiceToAddCompanyAndReturnId(COMPANY_WASBUD);
    int thirdResponse = restTestHelper.callRestServiceToAddCompanyAndReturnId(COMPANY_TRANSPOL);

    mockMvc
        .perform(delete(COMPANY_SERVICE_PATH + "/" + secondResponse))
        .andExpect(status().isOk());

    mockMvc
        .perform(get(COMPANY_SERVICE_PATH))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(2)));

    Company firstSavedCompany = restTestHelper.callRestServiceToReturnCompanyById(firstResponse);
    Company thirdSavedCompany = restTestHelper.callRestServiceToReturnCompanyById(thirdResponse);

    companyAssertion.assertCompany(firstResponse, COMPANY_DRUTEX, firstSavedCompany);
    companyAssertion.assertCompany(thirdResponse, COMPANY_TRANSPOL, thirdSavedCompany);
  }

  @Test
  public void shouldReturnErrorCausedByNotExistingIdPassedToDeleteRequest() throws Exception {
    mockMvc
        .perform(delete(COMPANY_SERVICE_PATH + "/0"))
        .andExpect(status().isNotFound());
  }

  @Test
  public void shouldReturnErrorCausedByNotValidNamePassedToUpdate() throws Exception {
    int companyId = restTestHelper.callRestServiceToAddCompanyAndReturnId(COMPANY_DRUTEX);

    mockMvc
        .perform(put(COMPANY_SERVICE_PATH + "/" + companyId)
            .contentType(JSON_CONTENT_TYPE)
            .content(itemConverter.convertItemToJson(COMPANY_BLANK_NAME)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$[0]", is(" name not found")));
  }
}