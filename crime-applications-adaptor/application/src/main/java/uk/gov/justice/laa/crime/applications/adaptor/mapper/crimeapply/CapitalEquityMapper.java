package uk.gov.justice.laa.crime.applications.adaptor.mapper.crimeapply;

import uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.common.Address;
import uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.common.CapitalEquity;
import uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.common.ThirdPartyOwner;
import uk.gov.justice.laa.crime.applications.adaptor.model.criminalapplicationsdatastore.MaatApplicationExternal;
import uk.gov.justice.laa.crime.applications.adaptor.model.criminalapplicationsdatastore.general.CapitalDetails;
import uk.gov.justice.laa.crime.applications.adaptor.model.criminalapplicationsdatastore.general.Property;
import uk.gov.justice.laa.crime.applications.adaptor.model.criminalapplicationsdatastore.general.PropertyOwner;

import java.util.List;
import java.util.Objects;

public class CapitalEquityMapper {

    CapitalEquity map(MaatApplicationExternal crimeApplyResponse) {
        CapitalEquity capitalEquity = new CapitalEquity();

        if (hasProperties(crimeApplyResponse)) {
            mapPropertiesToCapitalEquity(crimeApplyResponse.getMeansDetails().getCapitalDetails().getProperties(), capitalEquity);
        }

        if (hasOtherCapital(crimeApplyResponse)) {
            mapOtherCapitalToCapitalEquity(crimeApplyResponse.getMeansDetails().getCapitalDetails(), capitalEquity);
        }

        return capitalEquity;
    }

    private void mapOtherCapitalToCapitalEquity(CapitalDetails capitalDetails, CapitalEquity capitalEquity) {
        // TODO
    }

    boolean hasOtherCapital(MaatApplicationExternal crimeApplyResponse) {
        // TODO finish other capital checks
        return  Objects.nonNull(crimeApplyResponse.getMeansDetails().getCapitalDetails().getInvestments())
                && !crimeApplyResponse.getMeansDetails().getCapitalDetails().getInvestments().isEmpty();
    }

    boolean hasProperties(MaatApplicationExternal crimeApplyResponse) {
        return Objects.nonNull(crimeApplyResponse.getMeansDetails().getCapitalDetails().getProperties())
                && !crimeApplyResponse.getMeansDetails().getCapitalDetails().getProperties().isEmpty();
    }

    void mapPropertiesToCapitalEquity(List<Property> properties, CapitalEquity capitalEquity) {
        for (Property crimeApplyDataStoreProperty: properties) {
            if (isHomeProperty(crimeApplyDataStoreProperty)) {
                mapPropertyToEquity(crimeApplyDataStoreProperty, capitalEquity.getEquity());
            } else {
                mapPropertyToCapitalProperty(crimeApplyDataStoreProperty, capitalEquity.getCapitalProperty());
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
                && "yes".equals(crimeApplyDataStoreProperty.getIsHomeAddress());
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
