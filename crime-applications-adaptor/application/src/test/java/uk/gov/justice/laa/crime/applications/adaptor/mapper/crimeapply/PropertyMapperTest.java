package uk.gov.justice.laa.crime.applications.adaptor.mapper.crimeapply;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.common.ThirdPartyOwner;
import uk.gov.justice.laa.crime.applications.adaptor.model.criminalapplicationsdatastore.general.Property;
import uk.gov.justice.laa.crime.applications.adaptor.model.criminalapplicationsdatastore.general.PropertyOwner;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PropertyMapperTest {
    private PropertyMapper propertyMapper;

    @BeforeEach()
    void setUp() { propertyMapper = new PropertyMapper(); }

    @ParameterizedTest
    @MethodSource("propertyTypeMappingTestData")
    void shouldMapFromCrimeApplyPropertyTypeToCrimeApplicationsPropertyType(Property.PropertyType inputCrimeApplyPropertyType,
                                                                            Property.HouseType inputCrimeApplyHouseType,
                                                                            uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.common.Property.PropertyType expectedPropertyType) {
        uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.common.Property.PropertyType actualPropertyType
                = propertyMapper.mapPropertyType(inputCrimeApplyPropertyType, inputCrimeApplyHouseType);

        assertEquals(expectedPropertyType, actualPropertyType);
    }

    private static Stream<Arguments> propertyTypeMappingTestData() {
        return Stream.of(
                Arguments.of(Property.PropertyType.LAND,
                        null,
                        uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.common.Property.PropertyType.LAND),
                Arguments.of(Property.PropertyType.COMMERCIAL,
                        null,
                        uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.common.Property.PropertyType.COMMERCIAL),
                Arguments.of(Property.PropertyType.RESIDENTIAL,
                        Property.HouseType.BUNGALOW,
                        uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.common.Property.PropertyType.BUNGALOW),
                Arguments.of(Property.PropertyType.RESIDENTIAL,
                        Property.HouseType.DETACHED,
                        uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.common.Property.PropertyType.DETACHED),
                Arguments.of(Property.PropertyType.RESIDENTIAL,
                        Property.HouseType.FLAT_OR_MAISONETTE,
                        uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.common.Property.PropertyType.FLAT),
                Arguments.of(Property.PropertyType.RESIDENTIAL,
                        Property.HouseType.SEMIDETACHED,
                        uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.common.Property.PropertyType.SEMI),
                Arguments.of(Property.PropertyType.RESIDENTIAL,
                        Property.HouseType.OTHER,
                        uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.common.Property.PropertyType.OTHER)

        );
    }

    @ParameterizedTest
    @MethodSource("relationshipMappingTestData")
    void shouldMapFromCrimeApplyRelationshipToCrimeApplicationsOwnerRelation(PropertyOwner.Relationship inputCrimeApplyRelationship,
                                                                             ThirdPartyOwner.OwnerRelation expectedOwnerRelation) {
        ThirdPartyOwner.OwnerRelation actualOwnerRelation
                = propertyMapper.mapOwnerRelation(inputCrimeApplyRelationship);

        assertEquals(expectedOwnerRelation, actualOwnerRelation);
    }

    private static Stream<Arguments> relationshipMappingTestData() {
        return Stream.of(
                Arguments.of(PropertyOwner.Relationship.OTHER, ThirdPartyOwner.OwnerRelation.OTHER),
                Arguments.of(PropertyOwner.Relationship.EX_PARTNER, ThirdPartyOwner.OwnerRelation.EX_PARTNER),
                Arguments.of(PropertyOwner.Relationship.BUSINESS_ASSOCIATES, ThirdPartyOwner.OwnerRelation.BUSINESS),
                Arguments.of(PropertyOwner.Relationship.FAMILY_MEMBERS, ThirdPartyOwner.OwnerRelation.FAMILY),
                Arguments.of(PropertyOwner.Relationship.FRIENDS, ThirdPartyOwner.OwnerRelation.FRIENDS),
                Arguments.of(PropertyOwner.Relationship.HOUSE_BUILDER, ThirdPartyOwner.OwnerRelation.BUILDER),
                Arguments.of(PropertyOwner.Relationship.HOUSING_ASSOCIATION, ThirdPartyOwner.OwnerRelation.HOUSING_ASSOC),
                Arguments.of(PropertyOwner.Relationship.LOCAL_AUTHORITY, ThirdPartyOwner.OwnerRelation.LOCAL_AUTH),
                Arguments.of(PropertyOwner.Relationship.PROPERTY_COMPANY, ThirdPartyOwner.OwnerRelation.PPROPERTY_CO),
                Arguments.of(PropertyOwner.Relationship.PARTNER_WITH_A_CONTRARY_INTEREST, ThirdPartyOwner.OwnerRelation.PARTNER_CONT),
                Arguments.of(null, null)
        );
    }
}
