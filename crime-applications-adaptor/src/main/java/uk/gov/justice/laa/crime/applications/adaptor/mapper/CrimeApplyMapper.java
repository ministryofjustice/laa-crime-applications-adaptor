package uk.gov.justice.laa.crime.applications.adaptor.mapper;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.Address;
import uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.Applicant;
import uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.CrimeApplication;
import uk.gov.justice.laa.crime.applications.adaptor.model.criminalapplicationsdatastore.MaatApplication;
import uk.gov.justice.laa.crime.applications.adaptor.model.criminalapplicationsdatastore.general.Provider;

import java.util.List;

/**
 * The responsibility of this class is to encapsulate the required logic to map from a
 * Criminal Applications Datastore response
 * to a
 * Crime Applications Adaptor MAAT Application (which should be structured like a MAAT ApplicationDTO)
 */
@Component
public class CrimeApplyMapper {

    public CrimeApplication mapToCrimeApplication(MaatApplication crimeApplyResponse) {

        CrimeApplication crimeApplication = new CrimeApplication();
        crimeApplication.setSolicitorName(mapSolicitorName(crimeApplyResponse.getProviderDetails()));
        crimeApplication.setUsn(crimeApplyResponse.getReference());
        crimeApplication.setSolicitorAdminEmail(crimeApplyResponse.getProviderDetails().getProviderEmail());
        crimeApplication.setDateCreated(crimeApplyResponse.getSubmittedAt());
        crimeApplication.setDateStamp(crimeApplyResponse.getDateStamp());
        crimeApplication.setApplicant(mapApplicant(crimeApplyResponse));

        return crimeApplication;
    }

    private String mapSolicitorName(Provider providerDetails) {
        if (providerDetails == null) {
            return null;
        }

        List<String> firstAndLastName = List.of(providerDetails.getLegalRepFirstName(),
                providerDetails.getLegalRepLastName());
        return String.join(StringUtils.SPACE, firstAndLastName);
    }

    private Applicant mapApplicant(MaatApplication crimeApplyResponse) {
        Applicant applicant = new Applicant();

        uk.gov.justice.laa.crime.applications.adaptor.model.criminalapplicationsdatastore.Applicant crimeApplyApplicant =
                crimeApplyResponse.getClientDetails().getApplicant();

        applicant.setFirstName(crimeApplyApplicant.getFirstName());
        applicant.setOtherNames(crimeApplyApplicant.getOtherNames());
        applicant.setSurname(crimeApplyApplicant.getLastName());
        applicant.setDateOfBirth(crimeApplyApplicant.getDateOfBirth());
        applicant.setTelephone(crimeApplyApplicant.getTelephoneNumber());
        applicant.setNiNumber(crimeApplyApplicant.getNino());
        applicant.setNoFixedAbode(mapNoFixedAbode(crimeApplyApplicant.getHomeAddress()));
        applicant.setUseSupplierAddressForPost(mapUseSupplierAddressForPost(crimeApplyApplicant));
        applicant.setHomeAddress(mapAddress(crimeApplyApplicant.getHomeAddress()));
        applicant.setPostalAddress(mapAddress(crimeApplyApplicant.getCorrespondenceAddress()));

        return applicant;
    }

    private Boolean mapUseSupplierAddressForPost(uk.gov.justice.laa.crime.applications.adaptor.model.criminalapplicationsdatastore.Applicant crimeApplyApplicant) {
        uk.gov.justice.laa.crime.applications.adaptor.model.criminalapplicationsdatastore.Applicant.CorrespondenceAddressType addressType =
                crimeApplyApplicant.getCorrespondenceAddressType();
        switch (addressType) {
            case OTHER_ADDRESS, HOME_ADDRESS -> {
                return false;
            }
            case PROVIDERS_OFFICE_ADDRESS -> {
                return true;
            }
            default -> throw new IllegalStateException("Unexpected value: " + addressType);
        }
    }

    private boolean mapNoFixedAbode(uk.gov.justice.laa.crime.applications.adaptor.model.criminalapplicationsdatastore.general.Address homeAddress) {
        return homeAddress == null;
    }

    private Address mapAddress(uk.gov.justice.laa.crime.applications.adaptor.model.criminalapplicationsdatastore.general.Address crimeApplyAddress) {
        if (crimeApplyAddress == null) {
            return null;
        }
        Address Address = new Address();
        Address.setLookupId(crimeApplyAddress.getLookupId());
        Address.setLine1(crimeApplyAddress.getAddressLineOne());
        Address.setLine2(crimeApplyAddress.getAddressLineTwo());
        Address.setCity(crimeApplyAddress.getCity());
        Address.setCountry(crimeApplyAddress.getCountry());
        Address.setPostCode(crimeApplyAddress.getPostcode());
        return Address;
    }
}
