package uk.gov.justice.laa.crime.applications.adaptor.mapper.crimeapply;

import uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.common.ChildWeighting;
import uk.gov.justice.laa.crime.applications.adaptor.model.criminalapplicationsdatastore.general.Dependant;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ChildWeightingMapper {
    public List<ChildWeighting> mapChildWeighting(List<Dependant> crimeApplyDependants) {
        List<ChildWeighting> childWeightings = new ArrayList<>();

        int age0to1 =0;
        int age2to4 = 0;
        int age5to7 =0;
        int age8to10 =0;
        int age11to12 =0;
        int age13to15 = 0;
        int age16to18 =0;


        if (Objects.nonNull(crimeApplyDependants)) {
            for (Dependant crimeApplyDependant : crimeApplyDependants) {
                int age = crimeApplyDependant.getAge();
                if (0<=age && age <=1) age0to1 = age0to1+1;
                if (2<=age && age <=4) age2to4 = age2to4+1;
                if (5<=age && age <=7) age5to7 = age5to7+1;
                if (8<=age && age <=10) age8to10 = age8to10+1;
                if (11<=age && age <=12) age11to12 = age11to12+1;
                if (13<=age && age <=15) age13to15 = age13to15+1;
                if (16<=age && age <=18) age16to18 = age16to18+1;
            }
            map0To1Children(childWeightings, age0to1);
            map2To4Children(childWeightings, age2to4);
            map5To7Children(childWeightings, age5to7);
            map8To10Children(childWeightings, age8to10);
            map11To12Children(childWeightings, age11to12);
            map13To15Children(childWeightings, age13to15);
            map16To18Children(childWeightings, age16to18);
        }

        return childWeightings;
    }

    private void map0To1Children(List<ChildWeighting> childWeightings, int age0to1) {
        if(age0to1 > 0) {
            childWeightings.add(new ChildWeighting(age0to1, ChildWeighting.LowerAgeRange._0, ChildWeighting.UpperAgeRange._1));
        }
    }
    private void map2To4Children(List<ChildWeighting> childWeightings, int age2to4) {
        if(age2to4 > 0) {
            childWeightings.add(new ChildWeighting(age2to4, ChildWeighting.LowerAgeRange._2, ChildWeighting.UpperAgeRange._4));
        }
    }
    private void map5To7Children(List<ChildWeighting> childWeightings, int age5to7) {
        if(age5to7 > 0) {
            childWeightings.add(new ChildWeighting(age5to7, ChildWeighting.LowerAgeRange._5, ChildWeighting.UpperAgeRange._7));
        }
    }
    private void map8To10Children(List<ChildWeighting> childWeightings, int age8to10) {
        if(age8to10 > 0) {
            childWeightings.add(new ChildWeighting(age8to10, ChildWeighting.LowerAgeRange._8, ChildWeighting.UpperAgeRange._10));
        }
    }
    private void map11To12Children(List<ChildWeighting> childWeightings, int age11to12) {
        if(age11to12 > 0) {
            childWeightings.add(new ChildWeighting(age11to12, ChildWeighting.LowerAgeRange._11, ChildWeighting.UpperAgeRange._12));
        }
    }
    private void map13To15Children(List<ChildWeighting> childWeightings, int age13to15) {
        if(age13to15 > 0) {
            childWeightings.add(new ChildWeighting(age13to15, ChildWeighting.LowerAgeRange._13, ChildWeighting.UpperAgeRange._15));
        }
    }
    private void map16To18Children(List<ChildWeighting> childWeightings, int age16to18) {
        if(age16to18 > 0) {
            childWeightings.add(new ChildWeighting(age16to18, ChildWeighting.LowerAgeRange._16, ChildWeighting.UpperAgeRange._18));
        }
    }
}
