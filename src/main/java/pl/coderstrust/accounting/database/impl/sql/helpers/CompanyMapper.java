package pl.coderstrust.accounting.database.impl.sql.helpers;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import pl.coderstrust.accounting.model.Company;
import pl.coderstrust.accounting.model.ItemsForDatabase;
import pl.coderstrust.accounting.model.TaxType;

public class CompanyMapper<T extends ItemsForDatabase> implements RowMapper<Company> {

  JdbcTemplate jdbcTemplate;

  public CompanyMapper(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

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
        .isPersonalUsageOfCar(resultSet.getBoolean("is_active"))
        .taxType(stringToTaxTypeEnum(resultSet.getString("tax_type")))
        .build();
  }

  private TaxType stringToTaxTypeEnum(String taxType) {
    return TaxType.valueOf(taxType);
  }
}

