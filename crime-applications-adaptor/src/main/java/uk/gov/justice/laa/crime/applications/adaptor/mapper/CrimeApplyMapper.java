package uk.gov.justice.laa.crime.applications.adaptor.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uk.gov.justice.laa.crime.applications.adaptor.model.*;
import uk.gov.justice.laa.crime.applications.adaptor.model.criminalapplicationsdatastore.MaatApplication;

/**
 * The responsibility of this class is to encapsulate the required logic to map from a
 * Criminal Applications Datastore response
 * to a
 * Crime Applications Adaptor MAAT Application (which should be structured like a MAAT ApplicationDTO
 */
@Service
@RequiredArgsConstructor
public class CrimeApplyMapper {

    public MaatCaaContract mapToMaatApplication(MaatApplication crimeApplyResponse) {
        // TODO Alex implement this
        // apply mapping logic as per doc to go from Crime Apply response
        // uk.gov.justice.laa.crime.applications.adaptor.model.crimeapply.MaatCaaContract
        // to
        // uk.gov.justice.laa.crime.applications.adaptor.model.maat.MaatApplication
        MaatCaaContract maatApplication = new MaatCaaContract();
        maatApplication.setStatusReason("current");
        maatApplication.setSolicitorName(crimeApplyResponse.getProviderDetails().getLegalRepFirstName() + " " +
                crimeApplyResponse.getProviderDetails().getLegalRepLastName());
        maatApplication.setSolicitorAdminEmail(crimeApplyResponse.getProviderDetails().getProviderEmail());
        maatApplication.setRepId(null);
        maatApplication.setCourtCustody(null); // TODO no custody field present in datastore
        maatApplication.setWelshCorrespondence(null); // TODO no Welsh field present in datastore
        //maatApplication.setDateStamp(crimeApplyResponse.getDateStamp()); // TODO update schema
        maatApplication.setUsn(null); // TODO should come from originating request?
        //maatApplication.setHearingDate(crimeApplyResponse.getCaseDetails().getHearingDate()); // TODO parse/convert
        maatApplication.setCaseDetails(mapToCaseDetails(crimeApplyResponse));
        maatApplication.setMagsOutcome(mapMagsOutcome(crimeApplyResponse));

        maatApplication.setApplicant(mapToApplicant(crimeApplyResponse));
        maatApplication.setPassported(mapToPassported(crimeApplyResponse));
        maatApplication.setOffence(mapToOffence(crimeApplyResponse));

        return maatApplication;
    }

    private MagsOutcome mapMagsOutcome(MaatApplication crimeApplyResponse) {
        MagsOutcome magsOutcome = new MagsOutcome();
        magsOutcome.setOutcome("TODO"); // TODO
        return magsOutcome;
    }

    private Passported mapToPassported(MaatApplication crimeApplyResponse) {
        return null;
    }

    private Offence mapToOffence(MaatApplication crimeApplyResponse) {
        Offence offence = new Offence();
        offence.setOffenceType(crimeApplyResponse.getCaseDetails().getOffenceClass());
        return offence;
    }

    private Applicant mapToApplicant(MaatApplication crimeApplyResponse) {
        Applicant applicant = new Applicant();

        uk.gov.justice.laa.crime.applications.adaptor.model.criminalapplicationsdatastore.Applicant crimeApplyApplicant =
                crimeApplyResponse.getClientDetails().getApplicant();
        applicant.setFirstName(crimeApplyApplicant.toString()); // TODO missing properties?
        applicant.setSurname(crimeApplyApplicant.toString()); // TODO missing properties?
        applicant.setDateOfBirth(crimeApplyApplicant.getDateOfBirth());
        applicant.setEmail(crimeApplyApplicant.toString()); // TODO missing properties?
        applicant.setGender(crimeApplyApplicant.toString()); // TODO missing properties?
        applicant.setNiNumber(crimeApplyApplicant.getNino()); // TODO is this NI Number?
        applicant.setNoFixedAbode(crimeApplyApplicant.getHomeAddress() == null);
        applicant.setUseSupplierAddressForPost(false); // TODO missing properties?
        applicant.setPartnerContraryInterest(mapPartnerContraryInterest(crimeApplyResponse));
        applicant.setDisabilityStatement(mapToDisabilityStatement(crimeApplyResponse));

        return applicant;
    }

    private PartnerContraryInterest mapPartnerContraryInterest(MaatApplication crimeApplyResponse) {
        PartnerContraryInterest partnerContraryInterest = new PartnerContraryInterest();
        partnerContraryInterest.setCode(crimeApplyResponse.getClientDetails().getApplicant().toString()); // TODO missing properties?
        return partnerContraryInterest;
    }

    private DisabilityStatement mapToDisabilityStatement(MaatApplication crimeApplyResponse) {
        DisabilityStatement disabilityStatement = new DisabilityStatement();
        disabilityStatement.setCode(crimeApplyResponse.getClientDetails().getApplicant().toString()); // TODO missing properties?
        return disabilityStatement;
    }

    private CaseDetails mapToCaseDetails(MaatApplication crimeApplyResponse) {
        CaseDetails caseDetails = new CaseDetails();
        caseDetails.setAdditionalProperty("hearingCourtName", crimeApplyResponse.getCaseDetails().getHearingCourtName());
        caseDetails.setCaseType(crimeApplyResponse.getCaseDetails().getCaseType());

        return caseDetails;
    }
}
