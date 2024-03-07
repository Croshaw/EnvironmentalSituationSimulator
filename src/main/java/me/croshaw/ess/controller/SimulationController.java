package me.croshaw.ess.controller;

import me.croshaw.ess.model.CarSpecialDrivingMode;
import me.croshaw.ess.model.City;
import me.croshaw.ess.model.Company;
import me.croshaw.ess.model.SimulationSummary;
import me.croshaw.ess.settings.*;
import me.croshaw.ess.util.NumberHelper;
import me.croshaw.ess.util.RandomUtils;

import java.time.Duration;
import java.util.HashMap;

public class SimulationController {
//    public final CarSettings carSettings;
//    public final CompanySettings companySettings;
//    public final MapSettings mapSettings;
//    public final CitySettings citySettings;
    public final FilterSettings filterSettings;
    public final Duration simulationDuration;
    private int currentDay;
    private final HashMap<Integer, SimulationSummary> journal;
    private final City city;
    private final CompanyController companyController;
    private final CarController carController;
    private CarSpecialDrivingMode specialDrivingMode;
    public SimulationController(CarSettings carSettings
            , CompanySettings companySettings
            , MapSettings mapSettings
            , CitySettings citySettings
            , FilterSettings filterSettings
            , Duration simulationDuration) {
//        this.carSettings = carSettings;
//        this.companySettings = companySettings;
//        this.mapSettings = mapSettings;
//        this.citySettings = citySettings;
        this.filterSettings = filterSettings;
        this.simulationDuration = simulationDuration;
        currentDay = 0;
        journal = new HashMap<>();
        city = new City(citySettings.getStartWeather(), citySettings, mapSettings);

        companyController = new CompanyController(companySettings, mapSettings, filterSettings);
        carController = new CarController(carSettings, mapSettings, RandomUtils.RANDOM);
        specialDrivingMode = CarSpecialDrivingMode.NONE;
    }
    public void tryToSetupFilter(Company company) {
        if(city.getCityFund() > filterSettings.getPrice()) {
            city.removeFromFund(filterSettings.getPrice());
            company.addFilter();
        }
    }
    public void start() throws CloneNotSupportedException {
        while(currentDay <= simulationDuration.toDays()) {
            journal.put(currentDay, new SimulationSummary(city, companyController, carController, specialDrivingMode));
            simulate();
            currentDay++;
        }
    }
    private void simulate() {
        companyController.reduceSanctions();

        //? Шаг 1
        city.updatePollutionMap(NumberHelper.merge(companyController.getPollutionMapWithFluctuation(RandomUtils.RANDOM), (specialDrivingMode != CarSpecialDrivingMode.NONE && specialDrivingMode.isValid()) ? carController.getPollutionMapWithCondition(specialDrivingMode.getPredicate()) : carController.getPollutionMap()));
        city.reducePollution();
        var pointsWithIncreasingValues = city.getPointsWithIncreasingValues(); //! Доделать

        //? Шаг 2
        city.addToFund(companyController.getTaxes());

        //? Шаг 3 штрафные санкции
        var dd = companyController.getFinesAndBadCompanies(city.getPollutionMap());
        city.addToFund(dd.getFirst());
        dd.getSecond().forEach(company -> company.suspendWork(Duration.ofDays(3)));

        //? Шаг 4
        if(specialDrivingMode.isValid()) {
            specialDrivingMode.setDuration(specialDrivingMode.getDuration().minusDays(1));
        } else if(!pointsWithIncreasingValues.isEmpty()) {
            setSpecialDrivingMode(SimulationSettings.CAR_SPECIAL_DRIVING_MODE.get(RandomUtils.RANDOM.nextInt(0, SimulationSettings.CAR_SPECIAL_DRIVING_MODE.size()))
            , RandomUtils.RANDOM.nextInt(1, 5));
        }
        //? Шаг 5
        dd.getSecond().forEach(this::tryToSetupFilter);
    }
    public void setSpecialDrivingMode(CarSpecialDrivingMode mode, long dayCount) {
        specialDrivingMode = mode;
        specialDrivingMode.setDuration(Duration.ofDays(dayCount));
    }
    public HashMap<Integer, SimulationSummary> getJournal() {
        return journal;
    }
}
