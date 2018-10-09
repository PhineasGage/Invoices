package pl.coderstrust.accounting.database.impl.sql;

import static pl.coderstrust.accounting.database.impl.sql.helpers.SqlParametersSourceProvider.getInsuranceMapSqlParameterSource;
import static pl.coderstrust.accounting.database.impl.sql.helpers.SqlQueriesProvider.DELETE_FROM_INSURANCE_GIVEN_INSURANCE_ID;
import static pl.coderstrust.accounting.database.impl.sql.helpers.SqlQueriesProvider.INSERT_INTO_INSURANCE;
import static pl.coderstrust.accounting.database.impl.sql.helpers.SqlQueriesProvider.NAMED_PARAMETERS_FOR_INSURANCE_UPDATE;
import static pl.coderstrust.accounting.database.impl.sql.helpers.SqlQueriesProvider.SELECT_ALL_FROM;

import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import pl.coderstrust.accounting.database.impl.sql.helpers.InsuranceMapper;
import pl.coderstrust.accounting.model.Insurance;
import pl.coderstrust.accounting.model.ItemsForDatabase;

public class SqlDatabaseInsurance extends SqlDatabase<Insurance>  {

  public SqlDatabaseInsurance(JdbcTemplate jdbcTemplate, String tableName) {
    super(jdbcTemplate, tableName);
  }

  @Override
  public int saveItem(ItemsForDatabase item) {
    NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(
        jdbcTemplate);
    String update = INSERT_INTO_INSURANCE;
    MapSqlParameterSource namedParameters = getInsuranceMapSqlParameterSource((Insurance)item);
    KeyHolder keyHolder = new GeneratedKeyHolder();
    namedParameterJdbcTemplate.update(update, namedParameters, keyHolder);
    return keyHolder.getKey().intValue();
  }

  @Override
  public List<Insurance> getItems() {
    String query = SELECT_ALL_FROM + tableName;
    return (List<Insurance>) jdbcTemplate.query(query, new InsuranceMapper());
  }

  @Override
  public void updateItemById(int id, ItemsForDatabase item) {
    String query = "UPDATE " + tableName + " SET ";
    NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(
        jdbcTemplate);
    updateInsurance(id, item, query, namedParameterJdbcTemplate);
  }

  private void updateInsurance(int id, ItemsForDatabase item, String query, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
    query += NAMED_PARAMETERS_FOR_INSURANCE_UPDATE + id;
    namedParameterJdbcTemplate.update(query, getInsuranceMapSqlParameterSource((Insurance) item));
  }

  @Override
  public void removeItemById(int id) {
    jdbcTemplate.execute(DELETE_FROM_INSURANCE_GIVEN_INSURANCE_ID + id);
  }
}
