package uk.gov.justice.laa.crime.applications.adaptor.mapper.crimeapply;

import static uk.gov.justice.laa.crime.model.common.crimeapplication.common.CapitalEquity.ResidentialStatus.*;

import java.util.List;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import uk.gov.justice.laa.crime.model.common.crimeapplication.common.CapitalEquity;
import uk.gov.justice.laa.crime.model.common.crimeapplication.common.CapitalOther;
import uk.gov.justice.laa.crime.model.common.criminalapplicationsdatastore.Applicant;
import uk.gov.justice.laa.crime.model.common.criminalapplicationsdatastore.MaatApplicationExternal;
import uk.gov.justice.laa.crime.model.common.criminalapplicationsdatastore.general.*;

@Slf4j
public class CapitalEquityMapper {

  private static final String IS_HOME_PROPERTY = "yes";

  private final PropertyMapper propertyMapper = new PropertyMapper();
  private final InvestmentMapper investmentMapper = new InvestmentMapper();
  private final SavingMapper savingMapper = new SavingMapper();
  private final NationalSavingsCertificateMapper nationalSavingsCertificateMapper =
      new NationalSavingsCertificateMapper();
  private final PremiumBondMapper premiumBondMapper = new PremiumBondMapper();
  private final TrustFundMapper trustFundMapper = new TrustFundMapper();

  CapitalEquity map(MaatApplicationExternal crimeApplyResponse) {
    CapitalEquity capitalEquity = new CapitalEquity();

    mapPropertiesToCapitalEquity(crimeApplyResponse, capitalEquity);
    mapOtherCapitalToCapitalEquity(crimeApplyResponse, capitalEquity);
    mapResidentialStatusToCapitalEquity(crimeApplyResponse, capitalEquity);

    return capitalEquity;
  }

  private void mapResidentialStatusToCapitalEquity(
      MaatApplicationExternal crimeApplyResponse, CapitalEquity capitalEquity) {
    capitalEquity.setResidentialStatus(
        mapResidenceTypeToResidentialStatus(
            crimeApplyResponse.getClientDetails().getApplicant().getResidenceType()));
  }

  private CapitalEquity.ResidentialStatus mapResidenceTypeToResidentialStatus(
      Applicant.ResidenceType residenceType) {
    CapitalEquity.ResidentialStatus residentialStatus;

    switch (residenceType) {
      case APPLICANT_OWNED, PARTNER_OWNED, JOINT_OWNED -> residentialStatus = OWNER;
      case RENTED -> residentialStatus = TENANT;
      case TEMPORARY -> residentialStatus = TEMP;
      case PARENTS -> residentialStatus = PARENTS;
      default -> residentialStatus = null;
    }

    return residentialStatus;
  }

  private void mapOtherCapitalToCapitalEquity(
      MaatApplicationExternal crimeApplyResponse, CapitalEquity capitalEquity) {
    CapitalDetails capitalDetails = crimeApplyResponse.getMeansDetails().getCapitalDetails();

    if (hasInvestments(crimeApplyResponse)) {
      mapInvestmentsToCapitalEquity(capitalDetails.getInvestments(), capitalEquity);
    }

    if (hasSavings(crimeApplyResponse)) {
      mapSavingsToCapitalEquity(capitalDetails.getSavings(), capitalEquity);
    }

    if (hasNationalSavingsCertificates(crimeApplyResponse)) {
      mapNationalSavingsCertificatesToCapitalEquity(
          capitalDetails.getNationalSavingsCertificates(), capitalEquity);
    }

    if (hasPremiumBonds(crimeApplyResponse)) {
      mapPremiumBondsToCapitalEquity(capitalDetails.getPremiumBondsTotalValue(), capitalEquity);
    }

    if (hasTrustFund(crimeApplyResponse)) {
      mapTrustFundToCapitalEquity(capitalDetails.getTrustFundAmountHeld(), capitalEquity);
    }
  }

  private void mapTrustFundToCapitalEquity(
      Object trustFundAmountHeld, CapitalEquity capitalEquity) {
    CapitalOther trustFund = trustFundMapper.map((Integer) trustFundAmountHeld);
    capitalEquity.getCapital().add(trustFund);
  }

  private void mapPremiumBondsToCapitalEquity(
      Object premiumBondsTotalValue, CapitalEquity capitalEquity) {
    CapitalOther premiumBonds = premiumBondMapper.map((Integer) premiumBondsTotalValue);
    capitalEquity.getCapital().add(premiumBonds);
  }

  private void mapNationalSavingsCertificatesToCapitalEquity(
      List<NationalSavingsCertificate> nationalSavingsCertificates, CapitalEquity capitalEquity) {
    for (NationalSavingsCertificate certificate : nationalSavingsCertificates) {
      if (Objects.nonNull(certificate)) {
        CapitalOther nationalSavingsCertificate = nationalSavingsCertificateMapper.map(certificate);
        capitalEquity.getCapital().add(nationalSavingsCertificate);
      }
    }
  }

  private void mapSavingsToCapitalEquity(List<Saving> savings, CapitalEquity capitalEquity) {
    for (Saving saving : savings) {
      CapitalOther capitalOther = savingMapper.map(saving);
      capitalEquity.getCapital().add(capitalOther);
    }
  }

  private void mapInvestmentsToCapitalEquity(
      List<Investment> investments, CapitalEquity capitalEquity) {
    for (Investment investment : investments) {
      CapitalOther capitalOther = investmentMapper.map(investment);
      capitalEquity.getCapital().add(capitalOther);
    }
  }

  private boolean hasTrustFund(MaatApplicationExternal crimeApplyResponse) {
    return Objects.nonNull(
            crimeApplyResponse.getMeansDetails().getCapitalDetails().getTrustFundAmountHeld())
        && crimeApplyResponse.getMeansDetails().getCapitalDetails().getTrustFundAmountHeld()
            instanceof Integer;
  }

  private boolean hasPremiumBonds(MaatApplicationExternal crimeApplyResponse) {
    return Objects.nonNull(
            crimeApplyResponse.getMeansDetails().getCapitalDetails().getPremiumBondsTotalValue())
        && crimeApplyResponse.getMeansDetails().getCapitalDetails().getPremiumBondsTotalValue()
            instanceof Integer;
  }

  private boolean hasNationalSavingsCertificates(MaatApplicationExternal crimeApplyResponse) {
    return Objects.nonNull(
            crimeApplyResponse
                .getMeansDetails()
                .getCapitalDetails()
                .getNationalSavingsCertificates())
        && !crimeApplyResponse
            .getMeansDetails()
            .getCapitalDetails()
            .getNationalSavingsCertificates()
            .isEmpty();
  }

  private boolean hasSavings(MaatApplicationExternal crimeApplyResponse) {
    return Objects.nonNull(crimeApplyResponse.getMeansDetails().getCapitalDetails().getSavings())
        && !crimeApplyResponse.getMeansDetails().getCapitalDetails().getSavings().isEmpty();
  }

  private boolean hasInvestments(MaatApplicationExternal crimeApplyResponse) {
    return Objects.nonNull(
            crimeApplyResponse.getMeansDetails().getCapitalDetails().getInvestments())
        && !crimeApplyResponse.getMeansDetails().getCapitalDetails().getInvestments().isEmpty();
  }

  private boolean hasProperties(MaatApplicationExternal crimeApplyResponse) {
    return Objects.nonNull(crimeApplyResponse.getMeansDetails().getCapitalDetails().getProperties())
        && !crimeApplyResponse.getMeansDetails().getCapitalDetails().getProperties().isEmpty();
  }

  private void mapPropertiesToCapitalEquity(
      MaatApplicationExternal crimeApplyResponse, CapitalEquity capitalEquity) {
    if (hasProperties(crimeApplyResponse)) {
      for (Property crimeApplyDataStoreProperty :
          crimeApplyResponse.getMeansDetails().getCapitalDetails().getProperties()) {
        if (isHomeProperty(crimeApplyDataStoreProperty)) {
          mapPropertyToEquity(crimeApplyDataStoreProperty, capitalEquity.getEquity());
        } else {
          mapPropertyToCapitalProperty(
              crimeApplyDataStoreProperty, capitalEquity.getCapitalProperty());
        }
      }
    }
  }

  private void mapPropertyToCapitalProperty(
      Property crimeApplyDataStoreProperty,
      List<uk.gov.justice.laa.crime.model.common.crimeapplication.common.Property>
          capitalProperty) {
    uk.gov.justice.laa.crime.model.common.crimeapplication.common.Property property =
        propertyMapper.map(crimeApplyDataStoreProperty);
    capitalProperty.add(property);
  }

  private void mapPropertyToEquity(
      Property crimeApplyDataStoreProperty,
      List<uk.gov.justice.laa.crime.model.common.crimeapplication.common.Property> equity) {
    uk.gov.justice.laa.crime.model.common.crimeapplication.common.Property property =
        propertyMapper.map(crimeApplyDataStoreProperty);
    equity.add(property);
  }

  private boolean isHomeProperty(Property crimeApplyDataStoreProperty) {
    return Objects.nonNull(crimeApplyDataStoreProperty)
        && Objects.nonNull(crimeApplyDataStoreProperty.getIsHomeAddress())
        && crimeApplyDataStoreProperty.getIsHomeAddress() instanceof String
        && IS_HOME_PROPERTY.equals(crimeApplyDataStoreProperty.getIsHomeAddress().toString());
  }
}
