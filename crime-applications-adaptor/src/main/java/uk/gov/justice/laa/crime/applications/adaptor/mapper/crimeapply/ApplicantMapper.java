package uk.gov.justice.laa.crime.applications.adaptor.mapper.crimeapply;

import uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.common.Address;
import uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.common.Applicant;
import uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.common.PartnerContraryInterest;
import uk.gov.justice.laa.crime.applications.adaptor.model.criminalapplicationsdatastore.ClientDetails;

import javax.validation.constraints.NotNull;

class ApplicantMapper {

    private static final boolean APPLICANT_HAS_PARTNER_DEFAULT_FALSE = false;
    private static final String PARTNER_CONTRARY_INTEREST_CODE_DEFAULT_NOCON = "NOCON";

    @NotNull
    Applicant map(ClientDetails crimeApplyClientDetails) {
        Applicant applicant = new Applicant();

        if (crimeApplyClientDetails == null) {
            return applicant;
        }

        uk.gov.justice.laa.crime.applications.adaptor.model.criminalapplicationsdatastore.Applicant crimeApplyApplicant =
                crimeApplyClientDetails.getApplicant();
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
}
