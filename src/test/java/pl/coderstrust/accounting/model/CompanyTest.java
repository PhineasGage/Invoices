package pl.coderstrust.accounting.model;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static pl.coderstrust.accounting.helpers.CompanyProvider.COMPANY_DRUTEX;
import static pl.coderstrust.accounting.helpers.InsuranceProvider.HEALTH_INSURANCE;
import static pl.coderstrust.accounting.helpers.InsuranceProvider.HEALTH_INSURANCE2;
import static pl.coderstrust.accounting.helpers.InsuranceProvider.PENSION_INSURANCE;
import static pl.pojo.tester.api.assertion.Assertions.assertPojoMethodsFor;

import java.util.List;
import org.junit.Test;
import pl.coderstrust.accounting.model.Company.CompanyBuilder;
import pl.pojo.tester.api.assertion.Method;

public class CompanyTest {

  @Test
  public void invoiceEntryShouldPassAllPojoTestsForGettersAndSetters() {
    // given
    final Class<?> classUnderTest = InvoiceEntry.class;

    // when

    // then
    assertPojoMethodsFor(classUnderTest).testing(Method.GETTER, Method.SETTER).areWellImplemented();
  }

  @Test
  public void insuranceEntryShouldPassAllPojoTestsForGettersAndSetters() {
    // given
    final Class<?> classUnderTest = Insurance.class;

    // when

    // then
    assertPojoMethodsFor(classUnderTest).testing(Method.GETTER, Method.SETTER).areWellImplemented();
  }

  @Test
  public void shouldPassAllPojoTestsForGettersAndSetters() {
    // given
    Class<?> classUnderTest = Company.class;

    // when

    // then
    assertPojoMethodsFor(classUnderTest)
        .testing(Method.GETTER, Method.SETTER)
        .testing(Method.EQUALS)
        .testing(Method.HASH_CODE)
        .areWellImplemented();
  }
}

