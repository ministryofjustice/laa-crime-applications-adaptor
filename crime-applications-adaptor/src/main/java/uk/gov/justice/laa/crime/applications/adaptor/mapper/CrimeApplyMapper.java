package uk.gov.justice.laa.crime.applications.adaptor.mapper;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.Applicant;
import uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.HomeAddress;
import uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.MaatCaaContract;
import uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.PostalAddress;
import uk.gov.justice.laa.crime.applications.adaptor.model.criminalapplicationsdatastore.Applicant__1;
import uk.gov.justice.laa.crime.applications.adaptor.model.criminalapplicationsdatastore.MaatApplication;
import uk.gov.justice.laa.crime.applications.adaptor.model.criminalapplicationsdatastore.Provider;

import java.util.List;

/**
 * The responsibility of this class is to encapsulate the required logic to map from a
 * Criminal Applications Datastore response
 * to a
 * Crime Applications Adaptor MAAT Application (which should be structured like a MAAT ApplicationDTO)
 */
@Service
@RequiredArgsConstructor
public class CrimeApplyMapper {

    public MaatCaaContract mapToMaatApplication(MaatApplication crimeApplyResponse) {

        MaatCaaContract maatApplication = new MaatCaaContract();
        maatApplication.setSolicitorName(mapSolicitorName(crimeApplyResponse.getProviderDetails()));
        // maatApplication.setApplicationType(mapApplicationType(crimeApplyResponse));
        // maatApplication.setCaseDetails(mapCaseDetails(crimeApplyResponse));
        // maatApplication.setMagsOutcome(mapMagsOutcome(crimeApplyResponse));
        // maatApplication.setIojResult();
        // maatApplication.setAreaId();
        maatApplication.setUsn(crimeApplyResponse.getReference());
        // maatApplication.setCommonPlatformData(mapCommonPlatformData(crimeApplyResponse));
        // maatApplication.setCaseId();
        // maatApplication.setOffence(mapOffence(crimeApplyResponse));
        // maatApplication.setArrestSummonsNumber();
        maatApplication.setSolicitorAdminEmail(crimeApplyResponse.getProviderDetails().getProviderEmail());
        // maatApplication.setCourtCustody();
        // maatApplication.setWelshCorrespondence();
        maatApplication.setDateCreated(crimeApplyResponse.getSubmittedAt());
        maatApplication.setDateStamp(crimeApplyResponse.getDateStamp());
        // maatApplication.setHearingDate();
        // maatApplication.setCommittalDate();
        // maatApplication.setDateOfSignature();
        // maatApplication.setDateReceived();
        maatApplication.setApplicant(mapToApplicant(crimeApplyResponse));
        // maatApplication.setSupplier(mapSupplier(crimeApplyResponse));
        // maatApplication.setPassported(mapPassported(crimeApplyResponse));
        // maatApplication.setAssessment(mapAssessment(crimeApplyResponse));
        // maatApplication.setCapitalEquity(mapCapitalEquity(crimeApplyResponse));

        return maatApplication;
    }

    private static String mapSolicitorName(Provider providerDetails) {
        if (providerDetails == null) {
            return null;
        }

        List<String> firstAndLastName = List.of(providerDetails.getLegalRepFirstName(),
                providerDetails.getLegalRepLastName());
        return String.join(StringUtils.SPACE, firstAndLastName);
    }

    private Applicant mapToApplicant(MaatApplication crimeApplyResponse) {
        Applicant applicant = new Applicant();

        Applicant__1 crimeApplyApplicant = crimeApplyResponse.getClientDetails().getApplicant();

        applicant.setFirstName(crimeApplyApplicant.getFirstName());
        applicant.setOtherNames(crimeApplyApplicant.getOtherNames());
        applicant.setSurname(crimeApplyApplicant.getLastName());
        applicant.setDateOfBirth(crimeApplyApplicant.getDateOfBirth());
        applicant.setTelephone(crimeApplyApplicant.getTelephoneNumber());
        applicant.setNiNumber(crimeApplyApplicant.getNino());
        applicant.setNoFixedAbode(mapNoFixedAbode(crimeApplyApplicant.getHomeAddress()));
        applicant.setUseSupplierAddressForPost(mapUseSupplierAddressForPost(crimeApplyApplicant));
        applicant.setHomeAddress(mapHomeAddress(crimeApplyResponse.getClientDetails().getApplicant().getHomeAddress()));
        applicant.setPostalAddress(mapPostalAddress(crimeApplyResponse.getClientDetails().getApplicant().getCorrespondenceAddress()));

        return applicant;
    }

    private Boolean mapUseSupplierAddressForPost(Applicant__1 crimeApplyApplicant) {
        Applicant__1.CorrespondenceAddressType addressType = crimeApplyApplicant.getCorrespondenceAddressType();
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

    private static boolean mapNoFixedAbode(uk.gov.justice.laa.crime.applications.adaptor.model.criminalapplicationsdatastore.Address homeAddress) {
        return homeAddress == null ||
                (StringUtils.isBlank(homeAddress.getAddressLineOne()) &&
                        StringUtils.isBlank(homeAddress.getPostcode()));
    }

    private PostalAddress mapPostalAddress(uk.gov.justice.laa.crime.applications.adaptor.model.criminalapplicationsdatastore.Address correspondenceAddress) {
        if (correspondenceAddress == null) {
            return null;
        }
        PostalAddress postalAddress = new PostalAddress();
        postalAddress.setCity(correspondenceAddress.getCity());
        postalAddress.setLine1(correspondenceAddress.getAddressLineOne());
        postalAddress.setLine2(correspondenceAddress.getAddressLineTwo());
        postalAddress.setPostCode(correspondenceAddress.getPostcode());
        return postalAddress;
    }

    private HomeAddress mapHomeAddress(uk.gov.justice.laa.crime.applications.adaptor.model.criminalapplicationsdatastore.Address crimeApplyHomeAddress) {
        if (crimeApplyHomeAddress == null) {
            return null;
        }
        HomeAddress homeAddress = new HomeAddress();
        homeAddress.setLookupId(crimeApplyHomeAddress.getLookupId());
        homeAddress.setLine1(crimeApplyHomeAddress.getAddressLineOne());
        homeAddress.setLine2(crimeApplyHomeAddress.getAddressLineTwo());
        homeAddress.setCity(crimeApplyHomeAddress.getCity());
        homeAddress.setCountry(crimeApplyHomeAddress.getCountry());
        homeAddress.setPostCode(crimeApplyHomeAddress.getPostcode());
        return homeAddress;
    }
}
