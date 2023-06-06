package uk.gov.justice.laa.crime.applications.adaptor.mapper;

import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.*;
import uk.gov.justice.laa.crime.applications.adaptor.model.criminalapplicationsdatastore.ClientDetails;
import uk.gov.justice.laa.crime.applications.adaptor.model.criminalapplicationsdatastore.MaatApplication;
import uk.gov.justice.laa.crime.applications.adaptor.model.criminalapplicationsdatastore.general.Ioj;
import uk.gov.justice.laa.crime.applications.adaptor.model.criminalapplicationsdatastore.general.Provider;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

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
        crimeApplication.setCaseDetails(mapCaseDetails(crimeApplyResponse.getCaseDetails()));
        crimeApplication.setMagsCourt(mapMagistrateCourt(crimeApplyResponse.getCaseDetails()));
        crimeApplication.setInterestsOfJustice(mapInterestsOfJustice(crimeApplyResponse.getInterestsOfJustice()));
        crimeApplication.setUsn(mapUsn(crimeApplyResponse));
        crimeApplication.setSolicitorAdminEmail(mapSolicitorAdminEmail(crimeApplyResponse.getProviderDetails()));
        crimeApplication.setDateCreated(crimeApplyResponse.getSubmittedAt());
        crimeApplication.setDateStamp(crimeApplyResponse.getDateStamp());
        crimeApplication.setApplicant(mapApplicant(crimeApplyResponse));
        crimeApplication.setSupplier(mapSupplier(crimeApplyResponse.getProviderDetails()));

        return crimeApplication;
    }

    private BigDecimal mapUsn(MaatApplication crimeApplyResponse) {
        return crimeApplyResponse.getReference();
    }

    private String mapSolicitorAdminEmail(Provider providerDetails) {
        if (providerDetails == null) {
            return null;
        }
        return providerDetails.getProviderEmail();
    }

    private List<InterestOfJustice> mapInterestsOfJustice(List<Ioj> crimeApplyInterestsOfJustice) {
        if (crimeApplyInterestsOfJustice == null) {
            return null;
        }

        return crimeApplyInterestsOfJustice.stream()
                .map(ioj -> {
                    InterestOfJustice.Type iojType = EnumUtils.getEnum(InterestOfJustice.Type.class, ioj.getType().name());
                    return new InterestOfJustice(iojType, ioj.getReason());
                }).toList();
    }

    private MagistrateCourt mapMagistrateCourt(uk.gov.justice.laa.crime.applications.adaptor.model.criminalapplicationsdatastore.CaseDetails crimeApplyCaseDetails) {
        if (crimeApplyCaseDetails == null) {
            return null;
        }

        MagistrateCourt magistrateCourt = new MagistrateCourt();
        magistrateCourt.setCourt(crimeApplyCaseDetails.getHearingCourtName());
        return magistrateCourt;
    }

    private CaseDetails mapCaseDetails(uk.gov.justice.laa.crime.applications.adaptor.model.criminalapplicationsdatastore.CaseDetails crimeApplyCaseDetails) {
        if (crimeApplyCaseDetails == null) {
            return null;
        }

        CaseDetails caseDetails = new CaseDetails();

        caseDetails.setUrn(crimeApplyCaseDetails.getUrn());
        uk.gov.justice.laa.crime.applications.adaptor.model.criminalapplicationsdatastore.CaseDetails.CaseType crimeApplyCaseType
                = crimeApplyCaseDetails.getCaseType();
        if (Objects.nonNull(crimeApplyCaseType)) {
            caseDetails.setCaseType(EnumUtils.getEnum(CaseDetails.CaseType.class, crimeApplyCaseType.name()));
        }

        uk.gov.justice.laa.crime.applications.adaptor.model.criminalapplicationsdatastore.CaseDetails.OffenceClass offenceClass
                = crimeApplyCaseDetails.getOffenceClass();
        if (Objects.nonNull(offenceClass)) {
            caseDetails.setOffenceClass(EnumUtils.getEnum(CaseDetails.OffenceClass.class, offenceClass.name()));
        }

        return caseDetails;
    }

    private Supplier mapSupplier(Provider providerDetails) {
        if (providerDetails == null) {
            return null;
        }

        Supplier supplier = new Supplier();

        supplier.setOfficeCode(providerDetails.getOfficeCode());
        supplier.setEmail(providerDetails.getProviderEmail());
        supplier.setTelephone(providerDetails.getLegalRepTelephone());
        supplier.setFirstName(providerDetails.getLegalRepFirstName());
        supplier.setSurname(providerDetails.getLegalRepLastName());

        return supplier;
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

        ClientDetails crimeApplyClientDetails = crimeApplyResponse.getClientDetails();
        if (crimeApplyClientDetails == null) {
            return null;
        }

        uk.gov.justice.laa.crime.applications.adaptor.model.criminalapplicationsdatastore.Applicant crimeApplyApplicant =
                crimeApplyClientDetails.getApplicant();
        if (crimeApplyApplicant == null) {
            return null;
        }

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
        if (addressType == null) {
            return null;
        }

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
