package uk.gov.justice.laa.crime.applications.adaptor.mapper.crimeapply;

import uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.common.ChildWeighting;
import uk.gov.justice.laa.crime.applications.adaptor.model.criminalapplicationsdatastore.general.Dependant;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ChildWeightingMapper {
    public List<ChildWeighting> mapChildWeighting(List<Dependant> crimeApplyDependants) {
        List<ChildWeighting> childWeightings = createChildWeightings();

        if (crimeApplyDependants != null) {
            for (Dependant crimeApplyDependant : crimeApplyDependants) {
                for (ChildWeighting childWeightingRange : childWeightings) {
                    if (withinAgeRange(crimeApplyDependant, childWeightingRange)) {
                        Integer newNoOfChildren = childWeightingRange.getNoOfChildren() + 1;
                        childWeightingRange.setNoOfChildren(newNoOfChildren);
                        break;
                    }
                }
            }
        }

        // We only want to retain dependant age ranges that have children in them
        childWeightings = removeEmptyChildWeightings(childWeightings);

        return childWeightings;
    }

    private List<ChildWeighting> createChildWeightings() {
        List<ChildWeighting> childWeightings = new ArrayList<>();
        childWeightings.add(new ChildWeighting(0, 0, 1));
        childWeightings.add(new ChildWeighting(0, 2, 4));
        childWeightings.add(new ChildWeighting(0, 5, 7));
        childWeightings.add(new ChildWeighting(0, 8, 10));
        childWeightings.add(new ChildWeighting(0, 11, 12));
        childWeightings.add(new ChildWeighting(0, 13, 15));
        childWeightings.add(new ChildWeighting(0, 16, 18));

        return childWeightings;
    }

    private List<ChildWeighting> removeEmptyChildWeightings(List<ChildWeighting> childWeightings) {
        Iterator<ChildWeighting> iterator = childWeightings.iterator();
        while (iterator.hasNext()) {
            ChildWeighting childWeightingsForAge = iterator.next();
            if (childWeightingsForAge.getNoOfChildren() == 0) {
                iterator.remove();
            }
        }

        return childWeightings;
    }

    private boolean withinAgeRange(Dependant crimeApplyDependant, ChildWeighting ageRange) {
        return crimeApplyDependant.getAge() >= ageRange.getLowerAgeRange() &&
                crimeApplyDependant.getAge() <= ageRange.getUpperAgeRange();
    }
}
