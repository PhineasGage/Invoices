package pl.coderstrust.accounting.database.impl.sql.helpers;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import pl.coderstrust.accounting.model.InvoiceEntry;
import pl.coderstrust.accounting.model.VatRate;

public class InvoiceEntryMapper implements RowMapper<InvoiceEntry> {

  @Override
  public InvoiceEntry mapRow(ResultSet resultSet, int rowNumber) throws SQLException {
    return new InvoiceEntry.InvoiceEntryBuilder()
        .id(resultSet.getInt("Id"))
        .description(resultSet.getString("description"))
        .netPrice(resultSet.getBigDecimal("net_price"))
        .vatRate(VatRate.findByKey(resultSet.getInt("vat_rate")))
        .quantity(resultSet.getBigDecimal("quantity"))
        .category(resultSet.getString("category"))
        .build();
  }
}

