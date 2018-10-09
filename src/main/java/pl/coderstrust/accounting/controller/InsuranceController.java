package pl.coderstrust.accounting.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.coderstrust.accounting.logic.InsuranceService;
import pl.coderstrust.accounting.model.Insurance;
import pl.coderstrust.accounting.model.InsuranceType;

@RestController
public class InsuranceController implements InsuranceApi {

  private InsuranceService insuranceService;

  @Autowired
  public InsuranceController(InsuranceService insuranceService) {
    this.insuranceService = insuranceService;
  }

  @Override
  public ResponseEntity<?> addInsurance(@PathVariable(name = "nip", required = true) String nip, @RequestBody Insurance insurance) {
    return ResponseEntity.ok(insuranceService.saveInsurance(nip, insurance));
  }

  @Override
  public ResponseEntity<Insurance> getInsuranceById(@PathVariable(name = "id", required = true) int id) {
    Optional<Insurance> insuranceById = insuranceService.getInsuranceById(id);
    return insuranceById
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @Override
  public ResponseEntity<?> getInsurancesByCompany(@PathVariable(name = "nip", required = true) String nip) {
    if (insuranceService.getAllInsurancesByCompany(nip).isEmpty()) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(insuranceService.getAllInsurancesByCompany(nip));
  }

  @Override
  public ResponseEntity<?> getInsurancesByTypeAndByCompany(
      @PathVariable(name = "nip", required = true) String nip,
      @RequestParam(value = "insuranceType", required = true) InsuranceType insuranceType) {
    if (insuranceService.getInsurancesByTypeAndCompany(insuranceType, nip).isEmpty()) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(insuranceService.getInsurancesByTypeAndCompany(insuranceType, nip));
  }

  @Override
  public List<Insurance> getInsurancesByDateAndByCompany(
      @PathVariable(name = "nip", required = true) String nip,
      @RequestParam(name = "startDate", required = true)
      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
      @RequestParam(name = "endDate", required = true)
      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
    return insuranceService.getInsurancesByIssueDateAndByCompany(startDate, endDate, nip);
  }

  @Override
  public ResponseEntity<?> removeInsuranceById(@PathVariable(name = "id", required = true) int id) {
    insuranceService.removeInsuranceById(id);
    return ResponseEntity.ok().build();
  }
}
