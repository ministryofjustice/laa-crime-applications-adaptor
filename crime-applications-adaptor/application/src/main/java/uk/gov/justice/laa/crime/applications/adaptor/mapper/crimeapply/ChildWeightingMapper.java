package uk.gov.justice.laa.crime.applications.adaptor.mapper.crimeapply;

import static uk.gov.justice.laa.crime.model.common.crimeapplication.common.ChildWeighting.LowerAgeRange._0;
import static uk.gov.justice.laa.crime.model.common.crimeapplication.common.ChildWeighting.LowerAgeRange._11;
import static uk.gov.justice.laa.crime.model.common.crimeapplication.common.ChildWeighting.LowerAgeRange._13;
import static uk.gov.justice.laa.crime.model.common.crimeapplication.common.ChildWeighting.LowerAgeRange._16;
import static uk.gov.justice.laa.crime.model.common.crimeapplication.common.ChildWeighting.LowerAgeRange._2;
import static uk.gov.justice.laa.crime.model.common.crimeapplication.common.ChildWeighting.LowerAgeRange._5;
import static uk.gov.justice.laa.crime.model.common.crimeapplication.common.ChildWeighting.LowerAgeRange._8;
import static uk.gov.justice.laa.crime.model.common.crimeapplication.common.ChildWeighting.UpperAgeRange._1;
import static uk.gov.justice.laa.crime.model.common.crimeapplication.common.ChildWeighting.UpperAgeRange._10;
import static uk.gov.justice.laa.crime.model.common.crimeapplication.common.ChildWeighting.UpperAgeRange._12;
import static uk.gov.justice.laa.crime.model.common.crimeapplication.common.ChildWeighting.UpperAgeRange._15;
import static uk.gov.justice.laa.crime.model.common.crimeapplication.common.ChildWeighting.UpperAgeRange._18;
import static uk.gov.justice.laa.crime.model.common.crimeapplication.common.ChildWeighting.UpperAgeRange._4;
import static uk.gov.justice.laa.crime.model.common.crimeapplication.common.ChildWeighting.UpperAgeRange._7;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import uk.gov.justice.laa.crime.model.common.crimeapplication.common.ChildWeighting;
import uk.gov.justice.laa.crime.model.common.criminalapplicationsdatastore.general.Dependant;

public class ChildWeightingMapper {

  public List<ChildWeighting> mapChildWeighting(List<Dependant> crimeApplyDependants) {
    List<ChildWeighting> childWeightings = new ArrayList<>();
    if (Objects.nonNull(crimeApplyDependants)) {
      mapChildrenBasedOnAgeRange(crimeApplyDependants, childWeightings, _0, _1);
      mapChildrenBasedOnAgeRange(crimeApplyDependants, childWeightings, _2, _4);
      mapChildrenBasedOnAgeRange(crimeApplyDependants, childWeightings, _5, _7);
      mapChildrenBasedOnAgeRange(crimeApplyDependants, childWeightings, _8, _10);
      mapChildrenBasedOnAgeRange(crimeApplyDependants, childWeightings, _11, _12);
      mapChildrenBasedOnAgeRange(crimeApplyDependants, childWeightings, _13, _15);
      mapChildrenBasedOnAgeRange(crimeApplyDependants, childWeightings, _16, _18);
    }
    return childWeightings;
  }

  private void mapChildrenBasedOnAgeRange(
      List<Dependant> crimeApplyDependants,
      List<ChildWeighting> childWeightings,
      ChildWeighting.LowerAgeRange lowerAgeRange,
      ChildWeighting.UpperAgeRange upperAgeRange) {
    long numberOfChildrenLong =
        crimeApplyDependants.stream()
            .filter(ageInsideInclusiveRange(lowerAgeRange, upperAgeRange))
            .count();

    int numberOfChildren = Math.toIntExact(numberOfChildrenLong);

    if (numberOfChildren > 0) {
      ChildWeighting childWeighting =
          new ChildWeighting(numberOfChildren, lowerAgeRange, upperAgeRange);
      childWeightings.add(childWeighting);
    }
  }

  private static Predicate<Dependant> ageInsideInclusiveRange(
      ChildWeighting.LowerAgeRange lowerAgeRange, ChildWeighting.UpperAgeRange upperAgeRange) {
    return dependant -> {
      int dependantAge = Integer.sum(dependant.getAge(), 1);
      return dependantAge >= lowerAgeRange.value() && dependantAge <= upperAgeRange.value();
    };
  }
}
