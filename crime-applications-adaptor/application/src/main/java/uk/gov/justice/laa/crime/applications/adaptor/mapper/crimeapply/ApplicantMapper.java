package uk.gov.justice.laa.crime.applications.adaptor.mapper.crimeapply;

import java.util.Objects;
import java.util.Optional;
import javax.validation.constraints.NotNull;
import uk.gov.justice.laa.crime.model.common.crimeapplication.common.Address;
import uk.gov.justice.laa.crime.model.common.crimeapplication.common.Applicant;
import uk.gov.justice.laa.crime.model.common.crimeapplication.common.EmploymentStatus;
import uk.gov.justice.laa.crime.model.common.crimeapplication.common.PartnerContraryInterest;
import uk.gov.justice.laa.crime.model.common.criminalapplicationsdatastore.ClientDetails;
import uk.gov.justice.laa.crime.model.common.criminalapplicationsdatastore.MaatApplicationExternal;
import uk.gov.justice.laa.crime.model.common.criminalapplicationsdatastore.Partner;
import uk.gov.justice.laa.crime.model.common.criminalapplicationsdatastore.general.EmploymentType;

class ApplicantMapper {

  private static final String YES = "yes";
  private static final String PARTNER_CONTRARY_INTEREST_CODE_DEFAULT_NOCON = "NOCON";
  private static final String PROSECUTION_WITNESS = "PROW";
  private static final String ALLEGED_VICTIM = "ALLV";
  private static final String CO_DEFENDANT_WITH_CONFLICT = "COCON";
  private static final String ON_BENEFIT_CHECK = "on_benefit_check";

  @NotNull Applicant map(MaatApplicationExternal crimeApplyResponse) {
    Applicant applicant = new Applicant();

    if (Objects.isNull(crimeApplyResponse)
        || Objects.isNull(crimeApplyResponse.getClientDetails())) {
      return applicant;
    }

    uk.gov.justice.laa.crime.model.common.criminalapplicationsdatastore.Applicant
        crimeApplyApplicant = crimeApplyResponse.getClientDetails().getApplicant();
    if (Objects.isNull(crimeApplyApplicant)) {
      return applicant;
    }

    applicant.setFirstName(crimeApplyApplicant.getFirstName());
    applicant.setOtherNames(crimeApplyApplicant.getOtherNames());
    applicant.setSurname(crimeApplyApplicant.getLastName());
    applicant.setDateOfBirth(crimeApplyApplicant.getDateOfBirth());
    applicant.setHasPartner(
        Objects.nonNull(crimeApplyApplicant.getHasPartner())
            && crimeApplyApplicant.getHasPartner().equals(YES));
    applicant.setTelephone(crimeApplyApplicant.getTelephoneNumber());
    applicant.setNiNumber(crimeApplyApplicant.getNino());

    uk.gov.justice.laa.crime.model.common.criminalapplicationsdatastore.Applicant
            .CorrespondenceAddressType
        crimeApplyAddressType = crimeApplyApplicant.getCorrespondenceAddressType();
    applicant.setUseHomeAddress(mapUseHomeAddress(crimeApplyAddressType));
    applicant.setNoFixedAbode(mapNoFixedAbode(crimeApplyApplicant.getResidenceType()));
    applicant.setUseSupplierAddressForPost(mapUseSupplierAddressForPost(crimeApplyAddressType));
    applicant.setPartnerContraryInterest(mapPartnerContraryInterest(crimeApplyResponse));

    applicant.setHomeAddress(mapAddress(crimeApplyApplicant.getHomeAddress()));
    applicant.setPostalAddress(mapAddress(crimeApplyApplicant.getCorrespondenceAddress()));

    applicant.setEmploymentStatus(mapEmploymentStatus(crimeApplyResponse));

    return applicant;
  }

  private EmploymentStatus mapEmploymentStatus(MaatApplicationExternal crimeApplyResponse) {
    EmploymentStatus employmentStatus = new EmploymentStatus();
    if (!crimeApplyResponse.getMeansPassport().isEmpty()
        && String.valueOf(crimeApplyResponse.getMeansPassport().get(0)).equals(ON_BENEFIT_CHECK)) {
      employmentStatus.setCode(EmploymentStatus.Code.PASSPORTED);
    } else if (Objects.nonNull(crimeApplyResponse.getMeansDetails())
        && Objects.nonNull(crimeApplyResponse.getMeansDetails().getIncomeDetails())
        && !crimeApplyResponse.getMeansDetails().getIncomeDetails().getEmploymentType().isEmpty()) {
      // We only ever need to deal with one employmentType in MAAT, so we just take the first
      EmploymentType employmentType =
          EmploymentType.fromValue(
              String.valueOf(
                  crimeApplyResponse
                      .getMeansDetails()
                      .getIncomeDetails()
                      .getEmploymentType()
                      .get(0)));
      switch (employmentType) {
        case NOT_WORKING -> employmentStatus.setCode(EmploymentStatus.Code.NONPASS);
        case EMPLOYED -> employmentStatus.setCode(EmploymentStatus.Code.EMPLOY);
        case SELF_EMPLOYED -> employmentStatus.setCode(EmploymentStatus.Code.SELF);
        default -> employmentStatus.setCode(null);
      }
    }
    return employmentStatus;
  }

  private PartnerContraryInterest mapPartnerContraryInterest(
      MaatApplicationExternal crimeApplyResponse) {
    PartnerContraryInterest partnerContraryInterest = new PartnerContraryInterest();
    Partner partner = crimeApplyResponse.getClientDetails().getPartner();
    if (Objects.nonNull(partner)) {
      mapContraryInterestCode(partner, partnerContraryInterest);
    } else {
      partnerContraryInterest.setCode(PARTNER_CONTRARY_INTEREST_CODE_DEFAULT_NOCON);
    }
    return partnerContraryInterest;
  }

  private void mapContraryInterestCode(
      Partner partner, PartnerContraryInterest partnerContraryInterest) {
    if (Objects.nonNull(partner.getConflictOfInterest())
        && partner.getConflictOfInterest().equals(YES)) {
      partnerContraryInterest.setCode(CO_DEFENDANT_WITH_CONFLICT);
    } else if (Objects.nonNull(partner.getInvolvementInCase())) {
      Partner.InvolvementInCase involvementInCase = partner.getInvolvementInCase();
      switch (involvementInCase) {
        case VICTIM -> partnerContraryInterest.setCode(ALLEGED_VICTIM);
        case PROSECUTION_WITNESS -> partnerContraryInterest.setCode(PROSECUTION_WITNESS);
        default -> partnerContraryInterest.setCode(PARTNER_CONTRARY_INTEREST_CODE_DEFAULT_NOCON);
      }
    }
  }

  private boolean mapUseHomeAddress(
      uk.gov.justice.laa.crime.model.common.criminalapplicationsdatastore.Applicant
              .CorrespondenceAddressType
          crimeApplyAddressType) {

    return uk.gov.justice.laa.crime.model.common.criminalapplicationsdatastore.Applicant
        .CorrespondenceAddressType.HOME_ADDRESS
        .equals(crimeApplyAddressType);
  }

  private boolean mapUseSupplierAddressForPost(
      uk.gov.justice.laa.crime.model.common.criminalapplicationsdatastore.Applicant
              .CorrespondenceAddressType
          crimeApplyAddressType) {

    return uk.gov.justice.laa.crime.model.common.criminalapplicationsdatastore.Applicant
        .CorrespondenceAddressType.PROVIDERS_OFFICE_ADDRESS
        .equals(crimeApplyAddressType);
  }

  boolean mapNoFixedAbode(
      uk.gov.justice.laa.crime.model.common.criminalapplicationsdatastore.Applicant.ResidenceType
          residenceType) {
    return residenceType != null
        && uk.gov.justice.laa.crime.model.common.criminalapplicationsdatastore.Applicant
            .ResidenceType.NONE
            .equals(residenceType);
  }

  private Address mapAddress(
      uk.gov.justice.laa.crime.model.common.criminalapplicationsdatastore.general.Address
          crimeApplyAddress) {
    if (crimeApplyAddress == null) {
      return null;
    }

    Address address = new Address();

    address.setLookupId(crimeApplyAddress.getLookupId());
    address.setLine1(crimeApplyAddress.getAddressLineOne());
    address.setLine2(crimeApplyAddress.getAddressLineTwo());
    address.setCity(crimeApplyAddress.getCity());
    address.setCountry(crimeApplyAddress.getCountry());
    address.setPostCode(crimeApplyAddress.getPostcode());
    return address;
  }

  boolean getWelshCorrespondence(MaatApplicationExternal crimeApplyResponse) {

    return Optional.ofNullable(crimeApplyResponse)
        .map(MaatApplicationExternal::getClientDetails)
        .map(ClientDetails::getApplicant)
        .filter(applicant -> Objects.nonNull(applicant.getWelshCorrespondence()))
        .map(applicant -> applicant.getWelshCorrespondence())
        .orElse(false);
  }
}
