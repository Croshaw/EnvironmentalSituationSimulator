package me.croshaw.ess.model;

import me.croshaw.ess.settings.CompanySettings;
import me.croshaw.ess.settings.FilterSettings;
import me.croshaw.ess.settings.MapSettings;
import me.croshaw.ess.util.NumberHelper;
import me.croshaw.ess.util.Pair;

import java.time.Duration;
import java.util.Random;

public class Company implements Cloneable {
    private final double emission;
    private int countFilters;
    private Pair<Integer, Integer> coordinates;
    private double[][] pollutionMap;
    private float emissionDistribution;
    private float emissionFluctuations;
    private float filterEmissionReduction;
    private Duration sanctionDuration;
    public Company(double emission, Pair<Integer, Integer> coordinates, CompanySettings companySettings, FilterSettings filterSettings, MapSettings mapSettings) {
        this.emission = emission;
        countFilters = 0;
        this.coordinates = coordinates;
        emissionFluctuations = companySettings.getEmissionFluctuations();
        emissionDistribution = companySettings.getEmissionDistribution();
        filterEmissionReduction = filterSettings.getEmissionReduction();
        pollutionMap = new double[mapSettings.getRows()][mapSettings.getColumns()];
        fill(emission, coordinates.getFirst(), coordinates.getSecond());
        sanctionDuration = Duration.ZERO;
    }
    private void fill(double emission, int x, int y) {
        if(x == -1 || x == pollutionMap.length || y == -1 || y == pollutionMap[0].length || pollutionMap[x][y] >= emission || emission == 0)
            return;
        pollutionMap[x][y] = emission;
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
    public void addFilter() {
        countFilters++;
        for(int i = 0; i < pollutionMap.length; i++) {
            for(int j = 0 ; j < pollutionMap[0].length; j++) {
                pollutionMap[i][j] = NumberHelper.round(pollutionMap[i][j] * (1f - filterEmissionReduction), 2);
            }
        }
    }
    public double[][] getPollutionMap() {
        return pollutionMap;
    }
    public double[][] getPollutionMapWithFluctuation(Random random) {
        double[][] result = new double[pollutionMap.length][pollutionMap[0].length];
        for(int i = 0; i < pollutionMap.length; i++) {
            for(int j = 0 ; j < pollutionMap[0].length; j++) {
                double temp = pollutionMap[i][j];
                double low = pollutionMap[i][j] - (pollutionMap[i][j] * emissionFluctuations);
                double top = pollutionMap[i][j] + (pollutionMap[i][j] * emissionFluctuations);
                try {
                    result[i][j] = pollutionMap[i][j] == 0 ? pollutionMap[i][j] : NumberHelper.round(random.nextDouble(low, top), 2);
                } catch (IllegalArgumentException ex) {
                    System.err.println(ex);
                }
            }
        }
        return result;
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
    @Override
    public Company clone() {
        try {
            Company clone = (Company) super.clone();
            // TODO: copy mutable state here, so the clone can't change the internals of the original
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
