package uk.gov.justice.laa.crime.applications.adaptor.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uk.gov.justice.laa.crime.applications.adaptor.model.Applicant;
import uk.gov.justice.laa.crime.applications.adaptor.model.DisabilityStatement;
import uk.gov.justice.laa.crime.applications.adaptor.model.MaatCaaContract;
import uk.gov.justice.laa.crime.applications.adaptor.model.PartnerContraryInterest;
import uk.gov.justice.laa.crime.applications.adaptor.model.criminalapplicationsdatastore.MaatApplication;

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

    public MaatCaaContract mapToMaatApplication(MaatApplication crimeApplyResponse) {
        // TODO Alex implement this

        MaatCaaContract maatApplication = new MaatCaaContract();
        maatApplication.setStatusReason(DEFAULT_STATUS_REASON);
        maatApplication.setSolicitorName(crimeApplyResponse.getProviderDetails().getLegalRepFirstName() + " " +
                crimeApplyResponse.getProviderDetails().getLegalRepLastName());
        maatApplication.setRepId(DEFAULT_REP_ID);
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
        // maatApplication.setDateCreated();
        // maatApplication.setDateStamp();
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

    private Applicant mapToApplicant(MaatApplication crimeApplyResponse) {
        Applicant applicant = new Applicant();

        uk.gov.justice.laa.crime.applications.adaptor.model.criminalapplicationsdatastore.Applicant crimeApplyApplicant =
                crimeApplyResponse.getClientDetails().getApplicant();
        applicant.setFirstName("TODO Alex"); // TODO missing properties?
        applicant.setSurname("TODO Alex"); // TODO missing properties?
        applicant.setDateOfBirth(crimeApplyApplicant.getDateOfBirth());
        applicant.setEmail("TODO Alex"); // TODO missing properties?
        applicant.setGender("TODO Alex"); // TODO missing properties?
        applicant.setNiNumber(crimeApplyApplicant.getNino()); // TODO is this NI Number?
        applicant.setNoFixedAbode(crimeApplyApplicant.getHomeAddress() == null);
        applicant.setUseSupplierAddressForPost(false); // TODO missing properties?
        applicant.setPartnerContraryInterest(mapPartnerContraryInterest(crimeApplyResponse));
        applicant.setDisabilityStatement(mapToDisabilityStatement(crimeApplyResponse));

        return applicant;
    }

    private PartnerContraryInterest mapPartnerContraryInterest(MaatApplication crimeApplyResponse) {
        PartnerContraryInterest partnerContraryInterest = new PartnerContraryInterest();
        partnerContraryInterest.setCode("TODO Alex"); // TODO missing properties?
        return partnerContraryInterest;
    }

    private DisabilityStatement mapToDisabilityStatement(MaatApplication crimeApplyResponse) {
        DisabilityStatement disabilityStatement = new DisabilityStatement();
        // disabilityStatement.setCode(); TODO not available in crimeApplyResponse
        disabilityStatement.setDisabilities(null);
        return disabilityStatement;
    }
}
