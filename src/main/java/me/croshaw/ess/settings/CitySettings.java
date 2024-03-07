package me.croshaw.ess.settings;

import me.croshaw.ess.model.Weather;
import me.croshaw.ess.util.NumberHelper;
import me.croshaw.ess.util.Range;

import java.io.Serializable;
import java.util.Random;

public class CitySettings extends DefaultSettings implements Serializable {
    public final static Range<Double> START_FUND_RANGE = new Range<>(15000d, 60000d);
    public final static Range<Double> PERMISSIBLE_CONCENTRATION_RANGE = new Range<>(30d, 100d);
    private Weather startWeather;
    private double startCityFund;
    private double permissibleConcentration;
    private double passiveReductionPollution = 5;
    @Override
    public void fillRandomly(Random random) {
        setStartCityFund(NumberHelper.round(START_FUND_RANGE.getRandom(random), 2));
        setStartWeather(SimulationSettings.WEATHERS.get(random.nextInt(0, SimulationSettings.WEATHERS.size())));
        setPermissibleConcentration(NumberHelper.round(PERMISSIBLE_CONCENTRATION_RANGE.getRandom(random), 2));
    }
    @Override
    public void fillDefault() {
        setStartCityFund(START_FUND_RANGE.getMin());
        setStartWeather(SimulationSettings.WEATHERS.getFirst());
        setPermissibleConcentration(PERMISSIBLE_CONCENTRATION_RANGE.getMin());
    }
    public Weather getStartWeather() {
        return startWeather;
    }

    public void setStartWeather(Weather startWeather) {
        this.startWeather = startWeather;
    }

    public double getStartCityFund() {
        return startCityFund;
    }

    public void setStartCityFund(double startCityFund) {
        this.startCityFund = startCityFund;
    }

    public double getPermissibleConcentration() {
        return permissibleConcentration;
    }

    public void setPermissibleConcentration(double permissibleConcentration) {
        this.permissibleConcentration = permissibleConcentration;
    }

    public double getPassiveReductionPollution() {
        return passiveReductionPollution;
    }

    public void setPassiveReductionPollution(double passiveReductionPollution) {
        this.passiveReductionPollution = passiveReductionPollution;
    }
}
