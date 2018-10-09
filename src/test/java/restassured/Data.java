package restassured;

import pl.coderstrust.accounting.helpers.InvoiceProvider;
import pl.coderstrust.accounting.model.Invoice;

public interface Data {

  Invoice invoiceDrutex_2018 = InvoiceProvider.INVOICE_DRUTEX_SPAN_CLAMP_SUPPORT_2018;
  Invoice invoiceDrutex_2016 = InvoiceProvider.INVOICE_DRUTEX_LINK_2016;
  Invoice invoiceWasbud_2018 = InvoiceProvider.INVOICE_WASBUD_LINK_2018;
  Invoice invoiceWasbud_2017 = InvoiceProvider.INVOICE_WASBUD_SPAN_CLAMP_2017;
  Invoice invoiceTranspol_2016 = InvoiceProvider.INVOICE_TRANSPOL_SPAN_CLAMP_SUPPORT_2016;

  String invoicesUrl = "http://localhost:8888/invoices";
  String taxCalculatorUrl = "http://localhost:8888/taxcalculator";
}