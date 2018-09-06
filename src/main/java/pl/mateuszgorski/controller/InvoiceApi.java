package pl.mateuszgorski.controller;

import io.swagger.annotations.ApiOperation;
import java.time.LocalDate;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import pl.mateuszgorski.model.Invoice;

public interface InvoiceApi {

  @ApiOperation(value = "Saves invoice",
      notes = "Returns ResponseEntity with id of saved invoice",
      response = ResponseEntity.class,
      responseContainer = "")
  @PostMapping
  ResponseEntity<?> saveInvoice(Invoice invoice);

  @ApiOperation(value = "Gets all invoices",
      notes = "Returns list of all saved invoices",
      response = Invoice.class,
      responseContainer = "List")
  @GetMapping
  List<Invoice> getInvoices();

  @ApiOperation(value = "Gets all invoices from date range",
      notes = "Returns list of all saved invoices within specified date range",
      response = Invoice.class,
      responseContainer = "List")
  @GetMapping("/dates")
  List<Invoice> getInvoicesByIssueDateRange(LocalDate startDate, LocalDate endDate);

  @ApiOperation(value = "Gets single invoice",
      notes = "Returns Invoice with ID specified",
      response = ResponseEntity.class,
      responseContainer = "")
  @GetMapping("/{id}")
  ResponseEntity<Invoice> getSingleInvoice(int id);

  @ApiOperation(value = "updates invoice",
      notes = "Replaces invoice with specified id by invoice provided ",
      response = ResponseEntity.class,
      responseContainer = "")
  @PutMapping("/{id}")
  ResponseEntity<?> updateInvoice(int id, Invoice invoice);

  @ApiOperation(value = "removes invoice",
      notes = "Deletes invoice with specified id",
      response = ResponseEntity.class,
      responseContainer = "")
  @DeleteMapping("/{id}")
  ResponseEntity<?> removeInvoiceById(int id);


}
