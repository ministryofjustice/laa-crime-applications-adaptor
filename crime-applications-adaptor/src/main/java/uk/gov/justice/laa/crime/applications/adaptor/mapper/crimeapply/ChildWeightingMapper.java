package uk.gov.justice.laa.crime.applications.adaptor.mapper.crimeapply;

import uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.common.ChildWeighting;
import uk.gov.justice.laa.crime.applications.adaptor.model.criminalapplicationsdatastore.general.Dependant;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ChildWeightingMapper {
    public List<ChildWeighting> mapChildWeighting(List<Dependant> crimeApplyDependants) {
        List<ChildWeighting> childWeightings = new ArrayList<>();
        if (Objects.nonNull(crimeApplyDependants)) {
            map0To1AgeChildren(crimeApplyDependants, childWeightings);
            map2To4AgeChildren(crimeApplyDependants, childWeightings);
            map5To7AgeChildren(crimeApplyDependants, childWeightings);
            map8To10AgeChildren(crimeApplyDependants, childWeightings);
            map11To12AgeChildren(crimeApplyDependants, childWeightings);
            map13To15AgeChildren(crimeApplyDependants, childWeightings);
            map16To18AgeChildren(crimeApplyDependants, childWeightings);
        }
        return childWeightings;
    }

    private void map0To1AgeChildren(List<Dependant> crimeApplyDependants, List<ChildWeighting> childWeightings) {
        int age0to1 = Math.toIntExact(crimeApplyDependants.stream().filter(dependant -> (dependant.getAge() <= 1)).count());
        if(age0to1 > 0) {
            childWeightings.add(new ChildWeighting(age0to1, ChildWeighting.LowerAgeRange._0, ChildWeighting.UpperAgeRange._1));
        }
    }

    private void map2To4AgeChildren(List<Dependant> crimeApplyDependants, List<ChildWeighting> childWeightings) {
        int age2to4 = Math.toIntExact(crimeApplyDependants.stream().filter(dependant -> (dependant.getAge() >=2 && dependant.getAge() <= 4)).count());
        if(age2to4 > 0) {
            childWeightings.add(new ChildWeighting(age2to4, ChildWeighting.LowerAgeRange._2, ChildWeighting.UpperAgeRange._4));
        }
    }

    private void map5To7AgeChildren(List<Dependant> crimeApplyDependants, List<ChildWeighting> childWeightings) {
        int age5to7 = Math.toIntExact(crimeApplyDependants.stream().filter(dependant -> (dependant.getAge() >=5 && dependant.getAge() <= 7)).count());
        if(age5to7 > 0) {
            childWeightings.add(new ChildWeighting(age5to7, ChildWeighting.LowerAgeRange._5, ChildWeighting.UpperAgeRange._7));
        }
    }

    private void map8To10AgeChildren(List<Dependant> crimeApplyDependants, List<ChildWeighting> childWeightings) {
        int age8to10 = Math.toIntExact(crimeApplyDependants.stream().filter(dependant -> (dependant.getAge() >=8 && dependant.getAge() <= 10)).count());
        if(age8to10 > 0) {
            childWeightings.add(new ChildWeighting(age8to10, ChildWeighting.LowerAgeRange._8, ChildWeighting.UpperAgeRange._10));
        }
    }

    private void map11To12AgeChildren(List<Dependant> crimeApplyDependants, List<ChildWeighting> childWeightings) {
        int age11to12 = Math.toIntExact(crimeApplyDependants.stream().filter(dependant -> (dependant.getAge() >=11 && dependant.getAge() <= 12)).count());
        if(age11to12 > 0) {
            childWeightings.add(new ChildWeighting(age11to12, ChildWeighting.LowerAgeRange._11, ChildWeighting.UpperAgeRange._12));
        }
    }

    private void map13To15AgeChildren(List<Dependant> crimeApplyDependants, List<ChildWeighting> childWeightings) {
        int age13to15 = Math.toIntExact(crimeApplyDependants.stream().filter(dependant -> (dependant.getAge() >=13 && dependant.getAge() <= 15)).count());
        if(age13to15 > 0) {
            childWeightings.add(new ChildWeighting(age13to15, ChildWeighting.LowerAgeRange._13, ChildWeighting.UpperAgeRange._15));
        }
    }
    private void map16To18AgeChildren(List<Dependant> crimeApplyDependants, List<ChildWeighting> childWeightings) {
        int age16to18 = Math.toIntExact(crimeApplyDependants.stream().filter(dependant -> (dependant.getAge() >= 16 && dependant.getAge() <= 18)).count());
        if (age16to18 > 0) {
            childWeightings.add(new ChildWeighting(age16to18, ChildWeighting.LowerAgeRange._16, ChildWeighting.UpperAgeRange._18));
        }
    }
}