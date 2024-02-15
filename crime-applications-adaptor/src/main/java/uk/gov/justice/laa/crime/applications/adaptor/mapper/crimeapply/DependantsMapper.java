package uk.gov.justice.laa.crime.applications.adaptor.mapper.crimeapply;

import uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.common.Dependants;
import uk.gov.justice.laa.crime.applications.adaptor.model.criminalapplicationsdatastore.Dependant;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DependantsMapper {
    public List<Dependants> mapDependants(List<Dependant> crimeApplyDependants) {
        List<Dependants> dependants = createDependants();

        if (crimeApplyDependants != null) {
            for (Dependant crimeApplyDependant : crimeApplyDependants) {
                for (Dependants dependantRange : dependants) {
                    if (withinAgeRange(crimeApplyDependant, dependantRange)) {
                        Integer newNoOfChildren = dependantRange.getNoOfChildren() + 1;
                        dependantRange.setNoOfChildren(newNoOfChildren);
                        break;
                    }
                }
            }
        }

        // We only want to retain dependant age ranges that have children in them
        dependants = removeEmptyDependants(dependants);

        return dependants;
    }

    private List<Dependants> createDependants() {
        List<Dependants> dependants = new ArrayList<>();
        dependants.add(new Dependants(0, 1, 0));
        dependants.add(new Dependants(2, 4, 0));
        dependants.add(new Dependants(5, 7, 0));
        dependants.add(new Dependants(8, 10, 0));
        dependants.add(new Dependants(11, 12, 0));
        dependants.add(new Dependants(13, 15, 0));
        dependants.add(new Dependants(16, 18, 0));

        return dependants;
    }

    private List<Dependants> removeEmptyDependants(List<Dependants> dependants) {
        Iterator<Dependants> iterator = dependants.iterator();
        while (iterator.hasNext()) {
            Dependants dependantsForAge = iterator.next();
            if (dependantsForAge.getNoOfChildren() == 0) {
                iterator.remove();
            }
        }

        return dependants;
    }

    private boolean withinAgeRange(Dependant crimeApplyDependant, Dependants ageRange) {
        return crimeApplyDependant.getAge() >= ageRange.getLowerAgeRange() &&
                crimeApplyDependant.getAge() <= ageRange.getUpperAgeRange();
    }
}
