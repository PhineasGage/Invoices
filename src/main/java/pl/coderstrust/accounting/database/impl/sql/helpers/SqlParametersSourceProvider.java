package pl.coderstrust.accounting.database.impl.sql.helpers;

import java.sql.Date;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import pl.coderstrust.accounting.model.Company;
import pl.coderstrust.accounting.model.Insurance;
import pl.coderstrust.accounting.model.Invoice;
import pl.coderstrust.accounting.model.InvoiceEntry;

public class SqlParametersSourceProvider {

  public static MapSqlParameterSource getInvoiceMapSqlParameterSource(Invoice invoice, int buyerId,
      int sellerId) {
    MapSqlParameterSource namedParameters = new MapSqlParameterSource();
    namedParameters.addValue("identifier", invoice.getIdentifier());
    namedParameters.addValue("issue_date", Date.valueOf(invoice.getIssueDate()));
    namedParameters.addValue("sale_date", Date.valueOf(invoice.getSaleDate()));
    namedParameters.addValue("sale_place", invoice.getSalePlace());
    namedParameters.addValue("buyer_id", buyerId);
    namedParameters.addValue("seller_id", sellerId);
    return namedParameters;
  }

  public static MapSqlParameterSource getCompanyMapSqlParameterSource(Company company) {
    MapSqlParameterSource namedParameters = new MapSqlParameterSource();
    namedParameters.addValue("name", company.getName());
    namedParameters.addValue("nip", company.getNip());
    namedParameters.addValue("street", company.getStreet());
    namedParameters.addValue("postal_code", company.getPostalCode());
    namedParameters.addValue("city", company.getCity());
    namedParameters.addValue("discount", company.getDiscount());
    namedParameters.addValue("is_active", company.getIsPersonalUsageOfCar());
    namedParameters.addValue("tax_type", company.getTaxType().getTaxTypeString());
    return namedParameters;
  }

  public static MapSqlParameterSource getInvoiceEntryMapSqlParameterSource(InvoiceEntry invoiceEntry,
      int invoiceId) {
    MapSqlParameterSource namedParameters = new MapSqlParameterSource();
    namedParameters.addValue("description", invoiceEntry.getDescription());
    namedParameters.addValue("vatRate", invoiceEntry.getVatRate());
    namedParameters.addValue("netPrice", invoiceEntry.getNetPrice());
    namedParameters.addValue("quantity", invoiceEntry.getQuantity());
    namedParameters.addValue("category", invoiceEntry.getCategory());
    namedParameters.addValue("invoice_id", invoiceId);
    return namedParameters;
  }

  public static MapSqlParameterSource getInsuranceMapSqlParameterSource(Insurance insurance) {
    MapSqlParameterSource namedParameters = new MapSqlParameterSource();
    namedParameters.addValue("issue_date", insurance.getIssueDate());
    namedParameters.addValue("type", insurance.getType().getInsuranceTypeString());
    namedParameters.addValue("amount", insurance.getAmount());
    namedParameters.addValue("nip", insurance.getNip());
    return  namedParameters;
  }
}
