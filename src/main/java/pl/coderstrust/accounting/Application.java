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
import pl.coderstrust.accounting.security.Account;
import pl.coderstrust.accounting.security.AccountRepository;

@SpringBootApplication
public class Application {

  private static final Logger log = LoggerFactory.getLogger(Application.class);

  public static void main(String[] args) {
    SpringApplication.run(Application.class);
  }

  @Bean
  public CommandLineRunner demo(InvoiceRepository invoiceRepository,
                                CompanyRepository companyRepository,
                                final AccountRepository accountRepository) {
    invoiceRepository.deleteAll();
    companyRepository.deleteAll();
    accountRepository.deleteAll();
    accountRepository.save(new Account("Kamil", "kamil"));
    accountRepository.save(new Account("Mat", "mat"));
    accountRepository.save(new Account("Mietek", "mietek"));
    accountRepository.save(new Account("Piotr", "piotr"));
    return (args) -> {
    };
  }
}