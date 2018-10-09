package pl.coderstrust.accounting.controller;

import java.util.List;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.coderstrust.accounting.logic.CompanyService;
import pl.coderstrust.accounting.model.Company;

@RestController
public class CompanyController implements CompanyApi {

  private CompanyService companyService;
  private CompanyValidator companyValidator;

  public CompanyController(CompanyService companyService, CompanyValidator companyValidator) {
    this.companyService = companyService;
    this.companyValidator = companyValidator;
  }

  @Override
  public ResponseEntity<?> saveCompany(@RequestBody Company company) {
    List<String> validationResult = companyValidator.validate(company, "");
    if (!validationResult.isEmpty()) {
      return ResponseEntity.badRequest().body(validationResult);
    }
    int id = companyService.saveCompany(company);
    return ResponseEntity.ok(id);
  }

  @Override
  public List<Company> getCompanies() {
    return companyService.getCompanies();
  }

  @Override
  public ResponseEntity<Company> getSingleCompany(@PathVariable(name = "id", required = true) int id) {
    Optional<Company> companyById = companyService.getCompanyById(id);
    return companyById
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @Override
  public ResponseEntity<?> updateCompany(@PathVariable(name = "id", required = true) int id, @RequestBody Company company) {
    if (!companyIsInDatabase(id)) {
      return ResponseEntity.notFound().build();
    }
    List<String> validationResult = companyValidator.validate(company, "");
    if (!validationResult.isEmpty()) {
      return ResponseEntity.badRequest().body(validationResult);
    }
    companyService.updateCompany(id, company);
    return ResponseEntity.ok().build();
  }

  @Override
  public ResponseEntity<?> removeCompanyById(@PathVariable(name = "id", required = true) int id) {
    if (!companyIsInDatabase(id)) {
      return ResponseEntity.notFound().build();
    }
    companyService.removeCompanyById(id);
    return ResponseEntity.ok().build();
  }

  private boolean companyIsInDatabase(int id) {
    return companyService.getCompanyById(id).isPresent();
  }
}