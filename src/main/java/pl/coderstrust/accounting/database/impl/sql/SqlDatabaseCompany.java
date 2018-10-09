package pl.coderstrust.accounting.database.impl.sql;

import static pl.coderstrust.accounting.database.impl.sql.helpers.SqlParametersSourceProvider.getCompanyMapSqlParameterSource;
import static pl.coderstrust.accounting.database.impl.sql.helpers.SqlQueriesProvider.DELETE_FROM_COMPANY_GIVEN_COMPANY_ID;
import static pl.coderstrust.accounting.database.impl.sql.helpers.SqlQueriesProvider.NAMED_PARAMETERS_FOR_COMPANY_UPDATE;
import static pl.coderstrust.accounting.database.impl.sql.helpers.SqlQueriesProvider.SELECT_ALL_FROM;

import java.sql.SQLException;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import pl.coderstrust.accounting.database.impl.sql.helpers.CompanyMapper;
import pl.coderstrust.accounting.model.Company;
import pl.coderstrust.accounting.model.ItemsForDatabase;

public class SqlDatabaseCompany extends SqlDatabase {

  public SqlDatabaseCompany(JdbcTemplate jdbcTemplate, String tableName) {
    super(jdbcTemplate, tableName);
  }

  @Override
  public int saveItem(ItemsForDatabase item) {
    try {
      return saveCompany((Company) item);
    } catch (SQLException exc) {
      throw new RuntimeException("cannot save Item", exc);
    }
  }

  @Override
  public List getItems() {
    String query = SELECT_ALL_FROM + tableName;
    return (List<Company>) jdbcTemplate.query(query, new CompanyMapper(jdbcTemplate));
  }

  @Override
  public void updateItemById(int id, ItemsForDatabase item) {
    String query = "UPDATE " + tableName + " SET ";
    NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(
        jdbcTemplate);
    updateCompany(id, item, query, namedParameterJdbcTemplate);
  }

  @Override
  public void removeItemById(int id) {
    jdbcTemplate.execute(DELETE_FROM_COMPANY_GIVEN_COMPANY_ID + id);
  }

  private void updateCompany(int id, ItemsForDatabase item, String query, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
    query += NAMED_PARAMETERS_FOR_COMPANY_UPDATE + id;
    namedParameterJdbcTemplate.update(query, getCompanyMapSqlParameterSource((Company) item));
  }
}