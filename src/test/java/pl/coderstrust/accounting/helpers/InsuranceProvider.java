package pl.coderstrust.accounting.helpers;

import static pl.coderstrust.accounting.helpers.BigDecimalProvider.createBigDecimal;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import pl.coderstrust.accounting.model.Insurance;
import pl.coderstrust.accounting.model.InsuranceType;

public class InsuranceProvider {

  public static final Insurance PENSION_INSURANCE = new Insurance.InsuranceBuilder()
      .issueDate(LocalDate.of(2018, 5, 12))
      .type(InsuranceType.PENSION) // string zamiast InsType
      .amount(createBigDecimal(400.00))
      .build();

  public static final Insurance PENSION_INSURANCE2 = new Insurance.InsuranceBuilder()
      .issueDate(LocalDate.of(2018, 6, 12))
      .type(InsuranceType.PENSION) // string zamiast InsType
      .amount(createBigDecimal(800.00))
      .build();

  public static final Insurance PENSION_INSURANCE3 = new Insurance.InsuranceBuilder()
      .issueDate(LocalDate.of(2018, 5, 25))
      .type(InsuranceType.PENSION) // string zamiast InsType
      .amount(createBigDecimal(600.00))
      .build();

  public static final Insurance HEALTH_INSURANCE = new Insurance.InsuranceBuilder()
      .issueDate(LocalDate.of(2018, 5, 10))
      .type(InsuranceType.HEALTH) // string zamiast InsType
      .amount(createBigDecimal(400.00))
      .build();

  public static final Insurance HEALTH_INSURANCE2 = new Insurance.InsuranceBuilder()
      .issueDate(LocalDate.of(2017, 6, 10))
      .type(InsuranceType.HEALTH) // string zamiast InsType
      .amount(createBigDecimal(800.00))
      .build();

  public static final Insurance HEALTH_INSURANCE3 = new Insurance.InsuranceBuilder()
      .issueDate(LocalDate.of(2017, 5, 30))
      .type(InsuranceType.HEALTH) // string zamiast InsType
      .amount(createBigDecimal(700.00))
      .build();

  public static final List<Insurance> TWO_PENSION_INSURANCES = new ArrayList<>(Arrays.asList(PENSION_INSURANCE, PENSION_INSURANCE2));
  public static final List<Insurance> TWO_HEALTH_INSURANCES = new ArrayList<>(Arrays.asList(HEALTH_INSURANCE, HEALTH_INSURANCE2));
  public static final List<Insurance> TWO_HELATH_AND_ONE_PENSION_INSURANCES = new ArrayList<>(
      Arrays.asList(HEALTH_INSURANCE2, HEALTH_INSURANCE3, PENSION_INSURANCE3));
  public static final List<Insurance> EMPTY = new ArrayList<>(Arrays.asList());

}
