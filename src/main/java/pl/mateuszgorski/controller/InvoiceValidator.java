package pl.coderstrust.accounting.controller;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import pl.coderstrust.accounting.model.Invoice;

@Service
public class InvoiceValidator {

  private CompanyValidator companyValidator;
  private InvoiceEntryValidator invoiceEntryValidator;

  public InvoiceValidator(CompanyValidator companyValidator, InvoiceEntryValidator invoiceEntryValidator) {
    this.companyValidator = companyValidator;
    this.invoiceEntryValidator = invoiceEntryValidator;
  }

  List<String> validate(Invoice invoice) {
    List<String> validationErrors = new ArrayList<>();

    if (invoice.getIdentifier() == null || invoice.getIdentifier().trim().equals("")) {
      validationErrors.add("Identifier not found");
    }

    if (invoice.getIssueDate() == null) {
      validationErrors.add("Issue date not found");
    }

    if (invoice.getSaleDate() == null) {
      validationErrors.add("Sale date not found");
    }

    if (invoice.getSalePlace() == null || invoice.getSalePlace().trim().equals("")) {
      validationErrors.add("Sale place not found");
    }

    validationErrors.addAll(companyValidator.validate(invoice.getSeller(), "Seller"));
    validationErrors.addAll(companyValidator.validate(invoice.getBuyer(), "Buyer"));
    validationErrors.addAll(invoiceEntryValidator.validate(invoice.getEntries()));

    return validationErrors;
  }
}
