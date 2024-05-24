package uk.gov.justice.laa.crime.applications.adaptor.mapper.crimeapply;

import java.util.Objects;
import javax.validation.constraints.NotNull;
import uk.gov.justice.laa.crime.model.common.crimeapplication.common.Passported;
import uk.gov.justice.laa.crime.model.common.criminalapplicationsdatastore.Applicant;
import uk.gov.justice.laa.crime.model.common.criminalapplicationsdatastore.MaatApplicationExternal;
import uk.gov.justice.laa.crime.model.common.criminalapplicationsdatastore.Partner;

class PassportedMapper {

  private static final boolean FALSE = false;
  private static final boolean TRUE = true;
  private static final Passported.WhoDwpChecked WHO_DWP_CHECKED_DEFAULT_NULL = null;
  private static final String UNIVERSAL_CREDIT = "universal_credit";
  private static final String GUARANTEE_PENSION = "guarantee_pension";
  private static final String JSA = "jsa";
  private static final String ESA = "esa";
  private static final String INCOME_SUPPORT = "income_support";
  private static final String NONE = "none";

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

    mapApplicantBenefitType(
        crimeApplyMaatApplicationExternal.getClientDetails().getApplicant(), passported);

    if (Objects.nonNull(crimeApplyMaatApplicationExternal.getClientDetails().getPartner())) {
      mapPartner(crimeApplyMaatApplicationExternal.getClientDetails().getPartner(), passported);
    }

    return passported;
  }

  private void mapApplicantBenefitType(Applicant applicant, Passported passported) {
    if (Objects.nonNull(applicant) && Objects.nonNull(applicant.getBenefitType())) {
      mapBenefitType(applicant.getBenefitType().value(), passported);
      passported.setBenefitClaimedByPartner(FALSE);
      passported.setWhoDwpChecked(Passported.WhoDwpChecked.APPLICANT);
      if (Objects.nonNull(applicant.getLastJsaAppointmentDate())) {
        passported.setLastJsaAppointmentDate(applicant.getLastJsaAppointmentDate());
      }
    }
  }

  private void mapBenefitType(String benefitType, Passported passported) {
    switch (benefitType) {
      case UNIVERSAL_CREDIT -> passported.setBenefitUniversalCredit(TRUE);
      case GUARANTEE_PENSION -> passported.setBenefitGuaranteedStatePension(TRUE);
      case JSA -> passported.setBenefitJobSeeker(TRUE);
      case ESA -> passported.setBenefitEmploymentSupport(TRUE);
      case INCOME_SUPPORT -> passported.setBenefitIncomeSupport(TRUE);
    }
  }

  private void mapPartner(Partner partner, Passported passported) {
    passported.getPartnerDetails().setFirstName(partner.getFirstName());
    passported.getPartnerDetails().setLastName(partner.getLastName());
    passported.getPartnerDetails().setNino(partner.getNino());
    passported.getPartnerDetails().setDateOfBirth(partner.getDateOfBirth());
    if (Objects.nonNull(partner.getBenefitType())
        && !partner.getBenefitType().value().equalsIgnoreCase(NONE)) {
      mapPartnerBenefitType(partner.getBenefitType(), passported);
    }
    if (Objects.nonNull(partner.getLastJsaAppointmentDate())) {
      passported.setLastJsaAppointmentDate(partner.getLastJsaAppointmentDate());
    }
  }

  private void mapPartnerBenefitType(
      Partner.BenefitType partnerBenefitType, Passported passported) {
    mapBenefitType(partnerBenefitType.value(), passported);
    passported.setBenefitClaimedByPartner(TRUE);
    passported.setWhoDwpChecked(Passported.WhoDwpChecked.PARTNER);
  }
}
