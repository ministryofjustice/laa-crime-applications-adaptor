package uk.gov.justice.laa.crime.applications.adaptor.mapper.crimeapply;

import java.util.Objects;
import uk.gov.justice.laa.crime.model.common.crimeapplication.common.InitialMeansAssessment;
import uk.gov.justice.laa.crime.model.common.criminalapplicationsdatastore.general.IncomeDetails;

public class ManageWithoutIncomeMapper {

  private static final String FRIENDS_SOFA = "friends_sofa";
  private static final String FAMILY = "family";
  private static final String LIVING_ON_STREETS = "living_on_streets";
  private static final String CUSTODY = "custody";
  private static final String OTHER = "other";

  public void map(
      IncomeDetails crimeApplyIncomeDetails, InitialMeansAssessment initialMeansAssessment) {
    if (Objects.nonNull(crimeApplyIncomeDetails.getManageWithoutIncome())) {
      switch (crimeApplyIncomeDetails.getManageWithoutIncome().toString()) {
        case FRIENDS_SOFA ->
            initialMeansAssessment.setInitialAssessmentNote(
                "They sleep on a friend's sofa for free");
        case FAMILY ->
            initialMeansAssessment.setInitialAssessmentNote("They stay with family for free");
        case LIVING_ON_STREETS ->
            initialMeansAssessment.setInitialAssessmentNote(
                "They are living on the streets or homeless");
        case CUSTODY ->
            initialMeansAssessment.setInitialAssessmentNote(
                "They have been in custody for more than 3 months");
        case OTHER ->
            initialMeansAssessment.setInitialAssessmentNote(
                Objects.nonNull(crimeApplyIncomeDetails.getManageOtherDetails())
                    ? (String) crimeApplyIncomeDetails.getManageOtherDetails()
                    : null);
      }
    }
  }
}
