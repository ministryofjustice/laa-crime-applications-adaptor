package uk.gov.justice.laa.crime.applications.adaptor.mapper.crimeapply;

import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import uk.gov.justice.laa.crime.applications.adaptor.factory.PoundSterling;
import uk.gov.justice.laa.crime.model.common.crimeapplication.common.CapitalOther;
import uk.gov.justice.laa.crime.model.common.criminalapplicationsdatastore.general.Investment;

@Slf4j
public class InvestmentMapper {

  private static final String REQUIRED_CAPITAL_TYPE_NULL = "Required capital type is null.";
  private static final String UNKNOWN_OWNERSHIP_TYPE = "Unknown owner type: {}";
  private static final String NULL_OWNERSHIP_TYPE = "Required ownership type is null.";

  CapitalOther map(Investment investment) {
    CapitalOther capitalOther = new CapitalOther();

    capitalOther.setCapitalType(mapInvestmentTypeToCapitalType(investment.getInvestmentType()));
    capitalOther.setAssetAmount(PoundSterling.ofPennies(investment.getValue()).toPounds());
    capitalOther.setAccountOwner(mapInvestmentOwnerToAccountOwner(investment.getOwnershipType()));
    capitalOther.setOtherDescription(investment.getDescription());
    return capitalOther;
  }

  CapitalOther.CapitalType mapInvestmentTypeToCapitalType(
      Investment.InvestmentType investmentType) {
    if (Objects.isNull(investmentType)) {
      log.debug(REQUIRED_CAPITAL_TYPE_NULL);
      return CapitalOther.CapitalType.INVESTMENT;
    }

    switch (investmentType) {
      case PEP:
        return CapitalOther.CapitalType.PEPS;
      case SHARE:
        return CapitalOther.CapitalType.SHARES;
      case STOCK:
        return CapitalOther.CapitalType.STOCKS;
      case SHARE_ISA:
        return CapitalOther.CapitalType.SHARE_ISA;
      case UNIT_TRUST:
        return CapitalOther.CapitalType.OTHER;
      default:
        return CapitalOther.CapitalType.INVESTMENT;
    }
  }

  CapitalOther.AccountOwner mapInvestmentOwnerToAccountOwner(
      Investment.OwnershipType ownershipType) {
    if (Objects.isNull(ownershipType)) {
      log.debug(NULL_OWNERSHIP_TYPE);
      return null;
    }

    switch (ownershipType) {
      case PARTNER:
        return CapitalOther.AccountOwner.PARTNER;
      case APPLICANT:
        return CapitalOther.AccountOwner.APPLICANT;
      case APPLICANT_AND_PARTNER:
        return CapitalOther.AccountOwner.JOINT;
      default:
        log.debug(UNKNOWN_OWNERSHIP_TYPE, ownershipType);
        return null;
    }
  }
}
