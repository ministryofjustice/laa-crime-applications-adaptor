package uk.gov.justice.laa.crime.applications.adaptor.mapper.crimeapply;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.common.Passported;
import uk.gov.justice.laa.crime.applications.adaptor.model.criminalapplicationsdatastore.MaatApplication;
import uk.gov.justice.laa.crime.applications.adaptor.testutils.TestData;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

class PassportedMapperTest {

    private PassportedMapper passportedMapper;

    @BeforeEach
    void setUp() {
        passportedMapper = new PassportedMapper();
    }

    @Test
    void map() {
        MaatApplication crimeApplyMaatApplication = TestData.getMaatApplication();

        Passported actualPassported = passportedMapper.map(crimeApplyMaatApplication);

        assertFalse(actualPassported.getBenefitIncomeSupport());
        assertFalse(actualPassported.getBenefitGuaranteedStatePension());
        assertFalse(actualPassported.getJobSeeker());
        assertFalse(actualPassported.getBenefitEmploymentSupport());
        assertFalse(actualPassported.getBenefitClaimedByPartner());
        assertNull(actualPassported.getWhoDwpChecked());
    }
}