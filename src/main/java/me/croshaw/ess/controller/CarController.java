package me.croshaw.ess.controller;

import me.croshaw.ess.model.Car;
import me.croshaw.ess.settings.CarSettings;
import me.croshaw.ess.settings.MapSettings;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.function.Predicate;

public class CarController implements Cloneable {
    private ArrayList<Car> cars;
    private double avgExhaust;
    private int rows;
    private int cols;
    public CarController (CarSettings carSettings, MapSettings mapSettings, Random random) {
        cars = new ArrayList<>();
        Set<Integer> carNumbersSet = new HashSet<>();
        int carCount = carSettings.getCarCount();
        while (cars.size() < carCount) {
            int number = random.nextInt(1000, 9999);
            if (carNumbersSet.add(number)) {
                cars.add(new Car(number));
            }
        }
        avgExhaust = carSettings.getExhaust();
        rows = mapSettings.getRows();
        cols = mapSettings.getColumns();
    }
    public double[][] getPollutionMap() {
        double[][] result = new double[rows][cols];
        double exhaust = ((cars.size()*0.70)*avgExhaust) / (rows*cols);
        for(int i = 0; i < rows; i++) {
            for(int j = 0 ; j < cols; j++) {
                result[i][j] = exhaust;
            }
        }
        return result;
    }
    public double[][] getPollutionMapWithCondition(Predicate<Car> predicate) {
        double[][] result = new double[rows][cols];
        double exhaust = ((cars.stream().filter(predicate).count()*0.70)*avgExhaust) / (rows*cols);
        for(int i = 0; i < rows; i++) {
            for(int j = 0 ; j < cols; j++) {
                result[i][j] = exhaust;
            }
        }
        return result;
    }

    @Override
    public CarController clone() {
        try {
            CarController clone = (CarController) super.clone();
            // TODO: copy mutable state here, so the clone can't change the internals of the original
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
