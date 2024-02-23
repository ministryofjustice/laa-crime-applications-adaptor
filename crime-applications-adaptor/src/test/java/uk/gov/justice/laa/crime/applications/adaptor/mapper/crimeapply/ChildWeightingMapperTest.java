package uk.gov.justice.laa.crime.applications.adaptor.mapper.crimeapply;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.common.ChildWeighting;
import uk.gov.justice.laa.crime.applications.adaptor.model.criminalapplicationsdatastore.general.Dependant;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ChildWeightingMapperTest {

    private ChildWeightingMapper childWeightingMapper;

    @BeforeEach
    void setUp() { childWeightingMapper = new ChildWeightingMapper(); }

    @Test
    void shouldMapDependant0to1ToToAdapterChildWeighting0to1() {
        Dependant dependant = new Dependant();
        dependant.setAge(1);
        List<Dependant> crimeApplyDependants = List.of(dependant);

        ChildWeighting childWeighting = new ChildWeighting();
        childWeighting.setLowerAgeRange(ChildWeighting.LowerAgeRange._0);
        childWeighting.setUpperAgeRange(ChildWeighting.UpperAgeRange._1);
        childWeighting.setNoOfChildren(1);

        List<ChildWeighting> expectedChildWeightings = List.of(childWeighting);
        List<ChildWeighting> actualChildWeightings = childWeightingMapper.mapChildWeighting(crimeApplyDependants);

        assertEquals(expectedChildWeightings, actualChildWeightings);
    }

    @Test
    void shouldMapDependant2to4ToToAdapterChildWeighting2to4() {
        Dependant dependant = new Dependant();
        dependant.setAge(2);
        List<Dependant> crimeApplyDependants = List.of(dependant);

        ChildWeighting childWeighting = new ChildWeighting();
        childWeighting.setLowerAgeRange(ChildWeighting.LowerAgeRange._2);
        childWeighting.setUpperAgeRange(ChildWeighting.UpperAgeRange._4);
        childWeighting.setNoOfChildren(1);

        List<ChildWeighting> expectedChildWeightings = List.of(childWeighting);
        List<ChildWeighting> actualChildWeightings = childWeightingMapper.mapChildWeighting(crimeApplyDependants);

        assertEquals(expectedChildWeightings, actualChildWeightings);
    }

    @Test
    void shouldMapDependant5to7ToToAdapterChildWeighting5to7() {
        Dependant dependant = new Dependant();
        dependant.setAge(5);
        List<Dependant> crimeApplyDependants = List.of(dependant);

        ChildWeighting childWeighting = new ChildWeighting();
        childWeighting.setLowerAgeRange(ChildWeighting.LowerAgeRange._5);
        childWeighting.setUpperAgeRange(ChildWeighting.UpperAgeRange._7);
        childWeighting.setNoOfChildren(1);

        List<ChildWeighting> expectedChildWeightings = List.of(childWeighting);
        List<ChildWeighting> actualChildWeightings = childWeightingMapper.mapChildWeighting(crimeApplyDependants);

        assertEquals(expectedChildWeightings, actualChildWeightings);
    }

    @Test
    void shouldMapDependant8to10ToToAdapterChildWeighting8to10() {
        Dependant dependant = new Dependant();
        dependant.setAge(8);
        List<Dependant> crimeApplyDependants = List.of(dependant);

        ChildWeighting childWeighting = new ChildWeighting();
        childWeighting.setLowerAgeRange(ChildWeighting.LowerAgeRange._8);
        childWeighting.setUpperAgeRange(ChildWeighting.UpperAgeRange._10);
        childWeighting.setNoOfChildren(1);

        List<ChildWeighting> expectedChildWeightings = List.of(childWeighting);
        List<ChildWeighting> actualChildWeightings = childWeightingMapper.mapChildWeighting(crimeApplyDependants);

        assertEquals(expectedChildWeightings, actualChildWeightings);
    }

    @Test
    void shouldMapDependant11to12ToToAdapterChildWeighting11to12() {
        Dependant dependant = new Dependant();
        dependant.setAge(11);
        List<Dependant> crimeApplyDependants = List.of(dependant);

        ChildWeighting childWeighting = new ChildWeighting();
        childWeighting.setLowerAgeRange(ChildWeighting.LowerAgeRange._11);
        childWeighting.setUpperAgeRange(ChildWeighting.UpperAgeRange._12);
        childWeighting.setNoOfChildren(1);

        List<ChildWeighting> expectedChildWeightings = List.of(childWeighting);
        List<ChildWeighting> actualChildWeightings = childWeightingMapper.mapChildWeighting(crimeApplyDependants);

        assertEquals(expectedChildWeightings, actualChildWeightings);
    }

    @Test
    void shouldMapDependant13to15ToToAdapterChildWeighting13to15() {
        Dependant dependant = new Dependant();
        dependant.setAge(13);
        List<Dependant> crimeApplyDependants = List.of(dependant);

        ChildWeighting childWeighting = new ChildWeighting();
        childWeighting.setLowerAgeRange(ChildWeighting.LowerAgeRange._13);
        childWeighting.setUpperAgeRange(ChildWeighting.UpperAgeRange._15);
        childWeighting.setNoOfChildren(1);

        List<ChildWeighting> expectedChildWeightings = List.of(childWeighting);
        List<ChildWeighting> actualChildWeightings = childWeightingMapper.mapChildWeighting(crimeApplyDependants);

        assertEquals(expectedChildWeightings, actualChildWeightings);
    }

    @Test
    void shouldMapDependant16to18ToToAdapterChildWeighting16to18() {
        Dependant dependant = new Dependant();
        dependant.setAge(16);
        List<Dependant> crimeApplyDependants = List.of(dependant);

        ChildWeighting childWeighting = new ChildWeighting();
        childWeighting.setLowerAgeRange(ChildWeighting.LowerAgeRange._16);
        childWeighting.setUpperAgeRange(ChildWeighting.UpperAgeRange._18);
        childWeighting.setNoOfChildren(1);

        List<ChildWeighting> expectedChildWeightings = List.of(childWeighting);
        List<ChildWeighting> actualChildWeightings = childWeightingMapper.mapChildWeighting(crimeApplyDependants);

        assertEquals(expectedChildWeightings, actualChildWeightings);
    }
}
