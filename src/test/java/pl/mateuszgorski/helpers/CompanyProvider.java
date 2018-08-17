package pl.mateuszgorski.helpers;

import java.math.BigDecimal;
import pl.mateuszgorski.model.Company;

public class CompanyProvider {

  // TODO use Builder instead of constructor - with so many argument it's hard to know what is what.
  public static final Company COMPANY_TRANSPOL = new Company(
      "Transpol",
      "5621760000",
      "Grodzka",
      "32008",
      "Krakow",
      BigDecimal.valueOf(0.0));

  public static final Company COMPANY_DRUTEX = new Company(
      "Drutex",
      "8421622720",
      "Rybna",
      "31158",
      "Rybnik",
      BigDecimal.valueOf(0.2));

  public static final Company COMPANY_WASBUD = new Company(
      "WasBud",
      "6271206366",
      "Targowa",
      "15689",
      "warszawa",
      BigDecimal.valueOf(0.1));

  public static final Company COMPANY_DRUKPOL = new Company(
      "DrukPol",
      "5311688030",
      "Bolesnej Meki Panskiej",
      "58963",
      "Sosnowiec",
      BigDecimal.valueOf(0.3));

  public static final Company COMPANY_BLANK_NAME = new Company(
      "",
      "1452369135",
      "Bolesnej Meki Panskiej",
      "58963",
      "Sosnowiec",
      BigDecimal.valueOf(0.3));

  public static final Company COMPANY_BLANK_NIP = new Company(
      "Flex",
      "",
      "Bolesnej Meki Panskiej",
      "58963",
      "Sosnowiec",
      BigDecimal.valueOf(0.3));

  public static final Company COMPANY_BLANK_STREET = new Company(
      "Flex",
      "1452369130",
      "",
      "58963",
      "Sosnowiec",
      BigDecimal.valueOf(0.3));

  public static final Company COMPANY_BLANK_POSTAL_CODE = new Company(
      "Flex",
      "1452369130",
      "Piltza",
      "",
      "Sosnowiec",
      BigDecimal.valueOf(0.3));

  public static final Company COMPANY_BLANK_CITY = new Company(
      "Flex",
      "1452369130",
      "Piltza",
      "5896323348",
      "",
      BigDecimal.valueOf(0.3));

  public static final Company COMPANY_BLANK_DISCOUNT = new Company(
      "Flex",
      "1452369130",
      "Las",
      "58963",
      "Sosnowiec",
      null);

  public static final Company COMPANY_DISCOUNT_BIGGER_THAN_1 = new Company(
      "Flexi",
      "1452369130",
      "Lasy",
      "58963",
      "Sosnowiec",
      BigDecimal.valueOf(1.3));
}
