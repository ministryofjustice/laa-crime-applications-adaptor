package uk.gov.justice.laa.crime.applications.adaptor.mapper.crimeapply;

import lombok.extern.slf4j.Slf4j;
import uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.common.CapitalOther;
import uk.gov.justice.laa.crime.applications.adaptor.model.criminalapplicationsdatastore.general.Investment;

import java.math.BigDecimal;
import java.util.Objects;

@Slf4j
public class InvestmentMapper {

    private static final String REQUIRED_CAPITAL_TYPE_NULL = "Required capital type is null.";
    private static final String UNKNOWN_OWNERSHIP_TYPE = "Unknown owner type: {}";
    private static final String NULL_OWNERSHIP_TYPE = "Required ownership type is null.";

    CapitalOther map(Investment investment) {
        CapitalOther capitalOther = new CapitalOther();

        capitalOther.setCapitalType(mapInvestmentTypeToCapitalType(investment.getInvestmentType()));
        capitalOther.setAssetAmount(BigDecimal.valueOf(investment.getValue()));
        capitalOther.setAccountOwner(mapInvestmentOwnerToAccountOwner(investment.getOwnershipType()));
        capitalOther.setOtherDescription(investment.getDescription());
        return capitalOther;
    }

    CapitalOther.CapitalType mapInvestmentTypeToCapitalType(Investment.InvestmentType investmentType) {
        if (Objects.isNull(investmentType)) {
            log.debug(REQUIRED_CAPITAL_TYPE_NULL);
            return CapitalOther.CapitalType.INVESTMENT;
        } else if (Investment.InvestmentType.PEP.equals(investmentType)) {
            return CapitalOther.CapitalType.PEPS;
        } else if (Investment.InvestmentType.SHARE.equals(investmentType)) {
            return CapitalOther.CapitalType.SHARES;
        } else if (Investment.InvestmentType.STOCK.equals(investmentType)) {
            return CapitalOther.CapitalType.STOCKS;
        } else if (Investment.InvestmentType.SHARE_ISA.equals(investmentType)) {
            return CapitalOther.CapitalType.SHARE_ISA;
        } else if (Investment.InvestmentType.UNIT_TRUST.equals(investmentType)) {
            return CapitalOther.CapitalType.OTHER;
        } else {
            return CapitalOther.CapitalType.INVESTMENT;
        }
    }

    CapitalOther.AccountOwner mapInvestmentOwnerToAccountOwner(Investment.OwnershipType ownershipType) {
        if (Objects.isNull(ownershipType)) {
            log.debug(NULL_OWNERSHIP_TYPE);
            return null;
        } else if (Investment.OwnershipType.PARTNER.equals(ownershipType)) {
            return CapitalOther.AccountOwner.PARTNER;
        } else if (Investment.OwnershipType.APPLICANT.equals(ownershipType)) {
            return CapitalOther.AccountOwner.APPLICANT;
        } else if (Investment.OwnershipType.APPLICANT_AND_PARTNER.equals(ownershipType)) {
            return CapitalOther.AccountOwner.JOINT;
        } else {
            log.debug(UNKNOWN_OWNERSHIP_TYPE, ownershipType);
            return null;
        }
    }

}
