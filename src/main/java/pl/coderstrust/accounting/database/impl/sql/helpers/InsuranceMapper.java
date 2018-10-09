package pl.coderstrust.accounting.database.impl.sql.helpers;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import pl.coderstrust.accounting.model.Insurance;
import pl.coderstrust.accounting.model.InsuranceType;

public class InsuranceMapper implements RowMapper<Insurance> {

  @Override
  public Insurance mapRow(ResultSet resultSet, int rowNum) throws SQLException {
    return new Insurance.InsuranceBuilder()
        .id(resultSet.getInt("id"))
        .issueDate(resultSet.getDate("issue_date").toLocalDate())
        .amount(resultSet.getBigDecimal("amount"))
        .type(stringToInsuranceTypeEnum(resultSet.getString("type")))
        .nip(resultSet.getString("nip"))
        .build();
  }

  private InsuranceType stringToInsuranceTypeEnum(String insuranceType) {
    return InsuranceType.valueOf(insuranceType);
  }
}
