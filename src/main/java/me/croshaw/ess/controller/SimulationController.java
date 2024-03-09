package me.croshaw.ess.controller;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.canvas.Canvas;
import me.croshaw.ess.model.CarSpecialDrivingMode;
import me.croshaw.ess.model.City;
import me.croshaw.ess.model.Company;
import me.croshaw.ess.model.SimulationSummary;
import me.croshaw.ess.settings.*;
import me.croshaw.ess.util.NumberHelper;
import me.croshaw.ess.util.RandomUtils;
import me.croshaw.ess.view.CityView;
import me.croshaw.ess.view.SimulationView;

import java.io.Serializable;
import java.time.Duration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.function.UnaryOperator;

public class SimulationController implements Serializable {
    public final FilterSettings filterSettings;
    public final CitySettings citySettings;
    public final Duration simulationDuration;
    private int currentDay;
    private transient final HashMap<Integer, SimulationSummary> journal;
    private final City city;
    private final CompanyManager companyManager;
    private final CarManager carManager;
    private transient SimulationView simulationView;
    private transient Timeline simulationTimeline;
    private transient Timeline drawTimeLine;
    private float speed;
    private HashSet<Company> badCompany;
    public SimulationController(CarSettings carSettings
            , CompanySettings companySettings
            , MapSettings mapSettings
            , CitySettings citySettings
            , FilterSettings filterSettings
            , Duration simulationDuration, Canvas canvas) {
        this.filterSettings = filterSettings;
        this.simulationDuration = simulationDuration;
        currentDay = 0;
        journal = new HashMap<>();
        city = new City(citySettings.getStartWeather(), citySettings, mapSettings);
        companyManager = new CompanyManager(companySettings, mapSettings);
        carManager = new CarManager(carSettings, mapSettings, RandomUtils.RANDOM);
        this.citySettings = citySettings;
        speed = 0.1f;
        badCompany = new HashSet<>();
        setupView(canvas);
    }
    public void setupCarMode() {
        if(carManager.getSpecialDrivingMode().getPredicate() == null)
            carManager.setSpecialDrivingMode(CarSpecialDrivingMode.NONE, Duration.ZERO);
        else {
            var sp = carManager.getSpecialDrivingMode();
            for(int i = 0; i < SimulationSettings.CAR_SPECIAL_DRIVING_MODE.size();i++) {
                if(SimulationSettings.CAR_SPECIAL_DRIVING_MODE.get(i).getPredicate().equals(sp.getPredicate())){
                    carManager.setSpecialDrivingMode(SimulationSettings.CAR_SPECIAL_DRIVING_MODE.get(i), sp.getDuration());
                    break;
                }
            }
        }
    }
    public void setupView(Canvas canvas) {
        simulationView = new SimulationView(city, citySettings, canvas);
    }
    public City getCity() {
        return city;
    }
    public void setupTimeline(Runnable runnable) {
        simulationTimeline = new Timeline(
                new KeyFrame(javafx.util.Duration.millis(20)),
                //new KeyFrame(javafx.util.Duration.ZERO, actionEvent -> journal.put(currentDay, new SimulationSummary(city, companyManager, carManager))),
                new KeyFrame(javafx.util.Duration.ZERO, actionEvent -> {
                    if(currentDay <= simulationDuration.toDays()) {
                        //runnable.run();
                        simulate();
                        currentDay++;
                    } else {
                        simulationTimeline.stop();
                    }
                })
        );
        simulationTimeline.setCycleCount(Animation.INDEFINITE);
        simulationTimeline.rateProperty().setValue(speed);
    }
    public void tryToSetupFilter(Company company) {
        if(city.getCityFund() > filterSettings.getPrice()) {
            city.removeFromFund(filterSettings.getPrice());
            company.addFilter(filterSettings.getEmissionReduction());
        }
    }
    public void stop() {
        if(simulationTimeline != null)
            simulationTimeline.stop();
        if(drawTimeLine != null)
            drawTimeLine.stop();
    }
    public void pause() {
        if(simulationTimeline != null)
            simulationTimeline.pause();
    }
    public void resume() {
        if(simulationTimeline != null)
            simulationTimeline.play();
    }
    public void start(Runnable runnable) {
        setupTimeline(runnable);
        simulationTimeline.play();
        drawTimeLine = new Timeline(
                new KeyFrame(javafx.util.Duration.millis(20)),
                new KeyFrame(javafx.util.Duration.ONE, actionEvent -> simulationView.draw()),
                new KeyFrame(javafx.util.Duration.ONE, actionEvent -> runnable.run())
        );
        drawTimeLine.setCycleCount(Animation.INDEFINITE);
        drawTimeLine.play();
    }
    private void simulate() {
        companyManager.reduceSanctions();
        if(RandomUtils.RANDOM.nextFloat() <= city.getCurrentWeather().chanceOfChange()) {
            city.setCurrentWeather(SimulationSettings.WEATHERS.get(RandomUtils.RANDOM.nextInt(0, SimulationSettings.WEATHERS.size())));
        }

        //? Шаг 1
        companyManager.updatePollutionMap(RandomUtils.RANDOM);
        carManager.updateMap();
        city.updatePollutionMap(NumberHelper.merge(companyManager.getPollutionMap(), carManager.getPollutionMap()));
        city.reducePollution();
        var pointsWithIncreasingValues = city.getPointsWithIncreasingValues(); //! Доделать??

        //? Шаг 2
        city.addToFund(companyManager.getTaxes());

        //? Шаг 3 штрафные санкции
        var fines = companyManager.getFinesAndBadCompanies(city.getPollutionMap());
        city.addToFund(fines.getFirst());
        badCompany = fines.getSecond();
        badCompany.forEach(company -> company.suspendWork(Duration.ofDays(3)));

        //? Шаг 4
        if(carManager.getSpecialDrivingMode().isValid()) {
            carManager.getSpecialDrivingMode().setDuration(carManager.getSpecialDrivingMode().getDuration().minusDays(1));
        } else if(!pointsWithIncreasingValues.isEmpty()) {
            setSpecialDrivingMode(SimulationSettings.CAR_SPECIAL_DRIVING_MODE.get(RandomUtils.RANDOM.nextInt(0, SimulationSettings.CAR_SPECIAL_DRIVING_MODE.size()))
            , RandomUtils.RANDOM.nextInt(1, 5));
        }
        //? Шаг 5
        fines.getSecond().forEach(this::tryToSetupFilter);
    }
    public void setSpecialDrivingMode(CarSpecialDrivingMode mode, long dayCount) {
        carManager.setSpecialDrivingMode(mode, Duration.ofDays(dayCount));
    }
    public HashMap<Integer, SimulationSummary> getJournal() {
        return journal;
    }
    public CompanyManager getCompanyManager() {
        return companyManager;
    }
    public HashSet<Company> getBadCompany() {
        if(badCompany == null)
            badCompany = new HashSet<>();
        return badCompany;
    }
    public int getCurrentDay() {
        return currentDay;
    }

    public CarManager getCarManager() {
        return carManager;
    }
    public void setSpeed(float speed) {
        simulationTimeline.rateProperty().setValue(speed);
        this.speed = speed;
    }
}
