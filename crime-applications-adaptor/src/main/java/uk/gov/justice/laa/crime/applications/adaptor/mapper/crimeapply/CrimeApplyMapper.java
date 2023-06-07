package uk.gov.justice.laa.crime.applications.adaptor.mapper.crimeapply;

import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.*;
import uk.gov.justice.laa.crime.applications.adaptor.model.criminalapplicationsdatastore.ClientDetails;
import uk.gov.justice.laa.crime.applications.adaptor.model.criminalapplicationsdatastore.MaatApplication;
import uk.gov.justice.laa.crime.applications.adaptor.model.criminalapplicationsdatastore.general.Ioj;
import uk.gov.justice.laa.crime.applications.adaptor.model.criminalapplicationsdatastore.general.Provider;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

/**
 * The responsibility of this class is to encapsulate the required logic to map from a
 * Criminal Applications Datastore response
 * to a
 * Crime Applications Adaptor MAAT Application (which should be structured like a MAAT ApplicationDTO)
 */
@Component
public class CrimeApplyMapper {

    private static final String STATUS_REASON_DEFAULT_CURRENT = "current";
    private static final boolean COURT_CUSTODY_DEFAULT_FALSE = false;
    private static final String PARTNER_CONTRARY_INTEREST_CODE_DEFAULT_NOCON = "NOCON";
    private static final String APPLICANT_GENDER_DEFAULT_PREFER_NOT_TO_SAY = "Prefer not to say";
    private static final boolean APPLICANT_HAS_PARTNER_DEFAULT_FALSE = false;

    private final CaseDetailsMapper caseDetailsMapper;
    private final OffenceClassMapper offenceClassMapper;
    private final PassportedMapper passportedMapper;

    public CrimeApplyMapper() {
        caseDetailsMapper = new CaseDetailsMapper();
        offenceClassMapper = new OffenceClassMapper();
        passportedMapper = new PassportedMapper();
    }

    public CrimeApplication mapToCrimeApplication(MaatApplication crimeApplyResponse) {

        CrimeApplication crimeApplication = new CrimeApplication();
        crimeApplication.setStatusReason(STATUS_REASON_DEFAULT_CURRENT);
        crimeApplication.setSolicitorName(mapSolicitorName(crimeApplyResponse.getProviderDetails()));
        crimeApplication.setApplicationType(crimeApplyResponse.getApplicationType());

        crimeApplication.setCaseDetails(caseDetailsMapper.map(crimeApplyResponse.getCaseDetails()));

        crimeApplication.setMagsCourt(mapMagistrateCourt(crimeApplyResponse.getCaseDetails()));
        crimeApplication.setInterestsOfJustice(mapInterestsOfJustice(crimeApplyResponse.getInterestsOfJustice()));
        crimeApplication.setUsn(mapUsn(crimeApplyResponse));
        crimeApplication.setSolicitorAdminEmail(mapSolicitorAdminEmail(crimeApplyResponse.getProviderDetails()));
        crimeApplication.setCourtCustody(COURT_CUSTODY_DEFAULT_FALSE);
        crimeApplication.setDateCreated(crimeApplyResponse.getSubmittedAt());
        crimeApplication.setDateStamp(crimeApplyResponse.getDateStamp());
        crimeApplication.setHearingDate(mapHearingDate(crimeApplyResponse.getCaseDetails()));
        crimeApplication.setApplicant(mapApplicant(crimeApplyResponse));
        crimeApplication.setSupplier(mapSupplier(crimeApplyResponse.getProviderDetails()));
        crimeApplication.setPassported(passportedMapper.map(crimeApplyResponse));

        return crimeApplication;
    }

    private String mapHearingDate(uk.gov.justice.laa.crime.applications.adaptor.model.criminalapplicationsdatastore.CaseDetails crimeApplyCaseDetails) {
        if (crimeApplyCaseDetails == null) {
            return null;
        }

        return crimeApplyCaseDetails.getHearingDate();
    }

    private BigDecimal mapUsn(MaatApplication crimeApplyResponse) {
        return crimeApplyResponse.getReference();
    }

    private String mapSolicitorAdminEmail(Provider crimeApplyProviderDetails) {
        if (crimeApplyProviderDetails == null) {
            return null;
        }
        return crimeApplyProviderDetails.getProviderEmail();
    }

    private List<InterestOfJustice> mapInterestsOfJustice(List<Ioj> crimeApplyInterestsOfJustice) {
        if (crimeApplyInterestsOfJustice == null) {
            return Collections.emptyList();
        }

        return crimeApplyInterestsOfJustice.stream()
                .map(ioj -> {
                    Ioj.Type crimeApplyType = ioj.getType();
                    InterestOfJustice.Type iojType = crimeApplyType == null ? null : EnumUtils.getEnum(InterestOfJustice.Type.class, crimeApplyType.name());
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

    private Supplier mapSupplier(Provider crimeApplyProviderDetails) {
        if (crimeApplyProviderDetails == null) {
            return null;
        }

        Supplier supplier = new Supplier();

        supplier.setOfficeCode(crimeApplyProviderDetails.getOfficeCode());
        supplier.setEmail(crimeApplyProviderDetails.getProviderEmail());
        supplier.setTelephone(crimeApplyProviderDetails.getLegalRepTelephone());
        supplier.setFirstName(crimeApplyProviderDetails.getLegalRepFirstName());
        supplier.setSurname(crimeApplyProviderDetails.getLegalRepLastName());

        return supplier;
    }

    private String mapSolicitorName(Provider crimeApplyProviderDetails) {
        if (crimeApplyProviderDetails == null) {
            return null;
        }

        List<String> firstAndLastName = List.of(crimeApplyProviderDetails.getLegalRepFirstName(),
                crimeApplyProviderDetails.getLegalRepLastName());
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
        applicant.setGender(APPLICANT_GENDER_DEFAULT_PREFER_NOT_TO_SAY);
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

    private boolean mapUseSupplierAddressForPost(uk.gov.justice.laa.crime.applications.adaptor.model.criminalapplicationsdatastore.Applicant.CorrespondenceAddressType crimeApplyAddressType) {
        if (crimeApplyAddressType == null) {
            return COURT_CUSTODY_DEFAULT_FALSE;
        }

        switch (crimeApplyAddressType) {
            case OTHER_ADDRESS, HOME_ADDRESS -> {
                return COURT_CUSTODY_DEFAULT_FALSE;
            }
            case PROVIDERS_OFFICE_ADDRESS -> {
                return true;
            }
            default -> throw new IllegalStateException("Unexpected value: " + crimeApplyAddressType);
        }
    }

    private boolean mapNoFixedAbode(uk.gov.justice.laa.crime.applications.adaptor.model.criminalapplicationsdatastore.general.Address crimeApplyAddress) {
        return crimeApplyAddress == null;
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
