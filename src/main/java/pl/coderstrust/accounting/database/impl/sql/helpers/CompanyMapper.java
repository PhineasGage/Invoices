package pl.coderstrust.accounting.database.impl.sql.helpers;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import pl.coderstrust.accounting.model.Company;
import pl.coderstrust.accounting.model.Invoice;
import pl.coderstrust.accounting.model.ItemsForDatabase;

public class CompanyMapper<T extends ItemsForDatabase> implements RowMapper<Company> {

  @Override
  public Company mapRow(ResultSet resultSet, int rowNumber) throws SQLException {
    return new Company.CompanyBuilder()
        .id(resultSet.getInt("id"))
        .name(resultSet.getString("name"))
        .nip(resultSet.getString("nip"))
        .street(resultSet.getString("street"))
        .postalCode(resultSet.getString("postal_code"))
        .city(resultSet.getString("city"))
        .discount(resultSet.getBigDecimal("discount"))
        .build();
  }
}
