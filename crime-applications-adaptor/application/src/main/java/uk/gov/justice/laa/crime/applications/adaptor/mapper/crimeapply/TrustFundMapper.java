package uk.gov.justice.laa.crime.applications.adaptor.mapper.crimeapply;

import java.math.BigDecimal;
import uk.gov.justice.laa.crime.model.common.crimeapplication.common.CapitalOther;

public class TrustFundMapper {

  CapitalOther map(Integer trustFundAmount) {
    CapitalOther trustFund = new CapitalOther();
    trustFund.setCapitalType(CapitalOther.CapitalType.TRUST_FUND);
    trustFund.setAssetAmount(BigDecimal.valueOf(trustFundAmount));
    return trustFund;
  }
}
