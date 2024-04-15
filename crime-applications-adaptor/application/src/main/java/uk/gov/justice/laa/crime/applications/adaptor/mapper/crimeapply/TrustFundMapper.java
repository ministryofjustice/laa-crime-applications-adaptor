package uk.gov.justice.laa.crime.applications.adaptor.mapper.crimeapply;

import uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.common.CapitalOther;

import java.math.BigDecimal;

public class TrustFundMapper {

    CapitalOther map(Integer trustFundAmount) {
        CapitalOther trustFund = new CapitalOther();
        trustFund.setCapitalType(CapitalOther.CapitalType.TRUST_FUND);
        trustFund.setAssetAmount(BigDecimal.valueOf(trustFundAmount));
        return trustFund;
    }

}
