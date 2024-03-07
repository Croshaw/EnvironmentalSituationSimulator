package me.croshaw.ess.model;

import me.croshaw.ess.settings.CitySettings;
import me.croshaw.ess.settings.MapSettings;
import me.croshaw.ess.util.NumberHelper;
import me.croshaw.ess.util.Pair;

import java.util.ArrayList;

public class City implements Cloneable {
    private Weather currentWeather;
    private double cityFund;
    private double[][] pollutionMap;
    private double[][] passiveReductionPollutionMap;
    private final double permissibleConcentration;
    public City(Weather currentWeather, CitySettings citySettings, MapSettings mapSettings) {
        this.currentWeather = currentWeather;
        this.cityFund = citySettings.getStartCityFund();
        pollutionMap = new double[mapSettings.getRows()][mapSettings.getColumns()];
        passiveReductionPollutionMap = new double[mapSettings.getRows()][mapSettings.getColumns()];
        for (int i = 0; i < mapSettings.getRows(); i++) {
            for (int j = 0; j < mapSettings.getColumns(); j++) {
                passiveReductionPollutionMap[i][j] = citySettings.getPassiveReductionPollution();
            }
        }
        permissibleConcentration = citySettings.getPermissibleConcentration();
    }
    public void updatePollutionMap(double[][] pollutionMap) {
        for(int i = 0; i < pollutionMap.length; i++) {
            for(int j = 0; j < pollutionMap[i].length; j++) {
                this.pollutionMap[i][j] += pollutionMap[i][j];
            }
        }
    }
    private double[][] getPassiveReductionPollutionMap() {
        double[][] result = new double[passiveReductionPollutionMap.length][passiveReductionPollutionMap[0].length];
        for(int i = 0 ; i < passiveReductionPollutionMap.length; i++) {
            for(int j = 0; j < passiveReductionPollutionMap[i].length; j++) {
                result[i][j] = passiveReductionPollutionMap[i][j] * currentWeather.multiplier();
            }
        }
        return result;
    }
    public void reducePollution() {
        pollutionMap = NumberHelper.matrixReplaceMax(NumberHelper.matrixMinus(NumberHelper.matrixMinus(pollutionMap), getPassiveReductionPollutionMap()), 0);
    }
    public void setCurrentWeather(Weather weather) {
        currentWeather = weather;
    }
    public double[][] getPollutionMap() {
        return pollutionMap;
    }
    public ArrayList<Pair<Integer, Integer>> getPointsWithIncreasingValues() {
        ArrayList<Pair<Integer, Integer>> result = new ArrayList<>();
        for(int i = 0 ; i < pollutionMap.length; i++) {
            for(int j = 0 ; j < pollutionMap[i].length; j++) {
                if(pollutionMap[i][j] > permissibleConcentration) {
                    result.add(new Pair<>(i, j));
                }
            }
        }
        return result;
    }
    public void addToFund(double money) {
        cityFund += money;
    }
    public void removeFromFund(double money) {
        cityFund -= money;
    }
    public double getCityFund() {
        return cityFund;
    }

    @Override
    public City clone() {
        try {
            return (City) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
