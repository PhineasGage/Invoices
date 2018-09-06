package pl.coderstrust.accounting.controller;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import org.springframework.stereotype.Service;

@Service
public class NipValidator {

  private static final int[] CHECKS_ARRAY = {6, 5, 7, 2, 3, 4, 5, 6, 7};
  private static final Pattern PATTERN = Pattern.compile("[1-9]\\d{9}");

  public boolean isValid(String nip) {
    if (nip == null) {
      return false;
    }
    Matcher matcher = PATTERN.matcher(nip);
    return matcher.matches() && controlSumIsCorrect(nip);
  }

  private boolean controlSumIsCorrect(String nip) {
    int[] nipAsArray = Stream.of(nip.split("")).mapToInt(Integer::parseInt).toArray();

    int checksumExpected = 0;
    for (int i = 0; i < 9; i++) {
      checksumExpected += CHECKS_ARRAY[i] * nipAsArray[i];
    }

    return checksumExpected % 11 == nipAsArray[9];
  }
}
