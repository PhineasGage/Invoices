package pl.mateuszgorski.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.mateuszgorski.logic.InvoiceService;
import pl.mateuszgorski.model.Invoice;

@RequestMapping("/invoices")
@RestController
@Api(value = "/invoices", description = "Operations on invoices")
public class InvoiceController {

  private InvoiceValidator invoiceValidator;
  private InvoiceService invoiceService;

  public InvoiceController(InvoiceService invoiceService, InvoiceValidator invoiceValidator) {
    this.invoiceService = invoiceService;
    this.invoiceValidator = invoiceValidator;
  }

  @ApiOperation(value = "Saves invoice", // TODO please extract interface and move swagger annotations to it
      notes = "Returns ResponseEntity with id of saved invoice",
      response = ResponseEntity.class,
      responseContainer = "")
  @PostMapping // TODO RequestMapping etc can be moved too, RequestBody etc must stay in this class
  public ResponseEntity<?> saveInvoice(@RequestBody Invoice invoice) throws IOException {
    List<String> validationResult = invoiceValidator.validate(invoice);
    if (!validationResult.isEmpty()) {
      return ResponseEntity.badRequest().body(validationResult);
    }
    invoiceService.saveInvoice(invoice);
    return ResponseEntity.ok(invoice.getId());
  }

  @ApiOperation(value = "Gets all invoices",
      notes = "Returns list of all saved invoices",
      response = Invoice.class,
      responseContainer = "List")
  @GetMapping
  public List<Invoice> getInvoices() throws IOException {
    return invoiceService.getInvoices(); // TODO it's not good practice to throw exception from controller - user should receive nice errors only
  }

  @ApiOperation(value = "Gets all invoices from date range",
      notes = "Returns list of all saved invoices within specified date range",
      response = Invoice.class,
      responseContainer = "List")
  @GetMapping("/dates")
  public List<Invoice> getInvoicesByIssueDateRange(
      @RequestParam(name = "startDate", required = true)
      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
      @RequestParam(name = "endDate", required = true)
      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) throws IOException {
    return invoiceService.getInvoicesByIssueDate(startDate, endDate);
  }

  @ApiOperation(value = "Gets single invoice",
      notes = "Returns Invoice with ID specified",
      response = ResponseEntity.class,
      responseContainer = "")
  @GetMapping("/{id}")
  public ResponseEntity<Invoice> getSingleInvoice(@PathVariable(name = "id", required = true) int id) throws IOException {
    if (!invoiceService.getInvoiceById(id).isPresent()) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(invoiceService.getInvoiceById(id).get()); // TODO don't call getInvoiceById twice!
  }

  @ApiOperation(value = "updates invoice",
      notes = "Replaces invoice with specified id by invoice provided ",
      response = ResponseEntity.class,
      responseContainer = "")
  @PutMapping("/{id}")
  public ResponseEntity<?> updateInvoice(@PathVariable(name = "id", required = true) int id,
      @RequestBody Invoice invoice) throws IOException {
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

  @ApiOperation(value = "removes invoice",
      notes = "Deletes invoice with specified id",
      response = ResponseEntity.class,
      responseContainer = "")
  @DeleteMapping("/{id}")
  public ResponseEntity<?> removeInvoiceById(@PathVariable(name = "id", required = true) int id) throws IOException {
    if (!invoiceService.getInvoiceById(id).isPresent()) {
      return ResponseEntity.notFound().build();
    }
    invoiceService.removeInvoiceById(id);
    return ResponseEntity.ok().build();
  }
}