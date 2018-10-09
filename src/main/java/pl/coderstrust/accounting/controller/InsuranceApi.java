package pl.coderstrust.accounting.controller;

import io.swagger.annotations.ApiOperation;
import java.time.LocalDate;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.coderstrust.accounting.model.Insurance;
import pl.coderstrust.accounting.model.InsuranceType;

@RequestMapping("/insurance")
public interface InsuranceApi {

  @ApiOperation(value = "Add Insurance to Company",
      notes = "Add Insurance to List of Insurances and update Company with it",
      response = ResponseEntity.class)
  @PostMapping("/add/{nip}")
  public ResponseEntity<?> addInsurance(String nip, @RequestBody Insurance insurance);

  @ApiOperation(value = "GetInsurance by id",
      notes = "Return insurance with specified id",
      response = ResponseEntity.class)
  @GetMapping("/{id}/company")
  public ResponseEntity<Insurance> getInsuranceById(int id);

  @ApiOperation(value = "Get insurances by Company",
      notes = "Returns all insurances that specified company paid",
      response = ResponseEntity.class)
  @GetMapping("/company/{nip}")
  public ResponseEntity<?> getInsurancesByCompany(String nip);

  @ApiOperation(value = "Get insurances by Type and Company",
      notes = "Returns all insurances by type that specified company paid",
      response = ResponseEntity.class)
  @GetMapping("/{nip}/type")
  public ResponseEntity<?> getInsurancesByTypeAndByCompany(String nip, InsuranceType insuranceType);

  @ApiOperation(value = "Get insurances by date and company",
      notes = "Returns all insurances by date that specified company paid",
      response = ResponseEntity.class)
  @GetMapping("/{nip}/dates")
  public List<Insurance> getInsurancesByDateAndByCompany(String nip, LocalDate startDate, LocalDate endDate);

  @ApiOperation(value = "Remove insurance by id",
      notes = "Remove insurance with specified id",
      response = ResponseEntity.class)
  @DeleteMapping("/remove/{id}")
  public ResponseEntity<?> removeInsuranceById(int id);
}
