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
    private static final String NULL_OWNERSHIP_TYPE = "Required ownership type is null.";
    private static final String UNKNOWN_OWNERSHIP_TYPE = "Unknown owner type: {}";
    private static final String REQUIRED_CAPITAL_TYPE_NULL = "Required capital type is null.";
    private static final String NO_PROPERTY_TYPE_FOUND = "No property type found.";
    private static final String BEDROOMS_ERROR = "There was an issue obtaining number of bedrooms.";
    private static final String SIX_PLUS_BEDROOMS = "6+";
    private static final String NO_PROPERTY_OWNER_TYPE_FOUND = "No property owner type found.";

    CapitalEquity map(MaatApplicationExternal crimeApplyResponse) {
        CapitalEquity capitalEquity = new CapitalEquity();

        mapPropertiesToCapitalEquity(crimeApplyResponse, capitalEquity);
        mapOtherCapitalToCapitalEquity(crimeApplyResponse, capitalEquity);

        return capitalEquity;
    }

    void mapOtherCapitalToCapitalEquity(MaatApplicationExternal crimeApplyResponse, CapitalEquity capitalEquity) {
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

    void mapTrustFundToCapitalEquity(Object trustFundAmountHeld, CapitalEquity capitalEquity) {
        CapitalOther trustFund = new CapitalOther();

        trustFund.setCapitalType(CapitalOther.CapitalType.TRUST_FUND);
        Integer amountHeld = (Integer) trustFundAmountHeld;
        trustFund.setAssetAmount(BigDecimal.valueOf(amountHeld));

        capitalEquity.getCapital().add(trustFund);
    }

    void mapPremiumBondsToCapitalEquity(Object premiumBondsTotalValue, CapitalEquity capitalEquity) {
        CapitalOther premiumBonds = new CapitalOther();

        premiumBonds.setCapitalType(CapitalOther.CapitalType.PREMIUM_BONDS);
        Integer amountHeld = (Integer) premiumBondsTotalValue;
        premiumBonds.setAssetAmount(BigDecimal.valueOf(amountHeld));

        capitalEquity.getCapital().add(premiumBonds);
    }

    void mapNationalSavingsCertificatesToCapitalEquity(List<NationalSavingsCertificate> nationalSavingsCertificates, CapitalEquity capitalEquity) {
        for (NationalSavingsCertificate certificate : nationalSavingsCertificates) {
            if (Objects.nonNull(certificate)) {
                CapitalOther nationalSavingsCertificate = new CapitalOther();

                nationalSavingsCertificate.setCapitalType(CapitalOther.CapitalType.PREMIUM_BONDS);
                nationalSavingsCertificate.setAssetAmount(BigDecimal.valueOf(certificate.getValue()));
                nationalSavingsCertificate.setAccountOwner(mapOwnershipTypeToAccountOwner(certificate.getOwnershipType()));

                capitalEquity.getCapital().add(nationalSavingsCertificate);
            }
        }
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

    void mapSavingsToCapitalEquity(List<Saving> savings, CapitalEquity capitalEquity) {
        for (Saving saving : savings) {
            CapitalOther capitalOther = new CapitalOther();

            capitalOther.setCapitalType(mapSavingsTypeToCapitalType(saving));
            capitalOther.setAssetAmount(BigDecimal.valueOf(saving.getAccountBalance()));
            capitalOther.setBankName(saving.getProviderName());
            capitalOther.setBranchSortCode(saving.getSortCode());
            capitalOther.setAccountOwner(mapSavingOwnershipTypeToAccountOwner(saving.getOwnershipType()));

            capitalEquity.getCapital().add(capitalOther);
        }
    }

    CapitalOther.AccountOwner mapSavingOwnershipTypeToAccountOwner(Saving.OwnershipType ownershipType) {
        if (Objects.isNull(ownershipType)) {
            log.debug(NULL_OWNERSHIP_TYPE);
            return null;
        } else if (Saving.OwnershipType.APPLICANT.equals(ownershipType)) {
            return CapitalOther.AccountOwner.APPLICANT;
        } else if (Saving.OwnershipType.PARTNER.equals(ownershipType)) {
            return CapitalOther.AccountOwner.PARTNER;
        } else if (Saving.OwnershipType.APPLICANT_AND_PARTNER.equals(ownershipType)) {
            return CapitalOther.AccountOwner.JOINT;
        } else {
            log.debug(UNKNOWN_OWNERSHIP_TYPE, ownershipType);
            return null;
        }
    }

    CapitalOther.CapitalType mapSavingsTypeToCapitalType(Saving saving) {
        if (Objects.isNull(saving)) {
            log.debug(REQUIRED_CAPITAL_TYPE_NULL);
            return CapitalOther.CapitalType.SAVINGS;
        } else if (Saving.SavingType.CASH_ISA.equals(saving.getSavingType())) {
            return CapitalOther.CapitalType.CASH_ISA;
        } else {
            return CapitalOther.CapitalType.SAVINGS;
        }
    }

    void mapInvestmentsToCapitalEquity(List<Investment> investments, CapitalEquity capitalEquity) {
        for (Investment investment : investments) {
            CapitalOther capitalOther = new CapitalOther();

            capitalOther.setCapitalType(mapInvestmentTypeToCapitalType(investment.getInvestmentType()));
            capitalOther.setAssetAmount(BigDecimal.valueOf(investment.getValue()));
            capitalOther.setAccountOwner(mapInvestmentOwnerToAccountOwner(investment.getOwnershipType()));
            capitalOther.setOtherDescription(investment.getDescription());

            capitalEquity.getCapital().add(capitalOther);
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

        property.setPropertyType(mapPropertyType(crimeApplyDataStoreProperty.getPropertyType(), crimeApplyDataStoreProperty.getHouseType()));
        property.setBedrooms(getBedrooms(crimeApplyDataStoreProperty.getBedrooms()));
        property.setDeclaredMortgageCharges(BigDecimal.valueOf(crimeApplyDataStoreProperty.getOutstandingMortgage()));
        property.setDeclaredMarketValue(BigDecimal.valueOf(crimeApplyDataStoreProperty.getValue()));
        property.setPercentageOwnedApplicant(crimeApplyDataStoreProperty.getPercentageApplicantOwned());
        property.setPercentageOwnedPartner(getPercentagePartnerOwned(crimeApplyDataStoreProperty.getPercentagePartnerOwned()));
        property.setAddress(mapAddress(crimeApplyDataStoreProperty.getAddress()));

        if (hasThirdPartyOwners(crimeApplyDataStoreProperty.getPropertyOwners())) {
            mapThirdPartyOwners(property, crimeApplyDataStoreProperty.getPropertyOwners());
        }

        return property;
    }

    BigDecimal getPercentagePartnerOwned(Object percentagePartnerOwned) {
        if (Objects.nonNull(percentagePartnerOwned)) {
            if (percentagePartnerOwned instanceof Integer) {
                return BigDecimal.valueOf((Integer) percentagePartnerOwned);
            } else if (percentagePartnerOwned instanceof BigDecimal) {
                return (BigDecimal) percentagePartnerOwned;
            } else {
                return BigDecimal.ZERO;
            }
        } else {
            return BigDecimal.ZERO;
        }
    }

    String getBedrooms(Object bedrooms) {
        if (Objects.nonNull(bedrooms) && bedrooms instanceof Integer) {
            Integer bedroomsValue = (Integer) bedrooms;
            if (bedroomsValue < 7) {
                return bedroomsValue.toString();
            } else {
                return SIX_PLUS_BEDROOMS;
            }
        } else {
            log.debug(BEDROOMS_ERROR);
            return null;
        }
    }

    boolean hasThirdPartyOwners(List<PropertyOwner> thirdPartyPropertyOwners) {
        return Objects.nonNull(thirdPartyPropertyOwners) && !thirdPartyPropertyOwners.isEmpty();
    }

    void mapThirdPartyOwners(uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.common.Property property, List<PropertyOwner> propertyOwners) {
        for (PropertyOwner propertyOwner: propertyOwners) {
            if (Objects.nonNull(propertyOwner)) {
                ThirdPartyOwner thirdPartyOwner = new ThirdPartyOwner();

                thirdPartyOwner.setOwnerRelation(mapOwnerRelation(propertyOwner.getRelationship()));
                thirdPartyOwner.setOtherRelation(mapOtherRelation(propertyOwner.getOtherRelationship()));
                thirdPartyOwner.setOwnerName(propertyOwner.getName());

                property.getThirdPartyOwner().add(thirdPartyOwner);
            }
        }
    }

    String mapOtherRelation(Object otherRelationship) {
        if (Objects.nonNull(otherRelationship) && otherRelationship instanceof String) {
            return (String) otherRelationship;
        } else {
            return null;
        }
    }

    ThirdPartyOwner.OwnerRelation mapOwnerRelation(PropertyOwner.Relationship relationship) {
        if (Objects.nonNull(relationship)) {
            if (PropertyOwner.Relationship.OTHER.equals(relationship)) {
                return ThirdPartyOwner.OwnerRelation.OTHER;
            } else if (PropertyOwner.Relationship.EX_PARTNER.equals(relationship)) {
                return ThirdPartyOwner.OwnerRelation.EX_PARTNER;
            } else if (PropertyOwner.Relationship.BUSINESS_ASSOCIATES.equals(relationship)) {
                return ThirdPartyOwner.OwnerRelation.BUSINESS;
            } else if (PropertyOwner.Relationship.FAMILY_MEMBERS.equals(relationship)) {
                return ThirdPartyOwner.OwnerRelation.FAMILY;
            } else if (PropertyOwner.Relationship.FRIENDS.equals(relationship)) {
                return ThirdPartyOwner.OwnerRelation.FRIENDS;
            } else if (PropertyOwner.Relationship.HOUSE_BUILDER.equals(relationship)) {
                return ThirdPartyOwner.OwnerRelation.BUILDER;
            } else if (PropertyOwner.Relationship.HOUSING_ASSOCIATION.equals(relationship)) {
                return ThirdPartyOwner.OwnerRelation.HOUSING_ASSOC;
            } else if (PropertyOwner.Relationship.LOCAL_AUTHORITY.equals(relationship)) {
                return ThirdPartyOwner.OwnerRelation.LOCAL_AUTH;
            } else if (PropertyOwner.Relationship.PROPERTY_COMPANY.equals(relationship)) {
                return ThirdPartyOwner.OwnerRelation.PPROPERTY_CO;
            } else if (PropertyOwner.Relationship.PARTNER_WITH_A_CONTRARY_INTEREST.equals(relationship)) {
                return ThirdPartyOwner.OwnerRelation.PARTNER_CONT;
            }
        } else {
            log.debug(NULL_OWNERSHIP_TYPE);
            return null;
        }
        log.debug(NO_PROPERTY_OWNER_TYPE_FOUND);
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

    uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.common.Property.PropertyType mapPropertyType(Property.PropertyType crimeApplyDataStorePropertyType, Property.HouseType houseType) {
        if (Objects.nonNull(crimeApplyDataStorePropertyType)) {
            if (Property.PropertyType.LAND.equals(crimeApplyDataStorePropertyType)) {
                return uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.common.Property.PropertyType.LAND;
            } else if (Property.PropertyType.COMMERCIAL.equals(crimeApplyDataStorePropertyType)) {
                return uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.common.Property.PropertyType.COMMERCIAL;
            } else if (Property.PropertyType.RESIDENTIAL.equals(crimeApplyDataStorePropertyType)) {
                if (Objects.nonNull(houseType)) {
                    return getHouseType(houseType);
                }
            }
        }

        if (Objects.nonNull(houseType)) {
            return getHouseType(houseType);
        }

        return null;
    }

    uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.common.Property.PropertyType getHouseType(Property.HouseType houseType) {
        if (Property.HouseType.OTHER.equals(houseType)) {
            return uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.common.Property.PropertyType.OTHER;
        } else if (Property.HouseType.BUNGALOW.equals(houseType)) {
            return uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.common.Property.PropertyType.BUNGALOW;
        } else if (Property.HouseType.DETACHED.equals(houseType)) {
            return uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.common.Property.PropertyType.DETACHED;
        } else if (Property.HouseType.FLAT_OR_MAISONETTE.equals(houseType)) {
            return uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.common.Property.PropertyType.FLAT;
        } else if (Property.HouseType.SEMIDETACHED.equals(houseType)) {
            return uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.common.Property.PropertyType.SEMI;
        } else if (Property.HouseType.TERRACED.equals(houseType)) {
            return uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.common.Property.PropertyType.TERRACE;
        } else {
            log.debug(NO_PROPERTY_TYPE_FOUND);
            return null;
        }
    }
}
