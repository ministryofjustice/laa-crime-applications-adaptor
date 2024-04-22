package uk.gov.justice.laa.crime.applications.adaptor.mapper.crimeapply;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.common.Address;
import uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.common.ThirdPartyOwner;
import uk.gov.justice.laa.crime.applications.adaptor.model.criminalapplicationsdatastore.general.Property;
import uk.gov.justice.laa.crime.applications.adaptor.model.criminalapplicationsdatastore.general.PropertyOwner;

@Slf4j
public class PropertyMapper {

  private static final String NO_PROPERTY_TYPE_FOUND = "No property type found.";
  private static final String BEDROOMS_ERROR = "There was an issue obtaining number of bedrooms.";
  private static final String SIX_PLUS_BEDROOMS = "6+";
  private static final String FAILED_TO_READ_ADDRESS_OBJECT = "Failed to read address object.";
  private static final String NO_PROPERTY_OWNER_TYPE_FOUND = "No property owner type found.";
  private static final String NULL_OWNERSHIP_TYPE = "Required ownership type is null.";

  uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.common.Property map(
      Property crimeApplyDataStoreProperty) {
    uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.common.Property
        property =
            new uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.common
                .Property();

    property.setPropertyType(
        mapPropertyType(
            crimeApplyDataStoreProperty.getPropertyType(),
            crimeApplyDataStoreProperty.getHouseType()));
    property.setBedrooms(getBedrooms(crimeApplyDataStoreProperty.getBedrooms()));
    property.setDeclaredMortgageCharges(
        BigDecimal.valueOf(crimeApplyDataStoreProperty.getOutstandingMortgage()));
    property.setDeclaredMarketValue(BigDecimal.valueOf(crimeApplyDataStoreProperty.getValue()));
    property.setPercentageOwnedApplicant(crimeApplyDataStoreProperty.getPercentageApplicantOwned());
    property.setPercentageOwnedPartner(
        getPercentagePartnerOwned(crimeApplyDataStoreProperty.getPercentagePartnerOwned()));
    property.setAddress(mapAddress(crimeApplyDataStoreProperty.getAddress()));

    if (hasThirdPartyOwners(crimeApplyDataStoreProperty.getPropertyOwners())) {
      mapThirdPartyOwners(property, crimeApplyDataStoreProperty.getPropertyOwners());
    }

    return property;
  }

  uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.common.Property
          .PropertyType
      mapPropertyType(
          Property.PropertyType crimeApplyDataStorePropertyType, Property.HouseType houseType) {
    if (Objects.nonNull(crimeApplyDataStorePropertyType)) {
      switch (crimeApplyDataStorePropertyType) {
        case LAND:
          return uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.common
              .Property.PropertyType.LAND;
        case COMMERCIAL:
          return uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.common
              .Property.PropertyType.COMMERCIAL;
        case RESIDENTIAL:
          if (Objects.nonNull(houseType)) {
            return getHouseType(houseType);
          }
          break; // This break only executes if houseType is null
      }
    }

    // This check is moved outside to still handle the houseType if crimeApplyDataStorePropertyType
    // is null or not matched.
    if (Objects.nonNull(houseType)) {
      return getHouseType(houseType);
    }

    return null; // Default return if no condition is met
  }

  uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.common.Property
          .PropertyType
      getHouseType(Property.HouseType houseType) {
    switch (houseType) {
      case OTHER:
        return uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.common
            .Property.PropertyType.OTHER;
      case BUNGALOW:
        return uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.common
            .Property.PropertyType.BUNGALOW;
      case DETACHED:
        return uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.common
            .Property.PropertyType.DETACHED;
      case FLAT_OR_MAISONETTE:
        return uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.common
            .Property.PropertyType.FLAT;
      case SEMIDETACHED:
        return uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.common
            .Property.PropertyType.SEMI;
      case TERRACED:
        return uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.common
            .Property.PropertyType.TERRACE;
      default:
        log.debug(NO_PROPERTY_TYPE_FOUND);
        return null;
    }
  }

  private BigDecimal getPercentagePartnerOwned(Object percentagePartnerOwned) {
    if (Objects.nonNull(percentagePartnerOwned)) {
      if (percentagePartnerOwned instanceof Integer integerPercentagePartnerOwned) {
        return BigDecimal.valueOf(integerPercentagePartnerOwned);
      } else if (percentagePartnerOwned instanceof BigDecimal bigDecimalPercentagePartnerOwned) {
        return bigDecimalPercentagePartnerOwned;
      } else {
        return BigDecimal.ZERO;
      }
    } else {
      return BigDecimal.ZERO;
    }
  }

  private String getBedrooms(Object bedrooms) {
    if (Objects.nonNull(bedrooms) && bedrooms instanceof Integer integerBedrooms) {
      if (integerBedrooms < 7) {
        return integerBedrooms.toString();
      } else {
        return SIX_PLUS_BEDROOMS;
      }
    } else {
      log.debug(BEDROOMS_ERROR);
      return null;
    }
  }

  private Address mapAddress(Object crimeApplyAddress) {
    if (Objects.nonNull(crimeApplyAddress)) {
      try {
        LinkedHashMap<String, String> mappedAddress = (LinkedHashMap) crimeApplyAddress;
        Address address = new Address();
        address.setLine1(mappedAddress.get("address_line_one"));
        address.setLine2(mappedAddress.get("address_line_two"));
        address.setCity(mappedAddress.get("city"));
        address.setCountry(mappedAddress.get("country"));
        address.setPostCode(mappedAddress.get("postcode"));
        return address;
      } catch (Exception e) {
        log.error(FAILED_TO_READ_ADDRESS_OBJECT);
      }
    }
    return null;
  }

  private boolean hasThirdPartyOwners(List<PropertyOwner> thirdPartyPropertyOwners) {
    return Objects.nonNull(thirdPartyPropertyOwners) && !thirdPartyPropertyOwners.isEmpty();
  }

  private void mapThirdPartyOwners(
      uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.common.Property
          property,
      List<PropertyOwner> propertyOwners) {
    for (PropertyOwner propertyOwner : propertyOwners) {
      if (Objects.nonNull(propertyOwner)) {
        ThirdPartyOwner thirdPartyOwner = new ThirdPartyOwner();

        thirdPartyOwner.setOwnerRelation(mapOwnerRelation(propertyOwner.getRelationship()));
        thirdPartyOwner.setOtherRelation(mapOtherRelation(propertyOwner.getOtherRelationship()));
        thirdPartyOwner.setOwnerName(propertyOwner.getName());

        property.getThirdPartyOwner().add(thirdPartyOwner);
      }
    }
  }

  private String mapOtherRelation(Object otherRelationship) {
    if (Objects.nonNull(otherRelationship)
        && otherRelationship instanceof String stringOtherRelationship) {
      return stringOtherRelationship;
    } else {
      return null;
    }
  }

  ThirdPartyOwner.OwnerRelation mapOwnerRelation(PropertyOwner.Relationship relationship) {
    if (Objects.nonNull(relationship)) {
      switch (relationship) {
        case OTHER:
          return ThirdPartyOwner.OwnerRelation.OTHER;
        case EX_PARTNER:
          return ThirdPartyOwner.OwnerRelation.EX_PARTNER;
        case BUSINESS_ASSOCIATES:
          return ThirdPartyOwner.OwnerRelation.BUSINESS;
        case FAMILY_MEMBERS:
          return ThirdPartyOwner.OwnerRelation.FAMILY;
        case FRIENDS:
          return ThirdPartyOwner.OwnerRelation.FRIENDS;
        case HOUSE_BUILDER:
          return ThirdPartyOwner.OwnerRelation.BUILDER;
        case HOUSING_ASSOCIATION:
          return ThirdPartyOwner.OwnerRelation.HOUSING_ASSOC;
        case LOCAL_AUTHORITY:
          return ThirdPartyOwner.OwnerRelation.LOCAL_AUTH;
        case PROPERTY_COMPANY:
          return ThirdPartyOwner.OwnerRelation.PPROPERTY_CO;
        case PARTNER_WITH_A_CONTRARY_INTEREST:
          return ThirdPartyOwner.OwnerRelation.PARTNER_CONT;
        default:
          log.debug(NO_PROPERTY_OWNER_TYPE_FOUND);
          return null;
      }
    } else {
      log.debug(NULL_OWNERSHIP_TYPE);
      return null;
    }
  }
}
