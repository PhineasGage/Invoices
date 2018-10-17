package pl.coderstrust.accounting.soap;

import invoice_service_soap.Company;
import invoice_service_soap.Insurance;
import invoice_service_soap.InsuranceType;
import invoice_service_soap.Invoice;
import invoice_service_soap.InvoiceEntry;
import invoice_service_soap.TaxType;
import invoice_service_soap.VatRate;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import pl.coderstrust.accounting.model.Company.CompanyBuilder;
import pl.coderstrust.accounting.model.Insurance.InsuranceBuilder;
import pl.coderstrust.accounting.model.Invoice.InvoiceBuilder;
import pl.coderstrust.accounting.model.InvoiceEntry.InvoiceEntryBuilder;

public class InvoiceConverter {

  pl.coderstrust.accounting.model.Invoice convertSoapInvoiceToInvoice(Invoice invoice) {

    return new InvoiceBuilder()
        .identifier(invoice.getIdentifier())
        .issueDate(LocalDate.parse(invoice.getIssueDate().toString()))
        .saleDate(LocalDate.parse(invoice.getSaleDate().toString()))
        .salePlace(invoice.getSalePlace())
        .buyer(convertSoapCompanyToCompany(invoice.getBuyer()))
        .seller(convertSoapCompanyToCompany(invoice.getSeller()))
        .entries(convertSoapEntriesToEntries(invoice.getEntries()))
        .build();
  }

  private pl.coderstrust.accounting.model.Company convertSoapCompanyToCompany(Company soapCompany) {
    return new CompanyBuilder()
        .name(soapCompany.getName())
        .nip(soapCompany.getNip())
        .city(soapCompany.getCity())
        .street(soapCompany.getStreet())
        .postalCode(soapCompany.getPostalCode())
        .discount(soapCompany.getDiscount())
        .taxType(pl.coderstrust.accounting.model.TaxType.valueOf(soapCompany.getTaxType().value()))
        .isPersonalUsageOfCar(soapCompany.isIsPersonalUsageOfCar())
        .build();
  }

  private pl.coderstrust.accounting.model.InvoiceEntry convertSoapEntryToEntry(InvoiceEntry soapEntry) {
    return new InvoiceEntryBuilder()
        .description(soapEntry.getDescription())
        .netPrice(soapEntry.getNetPrice())
        .quantity(soapEntry.getQuantity())
        .vatRate(pl.coderstrust.accounting.model.VatRate.valueOf(soapEntry.getVatRate().value()))
        // todo not sure if thi is correct - enum bigint ?
        // invoiceEntryBuilder.vatRate(new BigDecimal(soapEntry.getVatRate().toString()));
        .category(soapEntry.getCategory())
        .build();
  }

  private List<pl.coderstrust.accounting.model.InvoiceEntry> convertSoapEntriesToEntries(List<InvoiceEntry> soapEntries) {
    ArrayList<pl.coderstrust.accounting.model.InvoiceEntry> entries = new ArrayList<>();
    for (InvoiceEntry entry : soapEntries) {
      entries.add(new pl.coderstrust.accounting.model.InvoiceEntry(convertSoapEntryToEntry(entry))); //todo how to do it using stream
    }
    return entries;
  }

  private pl.coderstrust.accounting.model.Insurance convertInsurance(Insurance soapInsurance) {
    return new InsuranceBuilder()
        .type(pl.coderstrust.accounting.model.InsuranceType.valueOf(soapInsurance.getType().value()))
        .issueDate(LocalDate.parse(soapInsurance.getIssueDate().toString()))
        .amount(soapInsurance.getAmount())
        .nip(soapInsurance.getNip())
        .build();
  }

  private List<pl.coderstrust.accounting.model.Insurance> convertSoapInsurancesToInsurances(List<Insurance> soapInsurances) {
    ArrayList<pl.coderstrust.accounting.model.Insurance> insurances = new ArrayList<>();
    for (invoice_service_soap.Insurance insurance : soapInsurances) {
      insurances.add(new pl.coderstrust.accounting.model.Insurance(convertInsurance(insurance))); //todo how to do it using stream
    }
    return insurances;
  }

  Invoice invoiceToSoapInvoice(pl.coderstrust.accounting.model.Invoice invoice) throws DatatypeConfigurationException {
    Invoice soapInvoice = new Invoice();
    soapInvoice.setId(invoice.getId());
    soapInvoice.setIdentifier(invoice.getIdentifier());
    soapInvoice.setIssueDate(convertLocalDateToXmlDate(invoice.getIssueDate()));
    soapInvoice.setSaleDate(convertLocalDateToXmlDate(invoice.getSaleDate()));
    soapInvoice.setSalePlace(invoice.getSalePlace());
    soapInvoice.setBuyer(convertCompanyToSoapCompany(invoice.getBuyer()));
    soapInvoice.setSeller(convertCompanyToSoapCompany(invoice.getSeller()));
    soapInvoice.getEntries().addAll(convertEntriesToSoapEntries(invoice.getEntries()));
    return soapInvoice;
  }

  private Company convertCompanyToSoapCompany(pl.coderstrust.accounting.model.Company company) throws DatatypeConfigurationException {
    Company soapCompany = new Company();
    soapCompany.setName(company.getName());
    soapCompany.setNip(company.getNip());
    soapCompany.setCity(company.getCity());
    soapCompany.setStreet(company.getStreet());
    soapCompany.setPostalCode(company.getPostalCode());
    soapCompany.setDiscount(company.getDiscount());
    soapCompany.setTaxType(TaxType.valueOf(company.getTaxType().toString()));
    soapCompany.setIsPersonalUsageOfCar(company.getIsPersonalUsageOfCar());
    return soapCompany;
  }

  private List<InvoiceEntry> convertEntriesToSoapEntries(List<pl.coderstrust.accounting.model.InvoiceEntry> entries) {
    ArrayList<InvoiceEntry> soapEntries = new ArrayList<>();
    for (pl.coderstrust.accounting.model.InvoiceEntry entry : entries) {
      soapEntries.add(convertEntryToSoapEntry(entry)); //todo how to do it using stream
    }
    return soapEntries;
  }

  private InvoiceEntry convertEntryToSoapEntry(pl.coderstrust.accounting.model.InvoiceEntry entry) {
    InvoiceEntry soapInvoiceEntry = new InvoiceEntry();
    soapInvoiceEntry.setDescription(entry.getDescription());
    soapInvoiceEntry.setNetPrice(entry.getNetPrice());
    soapInvoiceEntry.setQuantity(entry.getQuantity());
    soapInvoiceEntry.setVatRate(VatRate.valueOf(entry.getVatRate().toString())); // todo not sure if thi is correct - enum bigint ?
    // invoiceEntryBuilder.vatRate(new BigDecimal(soapEntry.getVatRate().toString()));
    soapInvoiceEntry.setCategory(entry.getCategory());
    return soapInvoiceEntry;
  }

  private List<Insurance> convertInsurancesToSoapInsurances(List<pl.coderstrust.accounting.model.Insurance> insurances)
      throws DatatypeConfigurationException {
    ArrayList<Insurance> soapInsurances = new ArrayList<>();
    for (pl.coderstrust.accounting.model.Insurance insurance : insurances) {
      soapInsurances.add(convertInsuranceToSoapInsurance(insurance)); //todo add new before convert method ?
    }
    return soapInsurances;
  }

  private Insurance convertInsuranceToSoapInsurance(pl.coderstrust.accounting.model.Insurance insurance) throws DatatypeConfigurationException {
    Insurance soapInsurance = new Insurance();
    insurance.setId(insurance.getId());
    soapInsurance.setType(InsuranceType.valueOf(insurance.getType().toString()));
    soapInsurance.setIssueDate(convertLocalDateToXmlDate(insurance.getIssueDate()));
    soapInsurance.setAmount(insurance.getAmount());
    return soapInsurance;
  }

  private XMLGregorianCalendar convertLocalDateToXmlDate(LocalDate date) throws DatatypeConfigurationException {
    XMLGregorianCalendar xmlDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(date.toString());
    return xmlDate;
  }

}
