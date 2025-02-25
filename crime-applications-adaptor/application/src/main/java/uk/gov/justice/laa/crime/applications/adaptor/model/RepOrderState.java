package uk.gov.justice.laa.crime.applications.adaptor.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RepOrderState {

  private Integer usn;
  private Integer maatRef;
  private String caseId;
  private String caseType;

  private String iojResult;
  private String iojAssessorName;
  private LocalDate dateAppCreated;
  private String iojReason;

  private String meansInitResult;
  private String meansInitStatus;

  private String meansFullResult;
  private String meansFullStatus;

  private String meansAssessorName;
  private LocalDateTime dateMeansCreated;

  private String passportResult;
  private String passportStatus;
  private String passportAssessorName;
  private LocalDateTime datePassportCreated;

  private String iojAppealResult;
  private String iojAppealAssessorName;
  private LocalDateTime iojAppealDate;

  private String fundingDecision;
  private String ccRepDecision;

  private String meansReviewType;
  private String passportReviewType;
}
