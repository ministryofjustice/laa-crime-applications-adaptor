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
            mapChildrenBasedOnAgeRange(crimeApplyDependants, childWeightings, ChildWeighting.LowerAgeRange._0, ChildWeighting.UpperAgeRange._1);
            mapChildrenBasedOnAgeRange(crimeApplyDependants, childWeightings, ChildWeighting.LowerAgeRange._2, ChildWeighting.UpperAgeRange._4);
            mapChildrenBasedOnAgeRange(crimeApplyDependants, childWeightings, ChildWeighting.LowerAgeRange._5, ChildWeighting.UpperAgeRange._7);
            mapChildrenBasedOnAgeRange(crimeApplyDependants, childWeightings, ChildWeighting.LowerAgeRange._8, ChildWeighting.UpperAgeRange._10);
            mapChildrenBasedOnAgeRange(crimeApplyDependants, childWeightings, ChildWeighting.LowerAgeRange._11, ChildWeighting.UpperAgeRange._12);
            mapChildrenBasedOnAgeRange(crimeApplyDependants, childWeightings, ChildWeighting.LowerAgeRange._13, ChildWeighting.UpperAgeRange._15);
            mapChildrenBasedOnAgeRange(crimeApplyDependants, childWeightings, ChildWeighting.LowerAgeRange._16, ChildWeighting.UpperAgeRange._18);

        }
        return childWeightings;
    }
    private void mapChildrenBasedOnAgeRange(List<Dependant> crimeApplyDependants, List<ChildWeighting> childWeightings, ChildWeighting.LowerAgeRange lowerAgeRange, ChildWeighting.UpperAgeRange upperAgeRange) {
        int numberOfChildren = Math.toIntExact(crimeApplyDependants.stream().filter(dependant -> (dependant.getAge() >= lowerAgeRange.value() && dependant.getAge() <= upperAgeRange.value())).count());
        if (numberOfChildren > 0) {
            childWeightings.add(new ChildWeighting(numberOfChildren, lowerAgeRange, upperAgeRange));
        }
    }
}