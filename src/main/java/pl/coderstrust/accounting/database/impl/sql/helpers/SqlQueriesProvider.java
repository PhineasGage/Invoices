package pl.coderstrust.accounting.database.impl.sql.helpers;

public class SqlQueriesProvider {

  public static final String SELECT_ALL_FROM = "Select * FROM ";

  public static final String SELECT_ALL_FROM_COMPANY_GIVEN_ID = "SELECT * FROM company WHERE company.id = ";

  public static final String SELECT_FROM_iNVOICE_ENTRY_GIVEN_iNVOICE_ID = "SELECT * FROM invoice_entries "
      + "INNER JOIN invoice_entry ON invoice_entries.entries_id = invoice_entry.id "
      + "WHERE invoice_entries.invoice_id = ";

  public static final String NAMED_PARAMETERS_FOR_INVOICE_UPDATE =
      " identifier = :identifier, "
      + "issue_date = :issue_date, "
      + "sale_date = :sale_date, "
      + "sale_place = :sale_place, "
      + "buyer_id = :buyer_id, "
      + "seller_id = :seller_id "
      + "WHERE Id = ";

  public static final String NAMED_PARAMETERS_FOR_COMPANY_UPDATE =
      " name = :name, "
      + "nip = :nip, "
      + "street = :street, "
      + "postal_code = :postal_code, "
      + "city = :city, "
      + "discount = :discount "
      + "WHERE id = ";

  public static final String INSERT_INTO_COMPANY = "INSERT INTO Company (name, nip, street, postal_code, city, discount) "
      + "VALUES (:name,:nip, :street, :postal_code, :city, :discount) RETURNING id;";

  public static final String INSERT_INTO_INVOICE_ENTRY = "INSERT INTO invoice_entry ( description, vat_rate, net_price, quantity) "
      + "VALUES ( :description,:vatRate, :netPrice, :quantity) RETURNING id;";

  public static final String INSERT_INTO_INVOICE_ENTRIES = "INSERT INTO invoice_entries ( invoice_id, entries_id) "
      + "VALUES ( :invoice_id,:entries_id)";

  public static final String INSERT_INTO_INVOICE = "INSERT INTO Invoice (identifier, issue_date, sale_date, sale_place, buyer_id, seller_id) "
      + "VALUES (:identifier,:issue_date, :sale_date, :sale_place, :buyer_id, :seller_id) RETURNING id";

  public static final String DELETE_ALL_FROM_INVOICE_ENTRIES_GIVEN_INVOICE_ID = "DELETE FROM invoice_entries WHERE invoice_id = ";

  public static final String DELETE_FROM_INVOICE_GIVEN_INVOICE_ID = " DELETE FROM invoice WHERE Id = ";

  public static final String DELETE_FROM_COMPANY_GIVEN_COMPANY_ID = " DELETE FROM company WHERE Id = ";

  public static final String DELETE_INVOICE = "DELETE FROM Invoice";

  public static final String DELETE_COMPANY = "DELETE FROM Company";

  public static final String DELETE_INVOICE_ENTRY = "DELETE FROM Invoice_Entry";

  public static final String DELETE_INVOICE_ENTRIES = "DELETE FROM Invoice_Entries";


}
