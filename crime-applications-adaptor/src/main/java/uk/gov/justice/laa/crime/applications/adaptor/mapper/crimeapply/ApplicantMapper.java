package uk.gov.justice.laa.crime.applications.adaptor.mapper.crimeapply;

import uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.common.Address;
import uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.common.Applicant;
import uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.common.EmploymentStatus;
import uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.common.PartnerContraryInterest;
import uk.gov.justice.laa.crime.applications.adaptor.model.criminalapplicationsdatastore.*;
import uk.gov.justice.laa.crime.applications.adaptor.model.criminalapplicationsdatastore.general.EmploymentType;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class ApplicantMapper {

    private static final boolean APPLICANT_HAS_PARTNER_DEFAULT_FALSE = false;
    private static final String PARTNER_CONTRARY_INTEREST_CODE_DEFAULT_NOCON = "NOCON";
    private final Map<String, String> EMPLOYMENT_STATUSES = new HashMap<>();

    public ApplicantMapper() {
        EMPLOYMENT_STATUSES.put("not_working", "NONPASS");
        EMPLOYMENT_STATUSES.put("passported", "PASSPORTED");
    }

    @NotNull
    Applicant map(MaatApplicationExternal crimeApplyResponse) {
        Applicant applicant = new Applicant();

        if (crimeApplyResponse == null || crimeApplyResponse.getClientDetails() == null) {
            return applicant;
        }

        uk.gov.justice.laa.crime.applications.adaptor.model.criminalapplicationsdatastore.Applicant crimeApplyApplicant =
                crimeApplyResponse.getClientDetails().getApplicant();
        if (crimeApplyApplicant == null) {
            return applicant;
        }

        applicant.setFirstName(crimeApplyApplicant.getFirstName());
        applicant.setOtherNames(crimeApplyApplicant.getOtherNames());
        applicant.setSurname(crimeApplyApplicant.getLastName());
        applicant.setDateOfBirth(crimeApplyApplicant.getDateOfBirth());
        applicant.setHasPartner(APPLICANT_HAS_PARTNER_DEFAULT_FALSE);
        applicant.setTelephone(crimeApplyApplicant.getTelephoneNumber());
        applicant.setNiNumber(crimeApplyApplicant.getNino());

        uk.gov.justice.laa.crime.applications.adaptor.model.criminalapplicationsdatastore.Applicant.CorrespondenceAddressType crimeApplyAddressType =
                crimeApplyApplicant.getCorrespondenceAddressType();
        applicant.setUseHomeAddress(mapUseHomeAddress(crimeApplyAddressType));
        applicant.setNoFixedAbode(mapNoFixedAbode(crimeApplyApplicant.getHomeAddress()));
        applicant.setUseSupplierAddressForPost(mapUseSupplierAddressForPost(crimeApplyAddressType));
        applicant.setPartnerContraryInterest(mapPartnerContraryInterest());

        applicant.setHomeAddress(mapAddress(crimeApplyApplicant.getHomeAddress()));
        applicant.setPostalAddress(mapAddress(crimeApplyApplicant.getCorrespondenceAddress()));

        String employmentType = "";
        String meansPassport = "";

        if (!crimeApplyResponse.getMeansDetails().getIncomeDetails().getEmploymentType().isEmpty()) {
            // We only ever need to deal with one employmentType in MAAT, so we just take the first
            employmentType = String.valueOf(crimeApplyResponse.getMeansDetails().getIncomeDetails().getEmploymentType().get(0));
        }
        if (!crimeApplyResponse.getMeansPassport().isEmpty()) {
            // We only ever need to deal with one meansPassport in MAAT, so we just take the first
            meansPassport = String.valueOf(crimeApplyResponse.getMeansPassport().get(0));
        }

        applicant.setEmploymentStatus(mapEmploymentStatus(employmentType, meansPassport));

        return applicant;
    }

    private PartnerContraryInterest mapPartnerContraryInterest() {
        PartnerContraryInterest partnerContraryInterest = new PartnerContraryInterest();
        partnerContraryInterest.setCode(PARTNER_CONTRARY_INTEREST_CODE_DEFAULT_NOCON);
        return partnerContraryInterest;
    }

    private boolean mapUseHomeAddress(uk.gov.justice.laa.crime.applications.adaptor.model.criminalapplicationsdatastore.Applicant.
                                              CorrespondenceAddressType crimeApplyAddressType) {

        return uk.gov.justice.laa.crime.applications.adaptor.model.criminalapplicationsdatastore.Applicant.
                CorrespondenceAddressType.HOME_ADDRESS.equals(crimeApplyAddressType);
    }

    private boolean mapUseSupplierAddressForPost(uk.gov.justice.laa.crime.applications.adaptor.model.criminalapplicationsdatastore.Applicant.
                                                         CorrespondenceAddressType crimeApplyAddressType) {

        return uk.gov.justice.laa.crime.applications.adaptor.model.criminalapplicationsdatastore.Applicant.
                CorrespondenceAddressType.PROVIDERS_OFFICE_ADDRESS.equals(crimeApplyAddressType);
    }

    private boolean mapNoFixedAbode(uk.gov.justice.laa.crime.applications.adaptor.model.criminalapplicationsdatastore.general.
                                            Address crimeApplyAddress) {
        return crimeApplyAddress == null;
    }

    private Address mapAddress(uk.gov.justice.laa.crime.applications.adaptor.model.criminalapplicationsdatastore.general.
                                       Address crimeApplyAddress) {
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

    private EmploymentStatus mapEmploymentStatus(String crimeApplyEmploymentType, String meansPassport) {
        EmploymentStatus employmentStatus = new EmploymentStatus();

        // If there is any value in the means passport, we disregard any employment types that are sent
        if (!meansPassport.isEmpty()) {
            employmentStatus.setCode(EMPLOYMENT_STATUSES.get("passported"));
            return employmentStatus;
        }

        if (!crimeApplyEmploymentType.isEmpty() && crimeApplyEmploymentType.equals("not_working")) {
            // For now, we are only dealing with unemployed. If in future we need to work with other employment types
            // or additional (crime apply can send through multiple), we will need to rework this.
            employmentStatus.setCode(EMPLOYMENT_STATUSES.get(crimeApplyEmploymentType));
        }

        return employmentStatus;
    }
}
