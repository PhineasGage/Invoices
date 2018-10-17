package pl.coderstrust.accounting.logic;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static pl.coderstrust.accounting.helpers.BigDecimalProvider.createBigDecimal;
import static pl.coderstrust.accounting.helpers.CompanyProvider.COMPANY_DRUKPOL;
import static pl.coderstrust.accounting.helpers.CompanyProvider.COMPANY_DRUTEX;
import static pl.coderstrust.accounting.helpers.CompanyProvider.COMPANY_TRANSPOL;
import static pl.coderstrust.accounting.helpers.CompanyProvider.COMPANY_WASBUD;
import static pl.coderstrust.accounting.helpers.InsuranceProvider.HEALTH_INSURANCE;
import static pl.coderstrust.accounting.helpers.InsuranceProvider.HEALTH_INSURANCE2;
import static pl.coderstrust.accounting.helpers.InsuranceProvider.HEALTH_INSURANCE3;
import static pl.coderstrust.accounting.helpers.InsuranceProvider.PENSION_INSURANCE;
import static pl.coderstrust.accounting.helpers.InsuranceProvider.PENSION_INSURANCE2;
import static pl.coderstrust.accounting.helpers.InsuranceProvider.PENSION_INSURANCE3;
import static pl.coderstrust.accounting.helpers.InvoiceProvider.INVOICE_DRUTEX_CAR_FUEL_ENTRY_2018;
import static pl.coderstrust.accounting.helpers.InvoiceProvider.INVOICE_DRUTEX_SPAN_CLAMP_SUPPORT_2018;
import static pl.coderstrust.accounting.helpers.InvoiceProvider.INVOICE_TRANSPOL_SPAN_CLAMP_SUPPORT_2016;
import static pl.coderstrust.accounting.helpers.InvoiceProvider.INVOICE_WASBUD_LINK_2018;
import static pl.coderstrust.accounting.helpers.InvoiceProvider.INVOICE_WASBUD_SPAN_CLAMP_2017;

import java.math.BigDecimal;
import java.math.RoundingMode;
import javax.sql.DataSource;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import pl.coderstrust.accounting.configuration.DatabaseProvider;
import pl.coderstrust.accounting.database.impl.hibernate.CompanyRepository;
import pl.coderstrust.accounting.database.impl.hibernate.InsuranceRepository;
import pl.coderstrust.accounting.database.impl.hibernate.InvoiceRepository;

@RunWith(MockitoJUnitRunner.class)
public class TaxCalculatorServiceTest {

  @Autowired
  InvoiceRepository invoiceRepository;

  @Autowired
  CompanyRepository companyRepository;

  @Autowired
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

  private InvoiceService invoiceService = new InvoiceService(
      DatabaseProvider.provideInvoiceDatabase(
          "SqlDatabase",
          "src/test/resources/test", invoiceRepository, jdbcTemplate));

  private CompanyService companyService = new CompanyService(
      DatabaseProvider.provideCompanyDatabase(
          "SqlDatabase",
          "src/test/resources/test", companyRepository, jdbcTemplate));

  private InsuranceService insuranceService = new InsuranceService(
      DatabaseProvider.provideInsuranceDatabase(
          "SqlDatabase",
          "src/test/resources/test", insuranceRepository, jdbcTemplate));

  private TaxCalculatorService taxCalculatorService = new TaxCalculatorService(invoiceService,
      companyService, insuranceService);

  @Before
  public void beforeMethod() {
    invoiceService.clearDatabase();
    companyService.clearDatabase();
    insuranceService.clearDatabase();
  }

  @Test
  public void shouldReturnZeroWhenNoInvoices() {
    //given

    //when
    BigDecimal actual = taxCalculatorService.getTaxDue(COMPANY_WASBUD.getNip())
        .setScale(2, BigDecimal.ROUND_HALF_UP);

    //then
    assertThat(actual, is(createBigDecimal(0)));
  }

  @Test
  public void shouldReturnIncome() {
    //given
    invoiceService.saveInvoice(INVOICE_DRUTEX_SPAN_CLAMP_SUPPORT_2018);
    invoiceService.saveInvoice(INVOICE_WASBUD_LINK_2018);

    //when
    BigDecimal actual = taxCalculatorService.getIncome(COMPANY_DRUTEX.getNip())
        .setScale(2, BigDecimal.ROUND_HALF_UP);

    //then
    assertThat(actual, is(createBigDecimal(786.0)));
  }

  @Test
  public void shouldReturnCosts() {
    //given
    invoiceService.saveInvoice(INVOICE_DRUTEX_SPAN_CLAMP_SUPPORT_2018);
    invoiceService.saveInvoice(INVOICE_TRANSPOL_SPAN_CLAMP_SUPPORT_2016);

    //when
    BigDecimal actual = taxCalculatorService.getCosts(COMPANY_TRANSPOL.getNip())
        .setScale(2, BigDecimal.ROUND_HALF_UP);

    //then
    assertThat(actual, is(createBigDecimal(786.0)));
  }

  @Test
  public void shouldReturnCostsForCompanyWithPersonalCarUsage() {
    //given
    invoiceService.saveInvoice(INVOICE_DRUTEX_CAR_FUEL_ENTRY_2018);

    //when
    BigDecimal actual = taxCalculatorService.getCosts(COMPANY_DRUTEX.getNip())
        .setScale(2, RoundingMode.HALF_UP);

    //then
    assertThat(actual, is(createBigDecimal(107.64)));
  }

  @Test
  public void shouldReturnVatDue() {
    //given
    invoiceService.saveInvoice(INVOICE_WASBUD_LINK_2018);
    invoiceService.saveInvoice(INVOICE_WASBUD_SPAN_CLAMP_2017);

    //when
    BigDecimal actual = taxCalculatorService.getTaxDue(COMPANY_WASBUD.getNip())
        .setScale(2, RoundingMode.HALF_UP);

    //then
    assertThat(actual, is(createBigDecimal(120.29)));

  }

  @Test
  public void shouldReturnVatIncluded() {
    //given
    invoiceService.saveInvoice(INVOICE_WASBUD_LINK_2018);
    invoiceService.saveInvoice(INVOICE_WASBUD_SPAN_CLAMP_2017);
    invoiceService.saveInvoice(INVOICE_TRANSPOL_SPAN_CLAMP_SUPPORT_2016);

    // when
    BigDecimal actual = taxCalculatorService.getTaxIncluded(COMPANY_DRUKPOL.getNip())
        .setScale(2, BigDecimal.ROUND_HALF_UP);

    //then
    assertThat(actual, is(createBigDecimal(233.69)));
  }

  @Test
  public void shouldReturnTaxBaseBeforeRounding() {
    //given
    for (int i = 0; i < 5; i++) {
      invoiceService.saveInvoice(INVOICE_TRANSPOL_SPAN_CLAMP_SUPPORT_2016);
    }
    String nip = COMPANY_TRANSPOL.getNip();
    insuranceService.saveInsurance(nip, PENSION_INSURANCE);
    insuranceService.saveInsurance(nip, PENSION_INSURANCE2);

    //when
    BigDecimal actual = taxCalculatorService
        .taxCalculationBaseBeforeRounding(COMPANY_TRANSPOL.getNip())
        .setScale(2, RoundingMode.HALF_UP);

    //then
    assertThat(actual, is(createBigDecimal(1551.00)));
  }

  @Test
  public void shouldReturnSumOfPensionInsurances() {
    //given
    String nip = COMPANY_TRANSPOL.getNip();
    insuranceService.saveInsurance(nip, PENSION_INSURANCE);
    insuranceService.saveInsurance(nip, PENSION_INSURANCE2);

    //when
    BigDecimal actual = taxCalculatorService.sumOfPensionInsurance(COMPANY_TRANSPOL.getNip())
        .setScale(2, RoundingMode.HALF_UP);

    //then
    assertThat(actual, is(createBigDecimal(1200.00)));
  }

  @Test
  public void shouldReturnSumOfHealthInsurances() {
    //given
    String nip = COMPANY_DRUTEX.getNip();
    insuranceService.saveInsurance(nip, HEALTH_INSURANCE);
    insuranceService.saveInsurance(nip, HEALTH_INSURANCE2);

    //when
    BigDecimal actual = taxCalculatorService
        .sumOfHealthInsurancesToSubstract(COMPANY_DRUTEX.getNip())
        .setScale(2, RoundingMode.HALF_UP);

    //then
    assertThat(actual, is(BigDecimal.valueOf(1033.33)));
  }

  @Test
  public void shouldReturnProfit() {
    //given
    String nip = COMPANY_TRANSPOL.getNip();
    invoiceService.saveInvoice(INVOICE_TRANSPOL_SPAN_CLAMP_SUPPORT_2016);
    insuranceService.saveInsurance(nip, PENSION_INSURANCE);
    insuranceService.saveInsurance(nip, PENSION_INSURANCE2);

    //when
    BigDecimal actual = taxCalculatorService.getProfit(COMPANY_TRANSPOL.getNip())
        .setScale(2, RoundingMode.HALF_UP);

    //then
    assertThat(actual, is(createBigDecimal(550.20)));
  }

  @Test
  public void shouldCalculateNetValue() {
    //when
    BigDecimal actual = taxCalculatorService.getTotalNetValue(INVOICE_WASBUD_SPAN_CLAMP_2017)
        .setScale(2, RoundingMode.HALF_UP);
    BigDecimal expected = createBigDecimal(504.00);

    //then
    assertThat(actual, is(expected));
  }

  @Test
  public void shoulCalculateFinalIncomeTaxForIncomeBelowTaxThreshold() {
    //given
    invoiceService.saveInvoice(INVOICE_WASBUD_SPAN_CLAMP_2017);
    invoiceService.saveInvoice(INVOICE_WASBUD_SPAN_CLAMP_2017);
    invoiceService.saveInvoice(INVOICE_WASBUD_SPAN_CLAMP_2017);

    //when
    BigDecimal actual = taxCalculatorService.finalIncomeTax(COMPANY_WASBUD.getNip());

    //then
    assertThat(actual, is(createBigDecimal(272.16))); //todo check calculation again
  }

  @Test
  public void shouldCalculateFinalIncomeTaxForIncomeHigherThanTaxThreshold() {
    //given
    for (int i = 0; i < 200; i++) {
      invoiceService.saveInvoice(INVOICE_WASBUD_SPAN_CLAMP_2017);
    }
    String nip = COMPANY_WASBUD.getNip();
    insuranceService.saveInsurance(nip, HEALTH_INSURANCE2);
    insuranceService.saveInsurance(nip, HEALTH_INSURANCE3);
    insuranceService.saveInsurance(nip, PENSION_INSURANCE3);

    //when
    BigDecimal actual = taxCalculatorService.finalIncomeTax(COMPANY_WASBUD.getNip()).setScale(2)
        .setScale(2, RoundingMode.HALF_UP);

    //then
    assertThat(actual, is(createBigDecimal(18798.41).setScale(2)));
  }

  @Test
  public void shouldCalculateFinalIncomeTaxForLinearTaxType() {
    //given
    for (int i = 0; i < 10; i++) {
      invoiceService.saveInvoice(INVOICE_TRANSPOL_SPAN_CLAMP_SUPPORT_2016);
    }
    String nip = COMPANY_TRANSPOL.getNip();
    insuranceService.saveInsurance(nip, PENSION_INSURANCE);
    insuranceService.saveInsurance(nip, PENSION_INSURANCE2);

    //when
    BigDecimal actual = taxCalculatorService.finalIncomeTax(COMPANY_TRANSPOL.getNip()).setScale(2);

    //then
    assertThat(actual, is(createBigDecimal(817.38).setScale(2)));
  }
}