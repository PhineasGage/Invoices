package pl.mateuszgorski.helpers;

import java.math.BigDecimal;
import pl.mateuszgorski.model.Company;

public class CompanyProvider {


  public static final Company COMPANY_TRANSPOL = new Company.CompanyBuilder()
      .name("Transpol")
      .nip("5621760000")
      .street("Grodzka")
      .postalCode("32008")
      .city("Krakow")
      .discount(BigDecimal.valueOf(0.0))
      .build();


  public static final Company COMPANY_DRUTEX = new Company.CompanyBuilder()
      .name("Drutex")
      .nip("8421622720")
      .street("Rybna")
      .postalCode("31158")
      .city("Rybnik")
      .discount(BigDecimal.valueOf(0.2))
      .build();


  public static final Company COMPANY_WASBUD = new Company.CompanyBuilder()
      .name("WasBud")
      .nip("6271206366")
      .street("Targowa")
      .postalCode("15689")
      .city("warszawa")
      .discount(BigDecimal.valueOf(0.1))
      .build();


  public static final Company COMPANY_DRUKPOL = new Company.CompanyBuilder()
      .name("DrukPol")
      .nip("5311688030")
      .street("Bolesnej Meki Panskiej")
      .postalCode("58963")
      .city("Sosnowiec")
      .discount(BigDecimal.valueOf(0.3))
      .build();

  public static final Company COMPANY_BLANK_NAME = new Company.CompanyBuilder()
      .name("")
      .nip("1452369135")
      .street("Bolesnej Meki Panskiej")
      .postalCode("58963")
      .city("Sosnowiec")
      .discount(BigDecimal.valueOf(0.3))
      .build();

  public static final Company COMPANY_BLANK_NIP = new Company.CompanyBuilder()
      .name("Flex")
      .nip("")
      .street("Bolesnej Meki Panskiej")
      .postalCode("58963")
      .city("Sosnowiec")
      .discount(BigDecimal.valueOf(0.3))
      .build();


  public static final Company COMPANY_BLANK_STREET = new Company.CompanyBuilder()
      .name("Flex")
      .nip("1452369135")
      .street("")
      .postalCode("58963")
      .city("Sosnowiec")
      .discount(BigDecimal.valueOf(0.3))
      .build();

  public static final Company COMPANY_BLANK_POSTAL_CODE = new Company.CompanyBuilder()
      .name("Flex")
      .nip("1452369135")
      .street("Bolesnej Meki Panskiej")
      .postalCode("")
      .city("Sosnowiec")
      .discount(BigDecimal.valueOf(0.3))
      .build();

  public static final Company COMPANY_BLANK_CITY = new Company.CompanyBuilder()
      .name("Flex")
      .nip("1452369135")
      .street("Bolesnej Meki Panskiej")
      .postalCode("58963")
      .city("")
      .discount(BigDecimal.valueOf(0.3))
      .build();

  public static final Company COMPANY_BLANK_DISCOUNT = new Company.CompanyBuilder()
      .name("Flex")
      .nip("1452369135")
      .street("Bolesnej Meki Panskiej")
      .postalCode("58963")
      .city("Sosnowiec")
      .discount(null)
      .build();

  public static final Company COMPANY_DISCOUNT_BIGGER_THAN_1 = new Company.CompanyBuilder()
      .name("Flex")
      .nip("1452369135")
      .street("Bolesnej Meki Panskiej")
      .postalCode("58963")
      .city("Sosnowiec")
      .discount(BigDecimal.valueOf(1.3))
      .build();
}
