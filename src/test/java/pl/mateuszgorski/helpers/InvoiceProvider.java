package pl.coderstrust.accounting.helpers;

import static pl.coderstrust.accounting.helpers.InvoiceEntryProvider.EMPTY_DESCRIPTION;
import static pl.coderstrust.accounting.helpers.InvoiceEntryProvider.EMPTY_NET_PRICE;
import static pl.coderstrust.accounting.helpers.InvoiceEntryProvider.EMPTY_QUANTITY;
import static pl.coderstrust.accounting.helpers.InvoiceEntryProvider.EMPTY_VAT_RATE;
import static pl.coderstrust.accounting.helpers.InvoiceEntryProvider.INCORRECT_VAT_RATE;
import static pl.coderstrust.accounting.helpers.InvoiceEntryProvider.ONE_LINK;
import static pl.coderstrust.accounting.helpers.InvoiceEntryProvider.SPAN_CLAMP;
import static pl.coderstrust.accounting.helpers.InvoiceEntryProvider.SPAN_CLAMP_SUPPORT;

import java.time.LocalDate;
import java.util.Collections;
import pl.coderstrust.accounting.model.Invoice;
import pl.coderstrust.accounting.model.Invoice.InvoiceBuilder;

public class InvoiceProvider {

  public static final Invoice INVOICE_DRUTEX_SPAN_CLAMP_SUPPORT_2018 = new InvoiceBuilder()
      .identifier("1/2018")
      .issueDate(LocalDate.of(2018, 5, 12))
      .saleDate(LocalDate.of(2018, 5, 12))
      .salePlace("Krakow")
      .buyer(CompanyProvider.COMPANY_TRANSPOL)
      .seller(CompanyProvider.COMPANY_DRUTEX)
      .entries(SPAN_CLAMP_SUPPORT)
      .build();

  public static final Invoice INVOICE_WASBUD_SPAN_CLAMP_2017 = new InvoiceBuilder()
      .identifier("2/2018")
      .issueDate(LocalDate.of(2017, 3, 3))
      .saleDate(LocalDate.of(2017, 3, 2))
      .salePlace("Grudziadz")
      .buyer(CompanyProvider.COMPANY_DRUKPOL)
      .seller(CompanyProvider.COMPANY_WASBUD)
      .entries(SPAN_CLAMP)
      .build();

  public static final Invoice INVOICE_TRANSPOL_SPAN_CLAMP_SUPPORT_2016 = new InvoiceBuilder()
      .identifier("3/2018")
      .issueDate(LocalDate.of(2016, 7, 31))//
      .saleDate(LocalDate.of(2016, 7, 31))
      .salePlace("Grudziadz")
      .buyer(CompanyProvider.COMPANY_DRUKPOL)
      .seller(CompanyProvider.COMPANY_TRANSPOL)
      .entries(SPAN_CLAMP_SUPPORT)
      .build();

  public static final Invoice INVOICE_DRUTEX_LINK_2016 = new InvoiceBuilder()
      .identifier("4/2018")
      .issueDate(LocalDate.of(2016, 02, 29))
      .saleDate(LocalDate.of(2016, 02, 29))
      .salePlace("Bydgoszcz")
      .buyer(CompanyProvider.COMPANY_WASBUD)
      .seller(CompanyProvider.COMPANY_DRUTEX)
      .entries(ONE_LINK)
      .build();

  public static final Invoice INVOICE_WASBUD_LINK_2018 = new InvoiceBuilder()
      .identifier("ss999")
      .issueDate(LocalDate.of(2018, 06, 13))
      .saleDate(LocalDate.of(2018, 05, 12))
      .salePlace("Radomsko")
      .buyer(CompanyProvider.COMPANY_DRUTEX)
      .seller(CompanyProvider.COMPANY_WASBUD)
      .entries(ONE_LINK)
      .build();

  public static final Invoice INVOICE_EMPTY_ENTRIES = new InvoiceBuilder()
      .identifier("555/mm")
      .issueDate(LocalDate.of(2018, 11, 30))//
      .saleDate(LocalDate.of(2018, 11, 30))
      .salePlace("LODZ")
      .buyer(CompanyProvider.COMPANY_WASBUD)
      .seller(CompanyProvider.COMPANY_DRUTEX)
      .entries(Collections.emptyList())
      .build();

  public static final Invoice INVOICE_BLANK_SALE_PLACE = new InvoiceBuilder()
      .identifier("4/2018")
      .issueDate(LocalDate.of(2018, 06, 13))
      .saleDate(LocalDate.of(2018, 05, 12))
      .salePlace("")
      .buyer(CompanyProvider.COMPANY_WASBUD)
      .seller(CompanyProvider.COMPANY_DRUTEX)
      .entries(ONE_LINK)
      .build();

  public static final Invoice INVOICE_BLANK_IDENTIFIER = new InvoiceBuilder()
      .identifier("")
      .issueDate(LocalDate.of(2018, 06, 13))
      .saleDate(LocalDate.of(2018, 05, 12))
      .salePlace("Lodz")
      .buyer(CompanyProvider.COMPANY_WASBUD)
      .seller(CompanyProvider.COMPANY_DRUTEX)
      .entries(ONE_LINK)
      .build();

  public static final Invoice INVOICE_BLANK_ISSUE_DATE = new InvoiceBuilder()
      .identifier("555/mm")
      .issueDate(null)
      .saleDate(LocalDate.of(2018, 05, 12))
      .salePlace("Lodz")
      .buyer(CompanyProvider.COMPANY_WASBUD)
      .seller(CompanyProvider.COMPANY_DRUTEX)
      .entries(ONE_LINK)
      .build();

  public static final Invoice INVOICE_BLANK_SALE_DATE = new InvoiceBuilder()
      .identifier("555/mm")
      .issueDate(LocalDate.of(2018, 05, 12))
      .saleDate(null)
      .salePlace("Lodz")
      .buyer(CompanyProvider.COMPANY_WASBUD)
      .seller(CompanyProvider.COMPANY_DRUTEX)
      .entries(ONE_LINK)
      .build();

  public static final Invoice INVOICE_BLANK_BUYER_NAME = new InvoiceBuilder()
      .identifier("555/mm")
      .issueDate(LocalDate.of(2018, 06, 13))
      .saleDate(LocalDate.of(2018, 05, 12))
      .salePlace("Lodz")
      .buyer(CompanyProvider.COMPANY_BLANK_NAME)
      .seller(CompanyProvider.COMPANY_DRUTEX)
      .entries(ONE_LINK)
      .build();

  public static final Invoice INVOICE_BLANK_BUYER_NIP = new InvoiceBuilder()
      .identifier("ss999")
      .issueDate(LocalDate.of(2018, 06, 13))
      .saleDate(LocalDate.of(2018, 05, 12))
      .salePlace("Lodz")
      .buyer(CompanyProvider.COMPANY_BLANK_NIP)
      .seller(CompanyProvider.COMPANY_DRUTEX)
      .entries(ONE_LINK)
      .build();

  public static final Invoice INVOICE_BLANK_BUYER_STREET = new InvoiceBuilder()
      .identifier("ss999")
      .issueDate(LocalDate.of(2018, 06, 13))
      .saleDate(LocalDate.of(2018, 05, 12))
      .salePlace("Lodz")
      .buyer(CompanyProvider.COMPANY_BLANK_STREET)
      .seller(CompanyProvider.COMPANY_DRUTEX)
      .entries(ONE_LINK)
      .build();

  public static final Invoice INVOICE_BLANK_BUYER_POSTAL_CODE = new InvoiceBuilder()
      .identifier("ss999")
      .issueDate(LocalDate.of(2018, 06, 13))
      .saleDate(LocalDate.of(2018, 05, 12))
      .salePlace("Lodz")
      .buyer(CompanyProvider.COMPANY_BLANK_POSTAL_CODE)
      .seller(CompanyProvider.COMPANY_DRUTEX)
      .entries(ONE_LINK)
      .build();

  public static final Invoice INVOICE_BLANK_BUYER_CITY = new InvoiceBuilder()
      .identifier("ss999")
      .issueDate(LocalDate.of(2018, 06, 13))
      .saleDate(LocalDate.of(2018, 05, 12))
      .salePlace("Lodz")
      .buyer(CompanyProvider.COMPANY_BLANK_CITY)
      .seller(CompanyProvider.COMPANY_DRUTEX)
      .entries(ONE_LINK)
      .build();

  public static final Invoice INVOICE_BLANK_SELLER_NAME = new InvoiceBuilder()
      .identifier("ss999")
      .issueDate(LocalDate.of(2018, 06, 13))
      .saleDate(LocalDate.of(2018, 05, 12))
      .salePlace("Lodz")
      .buyer(CompanyProvider.COMPANY_DRUTEX)
      .seller(CompanyProvider.COMPANY_BLANK_NAME)
      .entries(ONE_LINK)
      .build();

  public static final Invoice INVOICE_BLANK_SELLER_NIP = new InvoiceBuilder()
      .identifier("ss999")
      .issueDate(LocalDate.of(2018, 06, 13))
      .saleDate(LocalDate.of(2018, 05, 12))
      .salePlace("Lodz")
      .buyer(CompanyProvider.COMPANY_DRUTEX)
      .seller(CompanyProvider.COMPANY_BLANK_NIP)
      .entries(ONE_LINK)
      .build();

  public static final Invoice INVOICE_BLANK_SELLER_STREET = new InvoiceBuilder()
      .identifier("ss999")
      .issueDate(LocalDate.of(2018, 06, 13))
      .saleDate(LocalDate.of(2018, 05, 12))
      .salePlace("Lodz")
      .buyer(CompanyProvider.COMPANY_DRUTEX)
      .seller(CompanyProvider.COMPANY_BLANK_STREET)
      .entries(ONE_LINK)
      .build();


  public static final Invoice INVOICE_BLANK_SELLER_POSTAL_CODE = new InvoiceBuilder()
      .identifier("ss999")
      .issueDate(LocalDate.of(2018, 06, 13))
      .saleDate(LocalDate.of(2018, 05, 12))
      .salePlace("Lodz")
      .buyer(CompanyProvider.COMPANY_DRUTEX)
      .seller(CompanyProvider.COMPANY_BLANK_POSTAL_CODE)
      .entries(ONE_LINK)
      .build();

  public static final Invoice INVOICE_BLANK_SELLER_CITY = new InvoiceBuilder()
      .identifier("ss999")
      .issueDate(LocalDate.of(2018, 06, 13))
      .saleDate(LocalDate.of(2018, 05, 12))
      .salePlace("Lodz")
      .buyer(CompanyProvider.COMPANY_DRUTEX)
      .seller(CompanyProvider.COMPANY_BLANK_CITY)
      .entries(ONE_LINK)
      .build();

  public static final Invoice INVOICE_BLANK_SELLER_DISCOUNT = new InvoiceBuilder()
      .identifier("ss999")
      .issueDate(LocalDate.of(2018, 06, 13))
      .saleDate(LocalDate.of(2018, 05, 12))
      .salePlace("Lodz")
      .buyer(CompanyProvider.COMPANY_DRUTEX)
      .seller(CompanyProvider.COMPANY_BLANK_DISCOUNT)
      .entries(ONE_LINK)
      .build();


  public static final Invoice INVOICE_BLANK_ENTRY_DESCRIPTION = new InvoiceBuilder()
      .identifier("ss999")
      .issueDate(LocalDate.of(2018, 06, 13))
      .saleDate(LocalDate.of(2018, 05, 12))
      .salePlace("Lodz")
      .buyer(CompanyProvider.COMPANY_DRUTEX)
      .seller(CompanyProvider.COMPANY_WASBUD)
      .entries(EMPTY_DESCRIPTION)
      .build();

  public static final Invoice INVOICE_BLANK_ENTRY_NET_PRICE = new InvoiceBuilder()
      .identifier("ss999")
      .issueDate(LocalDate.of(2018, 06, 13))
      .saleDate(LocalDate.of(2018, 05, 12))
      .salePlace("Lodz")
      .buyer(CompanyProvider.COMPANY_DRUTEX)
      .seller(CompanyProvider.COMPANY_WASBUD)
      .entries(EMPTY_NET_PRICE)
      .build();

  public static final Invoice INVOICE_BLANK_ENTRY_VAT_RATE = new InvoiceBuilder()
      .identifier("ss999")
      .issueDate(LocalDate.of(2018, 06, 13))
      .saleDate(LocalDate.of(2018, 05, 12))
      .salePlace("Lodz")
      .buyer(CompanyProvider.COMPANY_DRUTEX)
      .seller(CompanyProvider.COMPANY_WASBUD)
      .entries(EMPTY_VAT_RATE)
      .build();

  public static final Invoice INVOICE_BLANK_ENTRY_QUNTITY = new InvoiceBuilder()
      .identifier("ss999")
      .issueDate(LocalDate.of(2018, 06, 13))
      .saleDate(LocalDate.of(2018, 05, 12))
      .salePlace("Lodz")
      .buyer(CompanyProvider.COMPANY_DRUTEX)
      .seller(CompanyProvider.COMPANY_WASBUD)
      .entries(EMPTY_QUANTITY)
      .build();

  public static final Invoice INVOICE_BAD_DISCOUNT_VALUE = new InvoiceBuilder()
      .identifier("ss999")
      .issueDate(LocalDate.of(2018, 06, 13))
      .saleDate(LocalDate.of(2018, 05, 12))
      .salePlace("Radomsko")
      .buyer(CompanyProvider.COMPANY_DISCOUNT_BIGGER_THAN_1)
      .seller(CompanyProvider.COMPANY_DRUTEX)
      .entries(ONE_LINK)
      .build();

  public static final Invoice INVOICE_INCORRECT_VAT_RATE = new InvoiceBuilder()
      .identifier("ss999")
      .issueDate(LocalDate.of(2018, 06, 13))
      .saleDate(LocalDate.of(2018, 05, 12))
      .salePlace("Radomsko")
      .buyer(CompanyProvider.COMPANY_TRANSPOL)
      .seller(CompanyProvider.COMPANY_DRUTEX)
      .entries(INCORRECT_VAT_RATE)
      .build();

}
