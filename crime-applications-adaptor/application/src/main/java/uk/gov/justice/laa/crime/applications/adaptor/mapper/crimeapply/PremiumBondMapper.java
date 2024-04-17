package uk.gov.justice.laa.crime.applications.adaptor.mapper.crimeapply;

import uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.common.CapitalOther;

import java.math.BigDecimal;

public class PremiumBondMapper {

    CapitalOther map(Integer premiumBondValue) {
        CapitalOther premiumBond = new CapitalOther();
        premiumBond.setCapitalType(CapitalOther.CapitalType.PREMIUM_BONDS);
        premiumBond.setAssetAmount(BigDecimal.valueOf(premiumBondValue));
        return premiumBond;
    }
}
