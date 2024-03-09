package me.croshaw.ess.model;

import me.croshaw.ess.settings.CitySettings;
import me.croshaw.ess.settings.MapSettings;
import me.croshaw.ess.util.NumberHelper;
import me.croshaw.ess.util.Pair;

import java.io.Serializable;
import java.util.ArrayList;

public class City implements Cloneable, IPollutionMap, Serializable {
    private Weather currentWeather;
    private double cityFund;
    private double[][] currentPollutionMap;
    private final double[][] passiveReductionPollutionMap;
    private final double permissibleConcentration;
    public City(Weather currentWeather, CitySettings citySettings, MapSettings mapSettings) {
        this.currentWeather = currentWeather;
        this.cityFund = citySettings.getStartCityFund();
        currentPollutionMap = new double[mapSettings.getRows()][mapSettings.getColumns()];
        passiveReductionPollutionMap = new double[mapSettings.getRows()][mapSettings.getColumns()];
        for (int i = 0; i < mapSettings.getRows(); i++) {
            for (int j = 0; j < mapSettings.getColumns(); j++) {
                passiveReductionPollutionMap[i][j] = citySettings.getPassiveReductionPollution();
            }
        }
        permissibleConcentration = citySettings.getPermissibleConcentration();
    }
    public double getPermissibleConcentration() {
        return permissibleConcentration;
    }
    public void updatePollutionMap(double[][] pollutionMap) {
        currentPollutionMap = NumberHelper.merge(currentPollutionMap, pollutionMap);
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
        currentPollutionMap = NumberHelper.matrixReplaceMax(NumberHelper.matrixMinus(NumberHelper.matrixMinus(currentPollutionMap), getPassiveReductionPollutionMap()), 0);
    }
    public void setCurrentWeather(Weather weather) {
        currentWeather = weather;
    }
    @Override
    public double[][] getPollutionMap() {
        return currentPollutionMap;
    }

    @Override
    public String getInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("Текущая погода: %s\n".formatted(currentWeather.name()));
        sb.append("Казна: %s\n".formatted(cityFund));
        sb.append("Максимально допустимая концентрация: %s".formatted(permissibleConcentration));
        return sb.toString();
    }
    public ArrayList<Pair<Integer, Integer>> getPointsWithIncreasingValues() {
        ArrayList<Pair<Integer, Integer>> result = new ArrayList<>();
        for(int i = 0; i < currentPollutionMap.length; i++) {
            for(int j = 0; j < currentPollutionMap[i].length; j++) {
                if(currentPollutionMap[i][j] > permissibleConcentration) {
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

    public Weather getCurrentWeather() {
        return currentWeather;
    }

    @Override
    public String toString() {
         return getInfo();
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
