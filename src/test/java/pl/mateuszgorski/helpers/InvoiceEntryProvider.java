package pl.coderstrust.accounting.helpers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import pl.coderstrust.accounting.model.InvoiceEntry;


public class InvoiceEntryProvider {

  public static final InvoiceEntry SPAN = new InvoiceEntry.InvoiceEntryBuilder()
      .description("span")
      .netPrice(BigDecimal.valueOf(10))
      .vatRate(BigDecimal.valueOf(0.23))
      .quantity(BigDecimal.valueOf(6))
      .build();


  public static final InvoiceEntry CLAMP = new InvoiceEntry.InvoiceEntryBuilder()
      .description("clamp")
      .netPrice(BigDecimal.valueOf(6))
      .vatRate(BigDecimal.valueOf(0.08))
      .quantity(BigDecimal.valueOf(2))
      .build();


  public static final InvoiceEntry SUPPORT = new InvoiceEntry.InvoiceEntryBuilder()
      .description("support")
      .netPrice(BigDecimal.valueOf(11))
      .vatRate(BigDecimal.valueOf(0.04))
      .quantity(BigDecimal.valueOf(6))
      .build();


  public static final InvoiceEntry LINK = new InvoiceEntry.InvoiceEntryBuilder()
      .description("link")
      .netPrice(BigDecimal.valueOf(13))
      .vatRate(BigDecimal.valueOf(0.07))
      .quantity(BigDecimal.valueOf(6))
      .build();


  public static final InvoiceEntry BLANK_DESCRIPTION = new InvoiceEntry.InvoiceEntryBuilder()
      .description("")
      .netPrice(BigDecimal.valueOf(13))
      .vatRate(BigDecimal.valueOf(0.23))
      .quantity(BigDecimal.valueOf(6))
      .build();


  public static final InvoiceEntry NULL_NET_PRICE = new InvoiceEntry.InvoiceEntryBuilder()
      .description("1/2018")
      .netPrice(null)
      .vatRate(BigDecimal.valueOf(0.23))
      .quantity(BigDecimal.valueOf(6))
      .build();

  public static final InvoiceEntry NULL_VAT_RATE = new InvoiceEntry.InvoiceEntryBuilder()
      .description("1/2018")
      .netPrice(BigDecimal.valueOf(13))
      .vatRate(null)
      .quantity(BigDecimal.valueOf(6))
      .build();

  public static final InvoiceEntry NULL_QUANTITY = new InvoiceEntry.InvoiceEntryBuilder()
      .description("1/2018")
      .netPrice(BigDecimal.valueOf(13))
      .vatRate(BigDecimal.valueOf(0.23))
      .quantity(null)
      .build();

  public static final InvoiceEntry INCORRECT_VAT = new InvoiceEntry.InvoiceEntryBuilder()
      .description("1/2018")
      .netPrice(BigDecimal.valueOf(13))
      .vatRate(BigDecimal.valueOf(0.24))
      .quantity(BigDecimal.valueOf(6))
      .build();

  public static final List<InvoiceEntry> SPAN_CLAMP = new ArrayList<>(Arrays.asList(SPAN, CLAMP));

  public static final List<InvoiceEntry> SPAN_CLAMP_SUPPORT = new ArrayList<>(Arrays.asList(SPAN, CLAMP, SUPPORT));

  public static final List<InvoiceEntry> EMPTY = new ArrayList<>(Arrays.asList());

  public static final List<InvoiceEntry> ONE_LINK = new ArrayList<>(Arrays.asList(LINK));

  public static final List<InvoiceEntry> EMPTY_QUANTITY = new ArrayList<>(Arrays.asList(NULL_QUANTITY));

  public static final List<InvoiceEntry> EMPTY_DESCRIPTION = new ArrayList<>(Arrays.asList(BLANK_DESCRIPTION));

  public static final List<InvoiceEntry> EMPTY_NET_PRICE = new ArrayList<>(Arrays.asList(NULL_NET_PRICE));

  public static final List<InvoiceEntry> EMPTY_VAT_RATE = new ArrayList<>(Arrays.asList(NULL_VAT_RATE));

  public static final List<InvoiceEntry> INCORRECT_VAT_RATE = new ArrayList<>(Arrays.asList(INCORRECT_VAT));
}