package pl.coderstrust.accounting.controller;

import io.swagger.annotations.ApiOperation;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.coderstrust.accounting.model.Company;

@RequestMapping("/companies")
public interface CompanyApi {

  @ApiOperation(value = "Saves company",
      notes = "Returns ResponseEntity with id of saved company",
      response = ResponseEntity.class)
  @PostMapping
  ResponseEntity<?> saveCompany(@RequestBody Company company);

  @ApiOperation(value = "Gets all companies",
      notes = "Returns list of all saved companies",
      response = Company.class,
      responseContainer = "List")
  @GetMapping
  public List<Company> getCompanies();

  @ApiOperation(value = "Gets single company",
      notes = "Returns Company with ID specified",
      response = ResponseEntity.class)
  @GetMapping("/{id}")
  public ResponseEntity<Company> getSingleCompany(int id);

  @ApiOperation(value = "updates company",
      notes = "Replaces company with specified id with company  provided ",
      response = ResponseEntity.class)
  @PutMapping("/{id}")
  public ResponseEntity<?> updateCompany(int id, Company company);

  @ApiOperation(value = "removes company",
      notes = "Deletes company with specified id",
      response = ResponseEntity.class)
  @DeleteMapping("/{id}")
  public ResponseEntity<?> removeCompanyById(int id);
}
