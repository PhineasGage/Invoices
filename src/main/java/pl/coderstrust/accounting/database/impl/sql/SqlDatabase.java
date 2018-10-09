package pl.coderstrust.accounting.database.impl.sql;

import static pl.coderstrust.accounting.database.impl.sql.helpers.SqlParametersSourceProvider.getCompanyMapSqlParameterSource;
import static pl.coderstrust.accounting.database.impl.sql.helpers.SqlQueriesProvider.DELETE_COMPANY;
import static pl.coderstrust.accounting.database.impl.sql.helpers.SqlQueriesProvider.DELETE_INSURANCE;
import static pl.coderstrust.accounting.database.impl.sql.helpers.SqlQueriesProvider.DELETE_INVOICE;
import static pl.coderstrust.accounting.database.impl.sql.helpers.SqlQueriesProvider.DELETE_INVOICE_ENTRIES;
import static pl.coderstrust.accounting.database.impl.sql.helpers.SqlQueriesProvider.DELETE_INVOICE_ENTRY;
import static pl.coderstrust.accounting.database.impl.sql.helpers.SqlQueriesProvider.INSERT_INTO_COMPANY;

import java.sql.SQLException;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import pl.coderstrust.accounting.database.Database;
import pl.coderstrust.accounting.model.Company;
import pl.coderstrust.accounting.model.ItemsForDatabase;

public abstract class SqlDatabase<T extends ItemsForDatabase> implements Database {

  protected JdbcTemplate jdbcTemplate;
  protected String tableName;

  public SqlDatabase(JdbcTemplate jdbcTemplate, String tableName) {
    this.jdbcTemplate = jdbcTemplate;
    this.tableName = tableName;
  }

  @Override
  public abstract int saveItem(ItemsForDatabase item);

  @Override
  public abstract List<T> getItems();

  @Override
  public abstract void updateItemById(int id, ItemsForDatabase item);

  @Override
  public abstract void removeItemById(int id);

  @Override
  public void clearDatabase() {
    jdbcTemplate.update(DELETE_INSURANCE);
    jdbcTemplate.update(DELETE_INVOICE_ENTRIES);
    jdbcTemplate.update(DELETE_INVOICE_ENTRY);
    jdbcTemplate.update(DELETE_INVOICE);
    jdbcTemplate.update(DELETE_COMPANY);
  }

  protected int saveCompany(Company company) throws SQLException {
    NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(
        jdbcTemplate);
    String update = INSERT_INTO_COMPANY;
    KeyHolder keyHolder = new GeneratedKeyHolder();
    namedParameterJdbcTemplate.update(update, getCompanyMapSqlParameterSource(company), keyHolder);
    int idCompany = keyHolder.getKey().intValue();
    return idCompany;
  }


}