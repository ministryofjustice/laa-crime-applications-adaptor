package uk.gov.justice.laa.crime.applications.adaptor.mapper.crimeapply;

import java.util.Objects;
import javax.validation.constraints.NotNull;
import uk.gov.justice.laa.crime.model.common.crimeapplicationsadaptor.common.Address;
import uk.gov.justice.laa.crime.model.common.crimeapplicationsadaptor.common.Applicant;
import uk.gov.justice.laa.crime.model.common.crimeapplicationsadaptor.common.EmploymentStatus;
import uk.gov.justice.laa.crime.model.common.crimeapplicationsadaptor.common.PartnerContraryInterest;
import uk.gov.justice.laa.crime.model.common.criminalapplicationsdatastore.MaatApplicationExternal;
import uk.gov.justice.laa.crime.model.common.criminalapplicationsdatastore.general.EmploymentType;

class ApplicantMapper {

  private static final boolean APPLICANT_HAS_PARTNER_DEFAULT_FALSE = false;
  private static final String PARTNER_CONTRARY_INTEREST_CODE_DEFAULT_NOCON = "NOCON";
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
    applicant.setHasPartner(APPLICANT_HAS_PARTNER_DEFAULT_FALSE);
    applicant.setTelephone(crimeApplyApplicant.getTelephoneNumber());
    applicant.setNiNumber(crimeApplyApplicant.getNino());

    uk.gov.justice.laa.crime.model.common.criminalapplicationsdatastore.Applicant
            .CorrespondenceAddressType
        crimeApplyAddressType = crimeApplyApplicant.getCorrespondenceAddressType();
    applicant.setUseHomeAddress(mapUseHomeAddress(crimeApplyAddressType));
    applicant.setNoFixedAbode(mapNoFixedAbode(crimeApplyApplicant.getHomeAddress()));
    applicant.setUseSupplierAddressForPost(mapUseSupplierAddressForPost(crimeApplyAddressType));
    applicant.setPartnerContraryInterest(mapPartnerContraryInterest());

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

  private PartnerContraryInterest mapPartnerContraryInterest() {
    PartnerContraryInterest partnerContraryInterest = new PartnerContraryInterest();
    partnerContraryInterest.setCode(PARTNER_CONTRARY_INTEREST_CODE_DEFAULT_NOCON);
    return partnerContraryInterest;
  }

  private boolean mapUseHomeAddress(
      uk.gov.justice.laa.crime.model.common.criminalapplicationsdatastore.Applicant
              .CorrespondenceAddressType
          crimeApplyAddressType) {

    return uk.gov.justice.laa.crime.model.common.criminalapplicationsdatastore
        .Applicant.CorrespondenceAddressType.HOME_ADDRESS
        .equals(crimeApplyAddressType);
  }

  private boolean mapUseSupplierAddressForPost(
      uk.gov.justice.laa.crime.model.common.criminalapplicationsdatastore.Applicant
              .CorrespondenceAddressType
          crimeApplyAddressType) {

    return uk.gov.justice.laa.crime.model.common.criminalapplicationsdatastore
        .Applicant.CorrespondenceAddressType.PROVIDERS_OFFICE_ADDRESS
        .equals(crimeApplyAddressType);
  }

  private boolean mapNoFixedAbode(
      uk.gov.justice.laa.crime.model.common.criminalapplicationsdatastore.general
              .Address
          crimeApplyAddress) {
    return crimeApplyAddress == null;
  }

  private Address mapAddress(
      uk.gov.justice.laa.crime.model.common.criminalapplicationsdatastore.general
              .Address
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
}
