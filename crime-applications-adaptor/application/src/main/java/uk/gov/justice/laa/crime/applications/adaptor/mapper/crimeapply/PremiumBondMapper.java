package uk.gov.justice.laa.crime.applications.adaptor.mapper.crimeapply;

import uk.gov.justice.laa.crime.applications.adaptor.factory.PoundSterling;
import uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.common.CapitalOther;

public class PremiumBondMapper {

  CapitalOther map(Integer premiumBondValue) {
    CapitalOther premiumBond = new CapitalOther();
    premiumBond.setCapitalType(CapitalOther.CapitalType.PREMIUM_BONDS);
    premiumBond.setAssetAmount(PoundSterling.ofPennies(premiumBondValue).toPounds());
    return premiumBond;
  }
}
