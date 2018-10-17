package pl.coderstrust.accounting.logic;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static pl.coderstrust.accounting.helpers.CompanyProvider.COMPANY_DRUTEX;
import static pl.coderstrust.accounting.helpers.CompanyProvider.COMPANY_TRANSPOL;
import static pl.coderstrust.accounting.helpers.CompanyProvider.COMPANY_WASBUD;
import static pl.coderstrust.accounting.helpers.InsuranceProvider.HEALTH_INSURANCE;
import static pl.coderstrust.accounting.helpers.InsuranceProvider.HEALTH_INSURANCE2;
import static pl.coderstrust.accounting.helpers.InsuranceProvider.HEALTH_INSURANCE3;
import static pl.coderstrust.accounting.helpers.InsuranceProvider.PENSION_INSURANCE3;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import pl.coderstrust.accounting.configuration.DatabaseProvider;
import pl.coderstrust.accounting.database.impl.hibernate.CompanyRepository;
import pl.coderstrust.accounting.database.impl.hibernate.InsuranceRepository;
import pl.coderstrust.accounting.model.Company;
import pl.coderstrust.accounting.model.Insurance;
import pl.coderstrust.accounting.model.InsuranceType;

@RunWith(MockitoJUnitRunner.class)
public class InsuranceServiceTest {

  InsuranceRepository insuranceRepository;

  private static DataSource getDataSource() {
    DriverManagerDataSource dataSource = new DriverManagerDataSource();
    dataSource.setDriverClassName("org.postgresql.Driver");
    dataSource.setUrl("jdbc:postgresql://localhost:5432/accounting");
    dataSource.setUsername("postgres");
    dataSource.setPassword("postgres");
    return dataSource;
  }

  DataSource dataSource = getDataSource();
  JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

  CompanyRepository companyRepository;

  @Rule
  public ExpectedException expectedEx = ExpectedException.none();

  private CompanyService companyService = new CompanyService(
      DatabaseProvider.provideCompanyDatabase(
          "SqlDatabase",
          "src/test/resources/test", companyRepository, jdbcTemplate));

  private InsuranceService insuranceService = new InsuranceService(
      DatabaseProvider.provideInsuranceDatabase(
          "SqlDatabase",
          "src/test/resources/test", insuranceRepository, jdbcTemplate));

  @Before
  public void prepareMockedCompaniesListForAllTests() {
    List<Company> companies = new ArrayList<>();
    companies.add(COMPANY_TRANSPOL);
    companies.add(COMPANY_DRUTEX);
    companies.add(COMPANY_WASBUD);
  }

  @Before
  public void beforeMethod() {
    companyService.clearDatabase();
    insuranceService.clearDatabase();
  }

  @Test
  public void shouldAddInsuranceToInsurancesListOfSpecifiedCompany() {
    //given

    //when
    insuranceService.saveInsurance(COMPANY_TRANSPOL.getNip(), HEALTH_INSURANCE);
    insuranceService.saveInsurance(COMPANY_TRANSPOL.getNip(), HEALTH_INSURANCE2);
    insuranceService.saveInsurance(COMPANY_TRANSPOL.getNip(), HEALTH_INSURANCE3);
    List<Insurance> actual = insuranceService.getAllInsurancesByCompany(COMPANY_TRANSPOL.getNip());

    //then
    assertThat(actual.size(), is(3));
    assertThat(actual, hasItems(HEALTH_INSURANCE, HEALTH_INSURANCE2, HEALTH_INSURANCE3));
  }

  @Test
  public void shouldReturnAllInsurancesByCompany() {
    //given
    String nip = COMPANY_DRUTEX.getNip();
    insuranceService.saveInsurance(nip, HEALTH_INSURANCE);
    insuranceService.saveInsurance(nip, HEALTH_INSURANCE2);

    //when
    List<Insurance> actual = insuranceService.getAllInsurancesByCompany(nip);

    //then
    assertThat(actual.size(), is(2));
    assertThat(actual, hasItem(HEALTH_INSURANCE));
    assertThat(actual, hasItem(HEALTH_INSURANCE2));
  }

  @Test
  public void shouldReturnTListOfInsurancesByTypeAndCompany() {
    //given
    insuranceService.saveInsurance(COMPANY_WASBUD.getNip(), PENSION_INSURANCE3);

    //when
    List<Insurance> actual = insuranceService
        .getInsurancesByTypeAndCompany(InsuranceType.PENSION, COMPANY_WASBUD.getNip());

    //then
    assertThat(actual, hasSize(1));
    assertThat(actual, hasItem(PENSION_INSURANCE3));
  }

  @Test
  public void shouldReturnInvoicesByIssueDate() {
    //given
    String nip = COMPANY_WASBUD.getNip();
    insuranceService.saveInsurance(nip, HEALTH_INSURANCE);
    insuranceService.saveInsurance(nip, HEALTH_INSURANCE2);
    insuranceService.saveInsurance(nip, HEALTH_INSURANCE3);

    //when
    LocalDate fromDate = LocalDate.of(2017, 1, 01);
    LocalDate toDate = LocalDate.of(2017, 12, 31);
    List<Insurance> actual = insuranceService
        .getInsurancesByIssueDateAndByCompany(fromDate, toDate, COMPANY_WASBUD.getNip());

    //then
    assertThat(actual.size(), is(2));
    assertThat(actual, hasItems(HEALTH_INSURANCE2, HEALTH_INSURANCE3));
  }

  @Test
  public void getInsuranceByIdAndCompany() {
    //given
    int insuranceId1 = insuranceService.saveInsurance(COMPANY_WASBUD.getNip(), HEALTH_INSURANCE);
    int insuranceId2 = insuranceService.saveInsurance(COMPANY_WASBUD.getNip(), HEALTH_INSURANCE2);

    //when
    Optional<Insurance> actual = insuranceService.getInsuranceById(insuranceId2);

    //then
    Assert.assertTrue(actual.isPresent());
    assertThat(actual.get(), is(HEALTH_INSURANCE2));

  }

  @Test
  public void removeInsuranceByIdAndCompany() {
    //given
    int insuranceId1 = insuranceService.saveInsurance(COMPANY_WASBUD.getNip(), HEALTH_INSURANCE);
    int insuranceId2 = insuranceService.saveInsurance(COMPANY_WASBUD.getNip(), HEALTH_INSURANCE2);

    //when
    insuranceService.removeInsuranceById(insuranceId2);
    Optional<Insurance> healthInsurance = insuranceService.getInsuranceById(insuranceId1);
    Optional<Insurance> healthInsurance2 = insuranceService.getInsuranceById(insuranceId2);

    //then
    assertThat(healthInsurance.isPresent(), is(true));
    assertThat(healthInsurance2.isPresent(), is(false));
  }
}