package pl.coderstrust.accounting.controller;

import io.swagger.annotations.ApiOperation;
import java.time.LocalDate;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import pl.coderstrust.accounting.model.Company;
import pl.coderstrust.accounting.model.Invoice;

public interface InvoiceApi {

  @ApiOperation(value = "Saves invoice",
      notes = "Returns ResponseEntity with id of saved invoice",
      response = ResponseEntity.class)
  @PostMapping
  ResponseEntity<?> saveInvoice(Invoice invoice);

  @ApiOperation(value = "Gets all invoices",
      notes = "Returns list of all saved invoices",
      response = Invoice.class,
      responseContainer = "List")
  @GetMapping
  List<Invoice> getInvoices();

  @ApiOperation(value = "Gets single invoice",
      notes = "Returns Invoice with ID specified",
      response = ResponseEntity.class)
  @GetMapping("/{id}")
  ResponseEntity<Invoice> getSingleInvoice(int id);

  @ApiOperation(value = "updates invoice",
      notes = "Replaces invoice with specified id with invoice provided ",
      response = ResponseEntity.class)
  @PutMapping("/{id}")
  ResponseEntity<?> updateInvoice(int id, Invoice invoice);

  @ApiOperation(value = "removes invoice",
      notes = "Deletes invoice with specified id",
      response = ResponseEntity.class)
  @DeleteMapping("/{id}")
  ResponseEntity<?> removeInvoiceById(int id);

  @ApiOperation(value = "Gets all invoices from date range",
      notes = "Returns list of all saved invoices within specified date range",
      response = Invoice.class,
      responseContainer = "List")
  @GetMapping("/dates")
  List<Invoice> getInvoicesByIssueDateRange(LocalDate startDate, LocalDate endDate);

  @ApiOperation(value = "Gets all invoices for specific company",
      notes = "Returns list of all saved invoices for Company with provided Id",
      response = Invoice.class,
      responseContainer = "List")
  @GetMapping("/{companyId}/invoice/")
  List<Invoice> getAllInvoicesForSpecificCompany(int id);

  @ApiOperation(value = "Gets specific invoices for specific company",
      notes = "Returns Invoice with specific Id for Company with provided Id",
      response = ResponseEntity.class)
  @GetMapping("/{companyId}/invoice/{invoiceId}")
  ResponseEntity<?> getSpecificInvoiceForSpecifiedCompany(int invoiceId, int companyId);
}

