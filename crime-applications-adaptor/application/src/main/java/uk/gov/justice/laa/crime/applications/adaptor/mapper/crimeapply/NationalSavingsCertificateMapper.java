package uk.gov.justice.laa.crime.applications.adaptor.mapper.crimeapply;

import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import uk.gov.justice.laa.crime.applications.adaptor.factory.PoundSterling;
import uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.common.CapitalOther;
import uk.gov.justice.laa.crime.applications.adaptor.model.criminalapplicationsdatastore.general.NationalSavingsCertificate;

@Slf4j
public class NationalSavingsCertificateMapper {

  private static final String NULL_OWNERSHIP_TYPE = "Required ownership type is null.";
  private static final String UNKNOWN_OWNERSHIP_TYPE = "Unknown owner type: {}";

  CapitalOther map(NationalSavingsCertificate certificate) {
    CapitalOther nationalSavingsCertificate = new CapitalOther();

    nationalSavingsCertificate.setCapitalType(CapitalOther.CapitalType.PREMIUM_BONDS);
    nationalSavingsCertificate.setAssetAmount(PoundSterling.ofPennies(certificate.getValue()).toPounds());
    nationalSavingsCertificate.setAccountOwner(
        mapOwnershipTypeToAccountOwner(certificate.getOwnershipType()));

    return nationalSavingsCertificate;
  }

  CapitalOther.AccountOwner mapOwnershipTypeToAccountOwner(
      NationalSavingsCertificate.OwnershipType ownershipType) {
    if (Objects.isNull(ownershipType)) {
      log.debug(NULL_OWNERSHIP_TYPE);
      return null;
    }

    switch (ownershipType) {
      case APPLICANT:
        return CapitalOther.AccountOwner.APPLICANT;
      case PARTNER:
        return CapitalOther.AccountOwner.PARTNER;
      default:
        log.debug(UNKNOWN_OWNERSHIP_TYPE, ownershipType);
        return null;
    }
  }
}
