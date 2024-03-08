package me.croshaw.ess.controller;

import me.croshaw.ess.model.Car;
import me.croshaw.ess.model.CarSpecialDrivingMode;
import me.croshaw.ess.model.IPollutionMap;
import me.croshaw.ess.settings.CarSettings;
import me.croshaw.ess.settings.MapSettings;

import java.time.Duration;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CarManager implements Cloneable, IPollutionMap {
    private ArrayList<Car> cars;
    private double avgExhaust;
    private double[][] currentPollutionMap;
    private CarSpecialDrivingMode specialDrivingMode;
    public CarManager(CarSettings carSettings, MapSettings mapSettings, Random random) {
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
        currentPollutionMap = new double[mapSettings.getRows()][mapSettings.getColumns()];
        specialDrivingMode = CarSpecialDrivingMode.NONE;
    }
    @Override
    public double[][] getPollutionMap() {
        return currentPollutionMap;
    }
    public void updateMap() {
        long size = specialDrivingMode == CarSpecialDrivingMode.NONE ? cars.size() : cars.stream().filter(specialDrivingMode.getPredicate()).count();
        double exhaust = ((size*0.70)*avgExhaust) / (currentPollutionMap.length*currentPollutionMap[0].length);
        for (double[] doubles : currentPollutionMap) {
            Arrays.fill(doubles, exhaust);
        }
    }
    @Override
    public String getInfo() {
        return "null";
    }
    public CarSpecialDrivingMode getSpecialDrivingMode() {
        return specialDrivingMode;
    }
    public void setSpecialDrivingMode(CarSpecialDrivingMode mode, Duration duration) {
        specialDrivingMode = mode;
        specialDrivingMode.setDuration(duration);
    }

    @Override
    public CarManager clone() {
        try {
            CarManager clone = (CarManager) super.clone();
            // TODO: copy mutable state here, so the clone can't change the internals of the original
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    public double getAvgExhaust() {
        return avgExhaust;
    }

    public ArrayList<Car> getCars() {
        return cars;
    }
    public ArrayList<Car> getCurrentCars() {
        if(specialDrivingMode == CarSpecialDrivingMode.NONE || !specialDrivingMode.isValid()) {
            return new ArrayList<>(cars.stream().limit((int)(cars.size()*0.7)).toList());
        }
        var temp = cars.stream().filter(specialDrivingMode.getPredicate()).toList();
        return new ArrayList<>(temp.stream().limit((long)(temp.size() * 0.7)).toList());
    }
}
