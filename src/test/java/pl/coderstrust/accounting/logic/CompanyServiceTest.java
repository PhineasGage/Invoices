package pl.coderstrust.accounting.logic;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static pl.coderstrust.accounting.helpers.CompanyProvider.COMPANY_DRUKPOL;
import static pl.coderstrust.accounting.helpers.CompanyProvider.COMPANY_DRUTEX;
import static pl.coderstrust.accounting.helpers.CompanyProvider.COMPANY_TRANSPOL;
import static pl.coderstrust.accounting.helpers.CompanyProvider.COMPANY_WASBUD;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import pl.coderstrust.accounting.database.Database;
import pl.coderstrust.accounting.model.Company;

@RunWith(MockitoJUnitRunner.class)
public class CompanyServiceTest {

  @Rule
  public ExpectedException expectedEx = ExpectedException.none();

  @Mock
  private Database databaseMock;

  @InjectMocks
  private CompanyService companyService;

  @Test
  public void shouldSaveCompany() {
    //given
    when(databaseMock.saveItem(COMPANY_DRUTEX)).thenReturn(COMPANY_DRUTEX.getId());

    //when
    int id = companyService.saveCompany(COMPANY_DRUTEX);

    //then
    assertThat(id, is(equalTo(COMPANY_DRUTEX.getId())));
    verify(databaseMock).saveItem(COMPANY_DRUTEX);
  }

  @Test
  public void shouldReturnListOfCompanies() {
    //given
    List<Company> companies = new ArrayList<>();
    companies.add(COMPANY_DRUTEX);
    companies.add(COMPANY_WASBUD);
    companies.add(COMPANY_DRUKPOL);

    when(databaseMock.getItems()).thenReturn(companies);

    //when
    List<Company> actual = companyService.getCompanies();

    //then
    verify(databaseMock).getItems();
    assertThat(actual, is(companies));
    assertThat(actual, hasSize(3));
    assertThat(actual, hasItem(COMPANY_DRUTEX));
    assertThat(actual, hasItem(COMPANY_WASBUD));
    assertThat(actual, hasItem(COMPANY_DRUKPOL));
  }

  @Test
  public void getCompanyById() {
    //given
    List<Company> companies = new ArrayList<>();
    companies.add(COMPANY_DRUTEX);
    companies.add(COMPANY_WASBUD);
    companies.add(COMPANY_DRUKPOL);

    COMPANY_DRUKPOL.setId(3); //todo why in invoiceServiceTest it is working without this ?

    int id = COMPANY_DRUKPOL.getId();
    System.out.println(id);

    when(databaseMock.getItems()).thenReturn(companies);

    //when
    Optional<Company> actual = companyService.getCompanyById(id);
    Company expected = COMPANY_DRUKPOL; // todo something wrong with id still 0

    //then
    assertTrue(actual.isPresent());
    assertThat(actual.get(), is(expected));
  }

  @Test
  public void updateCompany() {
    //given
    List<Company> companies = new ArrayList<>();
    companies.add(COMPANY_DRUTEX);
    companies.add(COMPANY_DRUKPOL);

    int id = COMPANY_DRUKPOL.getId();
    when(databaseMock.getItems()).thenReturn(companies);

    //when
    companyService.updateCompany(id, COMPANY_TRANSPOL);

    //then
    verify(databaseMock).updateItemById(id, COMPANY_TRANSPOL);
  }

  @Test
  public void removeCompanyById() {
    //given
    List<Company> companies = new ArrayList<>();
    companies.add(COMPANY_DRUTEX);
    companies.add(COMPANY_DRUKPOL);

    int id = COMPANY_DRUKPOL.getId();
    when(databaseMock.getItems()).thenReturn(companies);

    //when
    companyService.removeCompanyById(id);

    //then
    verify(databaseMock).removeItemById(id);
  }

  @Test
  public void shouldThrowExceptionCausedByMissingCompanyWithProvidedIdForUpdate() {
    //given
    int id = 0;
    expectedEx.expect(IllegalStateException.class);
    expectedEx.expectMessage("Company with id: " + id + " does not exist");

    //when
    companyService.updateCompany(0, COMPANY_WASBUD);
  }

  @Test
  public void shouldThrowExceptionCausedByMissingCompanyWithProvidedIdToRemove() {
    //given
    int id = 0;
    expectedEx.expect(IllegalStateException.class);
    expectedEx.expectMessage("Company with id: " + id + " does not exist");

    //when
    companyService.removeCompanyById(0);
  }

  @Test
  public void shouldThrowExceptionCausedByNullCompany() {
    //given
    Company company = null;
    expectedEx.expect(IllegalArgumentException.class);
    expectedEx.expectMessage("Company cannot be null");

    //when
    companyService.saveCompany(company);
  }
}