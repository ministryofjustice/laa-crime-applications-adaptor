package uk.gov.justice.laa.crime.applications.adaptor.mapper.crimeapply;

import uk.gov.justice.laa.crime.applications.adaptor.factory.PoundSterling;
import uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.common.CapitalOther;

public class TrustFundMapper {

  CapitalOther map(Integer trustFundAmount) {
    CapitalOther trustFund = new CapitalOther();
    trustFund.setCapitalType(CapitalOther.CapitalType.TRUST_FUND);
    trustFund.setAssetAmount(PoundSterling.ofPennies(trustFundAmount).toPounds());
    return trustFund;
  }
}
