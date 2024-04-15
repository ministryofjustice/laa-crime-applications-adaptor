package uk.gov.justice.laa.crime.applications.adaptor.mapper.crimeapply;

import lombok.extern.slf4j.Slf4j;
import uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.common.CapitalOther;
import uk.gov.justice.laa.crime.applications.adaptor.model.criminalapplicationsdatastore.general.NationalSavingsCertificate;

import java.math.BigDecimal;
import java.util.Objects;

@Slf4j
public class NationalSavingsCertificateMapper {

    private static final String NULL_OWNERSHIP_TYPE = "Required ownership type is null.";
    private static final String UNKNOWN_OWNERSHIP_TYPE = "Unknown owner type: {}";

    CapitalOther map(NationalSavingsCertificate certificate) {
        CapitalOther nationalSavingsCertificate = new CapitalOther();

        nationalSavingsCertificate.setCapitalType(CapitalOther.CapitalType.PREMIUM_BONDS);
        nationalSavingsCertificate.setAssetAmount(BigDecimal.valueOf(certificate.getValue()));
        nationalSavingsCertificate.setAccountOwner(mapOwnershipTypeToAccountOwner(certificate.getOwnershipType()));

        return nationalSavingsCertificate;
    }

    CapitalOther.AccountOwner mapOwnershipTypeToAccountOwner(NationalSavingsCertificate.OwnershipType ownershipType) {
        if (Objects.isNull(ownershipType)) {
            log.debug(NULL_OWNERSHIP_TYPE);
            return null;
        } else if (NationalSavingsCertificate.OwnershipType.APPLICANT.equals(ownershipType)) {
            return CapitalOther.AccountOwner.APPLICANT;
        } else if (NationalSavingsCertificate.OwnershipType.PARTNER.equals(ownershipType)) {
            return CapitalOther.AccountOwner.PARTNER;
        } else {
            log.debug(UNKNOWN_OWNERSHIP_TYPE, ownershipType);
            return null;
        }
    }

}
