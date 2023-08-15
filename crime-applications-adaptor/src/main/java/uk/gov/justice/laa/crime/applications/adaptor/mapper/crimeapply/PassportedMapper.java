package uk.gov.justice.laa.crime.applications.adaptor.mapper.crimeapply;

import uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.common.Passported;
import uk.gov.justice.laa.crime.applications.adaptor.model.criminalapplicationsdatastore.MaatApplicationExternal;

import javax.validation.constraints.NotNull;

class PassportedMapper {

    private static final boolean INCOME_SUPPORT_DEFAULT_FALSE = false;
    private static final boolean STATE_PENSION_DEFAULT_FALSE = false;
    private static final boolean JOB_SEEKER_DEFAULT_FALSE = false;
    private static final boolean EMPLOYMENT_SUPPORT_DEFAULT_FALSE = false;
    private static final boolean CLAIMED_BY_PARTNER_DEFAULT_FALSE = false;
    private static final Passported.WhoDwpChecked WHO_DWP_CHECKED_DEFAULT_NULL = null;

    @NotNull
    Passported map(MaatApplicationExternal crimeApplyMaatApplicationExternal) {

        Passported passported = new Passported();

        passported.setBenefitIncomeSupport(INCOME_SUPPORT_DEFAULT_FALSE);
        passported.setBenefitGuaranteedStatePension(STATE_PENSION_DEFAULT_FALSE);
        passported.setJobSeeker(JOB_SEEKER_DEFAULT_FALSE);
        passported.setBenefitEmploymentSupport(EMPLOYMENT_SUPPORT_DEFAULT_FALSE);
        passported.setBenefitClaimedByPartner(CLAIMED_BY_PARTNER_DEFAULT_FALSE);
        passported.setWhoDwpChecked(WHO_DWP_CHECKED_DEFAULT_NULL);

        if(!crimeApplyMaatApplication.getMeansPassport().isEmpty()){
            passported.setMeansPassport(String.valueOf(crimeApplyMaatApplication.getMeansPassport().get(0)));
        }

        return passported;
    }
}
