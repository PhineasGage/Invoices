package pl.coderstrust.accounting.controller;

import io.swagger.annotations.Api;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.coderstrust.accounting.logic.InvoiceService;
import pl.coderstrust.accounting.model.Invoice;

@RequestMapping("/invoices")
@RestController
@Api(value = "/invoices", description = "Operations on invoices")
public class InvoiceController implements InvoiceApi {

  private InvoiceValidator invoiceValidator;
  private InvoiceService invoiceService;

  public InvoiceController(InvoiceService invoiceService, InvoiceValidator invoiceValidator) {
    this.invoiceService = invoiceService;
    this.invoiceValidator = invoiceValidator;
  }

  public ResponseEntity<?> saveInvoice(@RequestBody Invoice invoice) {
    List<String> validationResult = invoiceValidator.validate(invoice);
    if (!validationResult.isEmpty()) {
      return ResponseEntity.badRequest().body(validationResult);
    }
    int id = invoiceService.saveInvoice(invoice);
    return ResponseEntity.ok(id);
  }

  public List<Invoice> getInvoices() {
    return invoiceService.getInvoices();
  }

  public List<Invoice> getInvoicesByIssueDateRange(
      @RequestParam(name = "startDate", required = true)
      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
      @RequestParam(name = "endDate", required = true)
      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
    return invoiceService.getInvoicesByIssueDate(startDate, endDate);
  }

  public ResponseEntity<Invoice> getSingleInvoice(@PathVariable(name = "id", required = true) int id) {
    Optional<Invoice> invoiceById = invoiceService.getInvoiceById(id);
    return invoiceById.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
  }

  public ResponseEntity<?> updateInvoice(@PathVariable(name = "id", required = true) int id, @RequestBody Invoice invoice) {
    if (!invoiceService.getInvoiceById(id).isPresent()) {
      return ResponseEntity.notFound().build();
    }
    List<String> validationResult = invoiceValidator.validate(invoice);
    if (!validationResult.isEmpty()) {
      return ResponseEntity.badRequest().body(validationResult);
    }
    invoiceService.updateInvoice(id, invoice);
    return ResponseEntity.ok().build();
  }

  public ResponseEntity<?> removeInvoiceById(@PathVariable(name = "id", required = true) int id) {
    if (!invoiceService.getInvoiceById(id).isPresent()) {
      return ResponseEntity.notFound().build();
    }
    invoiceService.removeInvoiceById(id);
    return ResponseEntity.ok().build();
  }
}