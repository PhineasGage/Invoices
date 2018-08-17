package pl.mateuszgorski.model;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static pl.mateuszgorski.helpers.InvoiceEntryProvider.CLAMP;
import static pl.mateuszgorski.helpers.InvoiceEntryProvider.SPAN;
import static pl.mateuszgorski.helpers.InvoiceEntryProvider.SUPPORT;
import static pl.pojo.tester.api.assertion.Assertions.assertPojoMethodsFor;

import java.math.BigDecimal;
import java.util.List;
import org.junit.Test;
import pl.mateuszgorski.helpers.InvoiceProvider;
import pl.pojo.tester.api.assertion.Method;

public class InvoiceTest {

  @Test
  public void shouldCalculateNetValue() {
    //when
    BigDecimal actual = InvoiceProvider.INVOICE_GRUDZIADZ_2017.getTotalNetValue();
    BigDecimal expected = BigDecimal.valueOf(50.4);

    //then
    assertThat(actual, is(expected));
  }

  @Test
  public void returnsListOfEntries() {

    //when
    List<InvoiceEntry> actual = InvoiceProvider.INVOICE_CHELMNO_2016.getEntries();

    //then
    assertThat(actual.size(), is(3));
    assertThat(actual, hasItem(SPAN));
    assertThat(actual, hasItem(CLAMP));
    assertThat(actual, hasItem(SUPPORT));
  }

  @Test
  public void addEntryToList() {
    //given
    Invoice invoice = new Invoice();

    //when
    invoice.addInvoiceEntry(CLAMP);

    //then
    assertThat(invoice.getEntries(), hasItem(CLAMP));
  }

  @Test
  public void invoiceEntryShouldPassAllPojoTestsForGettersAndSetters() {
    // given
    final Class<?> classUnderTest = InvoiceEntry.class;

    // when

    // then
    assertPojoMethodsFor(classUnderTest).testing(Method.GETTER, Method.SETTER).areWellImplemented();
  }

  @Test
  public void invoiceShouldPassAllPojoTestsForGettersAndSetters() {
    // given
    final Class<?> classUnderTest = Invoice.class;

    // when

    // then
    assertPojoMethodsFor(classUnderTest)
        .testing(Method.GETTER, Method.SETTER)
        .areWellImplemented();
  }
}