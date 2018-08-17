package pl.mateuszgorski.helpers;

import static pl.mateuszgorski.helpers.InvoiceEntryProvider.EMPTY_DESCRIPTION;
import static pl.mateuszgorski.helpers.InvoiceEntryProvider.EMPTY_NET_PRICE;
import static pl.mateuszgorski.helpers.InvoiceEntryProvider.EMPTY_QUANTITY;
import static pl.mateuszgorski.helpers.InvoiceEntryProvider.EMPTY_VAT_RATE;
import static pl.mateuszgorski.helpers.InvoiceEntryProvider.ONE_LINK;
import static pl.mateuszgorski.helpers.InvoiceEntryProvider.SPAN_CLAMP;
import static pl.mateuszgorski.helpers.InvoiceEntryProvider.SPAN_CLAMP_SUPPORT;

import java.time.LocalDate;
import java.util.Collections;
import pl.mateuszgorski.model.Invoice;

public class InvoiceProvider {

  public static final Invoice INVOICE_KRAKOW_2018 = new Invoice(
      1, // TODO use builders everywhere - constructor with so many args is not readable
      "1/2018", // TODO if more than 2 args are used then use builder by default
      LocalDate.of(2018, 5, 12),
      LocalDate.of(2018, 5, 12),
      "Krakow",
      CompanyProvider.COMPANY_TRANSPOL,
      CompanyProvider.COMPANY_DRUTEX,
      SPAN_CLAMP_SUPPORT);

  // TODO I don't like your naming convention - is the city the most important aspect of each invoice? Do you know by sale place what is inside?
  public static final Invoice INVOICE_GRUDZIADZ_2017 = new Invoice(
      2,
      "2/2018",
      LocalDate.of(2017, 3, 3),
      LocalDate.of(2017, 3, 2),
      "Grudziadz",
      CompanyProvider.COMPANY_DRUKPOL,
      CompanyProvider.COMPANY_WASBUD,
      SPAN_CLAMP);

  public static final Invoice INVOICE_CHELMNO_2016 = new Invoice(
      3,
      "3/2018",
      LocalDate.of(2016, 03, 05), // TODO please use more various dates
      LocalDate.of(2016, 02, 04), // TODO 03 is octal not decimal - should be 3 :)
      "Chelmno",
      CompanyProvider.COMPANY_DRUKPOL,
      CompanyProvider.COMPANY_TRANSPOL,
      SPAN_CLAMP_SUPPORT);

  public static final Invoice INVOICE_BYDGOSZCZ_2018 = new Invoice(
      4,
      "4/2018",
      LocalDate.of(2018, 06, 13),
      LocalDate.of(2018, 05, 12),
      "Bydgoszcz",
      CompanyProvider.COMPANY_WASBUD,
      CompanyProvider.COMPANY_DRUTEX,
      ONE_LINK);

  public static final Invoice INVOICE_EMPTY_ENTRIES = new Invoice(
      4,
      "555/mm",
      LocalDate.of(2018, 02, 12),
      LocalDate.of(2018, 05, 12),
      "LODZ",
      CompanyProvider.COMPANY_WASBUD,
      CompanyProvider.COMPANY_DRUTEX,
      Collections.emptyList());

  public static final Invoice INVOICE_BLANK_SALE_PLACE = new Invoice(
      4,
      "4/2018",
      LocalDate.of(2018, 06, 13),
      LocalDate.of(2018, 05, 12),
      "",
      CompanyProvider.COMPANY_WASBUD,
      CompanyProvider.COMPANY_DRUTEX,
      ONE_LINK);

  public static final Invoice INVOICE_BLANK_IDENTIFIER = new Invoice(
      4,
      "",
      LocalDate.of(2018, 06, 13),
      LocalDate.of(2018, 05, 12),
      "LODZ",
      CompanyProvider.COMPANY_WASBUD,
      CompanyProvider.COMPANY_DRUTEX,
      ONE_LINK);

  public static final Invoice INVOICE_BLANK_ISSUE_DATE = new Invoice(
      4,
      "555/mm",
      null,
      LocalDate.of(2018, 05, 12),
      "LODZ",
      CompanyProvider.COMPANY_WASBUD,
      CompanyProvider.COMPANY_DRUTEX,
      ONE_LINK);

  public static final Invoice INVOICE_BLANK_SALE_DATE = new Invoice(
      4,
      "555/mm",
      LocalDate.of(2018, 05, 12),
      null,
      "LODZ",
      CompanyProvider.COMPANY_WASBUD,
      CompanyProvider.COMPANY_DRUTEX,
      ONE_LINK);

  public static final Invoice INVOICE_BLANK_BUYER_NAME = new Invoice(
      4,
      "sss999",
      LocalDate.of(2018, 06, 13),
      LocalDate.of(2018, 05, 12),
      "Berlin",
      CompanyProvider.COMPANY_BLANK_NAME,
      CompanyProvider.COMPANY_DRUTEX,
      ONE_LINK);

  public static final Invoice INVOICE_BLANK_BUYER_NIP = new Invoice(
      4,
      "sss999",
      LocalDate.of(2018, 06, 13),
      LocalDate.of(2018, 05, 12),
      "Berlin",
      CompanyProvider.COMPANY_BLANK_NIP,
      CompanyProvider.COMPANY_DRUTEX,
      ONE_LINK);

  public static final Invoice INVOICE_BLANK_BUYER_STREET = new Invoice(
      4,
      "sss999",
      LocalDate.of(2018, 06, 13),
      LocalDate.of(2018, 05, 12),
      "Berlin",
      CompanyProvider.COMPANY_BLANK_STREET,
      CompanyProvider.COMPANY_DRUTEX,
      ONE_LINK);

  public static final Invoice INVOICE_BLANK_BUYER_POSTAL_CODE = new Invoice(
      4,
      "sss999",
      LocalDate.of(2018, 06, 13),
      LocalDate.of(2018, 05, 12),
      "Berlin",
      CompanyProvider.COMPANY_BLANK_POSTAL_CODE,
      CompanyProvider.COMPANY_DRUTEX,
      ONE_LINK);

  public static final Invoice INVOICE_BLANK_BUYER_CITY = new Invoice(
      4,
      "sss999",
      LocalDate.of(2018, 06, 13),
      LocalDate.of(2018, 05, 12),
      "Berlin",
      CompanyProvider.COMPANY_BLANK_CITY,
      CompanyProvider.COMPANY_DRUTEX,
      ONE_LINK);

  public static final Invoice INVOICE_BLANK_BUYER_DISCOUNT = new Invoice(
      4,
      "sss999",
      LocalDate.of(2018, 06, 13),
      LocalDate.of(2018, 05, 12),
      "Berlin",
      CompanyProvider.COMPANY_BLANK_DISCOUNT,
      CompanyProvider.COMPANY_DRUTEX,
      ONE_LINK);

  public static final Invoice INVOICE_BLANK_SELLER_NAME = new Invoice(
      4,
      "sss999",
      LocalDate.of(2018, 06, 13),
      LocalDate.of(2018, 05, 12),
      "Berlin",
      CompanyProvider.COMPANY_DRUTEX,
      CompanyProvider.COMPANY_BLANK_NAME,
      ONE_LINK);

  public static final Invoice INVOICE_BLANK_SELLER_NIP = new Invoice(
      4,
      "sss999",
      LocalDate.of(2018, 06, 13),
      LocalDate.of(2018, 05, 12),
      "Berlin",
      CompanyProvider.COMPANY_DRUTEX,
      CompanyProvider.COMPANY_BLANK_NIP,
      ONE_LINK);

  public static final Invoice INVOICE_BLANK_SELLER_STREET = new Invoice(
      4,
      "sss999",
      LocalDate.of(2018, 06, 13),
      LocalDate.of(2018, 05, 12),
      "Berlin",
      CompanyProvider.COMPANY_DRUTEX,
      CompanyProvider.COMPANY_BLANK_STREET,
      ONE_LINK);

  public static final Invoice INVOICE_BLANK_SELLER_POSTAL_CODE = new Invoice(
      4,
      "sss999",
      LocalDate.of(2018, 06, 13),
      LocalDate.of(2018, 05, 12),
      "Berlin",
      CompanyProvider.COMPANY_DRUTEX,
      CompanyProvider.COMPANY_BLANK_POSTAL_CODE,
      ONE_LINK);

  public static final Invoice INVOICE_BLANK_SELLER_CITY = new Invoice(
      4,
      "sss999",
      LocalDate.of(2018, 06, 13),
      LocalDate.of(2018, 05, 12),
      "Berlin",
      CompanyProvider.COMPANY_DRUTEX,
      CompanyProvider.COMPANY_BLANK_CITY,
      ONE_LINK);

  public static final Invoice INVOICE_BLANK_SELLER_DISCOUNT = new Invoice(
      4,
      "sss999",
      LocalDate.of(2018, 06, 13),
      LocalDate.of(2018, 05, 12),
      "Berlin",
      CompanyProvider.COMPANY_DRUTEX,
      CompanyProvider.COMPANY_BLANK_DISCOUNT,
      ONE_LINK);

  public static final Invoice INVOICE_BLANK_ENTRY_DESCRIPTION = new Invoice(
      4,
      "sss999",
      LocalDate.of(2018, 06, 13),
      LocalDate.of(2018, 05, 12),
      "Berlin",
      CompanyProvider.COMPANY_DRUTEX,
      CompanyProvider.COMPANY_WASBUD,
      EMPTY_DESCRIPTION);

  public static final Invoice INVOICE_BLANK_ENTRY_NET_PRICE = new Invoice(
      4,
      "sss999",
      LocalDate.of(2018, 06, 13),
      LocalDate.of(2018, 05, 12),
      "Berlin",
      CompanyProvider.COMPANY_DRUTEX,
      CompanyProvider.COMPANY_WASBUD,
      EMPTY_NET_PRICE);

  public static final Invoice INVOICE_BLANK_ENTRY_VAT_RATE = new Invoice(
      4,
      "sss999",
      LocalDate.of(2018, 06, 13),
      LocalDate.of(2018, 05, 12),
      "Berlin",
      CompanyProvider.COMPANY_DRUTEX,
      CompanyProvider.COMPANY_WASBUD,
      EMPTY_VAT_RATE);

  public static final Invoice INVOICE_BLANK_ENTRY_QUNTITY = new Invoice(
      4,
      "sss999",
      LocalDate.of(2018, 06, 13),
      LocalDate.of(2018, 05, 12),
      "Berlin",
      CompanyProvider.COMPANY_DRUTEX,
      CompanyProvider.COMPANY_WASBUD,
      EMPTY_QUANTITY);

  public static final Invoice INVOICE_RADOMSKO_2018 = new Invoice(
      5,
      "4/2018",
      LocalDate.of(2018, 06, 13),
      LocalDate.of(2018, 05, 12),
      "Bydgoszcz",
      CompanyProvider.COMPANY_DRUTEX,
      CompanyProvider.COMPANY_WASBUD,
      ONE_LINK);

  public static final Invoice INVOICE_BAD_DISCOUNT_VALUE = new Invoice(
      14,
      "sss999121",
      LocalDate.of(2018, 06, 13),
      LocalDate.of(2018, 05, 12),
      "Berlin",
      CompanyProvider.COMPANY_DRUTEX,
      CompanyProvider.COMPANY_DISCOUNT_BIGGER_THAN_1,
      ONE_LINK);
}