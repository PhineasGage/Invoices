package pl.mateuszgorski.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

@RunWith(JUnitParamsRunner.class)
public class NipValidatorTest {

  private NipValidator nipValidator = new NipValidator();

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Test
  @Parameters(method = "incorrectNip")
  public void shouldRejectIncorrectNip(String nip) {
    assertThat(nipValidator.isValid(nip), is(false));
  }

  public Object[] incorrectNip() {
    return new String[]{
        "1234567890",
        "",
        "31783123k",
        "   ",
        "@#$%F",
        "000000000",
        null
    };
  }

  @Test
  @Parameters(method = "correctNip")
  public void shouldAcceptCorrectNip(String nip) {
    assertThat(nipValidator.isValid(nip), is(true));
  }

  public Object[] correctNip() {
    return new String[]{
        "6780038167",
        "8722410183",
        "5240107036",
        "6770065406   ",
    };
  }
}