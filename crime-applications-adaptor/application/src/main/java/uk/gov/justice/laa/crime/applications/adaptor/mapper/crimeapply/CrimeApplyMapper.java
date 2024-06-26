package uk.gov.justice.laa.crime.applications.adaptor.mapper.crimeapply;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import uk.gov.justice.laa.crime.model.common.crimeapplication.Assessment;
import uk.gov.justice.laa.crime.model.common.crimeapplication.MaatApplicationInternal;
import uk.gov.justice.laa.crime.model.common.crimeapplication.common.FullMeansAssessment;
import uk.gov.justice.laa.crime.model.common.crimeapplication.common.MagistrateCourt;
import uk.gov.justice.laa.crime.model.common.crimeapplication.common.Supplier;
import uk.gov.justice.laa.crime.model.common.criminalapplicationsdatastore.MaatApplicationExternal;
import uk.gov.justice.laa.crime.model.common.criminalapplicationsdatastore.general.Means;
import uk.gov.justice.laa.crime.model.common.criminalapplicationsdatastore.general.Provider;

/**
 * The responsibility of this class is to map from a "Criminal Applications Datastore" response to a
 * "Crime Applications Adaptor" MAAT Application (which should be structured like a MAAT
 * ApplicationDTO)
 */
@Component
public class CrimeApplyMapper {

  private static final String STATUS_REASON_DEFAULT_CURRENT = "current";
  private static final boolean COURT_CUSTODY_DEFAULT_FALSE = false;

  private final CaseDetailsMapper caseDetailsMapper = new CaseDetailsMapper();
  private final ApplicantMapper applicantMapper = new ApplicantMapper();
  private final PassportedMapper passportedMapper = new PassportedMapper();
  private final InitialMeansAssessmentMapper initialMeansAssessmentMapper =
      new InitialMeansAssessmentMapper();
  private final FullMeansAssessmentMapper fullMeansAssessmentMapper =
      new FullMeansAssessmentMapper();
  private final CapitalEquityMapper capitalEquityMapper = new CapitalEquityMapper();

  public MaatApplicationInternal mapToCrimeApplication(MaatApplicationExternal crimeApplyResponse) {
    MaatApplicationInternal maatApplicationInternal = new MaatApplicationInternal();

    maatApplicationInternal.setStatusReason(STATUS_REASON_DEFAULT_CURRENT);
    maatApplicationInternal.setSolicitorName(
        mapSolicitorName(crimeApplyResponse.getProviderDetails()));
    maatApplicationInternal.setApplicationType(
        String.valueOf(crimeApplyResponse.getApplicationType()));
    maatApplicationInternal.setCaseDetails(
        caseDetailsMapper.map(crimeApplyResponse.getCaseDetails()));
    maatApplicationInternal.setMagsCourt(mapMagistrateCourt(crimeApplyResponse.getCaseDetails()));
    maatApplicationInternal.setUsn(mapUsn(crimeApplyResponse));
    maatApplicationInternal.setSolicitorAdminEmail(
        mapSolicitorAdminEmail(crimeApplyResponse.getProviderDetails()));
    maatApplicationInternal.setCourtCustody(COURT_CUSTODY_DEFAULT_FALSE);
    maatApplicationInternal.setDateCreated(crimeApplyResponse.getSubmittedAt());
    maatApplicationInternal.setDateStamp(crimeApplyResponse.getDateStamp());
    maatApplicationInternal.setDateOfSignature(crimeApplyResponse.getDeclarationSignedAt());
    maatApplicationInternal.setHearingDate(mapHearingDate(crimeApplyResponse.getCaseDetails()));
    maatApplicationInternal.setApplicant(applicantMapper.map(crimeApplyResponse));
    maatApplicationInternal.setSupplier(mapSupplier(crimeApplyResponse.getProviderDetails()));
    maatApplicationInternal.setPassported(passportedMapper.map(crimeApplyResponse));
    maatApplicationInternal.setIojBypass(crimeApplyResponse.getIojBypass());
    if (Objects.nonNull(crimeApplyResponse.getMeansDetails())) {
      maatApplicationInternal.setAssessment(mapAssessment(crimeApplyResponse.getMeansDetails()));
      mapCapitalAndEquity(maatApplicationInternal, crimeApplyResponse);
    }
    return maatApplicationInternal;
  }

  private void mapCapitalAndEquity(
      MaatApplicationInternal maatApplicationInternal, MaatApplicationExternal crimeApplyResponse) {
    if (hasCapitalAndEquity(crimeApplyResponse)) {
      maatApplicationInternal.setCapitalEquity(capitalEquityMapper.map(crimeApplyResponse));
    }
  }

  private boolean hasCapitalAndEquity(MaatApplicationExternal crimeApplyResponse) {
    return Objects.nonNull(crimeApplyResponse.getMeansDetails().getCapitalDetails());
  }

  private Assessment mapAssessment(Means meansDetails) {
    Assessment assessment = new Assessment();
    if (Objects.nonNull(meansDetails.getIncomeDetails())) {
      assessment.setInitialMeansAssessment(
          initialMeansAssessmentMapper.map(meansDetails.getIncomeDetails()));
    }
    FullMeansAssessment fullMeansAssessment = fullMeansAssessmentMapper.map(meansDetails);
    if (!CollectionUtils.isEmpty(fullMeansAssessment.getAssessmentDetails())) {
      assessment.setFullMeansAssessment(fullMeansAssessment);
    }

    return assessment;
  }

  private LocalDate mapHearingDate(
      uk.gov.justice.laa.crime.model.common.criminalapplicationsdatastore.CaseDetails
          crimeApplyCaseDetails) {
    if (crimeApplyCaseDetails == null) {
      return null;
    }

    return crimeApplyCaseDetails.getHearingDate();
  }

  private BigDecimal mapUsn(MaatApplicationExternal crimeApplyResponse) {
    return BigDecimal.valueOf(crimeApplyResponse.getReference());
  }

  private String mapSolicitorAdminEmail(Provider crimeApplyProviderDetails) {
    if (crimeApplyProviderDetails == null) {
      return null;
    }
    return crimeApplyProviderDetails.getProviderEmail();
  }

  private MagistrateCourt mapMagistrateCourt(
      uk.gov.justice.laa.crime.model.common.criminalapplicationsdatastore.CaseDetails
          crimeApplyCaseDetails) {
    MagistrateCourt magistrateCourt = new MagistrateCourt();

    if (crimeApplyCaseDetails == null) {
      return magistrateCourt;
    }

    magistrateCourt.setCourt(crimeApplyCaseDetails.getHearingCourtName());
    return magistrateCourt;
  }

  private Supplier mapSupplier(Provider crimeApplyProviderDetails) {
    Supplier supplier = new Supplier();

    if (crimeApplyProviderDetails == null) {
      return supplier;
    }

    supplier.setOfficeCode(crimeApplyProviderDetails.getOfficeCode());
    supplier.setEmail(crimeApplyProviderDetails.getProviderEmail());
    supplier.setTelephone(crimeApplyProviderDetails.getLegalRepTelephone());
    supplier.setFirstName(crimeApplyProviderDetails.getLegalRepFirstName());
    supplier.setSurname(crimeApplyProviderDetails.getLegalRepLastName());

    return supplier;
  }

  private String mapSolicitorName(Provider crimeApplyProviderDetails) {
    if (crimeApplyProviderDetails == null) {
      return null;
    }

    List<String> firstAndLastName =
        List.of(
            crimeApplyProviderDetails.getLegalRepFirstName(),
            crimeApplyProviderDetails.getLegalRepLastName());
    return String.join(StringUtils.SPACE, firstAndLastName);
  }
}
