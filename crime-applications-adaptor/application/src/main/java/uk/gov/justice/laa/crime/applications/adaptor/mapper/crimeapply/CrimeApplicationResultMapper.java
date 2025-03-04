package uk.gov.justice.laa.crime.applications.adaptor.mapper.crimeapply;

import java.util.Objects;
import org.springframework.stereotype.Component;
import uk.gov.justice.laa.crime.applications.adaptor.model.RepOrderState;
import uk.gov.justice.laa.crime.model.common.atis.CrimeApplicationResult;

@Component
public class CrimeApplicationResultMapper {
  private static final String IN_PROGRESS = "IN PROGRESS";
  private static final String PASS = "PASS";
  private static final String FAIL = "FAIL";
  private static final String TEMP = "TEMP";
  private static final String FAIL_CONTINUE = "FAIL CONTINUE";

  public CrimeApplicationResult map(RepOrderState repOrderState) {
    CrimeApplicationResult crimeApplicationResult = new CrimeApplicationResult();
    crimeApplicationResult.setUsn(repOrderState.getUsn());
    crimeApplicationResult.setMaatRef(repOrderState.getMaatRef());
    crimeApplicationResult.setCaseId(repOrderState.getCaseId());
    crimeApplicationResult.setCaseType(
        Objects.nonNull(repOrderState.getCaseType())
            ? CrimeApplicationResult.CaseType.fromValue(repOrderState.getCaseType())
            : null);
    crimeApplicationResult.setAppCreatedDate(
        Objects.nonNull(repOrderState.getDateAppCreated())
            ? repOrderState.getDateAppCreated().atStartOfDay()
            : null);
    crimeApplicationResult.setIojResult(repOrderState.getIojResult());
    crimeApplicationResult.setIojReason(repOrderState.getIojReason());
    crimeApplicationResult.setIojAssessorName(repOrderState.getIojAssessorName());
    if (!isPendingAssessments(
        repOrderState.getMeansInitStatus(),
        repOrderState.getMeansFullStatus(),
        repOrderState.getPassportStatus())) {
      mapMeansAssessments(crimeApplicationResult, repOrderState);
      mapPassportAssessments(crimeApplicationResult, repOrderState);
    }
    crimeApplicationResult.setIojAppealResult(repOrderState.getIojAppealResult());
    crimeApplicationResult.setIojAppealAssessorName(repOrderState.getIojAppealAssessorName());
    crimeApplicationResult.setIojAppealDate(repOrderState.getIojAppealDate());
    crimeApplicationResult.setFundingDecision(repOrderState.getFundingDecision());
    crimeApplicationResult.setCcRepDecision(repOrderState.getCcRepDecision());
    return crimeApplicationResult;
  }

  private void mapMeansAssessments(
      CrimeApplicationResult crimeApplicationResult, RepOrderState repOrderState) {
    crimeApplicationResult.setMeansResult(
        Objects.nonNull(repOrderState.getMeansFullResult())
            ? repOrderState.getMeansFullResult()
            : repOrderState.getMeansInitResult());
    crimeApplicationResult.setDateMeansCreated(repOrderState.getDateMeansCreated());
    crimeApplicationResult.setMeansAssessorName(repOrderState.getMeansAssessorName());
    crimeApplicationResult.setMeansReviewType(repOrderState.getMeansReviewType());
    crimeApplicationResult.setMeansWorkReason(repOrderState.getMeansWorkReason());
  }

  private void mapPassportAssessments(
      CrimeApplicationResult crimeApplicationResult, RepOrderState repOrderState) {
    if (Objects.nonNull(repOrderState.getPassportStatus())) {
      crimeApplicationResult.setPassportResult(
          mapPassportResults(repOrderState.getPassportResult()));
      crimeApplicationResult.setDatePassportCreated(repOrderState.getDatePassportCreated());
      crimeApplicationResult.setPassportAssessorName(repOrderState.getPassportAssessorName());
      crimeApplicationResult.setPassportReviewType(repOrderState.getPassportReviewType());
      crimeApplicationResult.setPassportWorkReason(repOrderState.getPassportWorkReason());
    }
  }

  private String mapPassportResults(String passportResult) {
    switch (passportResult) {
      case PASS, TEMP -> {
        return PASS;
      }
      case FAIL_CONTINUE, FAIL -> {
        return FAIL;
      }
      default -> {
        return passportResult;
      }
    }
  }

  private boolean isPendingAssessments(
      String meansInitStatus, String meansFullStatus, String passportStatus) {
    return IN_PROGRESS.equals(meansInitStatus)
        || IN_PROGRESS.equals(meansFullStatus)
        || IN_PROGRESS.equals(passportStatus);
  }
}
