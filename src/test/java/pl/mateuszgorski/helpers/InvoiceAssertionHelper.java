package pl.mateuszgorski.helpers;

import static org.hamcrest.Matchers.is;

import org.junit.Rule;
import org.junit.rules.ErrorCollector;
import pl.mateuszgorski.model.Invoice;

public class InvoiceAssertionHelper {

  @Rule
  public ErrorCollector collector = new ErrorCollector();

  public void assertInvoice(int returnedId, Invoice expected, Invoice actual) {
    collector.checkThat(actual.getId(), is(returnedId));
    collector.checkThat(actual.getIdentifier(), is(expected.getIdentifier()));
    collector.checkThat(actual.getSalePlace(), is(expected.getSalePlace()));
    collector.checkThat(actual.getBuyer().getName(), is(expected.getBuyer().getName()));
    collector.checkThat(actual.getBuyer().getNip(), is(expected.getBuyer().getNip()));
    collector.checkThat(actual.getBuyer().getStreet(), is(expected.getBuyer().getStreet()));
    collector.checkThat(actual.getBuyer().getPostalCode(), is(expected.getBuyer().getPostalCode()));
    collector.checkThat(actual.getBuyer().getDiscount().doubleValue(), is(expected.getBuyer().getDiscount().doubleValue()));
    collector.checkThat(actual.getSeller().getName(), is(expected.getSeller().getName()));
    collector.checkThat(actual.getSeller().getNip(), is(expected.getSeller().getNip()));
    collector.checkThat(actual.getSeller().getStreet(), is(expected.getSeller().getStreet()));
    collector.checkThat(actual.getSeller().getPostalCode(), is(expected.getSeller().getPostalCode()));
    collector.checkThat(actual.getSeller().getDiscount().doubleValue(), is(expected.getSeller().getDiscount().doubleValue()));
    checkEntries(expected, actual);
  }

  private void checkEntries(Invoice invoice, Invoice savedInvoice) {
    int size = savedInvoice.getEntries().size();
    for (int i = 0; i < size; i++) {
      collector.checkThat(savedInvoice.getEntries().get(i).getDescription(), is(invoice.getEntries().get(i).getDescription()));
      collector.checkThat(savedInvoice.getEntries().get(i).getNetPrice().intValue(), is(invoice.getEntries().get(i).getNetPrice().intValue()));
      collector.checkThat(savedInvoice.getEntries().get(i).getVatRate().toString(), is(invoice.getEntries().get(i).getVatRate().toString()));
      collector.checkThat(savedInvoice.getEntries().get(i).getQuantity().intValue(), is(invoice.getEntries().get(i).getQuantity().intValue()));
    }
  }
}
