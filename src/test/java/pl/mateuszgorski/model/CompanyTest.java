package pl.coderstrust.accounting.model;

import static pl.pojo.tester.api.assertion.Assertions.assertPojoMethodsFor;

import org.junit.Test;
import pl.pojo.tester.api.assertion.Method;

public class CompanyTest {

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