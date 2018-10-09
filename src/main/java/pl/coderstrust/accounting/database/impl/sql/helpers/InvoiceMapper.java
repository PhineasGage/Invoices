package pl.coderstrust.accounting.database.impl.sql.helpers;

import static pl.coderstrust.accounting.database.impl.sql.helpers.SqlQueriesProvider.SELECT_ALL_FROM_COMPANY_GIVEN_ID;
import static pl.coderstrust.accounting.database.impl.sql.helpers.SqlQueriesProvider.SELECT_FROM_iNVOICE_ENTRY_GIVEN_iNVOICE_ID;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import pl.coderstrust.accounting.model.Company;
import pl.coderstrust.accounting.model.Invoice;
import pl.coderstrust.accounting.model.InvoiceEntry;

public class InvoiceMapper implements RowMapper<Invoice> {

  JdbcTemplate jdbcTemplate;

  public InvoiceMapper(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  @Override
  public Invoice mapRow(ResultSet resultSet, int rowNumber) throws SQLException {
    int idBuyer = resultSet.getInt("buyer_id");
    int idSeller = resultSet.getInt("seller_id");
    String companyQuery = SELECT_ALL_FROM_COMPANY_GIVEN_ID;
    Company buyer = jdbcTemplate.query(companyQuery + idBuyer, new CompanyMapper<Company>(jdbcTemplate)).get(0);
    Company seller = jdbcTemplate.query(companyQuery + idSeller, new CompanyMapper<Company>(jdbcTemplate)).get(0);
    String invoiceEntryQuery = SELECT_FROM_iNVOICE_ENTRY_GIVEN_iNVOICE_ID + resultSet.getInt("Id");
    List<InvoiceEntry> entries = jdbcTemplate.query(invoiceEntryQuery, new InvoiceEntryMapper());
    return new Invoice.InvoiceBuilder()
        .id(resultSet.getInt("Id"))
        .identifier(resultSet.getString("Identifier"))
        .issueDate(resultSet.getDate("issue_date").toLocalDate())
        .saleDate(resultSet.getDate("sale_date").toLocalDate())
        .salePlace(resultSet.getString("sale_place"))
        .buyer(buyer)
        .seller(seller)
        .entries(entries)
        .build();
  }
}
