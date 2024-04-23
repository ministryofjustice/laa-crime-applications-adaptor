package uk.gov.justice.laa.crime.applications.adaptor.mapper.crimeapply;

import java.math.BigDecimal;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import uk.gov.justice.laa.crime.model.common.crimeapplication.common.CapitalOther;
import uk.gov.justice.laa.crime.model.common.criminalapplicationsdatastore.general.Saving;

@Slf4j
public class SavingMapper {

  private static final String NULL_OWNERSHIP_TYPE = "Required ownership type is null.";
  private static final String UNKNOWN_OWNERSHIP_TYPE = "Unknown owner type: {}";
  private static final String REQUIRED_CAPITAL_TYPE_NULL = "Required capital type is null.";

  CapitalOther map(Saving saving) {
    CapitalOther capitalOther = new CapitalOther();

    capitalOther.setCapitalType(mapSavingsTypeToCapitalType(saving.getSavingType()));
    capitalOther.setAssetAmount(BigDecimal.valueOf(saving.getAccountBalance()));
    capitalOther.setBankName(saving.getProviderName());
    capitalOther.setBranchSortCode(saving.getSortCode());
    capitalOther.setAccountOwner(mapSavingOwnershipTypeToAccountOwner(saving.getOwnershipType()));

    return capitalOther;
  }

  CapitalOther.CapitalType mapSavingsTypeToCapitalType(Saving.SavingType savingType) {
    if (Objects.isNull(savingType)) {
      log.debug(REQUIRED_CAPITAL_TYPE_NULL);
      return CapitalOther.CapitalType.SAVINGS;
    } else if (Saving.SavingType.CASH_ISA.equals(savingType)) {
      return CapitalOther.CapitalType.CASH_ISA;
    } else {
      return CapitalOther.CapitalType.SAVINGS;
    }
  }

  CapitalOther.AccountOwner mapSavingOwnershipTypeToAccountOwner(
      Saving.OwnershipType ownershipType) {
    if (Objects.isNull(ownershipType)) {
      log.debug(NULL_OWNERSHIP_TYPE);
      return null;
    }

    switch (ownershipType) {
      case APPLICANT:
        return CapitalOther.AccountOwner.APPLICANT;
      case PARTNER:
        return CapitalOther.AccountOwner.PARTNER;
      case APPLICANT_AND_PARTNER:
        return CapitalOther.AccountOwner.JOINT;
      default:
        log.debug(UNKNOWN_OWNERSHIP_TYPE, ownershipType);
        return null;
    }
  }
}
