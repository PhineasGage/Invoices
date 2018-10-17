package pl.coderstrust.accounting.soap;

import invoice_service_soap.AddInvoiceRequest;
import invoice_service_soap.AddInvoiceResponse;
import invoice_service_soap.GetInvoicesByDateRequest;
import invoice_service_soap.GetInvoicesByDateResponse;
import invoice_service_soap.GetInvoicesRequest;
import invoice_service_soap.GetInvoicesResponse;
import invoice_service_soap.InvoiceList;
import invoice_service_soap.RemoveInvoiceRequest;
import invoice_service_soap.RemoveInvoiceResponse;
import invoice_service_soap.UpdateInvoiceRequest;
import invoice_service_soap.UpdateInvoiceResponse;
import java.time.LocalDate;
import java.util.List;
import javax.xml.datatype.DatatypeConfigurationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import pl.coderstrust.accounting.controller.InvoiceValidator;
import pl.coderstrust.accounting.logic.InvoiceService;
import pl.coderstrust.accounting.model.Invoice;

@Endpoint
public class InvoiceEndpoint {

  private static final String NAMESPACE_URI = "http://invoice-service-soap";

  private InvoiceService invoiceService;
  private InvoiceConverter invoiceConverter = new InvoiceConverter();
  private InvoiceValidator invoiceValidator;

  @Autowired
  public InvoiceEndpoint(InvoiceService invoiceService, InvoiceValidator invoiceValidator) {
    this.invoiceService = invoiceService;
    this.invoiceValidator = invoiceValidator;
  }

  @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getInvoicesRequest")
  @ResponsePayload
  //TODO getCountry?
  public GetInvoicesResponse getCountry(@RequestPayload GetInvoicesRequest request) throws DatatypeConfigurationException {
    GetInvoicesResponse response = new GetInvoicesResponse();
    response.setInvoice(invoiceConverter.invoiceToSoapInvoice(invoiceService.getInvoiceById(request.getId()).get()));
    return response;
  }

  @PayloadRoot(namespace = NAMESPACE_URI, localPart = "addInvoiceRequest")
  @ResponsePayload
  public AddInvoiceResponse addInvoice(@RequestPayload AddInvoiceRequest request) {
    Invoice invoice = invoiceConverter.convertSoapInvoiceToInvoice(request.getInvoice());
    AddInvoiceResponse response = new AddInvoiceResponse();

    List<String> validateResults = invoiceValidator.validate(invoice);

    if (validateResults.isEmpty()) {
      int id = invoiceService.saveInvoice(invoice);
      response.setId(id);
    } else {
      response.setId(-1);
    }

    return response;
  }

  @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getInvoicesByDateRequest")
  @ResponsePayload
  public GetInvoicesByDateResponse getInvoicesByDateRequest(@RequestPayload GetInvoicesByDateRequest request) throws DatatypeConfigurationException {
    GetInvoicesByDateResponse response = new GetInvoicesByDateResponse();

    LocalDate startDate = LocalDate.parse(request.getStartDate().toString());
    LocalDate endDate = LocalDate.parse(request.getEndDate().toString());

    List<Invoice> invoices = invoiceService.getInvoicesByIssueDate(startDate, endDate);
    InvoiceList soapInvoices = new InvoiceList();

    for (Invoice invoice : invoices) {
      soapInvoices.getInvoice().add(invoiceConverter.invoiceToSoapInvoice(invoice));
    }

    response.setInvoiceList(soapInvoices);
    return response;
  }

  @PayloadRoot(namespace = NAMESPACE_URI, localPart = "updateInvoiceRequest")
  @ResponsePayload
  public UpdateInvoiceResponse updateInvoiceRequest(@RequestPayload UpdateInvoiceRequest request) {
    Invoice invoice = invoiceConverter.convertSoapInvoiceToInvoice(request.getInvoice());
    UpdateInvoiceResponse response = new UpdateInvoiceResponse();

    List<String> validateResults = invoiceValidator.validate(invoice);
    int id = request.getId();
    invoice.setId(id);

    if (!invoiceService.getInvoiceById(id).isPresent()) {
      response.setResponse("Invoice with id " + id + "doesn't exist");
    } else if (validateResults.isEmpty()) {
      invoiceService.updateInvoice(id, invoice);
      response.setIdInvoice(id);
    } else {
      response.setIdInvoice(-1);
    }

    return response;
  }

  @PayloadRoot(namespace = NAMESPACE_URI, localPart = "removeInvoiceRequest")
  @ResponsePayload
  public RemoveInvoiceResponse removeInvoiceRequest(@RequestPayload RemoveInvoiceRequest request) {
    RemoveInvoiceResponse response = new RemoveInvoiceResponse();
    int id = request.getId();

    if (!invoiceService.getInvoiceById(id).isPresent()) {
      response.setResponse("Invoice with id " + id + "doesn't exist");
    } else {
      invoiceService.removeInvoiceById(id);
      response.setResponse("Invoice Removed");
    }

    return response;
  }
}