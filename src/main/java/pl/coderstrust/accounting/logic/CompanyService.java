package pl.coderstrust.accounting.logic;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import pl.coderstrust.accounting.database.Database;
import pl.coderstrust.accounting.model.Company;

@Service
public class CompanyService {

  private Database<Company> companyDatabase;

  public CompanyService(Database<Company> companyDatabase) {
    this.companyDatabase = companyDatabase;
  }

  public int saveCompany(Company company) {
    if (company == null) {
      throw new IllegalArgumentException("Company cannot be null");
    }
    Optional<Company> optionalCompany = companyDatabase
        .getItems()
        .stream()
        .filter(n -> n.getNip().equals(company.getNip()))
        .findFirst();
    if (!optionalCompany.isPresent()) {
      return companyDatabase.saveItem(company);
    } else {
      return optionalCompany.get().getId();
    }
  }

  public List<Company> getCompanies() {
    return companyDatabase.getItems();
  }

  public Optional<Company> getCompanyById(int id) {
    return companyDatabase.getItems()
        .stream()
        .filter(company -> company.getId() == id)
        .findAny();
  }

  public void updateCompany(int id, Company company) {
    Optional<Company> companyFromDatabase = getCompanyById(id);

    if (!companyFromDatabase.isPresent()) {
      throw new IllegalStateException("Company with id: " + id + " does not exist");
    }

    companyDatabase.updateItemById(id, company);
  }

  public void removeCompanyById(int id) {
    Optional<Company> companyFromDatabase = getCompanyById(id);

    if (!companyFromDatabase.isPresent()) {
      throw new IllegalStateException("Company with id: " + id + " does not exist");
    }

    companyDatabase.removeItemById(id);
  }

  public void clearDatabase() {
    companyDatabase.clearDatabase();
  }
}
