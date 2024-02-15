package uk.gov.justice.laa.crime.applications.adaptor.mapper.crimeapply;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.MaatApplicationInternal;
import uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.common.MagistrateCourt;
import uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.common.Supplier;
import uk.gov.justice.laa.crime.applications.adaptor.model.criminalapplicationsdatastore.MaatApplicationExternal;
import uk.gov.justice.laa.crime.applications.adaptor.model.criminalapplicationsdatastore.general.Provider;
import uk.gov.justice.laa.crime.applications.adaptor.util.DateTimeUtils;

import java.math.BigDecimal;
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
    private final InitialMeansAssessmentMapper initialMeansAssessmentMapper = new InitialMeansAssessmentMapper();
    private final FullMeansAssessmentMapper fullMeansAssessmentMapper = new FullMeansAssessmentMapper();

    public MaatApplicationInternal mapToCrimeApplication(MaatApplicationExternal crimeApplyResponse) {
        MaatApplicationInternal maatApplicationInternal = new MaatApplicationInternal();

        maatApplicationInternal.setStatusReason(STATUS_REASON_DEFAULT_CURRENT);
        maatApplicationInternal.setSolicitorName(mapSolicitorName(crimeApplyResponse.getProviderDetails()));
        maatApplicationInternal.setApplicationType(String.valueOf(crimeApplyResponse.getApplicationType()));
        maatApplicationInternal.setCaseDetails(caseDetailsMapper.map(crimeApplyResponse.getCaseDetails()));
        maatApplicationInternal.setMagsCourt(mapMagistrateCourt(crimeApplyResponse.getCaseDetails()));
        maatApplicationInternal.setUsn(mapUsn(crimeApplyResponse));
        maatApplicationInternal.setSolicitorAdminEmail(mapSolicitorAdminEmail(crimeApplyResponse.getProviderDetails()));
        maatApplicationInternal.setCourtCustody(COURT_CUSTODY_DEFAULT_FALSE);
        maatApplicationInternal.setDateCreated(DateTimeUtils.dateToString(DateTimeUtils.toDate(crimeApplyResponse.getSubmittedAt())));
        maatApplicationInternal.setDateStamp(DateTimeUtils.dateToString(DateTimeUtils.toDate(crimeApplyResponse.getDateStamp())));
        maatApplicationInternal.setDateOfSignature(DateTimeUtils.dateToString(DateTimeUtils.toDate(crimeApplyResponse.getDeclarationSignedAt())));
        maatApplicationInternal.setHearingDate(mapHearingDate(crimeApplyResponse.getCaseDetails()));
        maatApplicationInternal.setApplicant(applicantMapper.map(crimeApplyResponse));
        maatApplicationInternal.setSupplier(mapSupplier(crimeApplyResponse.getProviderDetails()));
        maatApplicationInternal.setPassported(passportedMapper.map(crimeApplyResponse));
        maatApplicationInternal.setIojBypass(crimeApplyResponse.getIojBypass());
        maatApplicationInternal.setInitialMeansAssessment(initialMeansAssessmentMapper.map(crimeApplyResponse.getMeansDetails().getIncomeDetails()));
        maatApplicationInternal.setFullMeansAssessment(fullMeansAssessmentMapper.map(crimeApplyResponse.getMeansDetails().getOutgoingsDetails()));

        return maatApplicationInternal;
    }

    private String mapHearingDate(uk.gov.justice.laa.crime.applications.adaptor.model.criminalapplicationsdatastore.
                                          CaseDetails crimeApplyCaseDetails) {
        if (crimeApplyCaseDetails == null) {
            return null;
        }

        return crimeApplyCaseDetails.getHearingDate();
    }

    private BigDecimal mapUsn(MaatApplicationExternal crimeApplyResponse) {
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
