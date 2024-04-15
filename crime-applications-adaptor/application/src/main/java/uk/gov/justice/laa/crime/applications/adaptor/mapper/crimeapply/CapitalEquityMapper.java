package uk.gov.justice.laa.crime.applications.adaptor.mapper.crimeapply;

import lombok.extern.slf4j.Slf4j;
import uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.common.CapitalEquity;
import uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.common.CapitalOther;
import uk.gov.justice.laa.crime.applications.adaptor.model.criminalapplicationsdatastore.MaatApplicationExternal;
import uk.gov.justice.laa.crime.applications.adaptor.model.criminalapplicationsdatastore.general.*;

import java.util.List;
import java.util.Objects;

@Slf4j
public class CapitalEquityMapper {

    private static final String IS_HOME_PROPERTY = "yes";

    private final PropertyMapper propertyMapper = new PropertyMapper();
    private final InvestmentMapper investmentMapper = new InvestmentMapper();
    private final SavingMapper savingMapper = new SavingMapper();
    private final NationalSavingsCertificateMapper nationalSavingsCertificateMapper = new NationalSavingsCertificateMapper();
    private final PremiumBondMapper premiumBondMapper = new PremiumBondMapper();
    private final TrustFundMapper trustFundMapper = new TrustFundMapper();

    CapitalEquity map(MaatApplicationExternal crimeApplyResponse) {
        CapitalEquity capitalEquity = new CapitalEquity();

        mapPropertiesToCapitalEquity(crimeApplyResponse, capitalEquity);
        mapOtherCapitalToCapitalEquity(crimeApplyResponse, capitalEquity);

        return capitalEquity;
    }

    void mapOtherCapitalToCapitalEquity(MaatApplicationExternal crimeApplyResponse, CapitalEquity capitalEquity) {
        if (hasInvestments(crimeApplyResponse)) {
            mapInvestmentsToCapitalEquity(crimeApplyResponse.getMeansDetails().getCapitalDetails().getInvestments(),
                    capitalEquity);
        }

        if (hasSavings(crimeApplyResponse)) {
            mapSavingsToCapitalEquity(crimeApplyResponse.getMeansDetails().getCapitalDetails().getSavings(),
                    capitalEquity);
        }

        if (hasNationalSavingsCertificates(crimeApplyResponse)) {
            mapNationalSavingsCertificatesToCapitalEquity(crimeApplyResponse.getMeansDetails().getCapitalDetails()
                    .getNationalSavingsCertificates(), capitalEquity);
        }

        if (hasPremiumBonds(crimeApplyResponse)) {
            mapPremiumBondsToCapitalEquity(crimeApplyResponse.getMeansDetails().getCapitalDetails()
                    .getPremiumBondsTotalValue(), capitalEquity);
        }

        if (hasTrustFund(crimeApplyResponse)) {
            mapTrustFundToCapitalEquity(crimeApplyResponse.getMeansDetails().getCapitalDetails()
                    .getTrustFundAmountHeld(), capitalEquity);
        }
    }

    void mapTrustFundToCapitalEquity(Object trustFundAmountHeld, CapitalEquity capitalEquity) {
        CapitalOther trustFund = trustFundMapper.map((Integer) trustFundAmountHeld);
        capitalEquity.getCapital().add(trustFund);
    }

    void mapPremiumBondsToCapitalEquity(Object premiumBondsTotalValue, CapitalEquity capitalEquity) {
        CapitalOther premiumBonds = premiumBondMapper.map((Integer) premiumBondsTotalValue);
        capitalEquity.getCapital().add(premiumBonds);
    }

    void mapNationalSavingsCertificatesToCapitalEquity(List<NationalSavingsCertificate> nationalSavingsCertificates, CapitalEquity capitalEquity) {
        for (NationalSavingsCertificate certificate : nationalSavingsCertificates) {
            if (Objects.nonNull(certificate)) {
                CapitalOther nationalSavingsCertificate = nationalSavingsCertificateMapper.map(certificate);
                capitalEquity.getCapital().add(nationalSavingsCertificate);
            }
        }
    }

    void mapSavingsToCapitalEquity(List<Saving> savings, CapitalEquity capitalEquity) {
        for (Saving saving : savings) {
            CapitalOther capitalOther = savingMapper.map(saving);
            capitalEquity.getCapital().add(capitalOther);
        }
    }

    void mapInvestmentsToCapitalEquity(List<Investment> investments, CapitalEquity capitalEquity) {
        for (Investment investment : investments) {
            CapitalOther capitalOther = investmentMapper.map(investment);
            capitalEquity.getCapital().add(capitalOther);
        }
    }

    boolean hasTrustFund(MaatApplicationExternal crimeApplyResponse) {
        return Objects.nonNull(crimeApplyResponse.getMeansDetails().getCapitalDetails().getTrustFundAmountHeld())
                && crimeApplyResponse.getMeansDetails().getCapitalDetails().getTrustFundAmountHeld() instanceof Integer;
    }

    boolean hasPremiumBonds(MaatApplicationExternal crimeApplyResponse) {
        return Objects.nonNull(crimeApplyResponse.getMeansDetails().getCapitalDetails().getPremiumBondsTotalValue())
                && crimeApplyResponse.getMeansDetails().getCapitalDetails()
                .getPremiumBondsTotalValue() instanceof Integer;
    }

    boolean hasNationalSavingsCertificates(MaatApplicationExternal crimeApplyResponse) {
        return Objects.nonNull(crimeApplyResponse.getMeansDetails().getCapitalDetails()
                .getNationalSavingsCertificates())
                && !crimeApplyResponse.getMeansDetails().getCapitalDetails().getNationalSavingsCertificates().isEmpty();
    }

    boolean hasSavings(MaatApplicationExternal crimeApplyResponse) {
        return Objects.nonNull(crimeApplyResponse.getMeansDetails().getCapitalDetails().getSavings())
                && !crimeApplyResponse.getMeansDetails().getCapitalDetails().getSavings().isEmpty();
    }

    boolean hasInvestments(MaatApplicationExternal crimeApplyResponse) {
        return Objects.nonNull(crimeApplyResponse.getMeansDetails().getCapitalDetails().getInvestments())
                && !crimeApplyResponse.getMeansDetails().getCapitalDetails().getInvestments().isEmpty();
    }

    boolean hasProperties(MaatApplicationExternal crimeApplyResponse) {
        return Objects.nonNull(crimeApplyResponse.getMeansDetails().getCapitalDetails().getProperties())
                && !crimeApplyResponse.getMeansDetails().getCapitalDetails().getProperties().isEmpty();
    }

    void mapPropertiesToCapitalEquity(MaatApplicationExternal crimeApplyResponse, CapitalEquity capitalEquity) {
        if (hasProperties(crimeApplyResponse)) {
            for (Property crimeApplyDataStoreProperty: crimeApplyResponse.getMeansDetails().getCapitalDetails().getProperties()) {
                if (isHomeProperty(crimeApplyDataStoreProperty)) {
                    mapPropertyToEquity(crimeApplyDataStoreProperty, capitalEquity.getEquity());
                } else {
                    mapPropertyToCapitalProperty(crimeApplyDataStoreProperty, capitalEquity.getCapitalProperty());
                }
            }
        }
    }

    void mapPropertyToCapitalProperty(Property crimeApplyDataStoreProperty, List<uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.common.Property> capitalProperty) {
        uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.common.Property property = propertyMapper.map(crimeApplyDataStoreProperty);
        capitalProperty.add(property);
    }

    void mapPropertyToEquity(Property crimeApplyDataStoreProperty, List<uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.common.Property> equity) {
        uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.common.Property property = propertyMapper.map(crimeApplyDataStoreProperty);
        equity.add(property);
    }

    boolean isHomeProperty(Property crimeApplyDataStoreProperty) {
        return Objects.nonNull(crimeApplyDataStoreProperty)
                && Objects.nonNull(crimeApplyDataStoreProperty.getIsHomeAddress())
                && crimeApplyDataStoreProperty.getIsHomeAddress() instanceof String
                && IS_HOME_PROPERTY.equals(crimeApplyDataStoreProperty.getIsHomeAddress().toString());
    }

}
