package uk.gov.justice.laa.crime.applications.adaptor.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import uk.gov.justice.laa.crime.model.common.crimeapplication.common.AssessmentDetail.ApplicantFrequency;
import uk.gov.justice.laa.crime.model.common.crimeapplication.common.AssessmentDetail.PartnerFrequency;

public class FrequencyMapperTest {

  @ParameterizedTest
  @MethodSource("applicantFrequency")
  void shouldMapApplicantFrequency(String frequency, ApplicantFrequency applicantFrequency) {
    assertEquals(applicantFrequency, FrequencyMapper.mapApplicantFrequency(frequency));
  }

  private static Stream<Arguments> applicantFrequency() {
    return Stream.of(
        Arguments.of("week", ApplicantFrequency.WEEKLY),
        Arguments.of("fortnight", ApplicantFrequency._2_WEEKLY),
        Arguments.of("four_weeks", ApplicantFrequency._4_WEEKLY),
        Arguments.of("month", ApplicantFrequency.MONTHLY),
        Arguments.of("annual", ApplicantFrequency.ANNUALLY));
  }

  @ParameterizedTest
  @MethodSource("partnerFrequency")
  void shouldMapPartnerFrequency(String frequency, PartnerFrequency applicantFrequency) {
    assertEquals(applicantFrequency, FrequencyMapper.mapPartnerFrequency(frequency));
  }

  private static Stream<Arguments> partnerFrequency() {
    return Stream.of(
        Arguments.of("week", PartnerFrequency.WEEKLY),
        Arguments.of("fortnight", PartnerFrequency._2_WEEKLY),
        Arguments.of("four_weeks", PartnerFrequency._4_WEEKLY),
        Arguments.of("month", PartnerFrequency.MONTHLY),
        Arguments.of("annual", PartnerFrequency.ANNUALLY));
  }
}
