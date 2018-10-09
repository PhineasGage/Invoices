package pl.coderstrust.accounting.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static pl.coderstrust.accounting.helpers.CompanyProvider.COMPANY_EMPTY_INSURANCES;
import static pl.coderstrust.accounting.helpers.CompanyProvider.COMPANY_EMPTY_INSURANCES2;
import static pl.coderstrust.accounting.helpers.CompanyProvider.COMPANY_TRANSPOL;
import static pl.coderstrust.accounting.helpers.InsuranceProvider.HEALTH_INSURANCE;
import static pl.coderstrust.accounting.helpers.InsuranceProvider.HEALTH_INSURANCE2;
import static pl.coderstrust.accounting.helpers.InsuranceProvider.HEALTH_INSURANCE3;
import static pl.coderstrust.accounting.helpers.InsuranceProvider.PENSION_INSURANCE;
import static pl.coderstrust.accounting.helpers.InsuranceProvider.PENSION_INSURANCE2;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.List;
import javax.annotation.PostConstruct;
import org.hamcrest.collection.IsCollectionWithSize;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import pl.coderstrust.accounting.configuration.JacksonProvider;
import pl.coderstrust.accounting.database.impl.file.helpers.ItemConverter;
import pl.coderstrust.accounting.helpers.InsuranceAssertion;
import pl.coderstrust.accounting.helpers.RestTestHelper;
import pl.coderstrust.accounting.logic.CompanyService;
import pl.coderstrust.accounting.logic.InsuranceService;
import pl.coderstrust.accounting.logic.InvoiceService;
import pl.coderstrust.accounting.model.Insurance;
import pl.coderstrust.accounting.model.InsuranceType;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class InsuranceControllerTest {

  private static final String INSURANCE_SERVICE_PATH = "/insurance/";

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper mapper;

  @Autowired
  private InvoiceService invoiceService;

  @Autowired
  private CompanyService companyService;

  @Autowired
  private InsuranceService insuranceService;

  ItemConverter<Insurance> itemConverter = new ItemConverter<Insurance>(JacksonProvider.getObjectMapper(), Insurance.class);

  private InsuranceController insuranceController = new InsuranceController(insuranceService);

  private RestTestHelper restTestHelper;

  private InsuranceAssertion insuranceAssertion = new InsuranceAssertion();

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
    assertThat(insuranceController, is(notNullValue()));
  }

  @Test
  public void addInsurance() throws Exception {
    String nip = COMPANY_EMPTY_INSURANCES.getNip();
    restTestHelper.callRestServiceToAddCompanyAndReturnId(COMPANY_EMPTY_INSURANCES);
    int idResponse1 = restTestHelper.callRestServiceToAddInsurance(nip, HEALTH_INSURANCE);
    int idResponse2 = restTestHelper.callRestServiceToAddInsurance(nip, HEALTH_INSURANCE2);
    int idResponse3 = restTestHelper.callRestServiceToAddInsurance(nip, HEALTH_INSURANCE3);

    String jsonString = restTestHelper.insurancesByCompanyRequest(nip);

    List<Insurance> actual = mapper.readValue(jsonString, new TypeReference<List<Insurance>>() {
    });

    insuranceAssertion.assertInsurance(idResponse1, HEALTH_INSURANCE, actual.get(0));
    insuranceAssertion.assertInsurance(idResponse2, HEALTH_INSURANCE2, actual.get(1));
    insuranceAssertion.assertInsurance(idResponse3, HEALTH_INSURANCE3, actual.get(2));
    assertThat(actual, hasSize(3));
  }

  @Test
  public void getInsurancesById() throws Exception {
    String nip = COMPANY_EMPTY_INSURANCES.getNip();
    restTestHelper.callRestServiceToAddCompanyAndReturnId(COMPANY_EMPTY_INSURANCES);
    int id1 = restTestHelper.callRestServiceToAddInsurance(nip, HEALTH_INSURANCE);
    int id2 = restTestHelper.callRestServiceToAddInsurance(nip, HEALTH_INSURANCE2);

    Insurance actual = restTestHelper.callRestServiceToReturnInsuranceById(id2);
    insuranceAssertion.assertInsurance(id2, HEALTH_INSURANCE2, actual);
  }

  @Test
  public void shouldReturnInsurancesByCompany() throws Exception {
    String nip = COMPANY_EMPTY_INSURANCES.getNip();
    restTestHelper.callRestServiceToAddCompanyAndReturnId(COMPANY_EMPTY_INSURANCES);
    int id1 = restTestHelper.callRestServiceToAddInsurance(nip, HEALTH_INSURANCE);
    int id2 = restTestHelper.callRestServiceToAddInsurance(nip, HEALTH_INSURANCE2);

    String jsonString = restTestHelper.insurancesByCompanyRequest(nip);

    List<Insurance> actual = mapper.readValue(jsonString, new TypeReference<List<Insurance>>() {
    });

    assertThat(actual, hasSize(2));
    insuranceAssertion.assertInsurance(id1, HEALTH_INSURANCE, actual.get(0));
    insuranceAssertion.assertInsurance(id2, HEALTH_INSURANCE2, actual.get(1));
  }

  @Test
  public void getInsurancesByTypeAndByCompany() throws Exception {
    restTestHelper.callRestServiceToAddCompanyAndReturnId(COMPANY_TRANSPOL);
    String nip = COMPANY_TRANSPOL.getNip();
    insuranceService.saveInsurance(nip, HEALTH_INSURANCE);
    insuranceService.saveInsurance(nip, HEALTH_INSURANCE2);
    insuranceService.saveInsurance(nip, PENSION_INSURANCE);
    insuranceService.saveInsurance(nip, PENSION_INSURANCE2);

    String jsonString = mockMvc
        .perform(get(INSURANCE_SERVICE_PATH + nip + "/type?insuranceType=" + InsuranceType.PENSION))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", IsCollectionWithSize.hasSize(2)))
        .andReturn()
        .getResponse()
        .getContentAsString();

    List<Insurance> actual = mapper.readValue(jsonString, new TypeReference<List<Insurance>>() {
    });

    assertThat(actual, hasSize(2));
    insuranceAssertion.assertInsurance(actual.get(0).getId(), PENSION_INSURANCE, actual.get(0));
    insuranceAssertion.assertInsurance(actual.get(1).getId(), PENSION_INSURANCE2, actual.get(1));

  }

  @Test
  public void getInsurancesByDateAndByCompany() throws Exception {
    restTestHelper.callRestServiceToAddCompanyAndReturnId(COMPANY_EMPTY_INSURANCES2);
    String nip = COMPANY_EMPTY_INSURANCES2.getNip();
    int idResponse1 = restTestHelper.callRestServiceToAddInsurance(nip, HEALTH_INSURANCE);
    int idResponse2 = restTestHelper.callRestServiceToAddInsurance(nip, HEALTH_INSURANCE2);
    int idResponse3 = restTestHelper.callRestServiceToAddInsurance(nip, HEALTH_INSURANCE3);

    LocalDate startDate = LocalDate.of(2018, 5, 9);
    LocalDate endDate = LocalDate.of(2018, 12, 30);

    String url = String.format("/dates?startDate=%1$s&endDate=%2$s", startDate, endDate);

    String jsonString = mockMvc
        .perform(get(INSURANCE_SERVICE_PATH + nip + url))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", IsCollectionWithSize.hasSize(1)))
        .andReturn()
        .getResponse()
        .getContentAsString();

    List<Insurance> actual = mapper.readValue(jsonString, new TypeReference<List<Insurance>>() {
    });

    assertThat(actual, hasSize(1));
    insuranceAssertion.assertInsurance(actual.get(0).getId(), HEALTH_INSURANCE, actual.get(0));
  }

  @Test
  public void shouldRemoveInsuranceById() throws Exception {
    restTestHelper.callRestServiceToAddCompanyAndReturnId(COMPANY_EMPTY_INSURANCES2);
    String nip = COMPANY_EMPTY_INSURANCES2.getNip();
    int idResponse1 = restTestHelper.callRestServiceToAddInsurance(nip, HEALTH_INSURANCE);
    int idResponse2 = restTestHelper.callRestServiceToAddInsurance(nip, HEALTH_INSURANCE2);
    int idResponse3 = restTestHelper.callRestServiceToAddInsurance(nip, HEALTH_INSURANCE3);

    StringBuilder url = new StringBuilder();
    url.append("/remove/").append(idResponse2);


    mockMvc.perform(delete(INSURANCE_SERVICE_PATH + url.toString()))
        .andExpect(status().isOk());

    String jsonString = restTestHelper.insurancesByCompanyRequest(nip);
    List<Insurance> actual = mapper.readValue(jsonString, new TypeReference<List<Insurance>>() {
    });

    assertThat(actual, hasSize(2));
    assertFalse(actual.contains(HEALTH_INSURANCE2));
    assertTrue(actual.contains(HEALTH_INSURANCE));
    assertTrue(actual.contains(HEALTH_INSURANCE3));
  }


}