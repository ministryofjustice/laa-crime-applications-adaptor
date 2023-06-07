package uk.gov.justice.laa.crime.applications.adaptor.mapper.crimeapply;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import uk.gov.justice.laa.crime.applications.adaptor.model.criminalapplicationsdatastore.CaseDetails;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OffenceClassMapperTest {

    private OffenceClassMapper offenceClassMapper;

    @BeforeEach
    void setUp() {
        offenceClassMapper = new OffenceClassMapper();
    }

    @ParameterizedTest
    @MethodSource("offenceClassMappingTestData")
    void shouldMapFromCrimeApplyOffenceClassToCrimeApplicationsOffenceClass(CaseDetails.OffenceClass inputCrimeApplyOffenceClass,
                                                                            uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.CaseDetails.OffenceClass expectedOffenceClass) {
        uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.CaseDetails.OffenceClass actualOffenceClass = offenceClassMapper.map(inputCrimeApplyOffenceClass);

        assertEquals(expectedOffenceClass, actualOffenceClass);
    }

    private static Stream<Arguments> offenceClassMappingTestData() {
        return Stream.of(
                Arguments.of(null, null),
                Arguments.of(CaseDetails.OffenceClass.A, uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.CaseDetails.OffenceClass.MURDER),
                Arguments.of(CaseDetails.OffenceClass.B, uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.CaseDetails.OffenceClass.SERIOUS_VIOL_DRUGS),
                Arguments.of(CaseDetails.OffenceClass.C, uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.CaseDetails.OffenceClass.LESSER_VIOL_DRUGS),
                Arguments.of(CaseDetails.OffenceClass.D, uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.CaseDetails.OffenceClass.SEX),
                Arguments.of(CaseDetails.OffenceClass.E, uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.CaseDetails.OffenceClass.BURGLARY),
                Arguments.of(CaseDetails.OffenceClass.F, uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.CaseDetails.OffenceClass.DISHONESTY_LT_30_K),
                Arguments.of(CaseDetails.OffenceClass.G, uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.CaseDetails.OffenceClass.DISHONESTY_30_K_100_K),
                Arguments.of(CaseDetails.OffenceClass.H, uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.CaseDetails.OffenceClass.OTHER),
                Arguments.of(CaseDetails.OffenceClass.I, uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.CaseDetails.OffenceClass.REVENUE_PUBLIC_ORDER),
                Arguments.of(CaseDetails.OffenceClass.J, uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.CaseDetails.OffenceClass.COMPLEX_SEX),
                Arguments.of(CaseDetails.OffenceClass.K, uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.CaseDetails.OffenceClass.DISHONESTY_GT_100_K)
        );
    }
}