package uk.gov.justice.laa.crime.applications.adaptor.mapper.crimeapply;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.common.Passported;
import uk.gov.justice.laa.crime.applications.adaptor.model.criminalapplicationsdatastore.MaatApplicationExternal;
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
        MaatApplicationExternal crimeApplyMaatApplicationExternal = TestData.getMaatApplication();

        Passported actualPassported = passportedMapper.map(crimeApplyMaatApplicationExternal);

        assertFalse(actualPassported.getBenefitIncomeSupport());
        assertFalse(actualPassported.getBenefitGuaranteedStatePension());
        assertFalse(actualPassported.getBenefitJobSeeker());
        assertFalse(actualPassported.getBenefitEmploymentSupport());
        assertFalse(actualPassported.getBenefitClaimedByPartner());
        assertNull(actualPassported.getWhoDwpChecked());
    }
}