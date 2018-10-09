package pl.coderstrust.accounting.helpers;

import static pl.coderstrust.accounting.helpers.BigDecimalProvider.createBigDecimal;

import pl.coderstrust.accounting.model.Company;
import pl.coderstrust.accounting.model.TaxType;

public class CompanyProvider {

  public static final Company COMPANY_TRANSPOL = new Company.CompanyBuilder()
      .name("Transpol")
      .nip("5621760000")
      .street("Grodzka")
      .postalCode("32008")
      .city("Krakow")
      .discount(createBigDecimal(0.0))
      .taxType(TaxType.LINEAR)
      .isPersonalUsageOfCar(false)
      .build();

  public static final Company COMPANY_DRUTEX = new Company.CompanyBuilder()
      .name("Drutex")
      .nip("8421622720")
      .street("Rybna")
      .postalCode("31158")
      .city("Rybnik")
      .discount(createBigDecimal(0.2))
      .taxType(TaxType.LINEAR)
      .isPersonalUsageOfCar(true)
      .build();

  public static final Company COMPANY_WASBUD = new Company.CompanyBuilder()
      .name("WasBud")
      .nip("6271206366")
      .street("Targowa")
      .postalCode("15689")
      .city("warszawa")
      .discount(createBigDecimal(0.1))
      .taxType(TaxType.GRADED)
      .isPersonalUsageOfCar(false)
      .build();

  public static final Company COMPANY_DRUKPOL = new Company.CompanyBuilder()
      .name("DrukPol")
      .nip("5311688030")
      .street("Bolesnej Meki Panskiej")
      .postalCode("58963")
      .city("Sosnowiec")
      .discount(createBigDecimal(0.3))
      .taxType(TaxType.LINEAR)
      .isPersonalUsageOfCar(false)
      .build();

  public static final Company COMPANY_EMPTY_INSURANCES = new Company.CompanyBuilder()
      .name("noInsurance")
      .nip("8451769793")
      .street("Piltza")
      .postalCode("58967")
      .city("Berlin")
      .discount(createBigDecimal(0.3))
      .taxType(TaxType.LINEAR)
      .isPersonalUsageOfCar(false)
      .build();

  public static final Company COMPANY_EMPTY_INSURANCES2 = new Company.CompanyBuilder()
      .name("noInsurance")
      .nip("8512624854")
      .street("3Maja")
      .postalCode("58907")
      .city("Oslo")
      .discount(createBigDecimal(0.3))
      .taxType(TaxType.LINEAR)
      .isPersonalUsageOfCar(false)
      .build();

  public static final Company COMPANY_BLANK_NAME = new Company.CompanyBuilder()
      .name("")
      .nip("1452369135")
      .street("Bolesnej Meki Panskiej")
      .postalCode("58963")
      .city("Sosnowiec")
      .discount(createBigDecimal(0.3))
      .taxType(TaxType.LINEAR)
      .isPersonalUsageOfCar(false)
      .build();

  public static final Company COMPANY_BLANK_NIP = new Company.CompanyBuilder()
      .name("Flex")
      .nip("")
      .street("Bolesnej Meki Panskiej")
      .postalCode("58963")
      .city("Sosnowiec")
      .discount(createBigDecimal(0.3))
      .taxType(TaxType.LINEAR)
      .isPersonalUsageOfCar(false)
      .build();


  public static final Company COMPANY_BLANK_STREET = new Company.CompanyBuilder()
      .name("Flex")
      .nip("1452369135")
      .street("")
      .postalCode("58963")
      .city("Sosnowiec")
      .discount(createBigDecimal(0.3))
      .taxType(TaxType.LINEAR)
      .isPersonalUsageOfCar(false)
      .build();

  public static final Company COMPANY_BLANK_POSTAL_CODE = new Company.CompanyBuilder()
      .name("Flex")
      .nip("1452369135")
      .street("Bolesnej Meki Panskiej")
      .postalCode("")
      .city("Sosnowiec")
      .discount(createBigDecimal(0.3))
      .taxType(TaxType.LINEAR)
      .isPersonalUsageOfCar(false)
      .build();

  public static final Company COMPANY_BLANK_CITY = new Company.CompanyBuilder()
      .name("Flex")
      .nip("1452369135")
      .street("Bolesnej Meki Panskiej")
      .postalCode("58963")
      .city("")
      .discount(createBigDecimal(0.3))
      .taxType(TaxType.LINEAR)
      .isPersonalUsageOfCar(false)
      .build();

  public static final Company COMPANY_BLANK_DISCOUNT = new Company.CompanyBuilder()
      .name("Flex")
      .nip("1452369135")
      .street("Bolesnej Meki Panskiej")
      .postalCode("58963")
      .city("Sosnowiec")
      .discount(null)
      .isPersonalUsageOfCar(false)
      .taxType(TaxType.LINEAR)
      .build();

  public static final Company COMPANY_DISCOUNT_BIGGER_THAN_1 = new Company.CompanyBuilder()
      .name("Flex")
      .nip("1452369135")
      .street("Bolesnej Meki Panskiej")
      .postalCode("58963")
      .city("Sosnowiec")
      .discount(createBigDecimal(1.3))
      .taxType(TaxType.LINEAR)
      .isPersonalUsageOfCar(false)
      .build();
}