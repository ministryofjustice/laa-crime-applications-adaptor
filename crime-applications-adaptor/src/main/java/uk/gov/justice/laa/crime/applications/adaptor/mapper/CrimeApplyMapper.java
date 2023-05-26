package uk.gov.justice.laa.crime.applications.adaptor.mapper;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import uk.gov.justice.laa.crime.applications.adaptor.model.*;
import uk.gov.justice.laa.crime.applications.adaptor.model.criminalapplicationsdatastore.MaatApplication;
import uk.gov.justice.laa.crime.applications.adaptor.model.criminalapplicationsdatastore.ProviderDetails__1;

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

    private static final String DEFAULT_STATUS_REASON = "current";
    private static final Integer DEFAULT_REP_ID = null;
    private static final String DEFAULT_GENDER = "Prefer not to say";
    private static final boolean DEFAULT_HAS_PARTNER = false;
    private static final String DEFAULT_LINE_3 = null;
    private static final String DEFAULT_EMPLOYMENT_STATUS_CODE = "Unknown";
    private static final int DEFAULT_ETHNICITY_ID = 245;
    private static final DisabilityStatement.Code DEFAULT_DISABILITY_STATEMENT_CODE = DisabilityStatement.Code.NO_COMMENT;
    private static final Integer DEFAULT_MAAT_REF = 0;

    public MaatCaaContract mapToMaatApplication(MaatApplication crimeApplyResponse) {
        // TODO Alex implement this

        MaatCaaContract maatApplication = new MaatCaaContract();
        maatApplication.setStatusReason(DEFAULT_STATUS_REASON);
        maatApplication.setSolicitorName(mapSolicitorName(crimeApplyResponse.getProviderDetails()));
        maatApplication.setRepId(DEFAULT_REP_ID);
        maatApplication.setMaatRef(DEFAULT_MAAT_REF);
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
        // maatApplication.setHasPartner();
        // maatApplication.setSupplier(mapSupplier(crimeApplyResponse));
        // maatApplication.setPassported(mapPassported(crimeApplyResponse));
        // maatApplication.setAssessment(mapAssessment(crimeApplyResponse));
        // maatApplication.setCapitalEquity(mapCapitalEquity(crimeApplyResponse));

        return maatApplication;
    }

    private static String mapSolicitorName(ProviderDetails__1 providerDetails) {
        if (providerDetails == null) {
            return null;
        }

        List<String> firstAndLastName = List.of(providerDetails.getLegalRepFirstName(),
                providerDetails.getLegalRepLastName());
        return String.join(StringUtils.SPACE, firstAndLastName);
    }

    private Applicant mapToApplicant(MaatApplication crimeApplyResponse) {
        Applicant applicant = new Applicant();

        uk.gov.justice.laa.crime.applications.adaptor.model.criminalapplicationsdatastore.Applicant crimeApplyApplicant =
                crimeApplyResponse.getClientDetails().getApplicant();

        applicant.setFirstName("TODO Alex"); // TODO allOfIssue
        applicant.setOtherNames("TODO Alex"); // TODO allOfIssue
        applicant.setSurname("TODO Alex"); // TODO allOfIssue
        applicant.setDateOfBirth(crimeApplyApplicant.getDateOfBirth());
        applicant.setEmail("TODO Alex - can't find in Crime Apply response"); // TODO missing property
        applicant.setGender(DEFAULT_GENDER); // TODO missing property
        applicant.setHasPartner(DEFAULT_HAS_PARTNER); // TODO missing property
        applicant.setForeignId("TODO Alex"); // TODO missing property
        applicant.setMobileTelephone("TODO Alex"); // TODO missing property only telephone_number avail
        applicant.setHomeTelephone("TODO Alex"); // TODO missing property only telephone_number avail
        applicant.setWorkTelephone("TODO Alex"); // TODO missing property only telephone_number avail
        applicant.setNiNumber(crimeApplyApplicant.getNino());
        applicant.setNoFixedAbode(mapNoFixedAbode(crimeApplyApplicant.getHomeAddress()));
        applicant.setUseSupplierAddressForPost(mapUseSupplierAddressForPost(crimeApplyApplicant));
        applicant.setPartnerContraryInterest(mapPartnerContraryInterest(crimeApplyResponse));
        applicant.setEthnicity(mapEthnicity(crimeApplyResponse));
        applicant.setEmploymentStatus(mapEmploymentStatus(crimeApplyResponse));
        applicant.setDisabilityStatement(mapToDisabilityStatement(crimeApplyResponse));
        applicant.setHomeAddress(mapHomeAddress(crimeApplyResponse.getClientDetails().getApplicant().getHomeAddress()));
        applicant.setPostalAddress(mapPostalAddress(crimeApplyResponse.getClientDetails().getApplicant().getCorrespondenceAddress()));

        return applicant;
    }

    private Boolean mapUseSupplierAddressForPost(uk.gov.justice.laa.crime.applications.adaptor.model.criminalapplicationsdatastore.Applicant crimeApplyApplicant) {
        uk.gov.justice.laa.crime.applications.adaptor.model.criminalapplicationsdatastore.Applicant.CorrespondenceAddressType addressType = crimeApplyApplicant.getCorrespondenceAddressType();
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
        postalAddress.setLine3(DEFAULT_LINE_3); // TODO missing property
        postalAddress.setPostCode(correspondenceAddress.getPostcode());
        return postalAddress;
    }

    private HomeAddress mapHomeAddress(uk.gov.justice.laa.crime.applications.adaptor.model.criminalapplicationsdatastore.Address crimeApplyHomeAddress) {
        if (crimeApplyHomeAddress == null) {
            return null;
        }
        HomeAddress homeAddress = new HomeAddress();
        homeAddress.setCity(crimeApplyHomeAddress.getCity());
        homeAddress.setLine1(crimeApplyHomeAddress.getAddressLineOne());
        homeAddress.setLine2(crimeApplyHomeAddress.getAddressLineTwo());
        homeAddress.setLine3(DEFAULT_LINE_3); // TODO missing property
        homeAddress.setPostCode(crimeApplyHomeAddress.getPostcode());
        return homeAddress;
    }

    private EmploymentStatus mapEmploymentStatus(MaatApplication crimeApplyResponse) {
        EmploymentStatus employmentStatus = new EmploymentStatus();
        employmentStatus.setCode(DEFAULT_EMPLOYMENT_STATUS_CODE); // TODO Alex missing in crimeApplyResponse
        return employmentStatus;
    }

    private Ethnicity mapEthnicity(MaatApplication crimeApplyResponse) {
        Ethnicity ethnicity = new Ethnicity();
        ethnicity.setId(DEFAULT_ETHNICITY_ID); // TODO Alex missing in crimeApplyResponse
        return ethnicity;
    }

    private PartnerContraryInterest mapPartnerContraryInterest(MaatApplication crimeApplyResponse) {
        PartnerContraryInterest partnerContraryInterest = new PartnerContraryInterest();
        partnerContraryInterest.setCode("TODO Alex"); // TODO missing properties?
        return partnerContraryInterest;
    }

    private DisabilityStatement mapToDisabilityStatement(MaatApplication crimeApplyResponse) {
        DisabilityStatement disabilityStatement = new DisabilityStatement();
        disabilityStatement.setCode(DEFAULT_DISABILITY_STATEMENT_CODE); // TODO not available in crimeApplyResponse
        disabilityStatement.setDisabilities(null);
        return disabilityStatement;
    }
}
