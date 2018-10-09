package pl.coderstrust.accounting.logic;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.coderstrust.accounting.database.Database;
import pl.coderstrust.accounting.model.Company;
import pl.coderstrust.accounting.model.Insurance;
import pl.coderstrust.accounting.model.InsuranceType;

@Service
public class InsuranceService {

  private Database<Insurance> insuranceDatabase;

  @Autowired
  public InsuranceService(Database<Insurance> insuranceDatabase) {
    this.insuranceDatabase = insuranceDatabase;
  }

  public int saveInsurance(String nip, Insurance insurance) {
    Insurance insuranceCopy = insurance.makeDeepCopy(insurance);
    insuranceCopy.setNip(nip);
    return insuranceDatabase.saveItem(insuranceCopy);
  }

  public Optional<Insurance> getInsuranceById(int id) {
    return insuranceDatabase.getItems()
        .stream()
        .filter(insurance -> insurance.getId() == id)
        .findAny();
  }

  public List<Insurance> getAllInsurancesByCompany(String nip) {
    return insuranceDatabase.getItems()
        .stream()
        .filter(n -> n.getNip().equals(nip))
        .collect(Collectors.toList());
  }

  public List<Insurance> getInsurancesByIssueDateAndByCompany(LocalDate startDate, LocalDate endDate, String nip) {
    return getAllInsurancesByCompany(nip)
        .stream()
        .filter(n -> n.getIssueDate().isAfter(startDate))
        .filter(n -> n.getIssueDate().isBefore(endDate))
        .collect(Collectors.toList());
  }

  public List<Insurance> getInsurancesByTypeAndCompany(InsuranceType type, String nip) {
    return getAllInsurancesByCompany(nip)
        .stream()
        .filter(insurance -> insurance.getType().equals(type))
        .collect(Collectors.toList());
  }

  public void removeInsuranceById(int id) {
    insuranceDatabase.removeItemById(id);
  }

  public void clearDatabase() {
    insuranceDatabase.clearDatabase();
  }
}