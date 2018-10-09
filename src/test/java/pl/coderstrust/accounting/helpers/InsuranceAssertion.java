package pl.coderstrust.accounting.helpers;

import static org.hamcrest.Matchers.is;

import org.junit.Rule;
import org.junit.rules.ErrorCollector;
import pl.coderstrust.accounting.model.Insurance;

public class InsuranceAssertion {

  @Rule
  public ErrorCollector collector = new ErrorCollector();

  public void assertInsurance(int returnedId, Insurance expected, Insurance actual) {
    collector.checkThat(actual.getId(), is(returnedId));
    collector.checkThat(actual.getIssueDate(), is(expected.getIssueDate()));
    collector.checkThat(actual.getType(), is(expected.getType()));
    collector.checkThat(actual.getAmount(), is(expected.getAmount()));
    collector.checkThat(actual.getNip(), is(expected.getNip()));
  }
}
