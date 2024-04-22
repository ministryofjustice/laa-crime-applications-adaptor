package uk.gov.justice.laa.crime.applications.adaptor.mapper.crimeapply;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.gov.justice.laa.crime.applications.adaptor.testutils.TestData;
import uk.gov.justice.laa.crime.model.common.crimeapplicationsadaptor.common.Passported;
import uk.gov.justice.laa.crime.model.common.criminalapplicationsdatastore.Applicant;
import uk.gov.justice.laa.crime.model.common.criminalapplicationsdatastore.MaatApplicationExternal;

class PassportedMapperTest {

  private PassportedMapper passportedMapper;

  @BeforeEach
  void setUp() {
    passportedMapper = new PassportedMapper();
  }

  @Test
  void map() {
    MaatApplicationExternal crimeApplyMaatApplicationExternal = TestData.getMaatApplication();

    Passported actualPassported = passportedMapper.map(crimeApplyMaatApplicationExternal);

    assertFalse(actualPassported.getBenefitUniversalCredit());
    assertFalse(actualPassported.getBenefitIncomeSupport());
    assertFalse(actualPassported.getBenefitGuaranteedStatePension());
    assertFalse(actualPassported.getBenefitJobSeeker());
    assertFalse(actualPassported.getBenefitEmploymentSupport());
    assertFalse(actualPassported.getBenefitClaimedByPartner());
    assertNull(actualPassported.getWhoDwpChecked());
  }

  @Test
  void shouldMap_Universal_Credit_BenefitType() {
    MaatApplicationExternal crimeApplyMaatApplicationExternal = TestData.getMaatApplication();
    crimeApplyMaatApplicationExternal
        .getClientDetails()
        .getApplicant()
        .setBenefitType(Applicant.BenefitType.UNIVERSAL_CREDIT);
    Passported actualPassported = passportedMapper.map(crimeApplyMaatApplicationExternal);
    assertTrue(actualPassported.getBenefitUniversalCredit());
  }

  @Test
  void shouldMap_Guarantee_Pension_BenefitType() {
    MaatApplicationExternal crimeApplyMaatApplicationExternal = TestData.getMaatApplication();
    crimeApplyMaatApplicationExternal
        .getClientDetails()
        .getApplicant()
        .setBenefitType(Applicant.BenefitType.GUARANTEE_PENSION);
    Passported actualPassported = passportedMapper.map(crimeApplyMaatApplicationExternal);
    assertTrue(actualPassported.getBenefitGuaranteedStatePension());
  }

  @Test
  void shouldMap_Job_Seeker_BenefitType() {
    MaatApplicationExternal crimeApplyMaatApplicationExternal = TestData.getMaatApplication();
    crimeApplyMaatApplicationExternal
        .getClientDetails()
        .getApplicant()
        .setBenefitType(Applicant.BenefitType.JSA);
    Passported actualPassported = passportedMapper.map(crimeApplyMaatApplicationExternal);
    assertTrue(actualPassported.getBenefitJobSeeker());
  }

  @Test
  void shouldMap_Employment_Support_BenefitType() {
    MaatApplicationExternal crimeApplyMaatApplicationExternal = TestData.getMaatApplication();
    crimeApplyMaatApplicationExternal
        .getClientDetails()
        .getApplicant()
        .setBenefitType(Applicant.BenefitType.ESA);
    Passported actualPassported = passportedMapper.map(crimeApplyMaatApplicationExternal);
    assertTrue(actualPassported.getBenefitEmploymentSupport());
  }

  @Test
  void shouldMap_Income_Support_BenefitType() {
    MaatApplicationExternal crimeApplyMaatApplicationExternal = TestData.getMaatApplication();
    crimeApplyMaatApplicationExternal
        .getClientDetails()
        .getApplicant()
        .setBenefitType(Applicant.BenefitType.INCOME_SUPPORT);
    Passported actualPassported = passportedMapper.map(crimeApplyMaatApplicationExternal);
    assertTrue(actualPassported.getBenefitIncomeSupport());
  }
}
