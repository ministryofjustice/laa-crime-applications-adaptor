package uk.gov.justice.laa.crime.applications.adaptor.mapper.crimeapply;

import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.CrimeApplication;
import uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.common.InterestOfJustice;
import uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.common.MagistrateCourt;
import uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.common.Supplier;
import uk.gov.justice.laa.crime.applications.adaptor.model.criminalapplicationsdatastore.MaatApplication;
import uk.gov.justice.laa.crime.applications.adaptor.model.criminalapplicationsdatastore.general.Provider;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

/**
 * The responsibility of this class is to map from a
 * "Criminal Applications Datastore" response
 * to a
 * "Crime Applications Adaptor" MAAT Application (which should be structured like a MAAT ApplicationDTO)
 */
@Component
public class CrimeApplyMapper {

    private static final String STATUS_REASON_DEFAULT_CURRENT = "current";
    private static final boolean COURT_CUSTODY_DEFAULT_FALSE = false;

    private final CaseDetailsMapper caseDetailsMapper = new CaseDetailsMapper();
    private final ApplicantMapper applicantMapper = new ApplicantMapper();
    private final PassportedMapper passportedMapper = new PassportedMapper();

    public CrimeApplication mapToCrimeApplication(MaatApplication crimeApplyResponse) {
        CrimeApplication crimeApplication = new CrimeApplication();

        crimeApplication.setStatusReason(STATUS_REASON_DEFAULT_CURRENT);
        crimeApplication.setSolicitorName(mapSolicitorName(crimeApplyResponse.getProviderDetails()));
        crimeApplication.setApplicationType(String.valueOf(crimeApplyResponse.getApplicationType()));
        crimeApplication.setCaseDetails(caseDetailsMapper.map(crimeApplyResponse.getCaseDetails()));
        crimeApplication.setMagsCourt(mapMagistrateCourt(crimeApplyResponse.getCaseDetails()));
        crimeApplication.setUsn(mapUsn(crimeApplyResponse));
        crimeApplication.setSolicitorAdminEmail(mapSolicitorAdminEmail(crimeApplyResponse.getProviderDetails()));
        crimeApplication.setCourtCustody(COURT_CUSTODY_DEFAULT_FALSE);
        crimeApplication.setDateCreated(crimeApplyResponse.getSubmittedAt());
        crimeApplication.setDateStamp(crimeApplyResponse.getDateStamp());
        crimeApplication.setDateOfSignature(crimeApplyResponse.getDeclarationSignedAt());
        crimeApplication.setHearingDate(mapHearingDate(crimeApplyResponse.getCaseDetails()));
        crimeApplication.setApplicant(applicantMapper.map(crimeApplyResponse.getClientDetails()));
        crimeApplication.setSupplier(mapSupplier(crimeApplyResponse.getProviderDetails()));
        crimeApplication.setPassported(passportedMapper.map(crimeApplyResponse));
        crimeApplication.setIojBypass(crimeApplyResponse.getIojBypass());

        return crimeApplication;
    }

    private String mapHearingDate(uk.gov.justice.laa.crime.applications.adaptor.model.criminalapplicationsdatastore.
                                          CaseDetails crimeApplyCaseDetails) {
        if (crimeApplyCaseDetails == null) {
            return null;
        }

        return crimeApplyCaseDetails.getHearingDate();
    }

    private BigDecimal mapUsn(MaatApplication crimeApplyResponse) {
        return BigDecimal.valueOf(crimeApplyResponse.getReference());
    }

    private String mapSolicitorAdminEmail(Provider crimeApplyProviderDetails) {
        if (crimeApplyProviderDetails == null) {
            return null;
        }
        return crimeApplyProviderDetails.getProviderEmail();
    }

    private MagistrateCourt mapMagistrateCourt(uk.gov.justice.laa.crime.applications.adaptor.model.criminalapplicationsdatastore.
                                                       CaseDetails crimeApplyCaseDetails) {
        MagistrateCourt magistrateCourt = new MagistrateCourt();

        if (crimeApplyCaseDetails == null) {
            return magistrateCourt;
        }

        magistrateCourt.setCourt(crimeApplyCaseDetails.getHearingCourtName());
        return magistrateCourt;
    }

    private Supplier mapSupplier(Provider crimeApplyProviderDetails) {
        Supplier supplier = new Supplier();

        if (crimeApplyProviderDetails == null) {
            return supplier;
        }

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
}
