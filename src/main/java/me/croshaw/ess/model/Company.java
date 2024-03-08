package me.croshaw.ess.model;

import me.croshaw.ess.settings.CompanySettings;
import me.croshaw.ess.settings.MapSettings;
import me.croshaw.ess.util.NumberHelper;
import me.croshaw.ess.util.Pair;

import java.time.Duration;
import java.util.Random;

public class Company implements IPollutionMap {
    private final double emission;
    private final double[][] defaultPollutionMap;
    private double[][] currentPollutionMap;
    private final Pair<Integer, Integer> coordinates;
    private float emissionDistribution;
    private float emissionFluctuations;
    private Duration sanctionDuration;
    private int countFilters;
    public Company(double emission, Pair<Integer, Integer> coordinates, CompanySettings companySettings, MapSettings mapSettings) {
        this.emission = emission;
        countFilters = 0;
        this.coordinates = coordinates;
        emissionFluctuations = companySettings.getEmissionFluctuations();
        emissionDistribution = companySettings.getEmissionDistribution();
        defaultPollutionMap = new double[mapSettings.getRows()][mapSettings.getColumns()];
        currentPollutionMap = new double[mapSettings.getRows()][mapSettings.getColumns()];
        fill(emission, coordinates.getFirst(), coordinates.getSecond());
        sanctionDuration = Duration.ZERO;
    }
    private void fill(double emission, int x, int y) {
        if(x == -1 || x == defaultPollutionMap.length || y == -1 || y == defaultPollutionMap[0].length || defaultPollutionMap[x][y] >= emission || emission == 0)
            return;
        defaultPollutionMap[x][y] = emission;
        double newEmission = NumberHelper.round(emission*emissionDistribution, 2);
        fill(newEmission, x-1,y);
        fill(newEmission, x, y+1);
        fill(newEmission, x+1,y);
        fill(newEmission, x,y-1);
        fill(newEmission, x-1,y-1);
        fill(newEmission, x-1,y+1);
        fill(newEmission, x+1,y-1);
        fill(newEmission, x+1,y+1);
    }
    public void addFilter(float filterEmissionReduction) {
        countFilters++;
        for(int i = 0; i < defaultPollutionMap.length; i++) {
            for(int j = 0 ; j < defaultPollutionMap[0].length; j++) {
                defaultPollutionMap[i][j] = NumberHelper.round(defaultPollutionMap[i][j] * (1f - filterEmissionReduction), 2);
            }
        }
    }
    @Override
    public double[][] getPollutionMap() {
        return currentPollutionMap;
    }
    public void updatePollutionMap(Random random) {
        copyFillCurrentPollutionMap();
        for(int i = 0; i < currentPollutionMap.length; i++) {
            for(int j = 0 ; j < currentPollutionMap[0].length; j++) {
                double temp = currentPollutionMap[i][j];
                double low = currentPollutionMap[i][j] - (currentPollutionMap[i][j] * emissionFluctuations);
                double top = currentPollutionMap[i][j] + (currentPollutionMap[i][j] * emissionFluctuations);
                try {
                    currentPollutionMap[i][j] = currentPollutionMap[i][j] == 0 ? currentPollutionMap[i][j] : NumberHelper.round(random.nextDouble(low, top), 2);
                } catch (IllegalArgumentException ex) {
                    System.err.println(ex);
                }
            }
        }
    }
    private void copyFillCurrentPollutionMap() {
        for(int i = 0; i < defaultPollutionMap.length; i++) {
            for(int j = 0; j < defaultPollutionMap[i].length; j++) {
                currentPollutionMap[i][j] = defaultPollutionMap[i][j];
            }
        }
    }
    public boolean isWork() {
        return sanctionDuration.toDays() == 0;
    };
    public void suspendWork(Duration duration) {
        sanctionDuration = duration;
    }
    public void reduceSanction() {
        sanctionDuration = sanctionDuration.minusDays(1);
    }
    public Pair<Integer, Integer> getCoordinates() {
        return coordinates;
    }

    public double getEmission() {
        return emission;
    }

    public Duration getSanctionDuration() {
        return sanctionDuration;
    }

    public int getCountFilters() {
        return countFilters;
    }

    @Override
    public String getInfo() {
        return "null";
    }
}
