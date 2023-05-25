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

    public MaatCaaContract mapToMaatApplication(MaatApplication crimeApplyResponse) {
        // TODO Alex implement this

        MaatCaaContract maatApplication = new MaatCaaContract();
        maatApplication.setStatusReason("current");
        maatApplication.setSolicitorName(crimeApplyResponse.getProviderDetails().getLegalRepFirstName() + " " +
                crimeApplyResponse.getProviderDetails().getLegalRepLastName());
        maatApplication.setRepId(null);
        maatApplication.setUsn(crimeApplyResponse.getReference());
        maatApplication.setSolicitorAdminEmail(crimeApplyResponse.getProviderDetails().getProviderEmail());


        maatApplication.setDateStamp(crimeApplyResponse.getDateStamp()); // TODO

        maatApplication.setApplicant(mapToApplicant(crimeApplyResponse));

        return maatApplication;
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
}
