package me.croshaw.ess.settings;

import java.util.Random;

public class CompanySettings extends DefaultSettings {
    private double tax;
    private double maxEmissions;

    @Override
    public void fillRandomly(Random random) {
        setTax((double) (int) (random.nextDouble(5000, 50000) * 100) / 100);
        setMaxEmissions((double) (int) (random.nextDouble(3, 20) * 100) / 100);
    }

    @Override
    public void fillDefault() {
        setTax(10000);
        setMaxEmissions(10);
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
}
