package uk.gov.justice.laa.crime.applications.adaptor.mapper.crimeapply;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.common.ChildWeighting;
import uk.gov.justice.laa.crime.applications.adaptor.model.criminalapplicationsdatastore.general.Dependant;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ChildWeightingMapperTest {

    private ChildWeightingMapper childWeightingMapper;

    @BeforeEach
    void setUp() { childWeightingMapper = new ChildWeightingMapper(); }

    private Dependant getDependantObject(Integer age) {
        Dependant dependant = new Dependant();
        dependant.setAge(age);

        return dependant;
    }

    private ChildWeighting getChildWeightingObject(ChildWeighting.LowerAgeRange lowerAgeRange, ChildWeighting.UpperAgeRange upperAgeRange) {
        ChildWeighting childWeighting = new ChildWeighting();
        childWeighting.setLowerAgeRange(lowerAgeRange);
        childWeighting.setUpperAgeRange(upperAgeRange);
        childWeighting.setNoOfChildren(1);

        return childWeighting;
    }

    @ParameterizedTest
    @MethodSource("childWeightingMappingTestData")
    void shouldMapDependantToAdapterChildWeighting(Integer age, ChildWeighting.LowerAgeRange lowerAgeRange, ChildWeighting.UpperAgeRange upperAgeRange) {
        Dependant dependant = getDependantObject(age);
        List<Dependant> crimeApplyDependants = List.of(dependant);

        ChildWeighting childWeighting = getChildWeightingObject(lowerAgeRange, upperAgeRange);

        List<ChildWeighting> expectedChildWeightings = List.of(childWeighting);
        List<ChildWeighting> actualChildWeightings = childWeightingMapper.mapChildWeighting(crimeApplyDependants);

        assertEquals(expectedChildWeightings, actualChildWeightings);
    }

    private static Stream<Arguments> childWeightingMappingTestData() {
        return Stream.of(
                Arguments.of(1, ChildWeighting.LowerAgeRange._0, ChildWeighting.UpperAgeRange._1),
                Arguments.of(2, ChildWeighting.LowerAgeRange._2, ChildWeighting.UpperAgeRange._4),
                Arguments.of(5, ChildWeighting.LowerAgeRange._5, ChildWeighting.UpperAgeRange._7),
                Arguments.of(8, ChildWeighting.LowerAgeRange._8, ChildWeighting.UpperAgeRange._10),
                Arguments.of(11, ChildWeighting.LowerAgeRange._11, ChildWeighting.UpperAgeRange._12),
                Arguments.of(13, ChildWeighting.LowerAgeRange._13, ChildWeighting.UpperAgeRange._15),
                Arguments.of(16, ChildWeighting.LowerAgeRange._16, ChildWeighting.UpperAgeRange._18)
        );
    }
}
