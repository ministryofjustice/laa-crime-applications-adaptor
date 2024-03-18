package uk.gov.justice.laa.crime.applications.adaptor.mapper.crimeapply;

import java.util.Objects;
import javax.validation.constraints.NotNull;
import uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.common.Passported;
import uk.gov.justice.laa.crime.applications.adaptor.model.criminalapplicationsdatastore.Applicant;
import uk.gov.justice.laa.crime.applications.adaptor.model.criminalapplicationsdatastore.MaatApplicationExternal;

class PassportedMapper {

  private static final boolean FALSE = false;
  private static final boolean TRUE = true;
  private static final Passported.WhoDwpChecked WHO_DWP_CHECKED_DEFAULT_NULL = null;

  @NotNull Passported map(MaatApplicationExternal crimeApplyMaatApplicationExternal) {

    Passported passported = new Passported();

    passported.setBenefitIncomeSupport(FALSE);
    passported.setBenefitGuaranteedStatePension(FALSE);
    passported.setBenefitJobSeeker(FALSE);
    passported.setBenefitUniversalCredit(FALSE);
    passported.setBenefitEmploymentSupport(FALSE);
    passported.setBenefitClaimedByPartner(FALSE);
    passported.setWhoDwpChecked(WHO_DWP_CHECKED_DEFAULT_NULL);

    if (!crimeApplyMaatApplicationExternal.getMeansPassport().isEmpty()) {
      passported.setMeansPassport(
          String.valueOf(crimeApplyMaatApplicationExternal.getMeansPassport().get(0)));
    }

    mapBenefitType(crimeApplyMaatApplicationExternal.getClientDetails().getApplicant(), passported);

    return passported;
  }

  private void mapBenefitType(Applicant applicant, Passported passported) {
    if (Objects.nonNull(applicant) && Objects.nonNull(applicant.getBenefitType())) {
      Applicant.BenefitType benefitType = applicant.getBenefitType();
      switch (benefitType) {
        case UNIVERSAL_CREDIT -> passported.setBenefitUniversalCredit(TRUE);
        case GUARANTEE_PENSION -> passported.setBenefitGuaranteedStatePension(TRUE);
        case JSA -> passported.setBenefitJobSeeker(TRUE);
        case ESA -> passported.setBenefitEmploymentSupport(TRUE);
        case INCOME_SUPPORT -> passported.setBenefitIncomeSupport(TRUE);
      }
    }
  }
}
