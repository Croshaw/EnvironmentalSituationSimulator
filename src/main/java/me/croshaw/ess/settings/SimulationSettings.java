package me.croshaw.ess.settings;

import javafx.scene.canvas.Canvas;
import me.croshaw.ess.controller.SimulationController;
import me.croshaw.ess.model.CarSpecialDrivingMode;
import me.croshaw.ess.model.Weather;
import me.croshaw.ess.util.NumberHelper;

import java.io.Serializable;
import java.time.Duration;
import java.util.ArrayList;

public final class SimulationSettings implements Serializable {
    public static ArrayList<Weather> WEATHERS = new ArrayList<>() {{
        add(new Weather("Нормальная", 0.9f, 0.2f));
        add(new Weather("Ветреная", 0.75f, 0.3f));
        add(new Weather("Дождливая", 0.8f, 0.6f));
        add(new Weather("Дождь+Ветер", 0.6f, 0.7f));
    }};
    public static ArrayList<CarSpecialDrivingMode> CAR_SPECIAL_DRIVING_MODE = new ArrayList<>() {{
        add(new CarSpecialDrivingMode("Только чётные номера", car -> car.getNumber() % 2 == 0));
        add(new CarSpecialDrivingMode("Только не чётные номера", car -> car.getNumber() % 2 != 0));
        add(new CarSpecialDrivingMode("Хотя бы одна 1 в номере", car -> NumberHelper.hasNumber(car.getNumber(), 1)));
    }};
    public static CarSettings CAR = new CarSettings();
    public static CompanySettings COMPANY = new CompanySettings();
    public static MapSettings MAP = new MapSettings();
    public static CitySettings CITY = new CitySettings();
    public static FilterSettings FILTER = new FilterSettings();
    public static Duration SIMULATION_DURATION = Duration.ofDays(30);
    public static SimulationController GET_CONTROLLER(Canvas canvas) {
        return new SimulationController(CAR, COMPANY, MAP, CITY, FILTER, SIMULATION_DURATION, canvas);
    }
}
