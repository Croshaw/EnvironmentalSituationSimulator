package me.croshaw.ess.settings;

import me.croshaw.ess.model.Weather;

import java.util.Random;

public class CitySettings extends DefaultSettings {
    private Weather startWeather;
    private double startCityFund;
    @Override
    public void fillRandomly(Random random) {
        setStartCityFund((double) (int) (random.nextDouble(15000, 60000) * 100) / 100);
        setStartWeather(SimulationSettings.WEATHERS.get(random.nextInt(0, SimulationSettings.WEATHERS.size())));
    }
    @Override
    public void fillDefault() {
        setStartCityFund(25000);
        setStartWeather(SimulationSettings.WEATHERS.getFirst());
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
}
