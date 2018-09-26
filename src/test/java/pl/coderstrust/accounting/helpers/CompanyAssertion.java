package pl.coderstrust.accounting.helpers;

import static org.hamcrest.Matchers.is;

import org.junit.Rule;
import org.junit.rules.ErrorCollector;
import pl.coderstrust.accounting.model.Company;

public class CompanyAssertion {

  @Rule
  public ErrorCollector collector = new ErrorCollector();

  public void assertCompany(int returnedId, Company expected, Company actual) {
    collector.checkThat(actual.getId(), is(returnedId));
    collector.checkThat(actual.getName(), is(expected.getName()));
    collector.checkThat(actual.getNip(), is(expected.getNip()));
    collector.checkThat(actual.getStreet(), is(expected.getStreet()));
    collector.checkThat(actual.getPostalCode(), is(expected.getPostalCode()));
    collector.checkThat(actual.getCity(), is(expected.getCity()));
    collector.checkThat(actual.getDiscount().doubleValue(), is(expected.getDiscount().doubleValue()));
  }
}
