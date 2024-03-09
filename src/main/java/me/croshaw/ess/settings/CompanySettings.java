package me.croshaw.ess.settings;

import me.croshaw.ess.util.NumberHelper;
import me.croshaw.ess.util.Range;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class CompanySettings extends DefaultSettings implements Serializable, Cloneable {
    public static final Range<Integer> COMPANY_COUNT_RANGE = new Range<>(5, 12);
    public static final Range<Double> EMISSIONS_COUNT_RANGE = new Range<>(1d, 2.7d);
    public static final Range<Double> TAX_RANGE = new Range<>(10d, 40000d);
    public static final Range<Double> MAX_EMISSIONS_RANGE = new Range<>(5d, 11d);
    public static final Range<Double> FINE_RANGE = new Range<>(10000d, 60000d);
    public static final Range<Float> EMMISION_DISTRIBUTION_RANGE = new Range<>(.01f, 1f);
    public static final Range<Float> EMMISION_FLUCTUATIONS_RANGE = new Range<>(.01f, 1f);
    private double tax;
    private double maxEmissions;
    private double fine;
    private float emissionDistribution;
    private float emissionFluctuations;
    private ArrayList<Double> emissions;
    @Override
    public void fillRandomly(Random random) {
        setTax(NumberHelper.round(TAX_RANGE.getRandom(random), 2));
        setMaxEmissions(NumberHelper.round(MAX_EMISSIONS_RANGE.getRandom(random), 2));
        setFine(NumberHelper.round(FINE_RANGE.getRandom(random), 2));
        setEmissionDistribution(NumberHelper.round(EMMISION_DISTRIBUTION_RANGE.getRandom(random), 2));
        setEmissionFluctuations(NumberHelper.round(EMMISION_FLUCTUATIONS_RANGE.getRandom(random), 2));
        ArrayList<Double> arrayList = new ArrayList<>();
        for(int i = 0; i < COMPANY_COUNT_RANGE.getRandom(random); i++)
            arrayList.add(NumberHelper.round(EMISSIONS_COUNT_RANGE.getRandom(random), 2));
        setEmissions(arrayList);
    }

    @Override
    public void fillDefault() {
        setTax(TAX_RANGE.getMin());
        setMaxEmissions(MAX_EMISSIONS_RANGE.getMin());
        setFine(FINE_RANGE.getMin());
        setEmissionDistribution(EMMISION_DISTRIBUTION_RANGE.getMin());
        setEmissionFluctuations(EMMISION_FLUCTUATIONS_RANGE.getMin());
        ArrayList<Double> arrayList = new ArrayList<>();
        for(int i = 0; i < COMPANY_COUNT_RANGE.getMin(); i++)
            arrayList.add(EMISSIONS_COUNT_RANGE.getMin());
        setEmissions(arrayList);
    }

    public double getTax() {
        return tax;
    }

    public void setTax(double tax) {
        this.tax = tax;
    }

    public double getMaxEmissions() {
        return maxEmissions;
    }

    public void setMaxEmissions(double maxEmissions) {
        this.maxEmissions = maxEmissions;
    }

    public double getFine() {
        return fine;
    }

    public void setFine(double fine) {
        this.fine = fine;
    }

    public float getEmissionDistribution() {
        return emissionDistribution;
    }

    public void setEmissionDistribution(float emissionDistribution) {
        this.emissionDistribution = emissionDistribution;
    }

    public float getEmissionFluctuations() {
        return emissionFluctuations;
    }

    public void setEmissionFluctuations(float emissionFluctuations) {
        this.emissionFluctuations = emissionFluctuations;
    }

    public ArrayList<Double> getEmissions() {
        return emissions;
    }

    public void setEmissions(ArrayList<Double> emissions) {
        this.emissions = emissions;
    }

    @Override
    public CompanySettings clone() {
        try {
            return (CompanySettings) super.clone();
        } catch (CloneNotSupportedException ex) {
            System.err.println(ex.getMessage());
        }
        return null;
    }
}
