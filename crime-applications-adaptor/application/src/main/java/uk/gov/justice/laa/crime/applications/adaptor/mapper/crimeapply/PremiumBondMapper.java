package uk.gov.justice.laa.crime.applications.adaptor.mapper.crimeapply;

import java.math.BigDecimal;
import uk.gov.justice.laa.crime.model.common.crimeapplicationsadaptor.common.CapitalOther;

public class PremiumBondMapper {

  CapitalOther map(Integer premiumBondValue) {
    CapitalOther premiumBond = new CapitalOther();
    premiumBond.setCapitalType(CapitalOther.CapitalType.PREMIUM_BONDS);
    premiumBond.setAssetAmount(BigDecimal.valueOf(premiumBondValue));
    return premiumBond;
  }
}
