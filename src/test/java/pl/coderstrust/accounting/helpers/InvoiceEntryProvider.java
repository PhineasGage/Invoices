package pl.coderstrust.accounting.helpers;

import static pl.coderstrust.accounting.helpers.BigDecimalProvider.createBigDecimal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import pl.coderstrust.accounting.model.InvoiceEntry;
import pl.coderstrust.accounting.model.InvoiceEntry.InvoiceEntryBuilder;
import pl.coderstrust.accounting.model.VatRate;


public class InvoiceEntryProvider {

  public static final InvoiceEntry SPAN = new InvoiceEntryBuilder()
      .description("span")
      .netPrice(createBigDecimal(100))
      .vatRate(VatRate.NORMAL)
      .quantity(createBigDecimal(6))
      .category("car")
      .build();

  public static final InvoiceEntry CLAMP = new InvoiceEntryBuilder()
      .description("clamp")
      .netPrice(createBigDecimal(6))
      .vatRate(VatRate.NORMAL)
      .quantity(createBigDecimal(20))
      .category("noCar")
      .build();

  public static final InvoiceEntry SUPPORT = new InvoiceEntryBuilder()
      .description("support")
      .netPrice(createBigDecimal(11))
      .vatRate(VatRate.REDUCED_4)
      .quantity(createBigDecimal(6))
      .category("noCar")
      .build();

  public static final InvoiceEntry LINK = new InvoiceEntryBuilder()
      .description("link")
      .netPrice(createBigDecimal(13))
      .vatRate(VatRate.REDUCED_7)
      .quantity(createBigDecimal(6))
      .category("noCar")
      .build();

  public static final InvoiceEntry CAR_FUEL = new InvoiceEntry.InvoiceEntryBuilder()
      .description("car_fuel")
      .netPrice(createBigDecimal(13))
      .vatRate(VatRate.REDUCED_7)
      .quantity(createBigDecimal(10))
      .category("car")
      .build();

  public static final InvoiceEntry BLANK_DESCRIPTION = new InvoiceEntryBuilder()
      .description("")
      .netPrice(createBigDecimal(13))
      .vatRate(VatRate.ZERO)
      .quantity(createBigDecimal(6))
      .category("noCar")
      .build();

  public static final InvoiceEntry NULL_NET_PRICE = new InvoiceEntryBuilder()
      .description("link")
      .netPrice(null)
      .vatRate(VatRate.ZERO)
      .quantity(createBigDecimal(6))
      .category("noCar")
      .build();

  public static final InvoiceEntry NULL_VAT_RATE = new InvoiceEntryBuilder()
      .description("link")
      .netPrice(createBigDecimal(13))
      .vatRate(null)
      .quantity(createBigDecimal(6))
      .category("noCar")
      .build();

  public static final InvoiceEntry NULL_QUANTITY = new InvoiceEntryBuilder()
      .description("link")
      .netPrice(createBigDecimal(13))
      .vatRate(VatRate.ZERO)
      .quantity(null)
      .category("noCar")
      .build();

  public static final List<InvoiceEntry> SPAN_CLAMP = new ArrayList<>(Arrays.asList(SPAN, CLAMP));

  public static final List<InvoiceEntry> SPAN_CLAMP_SUPPORT = new ArrayList<>(Arrays.asList(SPAN, CLAMP, SUPPORT));

  public static final List<InvoiceEntry> EMPTY = new ArrayList<>(Arrays.asList());

  public static final List<InvoiceEntry> ONE_LINK = new ArrayList<>(Arrays.asList(LINK));

  public static final List<InvoiceEntry> ONE_CAR_FUEL = new ArrayList<>(Arrays.asList(CAR_FUEL));

  public static final List<InvoiceEntry> EMPTY_QUANTITY = new ArrayList<>(Arrays.asList(NULL_QUANTITY));

  public static final List<InvoiceEntry> EMPTY_DESCRIPTION = new ArrayList<>(Arrays.asList(BLANK_DESCRIPTION));

  public static final List<InvoiceEntry> EMPTY_NET_PRICE = new ArrayList<>(Arrays.asList(NULL_NET_PRICE));

  public static final List<InvoiceEntry> EMPTY_VAT_RATE = new ArrayList<>(Arrays.asList(NULL_VAT_RATE));
}