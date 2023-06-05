package uk.gov.justice.laa.crime.applications.adaptor.mapper;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.*;
import uk.gov.justice.laa.crime.applications.adaptor.model.criminalapplicationsdatastore.MaatApplication;
import uk.gov.justice.laa.crime.applications.adaptor.model.criminalapplicationsdatastore.general.Provider;

import java.util.List;
import java.util.stream.Collectors;

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
        crimeApplication.setApplicationType(crimeApplyResponse.getApplicationType());
        crimeApplication.setCaseDetails(mapCaseDetails(crimeApplyResponse));
        crimeApplication.setMagsCourt(mapMagistrateCourt(crimeApplyResponse));
        crimeApplication.setInterestsOfJustice(mapInterestsOfJustice(crimeApplyResponse));
        crimeApplication.setUsn(crimeApplyResponse.getReference());
        crimeApplication.setSolicitorAdminEmail(crimeApplyResponse.getProviderDetails().getProviderEmail());
        crimeApplication.setDateCreated(crimeApplyResponse.getSubmittedAt());
        crimeApplication.setDateStamp(crimeApplyResponse.getDateStamp());
        crimeApplication.setApplicant(mapApplicant(crimeApplyResponse));
        crimeApplication.setSupplier(mapSupplier(crimeApplyResponse));

        return crimeApplication;
    }

    private List<InterestOfJustice> mapInterestsOfJustice(MaatApplication crimeApplyResponse) {

        return crimeApplyResponse.getInterestsOfJustice().stream().map(ioj -> {
            InterestOfJustice.Type iojType = InterestOfJustice.Type.fromValue(ioj.getType().value());
            return new InterestOfJustice(iojType, ioj.getReason());
        }).collect(Collectors.toList());
    }

    private MagistrateCourt mapMagistrateCourt(MaatApplication crimeApplyResponse) {
        MagistrateCourt magistrateCourt = new MagistrateCourt();
        magistrateCourt.setCourt(crimeApplyResponse.getCaseDetails().getHearingCourtName());
        return magistrateCourt;
    }

    private CaseDetails mapCaseDetails(MaatApplication crimeApplyResponse) {
        CaseDetails caseDetails = new CaseDetails();

        caseDetails.setUrn(crimeApplyResponse.getCaseDetails().getUrn());
        caseDetails.setCaseType(CaseDetails.CaseType.fromValue(
                crimeApplyResponse.getCaseDetails().getCaseType().value()));
        caseDetails.setOffenceClass(CaseDetails.OffenceClass.fromValue(
                crimeApplyResponse.getCaseDetails().getOffenceClass().value()));

        return caseDetails;
    }

    private Supplier mapSupplier(MaatApplication crimeApplyResponse) {
        Supplier supplier = new Supplier();

        Provider providerDetails = crimeApplyResponse.getProviderDetails();

        supplier.setOfficeCode(providerDetails.getOfficeCode());
        supplier.setEmail(providerDetails.getProviderEmail());
        supplier.setTelephone(providerDetails.getLegalRepTelephone());
        supplier.setFirstName(providerDetails.getLegalRepFirstName());
        supplier.setSurname(providerDetails.getLegalRepLastName());

        return supplier;
    }

    private String mapSolicitorName(Provider providerDetails) {
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
