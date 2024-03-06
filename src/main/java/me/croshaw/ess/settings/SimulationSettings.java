package me.croshaw.ess.settings;

import me.croshaw.ess.model.Weather;

import java.io.Serializable;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Random;

public final class SimulationSettings implements Serializable {
    public static Random RANDOM = new Random();
    public static ArrayList<Weather> WEATHERS = new ArrayList<>() {{
        add(new Weather("Нормальная", 1f));
        add(new Weather("Ветреная", 1.2f));
        add(new Weather("Дождливая", 1.1f));
        add(new Weather("Дождь+Ветер", 1.4f));
    }};
    public static CarSettings CAR = new CarSettings();
    public static CompanySettings COMPANY = new CompanySettings();
    public static MapSettings MAP = new MapSettings();
    public static CitySettings CITY = new CitySettings();
    public static FilterSettings FILTER = new FilterSettings();
    public static Duration SIMULATION_DURATION = Duration.ofDays(30);
    public static int SIMULATION_STEP = 1;
}
