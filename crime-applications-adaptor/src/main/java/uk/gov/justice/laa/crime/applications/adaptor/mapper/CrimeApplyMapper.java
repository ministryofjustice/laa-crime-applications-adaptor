package uk.gov.justice.laa.crime.applications.adaptor.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uk.gov.justice.laa.crime.applications.adaptor.model.*;
import uk.gov.justice.laa.crime.applications.adaptor.model.criminalapplicationsdatastore.CadApplicationResponse;

/**
 * The responsibility of this class is to encapsulate the required logic to map from a
 * Criminal Applications Datastore response
 * to a
 * Crime Applications Adaptor MAAT Application (which should be structured like a MAAT ApplicationDTO
 */
@Service
@RequiredArgsConstructor
public class CrimeApplyMapper {

    public MaatApplication mapToMaatApplication(CadApplicationResponse crimeApplyResponse) {
        // TODO Alex implement this
        // apply mapping logic as per doc to go from Crime Apply response
        // uk.gov.justice.laa.crime.applications.adaptor.model.crimeapply.MaatCaaContract
        // to
        // uk.gov.justice.laa.crime.applications.adaptor.model.maat.MaatApplication
        MaatApplication maatApplication = new MaatApplication();
        maatApplication.setStatusReason("current");
        maatApplication.setSolicitorName(crimeApplyResponse.getProviderDetails().getLegalRepFirstName() + " " +
                crimeApplyResponse.getProviderDetails().getLegalRepLastName());
        maatApplication.setSolicitorAdminEmail(crimeApplyResponse.getProviderDetails().getProviderEmail());
        maatApplication.setRepId(null);
        maatApplication.setCourtCustody(null);
        //maatApplication.setPartnerContraryInterest
        maatApplication.setCaseDetails(mapToCaseDetails(crimeApplyResponse));

        maatApplication.setApplicant(mapToApplicant(crimeApplyResponse));
        maatApplication.setPassported(mapToPassported(crimeApplyResponse));
        maatApplication.setOffence(mapToOffence(crimeApplyResponse));

        return maatApplication;
    }

    private Offence mapToOffence(CadApplicationResponse crimeApplyResponse) {
        Offence offence = new Offence();
        offence.setOffenceType(crimeApplyResponse.getCaseDetails().getCaseType());
        return offence;
    }

    private Passported mapToPassported(CadApplicationResponse crimeApplyResponse) {
        Passported passported = new Passported();
        passported.setNewWorkReason(mapToNewWorkReason(crimeApplyResponse));
        return passported;
    }

    private NewWorkReason mapToNewWorkReason(CadApplicationResponse crimeApplyResponse) {
        NewWorkReason newWorkReason = new NewWorkReason();
        newWorkReason.setCode(crimeApplyResponse.getApplicationType());
        newWorkReason.setDescription("TEST");
        return newWorkReason;
    }

    private Applicant mapToApplicant(CadApplicationResponse crimeApplyResponse) {
        Applicant applicant = new Applicant();
        applicant.setGender("Prefer not to say");
        applicant.setDisabilityStatement(mapToDisabilityStatement(crimeApplyResponse));
        return applicant;
    }

    private DisabilityStatement mapToDisabilityStatement(CadApplicationResponse crimeApplyResponse) {
        DisabilityStatement disabilityStatement = new DisabilityStatement();

        return disabilityStatement;
    }

    private CaseDetails mapToCaseDetails(CadApplicationResponse crimeApplyResponse) {
        CaseDetails caseDetails = new CaseDetails();
        caseDetails.setAdditionalProperty("hearingCourtName", crimeApplyResponse.getCaseDetails().getHearingCourtName());

        return caseDetails;
    }
}
