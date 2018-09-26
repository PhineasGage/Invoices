package pl.coderstrust.accounting.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.repository.CrudRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import pl.coderstrust.accounting.database.Database;
import pl.coderstrust.accounting.database.impl.file.InFileDatabase;
import pl.coderstrust.accounting.database.impl.file.helpers.FileHelper;
import pl.coderstrust.accounting.database.impl.file.helpers.IndexHelper;
import pl.coderstrust.accounting.database.impl.file.helpers.ItemConverter;
import pl.coderstrust.accounting.database.impl.hibernate.CompanyRepository;
import pl.coderstrust.accounting.database.impl.hibernate.HibernateDatabase;
import pl.coderstrust.accounting.database.impl.hibernate.InvoiceRepository;
import pl.coderstrust.accounting.database.impl.memory.InMemoryDatabase;
import pl.coderstrust.accounting.database.impl.sql.SqlDatabase;
import pl.coderstrust.accounting.model.Company;
import pl.coderstrust.accounting.model.Invoice;
import pl.coderstrust.accounting.model.ItemsForDatabase;

@Configuration
public class DatabaseProvider {

  @Bean
  public static Database<Company> provideCompanyDatabase(
      @Value("${databaseType}") String databaseType,
      @Value("${filePath}") String filePath,
      CompanyRepository companyRepository,
      JdbcTemplate jdbcTemplate) {
    String path = choosePathAccordingToClassObject(Company.class, filePath);
    return createDatabaseTypeAccordingToProperties(databaseType, Company.class, path, companyRepository, jdbcTemplate);
  }

  @Bean
  public static Database<Invoice> provideInvoiceDatabase(
      @Value("${databaseType}") String databaseType,
      @Value("${filePath}") String filePath,
      InvoiceRepository invoiceRepository,
      JdbcTemplate jdbcTemplate) {
    String path = choosePathAccordingToClassObject(Invoice.class, filePath);
    return createDatabaseTypeAccordingToProperties(databaseType, Invoice.class, path, invoiceRepository, jdbcTemplate);
  }

  private static <T extends ItemsForDatabase> Database<T> createDatabaseTypeAccordingToProperties(
      String databaseType,
      Class classObject,
      String path,
      CrudRepository crudRepository,
      JdbcTemplate jdbcTemplate) {
    switch (databaseType) {
      case "inMemoryDatabase":
        return new InMemoryDatabase<>();
      case "InFileDatabase":
        return new InFileDatabase<>(
            new FileHelper<>(classObject, path),
            new ItemConverter<>(JacksonProvider.getObjectMapper(), classObject),
            new IndexHelper<>(classObject, path));
      case "HibernateDatabase":
        return new HibernateDatabase<>(crudRepository);
      case "SqlDatabase":
        return new SqlDatabase<>(jdbcTemplate, classObject.getSimpleName());
      default:
        return new HibernateDatabase<>(crudRepository);
    }
  }

  private static String choosePathAccordingToClassObject(
      Class classObject,
      String filePath) {
    String className = classObject.getSimpleName();
    switch (className) {
      case "Invoice":
        return filePath + className;
      case "Company":
        return filePath + className;
      default:
        return "items.json";
    }
  }
}