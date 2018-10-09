package pl.coderstrust.accounting.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.repository.CrudRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import pl.coderstrust.accounting.database.Database;
import pl.coderstrust.accounting.database.impl.file.InFileCompanyDatabase;
import pl.coderstrust.accounting.database.impl.file.InFileDatabase;
import pl.coderstrust.accounting.database.impl.file.InFileInsuranceDatabase;
import pl.coderstrust.accounting.database.impl.file.InFileInvoiceDatabase;
import pl.coderstrust.accounting.database.impl.file.helpers.FileHelper;
import pl.coderstrust.accounting.database.impl.file.helpers.IndexHelper;
import pl.coderstrust.accounting.database.impl.file.helpers.ItemConverter;
import pl.coderstrust.accounting.database.impl.hibernate.CompanyRepository;
import pl.coderstrust.accounting.database.impl.hibernate.HibernateDatabase;
import pl.coderstrust.accounting.database.impl.hibernate.InsuranceRepository;
import pl.coderstrust.accounting.database.impl.hibernate.InvoiceRepository;
import pl.coderstrust.accounting.database.impl.memory.InMemoryDatabase;
import pl.coderstrust.accounting.database.impl.sql.SqlDatabase;
import pl.coderstrust.accounting.database.impl.sql.SqlDatabaseCompany;
import pl.coderstrust.accounting.database.impl.sql.SqlDatabaseInsurance;
import pl.coderstrust.accounting.database.impl.sql.SqlDatabaseInvoice;
import pl.coderstrust.accounting.model.Company;
import pl.coderstrust.accounting.model.Insurance;
import pl.coderstrust.accounting.model.Invoice;
import pl.coderstrust.accounting.model.ItemsForDatabase;

@Configuration
public class DatabaseProvider {

  @Bean
  public static Database<Company> provideCompanyDatabase(
      @Value("${databaseType}") String databaseType,
      @Value("${filePath}") String filepath,
      CompanyRepository companyRepository,
      JdbcTemplate jdbcTemplate) {
    return createDatabaseTypeAccordingToProperties(databaseType, Company.class, filepath, companyRepository, jdbcTemplate);
  }

  @Bean
  public static Database<Invoice> provideInvoiceDatabase(
      @Value("${databaseType}") String databaseType,
      @Value("${filePath}") String filepath,
      InvoiceRepository invoiceRepository,
      JdbcTemplate jdbcTemplate) {
    return createDatabaseTypeAccordingToProperties(databaseType, Invoice.class, filepath, invoiceRepository, jdbcTemplate);
  }

  @Bean
  public static Database<Insurance> provideInsuranceDatabase(
      @Value("${databaseType}") String databaseType,
      @Value("${filePath}") String filepath,
      InsuranceRepository insuranceRepository,
      JdbcTemplate jdbcTemplate) {
    return createDatabaseTypeAccordingToProperties(databaseType, Insurance.class, filepath, insuranceRepository, jdbcTemplate);
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
        return chooseCorrectInFileDatabase(classObject, path);
      case "HibernateDatabase":
        return new HibernateDatabase<>(crudRepository);
      case "SqlDatabase":
        return chooseCorrectSqlDatabase(jdbcTemplate, classObject.getSimpleName());
      default:
        return new HibernateDatabase<>(crudRepository);
    }
  }

  private static InFileDatabase chooseCorrectInFileDatabase(Class classObject, String filepath) {
    String path = choosePathAccordingToClassObject(classObject, filepath);
    FileHelper fileHelper = new FileHelper<>(classObject, path);
    ItemConverter itemConverter = new ItemConverter<>(JacksonProvider.getObjectMapper(), classObject);
    IndexHelper indexHelper = new IndexHelper<>(classObject, path);
    switch (classObject.getSimpleName()) {
      case "Invoice":
        return new InFileInvoiceDatabase(
            fileHelper,
            itemConverter,
            indexHelper,
            (InFileCompanyDatabase) chooseCorrectInFileDatabase(Company.class, filepath));
      case "Company":
        return new InFileCompanyDatabase(fileHelper, itemConverter, indexHelper);
      case "Insurance":
        return new InFileInsuranceDatabase(fileHelper, itemConverter, indexHelper);
      default:
        throw new RuntimeException("Cannot create InFileDatabase");
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
      case "Insurance":
        return filePath + className;
      default:
        return "items.json";
    }
  }

  private static SqlDatabase chooseCorrectSqlDatabase(JdbcTemplate jdbcTemplate, String classObjectname) {
    switch (classObjectname) {
      case "Invoice":
        return new SqlDatabaseInvoice(jdbcTemplate, classObjectname);
      case "Company":
        return new SqlDatabaseCompany(jdbcTemplate, classObjectname);
      case "Insurance":
        return new SqlDatabaseInsurance(jdbcTemplate, classObjectname);
      default:
        throw new RuntimeException("Cannot create SqlDatabase");
    }
  }
}