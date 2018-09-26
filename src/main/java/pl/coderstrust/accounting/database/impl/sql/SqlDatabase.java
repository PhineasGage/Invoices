package pl.coderstrust.accounting.database.impl.sql;

import static pl.coderstrust.accounting.database.impl.sql.helpers.SqlParametersSourceProvider.getCompanyMapSqlParameterSource;
import static pl.coderstrust.accounting.database.impl.sql.helpers.SqlParametersSourceProvider.getInvoiceEntryMapSqlParameterSource;
import static pl.coderstrust.accounting.database.impl.sql.helpers.SqlParametersSourceProvider.getInvoiceMapSqlParameterSource;
import static pl.coderstrust.accounting.database.impl.sql.helpers.SqlQueriesProvider.DELETE_ALL_FROM_INVOICE_ENTRIES_GIVEN_INVOICE_ID;
import static pl.coderstrust.accounting.database.impl.sql.helpers.SqlQueriesProvider.DELETE_COMPANY;
import static pl.coderstrust.accounting.database.impl.sql.helpers.SqlQueriesProvider.DELETE_FROM_COMPANY_GIVEN_COMPANY_ID;
import static pl.coderstrust.accounting.database.impl.sql.helpers.SqlQueriesProvider.DELETE_FROM_INVOICE_GIVEN_INVOICE_ID;
import static pl.coderstrust.accounting.database.impl.sql.helpers.SqlQueriesProvider.DELETE_INVOICE;
import static pl.coderstrust.accounting.database.impl.sql.helpers.SqlQueriesProvider.DELETE_INVOICE_ENTRIES;
import static pl.coderstrust.accounting.database.impl.sql.helpers.SqlQueriesProvider.DELETE_INVOICE_ENTRY;
import static pl.coderstrust.accounting.database.impl.sql.helpers.SqlQueriesProvider.INSERT_INTO_COMPANY;
import static pl.coderstrust.accounting.database.impl.sql.helpers.SqlQueriesProvider.INSERT_INTO_INVOICE;
import static pl.coderstrust.accounting.database.impl.sql.helpers.SqlQueriesProvider.INSERT_INTO_INVOICE_ENTRIES;
import static pl.coderstrust.accounting.database.impl.sql.helpers.SqlQueriesProvider.INSERT_INTO_INVOICE_ENTRY;
import static pl.coderstrust.accounting.database.impl.sql.helpers.SqlQueriesProvider.NAMED_PARAMETERS_FOR_COMPANY_UPDATE;
import static pl.coderstrust.accounting.database.impl.sql.helpers.SqlQueriesProvider.NAMED_PARAMETERS_FOR_INVOICE_UPDATE;
import static pl.coderstrust.accounting.database.impl.sql.helpers.SqlQueriesProvider.SELECT_ALL_FROM;

import java.sql.SQLException;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import pl.coderstrust.accounting.database.Database;
import pl.coderstrust.accounting.database.impl.sql.helpers.CompanyMapper;
import pl.coderstrust.accounting.database.impl.sql.helpers.InvoiceMapper;
import pl.coderstrust.accounting.model.Company;
import pl.coderstrust.accounting.model.Invoice;
import pl.coderstrust.accounting.model.InvoiceEntry;
import pl.coderstrust.accounting.model.ItemsForDatabase;

public class SqlDatabase<T extends ItemsForDatabase> implements Database {

  private JdbcTemplate jdbcTemplate;
  private String tableName;

  public SqlDatabase(JdbcTemplate jdbcTemplate, String tableName) {
    this.jdbcTemplate = jdbcTemplate;
    this.tableName = tableName;
  }

  @Override
  public int saveItem(ItemsForDatabase item) {
    try {
      switch (tableName) {
        case "Invoice":
          Company seller = ((Invoice) item).getSeller();
          Company buyer = ((Invoice) item).getBuyer();
          int idSeller = saveCompany(seller);
          int idBuyer = saveCompany(buyer);
          int idInvoice = saveInvoice((Invoice) item, idBuyer, idSeller);
          saveAllEntriesFromInvoice((Invoice) item, idInvoice);
          return idInvoice;
        case "Company":
          return saveCompany((Company) item);
        default:
          throw new RuntimeException("cannot save Item");
      }
    } catch (SQLException sqlException) {
      throw new RuntimeException("cannot save Item", sqlException);
    }
  }

  @Override
  public List<T> getItems() {
    String query = SELECT_ALL_FROM + tableName;
    switch (tableName) {
      case "Invoice":
        return (List<T>) jdbcTemplate.query(query, new InvoiceMapper(jdbcTemplate));
      case "Company":
        return (List<T>) jdbcTemplate.query(query, new CompanyMapper());
      default:
        throw new RuntimeException("Item cannot be saved - unknown type");
    }
  }

  @Override
  public void updateItemById(int id, ItemsForDatabase item) {
    String query = "UPDATE " + tableName + " SET ";
    NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(
        jdbcTemplate);
    try {
      switch (tableName) {
        case "Invoice":
          updateInvoice(id, item, query, namedParameterJdbcTemplate);
          break;
        case "Company":
          updateCompany(id, item, query, namedParameterJdbcTemplate);
          break;
        default:
          throw new RuntimeException("Item cannot be saved - unknown type");
      }
    } catch (SQLException sqlException) {
      throw new RuntimeException("cannot save Item", sqlException);
    }
  }

  @Override
  public void removeItemById(int id) {
    switch (tableName) {
      case "Invoice":
        jdbcTemplate.execute(DELETE_ALL_FROM_INVOICE_ENTRIES_GIVEN_INVOICE_ID + id);
        jdbcTemplate.execute(DELETE_FROM_INVOICE_GIVEN_INVOICE_ID + id);
        break;
      case "Company":
        jdbcTemplate.execute(DELETE_FROM_COMPANY_GIVEN_COMPANY_ID + id);
        break;
      default:
        throw new RuntimeException("Item cannot be saved - unknown type");
    }
  }

  @Override
  public void clearDatabase() {
    jdbcTemplate.update(DELETE_INVOICE_ENTRIES);
    jdbcTemplate.update(DELETE_INVOICE_ENTRY);
    jdbcTemplate.update(DELETE_INVOICE);
    jdbcTemplate.update(DELETE_COMPANY);
  }

  private int saveCompany(Company company) throws SQLException {
    NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(
        jdbcTemplate);
    String update = INSERT_INTO_COMPANY;
    KeyHolder keyHolder = new GeneratedKeyHolder();
    namedParameterJdbcTemplate.update(update, getCompanyMapSqlParameterSource(company), keyHolder);
    return keyHolder.getKey().intValue();
  }

  private void saveAllEntriesFromInvoice(Invoice item, int idInvoice) throws SQLException {
    for (InvoiceEntry invoiceEntry : item.getEntries()) {
      saveInvoiceEntry(invoiceEntry, idInvoice);
    }
  }

  private int saveInvoiceEntry(InvoiceEntry invoiceEntry, int invoiceId) throws SQLException {
    NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(
        jdbcTemplate);
    String update = INSERT_INTO_INVOICE_ENTRY;
    MapSqlParameterSource namedParameters = getInvoiceEntryMapSqlParameterSource(invoiceEntry, invoiceId);
    KeyHolder keyHolder = new GeneratedKeyHolder();
    namedParameterJdbcTemplate.update(update, namedParameters, keyHolder);
    int entriesId = keyHolder.getKey().intValue();
    namedParameters.addValue("entries_id", entriesId);
    String update2 = INSERT_INTO_INVOICE_ENTRIES;
    namedParameterJdbcTemplate.update(update2, namedParameters);
    return entriesId;
  }

  private int saveInvoice(Invoice invoice, int buyerId, int sellerId) throws SQLException {
    NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(
        jdbcTemplate);
    String update = INSERT_INTO_INVOICE;
    KeyHolder keyHolder = new GeneratedKeyHolder();
    namedParameterJdbcTemplate.update(update, getInvoiceMapSqlParameterSource(invoice, buyerId, sellerId), keyHolder);
    return keyHolder.getKey().intValue();
  }

  private void updateInvoice(int id, ItemsForDatabase item, String query, NamedParameterJdbcTemplate namedParameterJdbcTemplate) throws SQLException {
    Company seller = ((Invoice) item).getSeller();
    Company buyer = ((Invoice) item).getBuyer();
    int idSeller = saveCompany(seller);
    int idBuyer = saveCompany(buyer);
    query += NAMED_PARAMETERS_FOR_INVOICE_UPDATE + id;
    namedParameterJdbcTemplate.update(query, getInvoiceMapSqlParameterSource((Invoice) item, idBuyer, idSeller));
    String queryDelete = DELETE_ALL_FROM_INVOICE_ENTRIES_GIVEN_INVOICE_ID + id;
    jdbcTemplate.execute(queryDelete);
    saveAllEntriesFromInvoice((Invoice) item, id);
  }

  private void updateCompany(int id, ItemsForDatabase item, String query, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
    query += NAMED_PARAMETERS_FOR_COMPANY_UPDATE + id;
    namedParameterJdbcTemplate.update(query, getCompanyMapSqlParameterSource((Company) item));
  }
}