package uk.gov.justice.laa.crime.applications.adaptor.mapper.crimeapply;

import lombok.extern.slf4j.Slf4j;
import uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.common.Address;
import uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.common.CapitalEquity;
import uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.common.CapitalOther;
import uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.common.ThirdPartyOwner;
import uk.gov.justice.laa.crime.applications.adaptor.model.criminalapplicationsdatastore.MaatApplicationExternal;
import uk.gov.justice.laa.crime.applications.adaptor.model.criminalapplicationsdatastore.general.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Slf4j
public class CapitalEquityMapper {

    private static final String IS_HOME_PROPERTY = "yes";

    CapitalEquity map(MaatApplicationExternal crimeApplyResponse) {
        CapitalEquity capitalEquity = new CapitalEquity();

        mapPropertiesToCapitalEquity(crimeApplyResponse, capitalEquity);
        mapOtherCapitalToCapitalEquity(crimeApplyResponse, capitalEquity);

        return capitalEquity;
    }

    private void mapOtherCapitalToCapitalEquity(MaatApplicationExternal crimeApplyResponse, CapitalEquity capitalEquity) {
        if (hasInvestments(crimeApplyResponse)) {
            mapInvestmentsToCapitalEquity(crimeApplyResponse.getMeansDetails().getCapitalDetails().getInvestments(),
                    capitalEquity);
        }

        if (hasSavings(crimeApplyResponse)) {
            mapSavingsToCapitalEquity(crimeApplyResponse.getMeansDetails().getCapitalDetails().getSavings(),
                    capitalEquity);
        }

        if (hasNationalSavingsCertificates(crimeApplyResponse)) {
            mapNationalSavingsCertificatesToCapitalEquity(crimeApplyResponse.getMeansDetails().getCapitalDetails()
                    .getNationalSavingsCertificates(), capitalEquity);
        }

        if (hasPremiumBonds(crimeApplyResponse)) {
            mapPremiumBondsToCapitalEquity(crimeApplyResponse.getMeansDetails().getCapitalDetails()
                    .getPremiumBondsTotalValue(), capitalEquity);
        }

        if (hasTrustFund(crimeApplyResponse)) {
            mapTrustFundToCapitalEquity(crimeApplyResponse.getMeansDetails().getCapitalDetails()
                    .getTrustFundAmountHeld(), capitalEquity);
        }
    }

    private void mapTrustFundToCapitalEquity(Object trustFundAmountHeld, CapitalEquity capitalEquity) {
        CapitalOther trustFund = new CapitalOther();

        trustFund.setCapitalType(CapitalOther.CapitalType.TRUST_FUND);
        Integer amountHeld = (Integer) trustFundAmountHeld;
        trustFund.setAssetAmount(BigDecimal.valueOf(amountHeld));

        capitalEquity.getCapital().add(trustFund);
    }

    private void mapPremiumBondsToCapitalEquity(Object premiumBondsTotalValue, CapitalEquity capitalEquity) {
        CapitalOther premiumBonds = new CapitalOther();

        premiumBonds.setCapitalType(CapitalOther.CapitalType.PREMIUM_BONDS);
        Integer amountHeld = (Integer) premiumBondsTotalValue;
        premiumBonds.setAssetAmount(BigDecimal.valueOf(amountHeld));

        capitalEquity.getCapital().add(premiumBonds);
    }

    private void mapNationalSavingsCertificatesToCapitalEquity(List<NationalSavingsCertificate> nationalSavingsCertificates, CapitalEquity capitalEquity) {
        for (NationalSavingsCertificate certificate : nationalSavingsCertificates) {
            CapitalOther nationalSavingsCertificate = new CapitalOther();

            // TODO
            nationalSavingsCertificate.setCapitalType(CapitalOther.CapitalType.PREMIUM_BONDS);
            nationalSavingsCertificate.setAssetAmount(BigDecimal.valueOf(certificate.getValue()));
            nationalSavingsCertificate.setAccountOwner(mapOwnershipTypeToAccountOwner(certificate.getOwnershipType()));

            capitalEquity.getCapital().add(nationalSavingsCertificate);
        }
    }

    CapitalOther.AccountOwner mapOwnershipTypeToAccountOwner(NationalSavingsCertificate.OwnershipType ownershipType) {
        if (Objects.isNull(ownershipType)) {
            return null;
        } else if (NationalSavingsCertificate.OwnershipType.APPLICANT.equals(ownershipType)) {
            return CapitalOther.AccountOwner.APPLICANT;
        } else if (NationalSavingsCertificate.OwnershipType.PARTNER.equals(ownershipType)) {
            return CapitalOther.AccountOwner.PARTNER;
        } else {
            log.debug("Unknown owner type: {}", ownershipType);
            return null;
        }
    }

    void mapSavingsToCapitalEquity(List<Saving> saving, CapitalEquity capitalEquity) {
        for (Saving s : saving) {
            CapitalOther capitalOther = new CapitalOther();
            capitalOther.setCapitalType(CapitalOther.CapitalType.SAVINGS);
        }
    }

    void mapInvestmentsToCapitalEquity(List<Investment> investments, CapitalEquity capitalEquity) {
        // TODO
    }

    boolean hasTrustFund(MaatApplicationExternal crimeApplyResponse) {
        return Objects.nonNull(crimeApplyResponse.getMeansDetails().getCapitalDetails().getTrustFundAmountHeld())
                && crimeApplyResponse.getMeansDetails().getCapitalDetails().getTrustFundAmountHeld() instanceof Integer;
    }

    boolean hasPremiumBonds(MaatApplicationExternal crimeApplyResponse) {
        return Objects.nonNull(crimeApplyResponse.getMeansDetails().getCapitalDetails().getPremiumBondsTotalValue())
                && crimeApplyResponse.getMeansDetails().getCapitalDetails()
                .getPremiumBondsTotalValue() instanceof Integer;
    }

    boolean hasNationalSavingsCertificates(MaatApplicationExternal crimeApplyResponse) {
        return Objects.nonNull(crimeApplyResponse.getMeansDetails().getCapitalDetails()
                .getNationalSavingsCertificates())
                && !crimeApplyResponse.getMeansDetails().getCapitalDetails().getNationalSavingsCertificates().isEmpty();
    }

    boolean hasSavings(MaatApplicationExternal crimeApplyResponse) {
        return Objects.nonNull(crimeApplyResponse.getMeansDetails().getCapitalDetails().getSavings())
                && !crimeApplyResponse.getMeansDetails().getCapitalDetails().getSavings().isEmpty();
    }

    boolean hasInvestments(MaatApplicationExternal crimeApplyResponse) {
        return Objects.nonNull(crimeApplyResponse.getMeansDetails().getCapitalDetails().getInvestments())
                && !crimeApplyResponse.getMeansDetails().getCapitalDetails().getInvestments().isEmpty();
    }

    boolean hasProperties(MaatApplicationExternal crimeApplyResponse) {
        return Objects.nonNull(crimeApplyResponse.getMeansDetails().getCapitalDetails().getProperties())
                && !crimeApplyResponse.getMeansDetails().getCapitalDetails().getProperties().isEmpty();
    }

    void mapPropertiesToCapitalEquity(MaatApplicationExternal crimeApplyResponse, CapitalEquity capitalEquity) {
        if (hasProperties(crimeApplyResponse)) {
            for (Property crimeApplyDataStoreProperty: crimeApplyResponse.getMeansDetails().getCapitalDetails().getProperties()) {
                if (isHomeProperty(crimeApplyDataStoreProperty)) {
                    mapPropertyToEquity(crimeApplyDataStoreProperty, capitalEquity.getEquity());
                } else {
                    mapPropertyToCapitalProperty(crimeApplyDataStoreProperty, capitalEquity.getCapitalProperty());
                }
            }
        }
    }

    void mapPropertyToCapitalProperty(Property crimeApplyDataStoreProperty, List<uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.common.Property> capitalProperty) {
        uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.common.Property property = mapCrimeApplyDataStorePropertyToMAATProperty(crimeApplyDataStoreProperty);
        capitalProperty.add(property);
    }

    void mapPropertyToEquity(Property crimeApplyDataStoreProperty, List<uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.common.Property> equity) {
        uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.common.Property property = mapCrimeApplyDataStorePropertyToMAATProperty(crimeApplyDataStoreProperty);
        equity.add(property);
    }

    uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.common.Property mapCrimeApplyDataStorePropertyToMAATProperty(Property crimeApplyDataStoreProperty) {
        uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.common.Property property = new uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.common.Property();

        // TODO
        property.setPropertyType(mapPropertyType(crimeApplyDataStoreProperty.getPropertyType()));
        property.setBedrooms(null);
        property.setDeclaredMortgageCharges(null);
        property.setDeclaredMarketValue(null);
        property.setPercentageOwnedApplicant(null);
        property.setPercentageOwnedPartner(null);
        property.setAddress(mapAddress(crimeApplyDataStoreProperty.getAddress()));
        property.setBedrooms(null);

        if (hasThirdPartyOwners(crimeApplyDataStoreProperty.getPropertyOwners())) {
            mapThirdPartyOwners(property, crimeApplyDataStoreProperty.getPropertyOwners());
        }

        return property;
    }

    boolean hasThirdPartyOwners(List<PropertyOwner> thirdPartyPropertyOwners) {
        return Objects.nonNull(thirdPartyPropertyOwners) && !thirdPartyPropertyOwners.isEmpty();
    }

    void mapThirdPartyOwners(uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.common.Property property, List<PropertyOwner> propertyOwners) {
        for (PropertyOwner propertyOwner: propertyOwners) {
            if (Objects.nonNull(propertyOwner)) {
                ThirdPartyOwner thirdPartyOwner = new ThirdPartyOwner();

                // TODO
                thirdPartyOwner.setOwnerRelation(mapOwnerRelation(propertyOwner.getRelationship()));
                thirdPartyOwner.setOtherRelation(mapOtherRelation(propertyOwner.getOtherRelationship()));
                thirdPartyOwner.setOwnerName(null);

                property.getThirdPartyOwner().add(thirdPartyOwner);
            }
        }
    }

    String mapOtherRelation(Object otherRelationship) {
        // TODO
        return null;
    }

    ThirdPartyOwner.OwnerRelation mapOwnerRelation(PropertyOwner.Relationship relationship) {
        // TODO
        return null;
    }

    boolean isHomeProperty(Property crimeApplyDataStoreProperty) {
        return Objects.nonNull(crimeApplyDataStoreProperty)
                && Objects.nonNull(crimeApplyDataStoreProperty.getIsHomeAddress())
                && crimeApplyDataStoreProperty.getIsHomeAddress() instanceof String
                && IS_HOME_PROPERTY.equals(crimeApplyDataStoreProperty.getIsHomeAddress().toString());
    }

    Address mapAddress(Object address) {
        // TODO
        return null;
    }

    uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.common.Property.PropertyType mapPropertyType(Property.PropertyType crimeApplyDataStorePropertyTyoe) {
        // TODO
        return null;
    }
}
