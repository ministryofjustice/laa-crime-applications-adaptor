package uk.gov.justice.laa.crime.applications.adaptor.mapper.crimeapply;

import lombok.extern.slf4j.Slf4j;
import uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.common.Address;
import uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.common.ThirdPartyOwner;
import uk.gov.justice.laa.crime.applications.adaptor.model.criminalapplicationsdatastore.general.Property;
import uk.gov.justice.laa.crime.applications.adaptor.model.criminalapplicationsdatastore.general.PropertyOwner;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

@Slf4j
public class PropertyMapper {

    private static final String NO_PROPERTY_TYPE_FOUND = "No property type found.";
    private static final String BEDROOMS_ERROR = "There was an issue obtaining number of bedrooms.";
    private static final String SIX_PLUS_BEDROOMS = "6+";
    private static final String FAILED_TO_READ_ADDRESS_OBJECT = "Failed to read address object.";
    private static final String NO_PROPERTY_OWNER_TYPE_FOUND = "No property owner type found.";
    private static final String NULL_OWNERSHIP_TYPE = "Required ownership type is null.";

    uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.common.Property map(Property crimeApplyDataStoreProperty) {
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

    Address mapAddress(Object crimeApplyAddress) {
        if (Objects.nonNull(crimeApplyAddress)) {
            try {
                LinkedHashMap mappedAddress = (LinkedHashMap) crimeApplyAddress;
                Address address = new Address();
                address.setLine1(mappedAddress.get("address_line_one").toString());
                address.setLine2(mappedAddress.get("address_line_two").toString());
                address.setCity(mappedAddress.get("city").toString());
                address.setCountry(mappedAddress.get("country").toString());
                address.setPostCode(mappedAddress.get("postcode").toString());
                return address;
            } catch (Exception e) {
                log.error(FAILED_TO_READ_ADDRESS_OBJECT);
            }
        }
        return null;
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

}
