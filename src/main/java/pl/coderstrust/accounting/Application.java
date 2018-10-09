package pl.coderstrust.accounting;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import pl.coderstrust.accounting.database.impl.hibernate.CompanyRepository;
import pl.coderstrust.accounting.database.impl.hibernate.InsuranceRepository;
import pl.coderstrust.accounting.database.impl.hibernate.InvoiceRepository;

@SpringBootApplication
public class Application {

  private static final Logger log = LoggerFactory.getLogger(Application.class);

  public static void main(String[] args) {
    SpringApplication.run(Application.class);
  }

  @Bean
  public CommandLineRunner demo(InvoiceRepository invoiceRepository, CompanyRepository companyRepository, InsuranceRepository insuranceRepository) {
    return (args) -> {
      invoiceRepository.deleteAll();
      companyRepository.deleteAll();
      insuranceRepository.deleteAll();
    };
  }
}