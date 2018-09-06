package pl.mateuszgorski.database.impl.file.helpers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.mateuszgorski.model.Invoice;

@Service
public class InvoiceConverter {

  private ObjectMapper mapper;

  @Autowired
  public InvoiceConverter(ObjectMapper mapper) {
    this.mapper = mapper;
  }

  public String convertInvoiceToJson(Invoice invoice) {
    try {
      return mapper.writeValueAsString(invoice);
    } catch (JsonProcessingException exception) {
      throw new RuntimeException("cannot convert to Json", exception);
    }
  }

  public Invoice convertJsonToInvoice(String json) {
    try {
      return mapper.readValue(json, Invoice.class);
    } catch (IOException exception) {
      throw new RuntimeException("cannot convert from Json", exception);
    }
  }

  public List<String> convertListOfInvoicesToListOfStrings(List<Invoice> invoices) {
    return invoices.stream()
        .map(this::convertInvoiceToJson)
        .collect(Collectors.toList());
  }

  public List<Invoice> convertListOfStringsToListOfInvoices(List<String> invoices) {
    return invoices.stream()
        .map(this::convertJsonToInvoice)
        .collect(Collectors.toList());
  }


}