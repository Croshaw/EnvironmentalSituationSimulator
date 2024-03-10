package me.croshaw.ess.model;

import java.io.Serializable;
import java.time.Duration;
import java.util.function.Predicate;

public class CarSpecialDrivingMode implements Cloneable, Serializable {
    public static CarSpecialDrivingMode NONE = new CarSpecialDrivingMode("",null);
    private final Predicate<Car> predicate;
    private Duration duration;
    private final String name;

    public CarSpecialDrivingMode(String name, Predicate<Car> predicate) {
        this.name = name;
        this.predicate = predicate;
        duration = Duration.ZERO;
    }
    public String getName() {
        return name;
    }

    public Predicate<Car> getPredicate() {
        return predicate;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }
    public boolean isValid() {
        return duration.toDays() != 0;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public CarSpecialDrivingMode clone() {
        try {
            CarSpecialDrivingMode clone = (CarSpecialDrivingMode) super.clone();
            // TODO: copy mutable state here, so the clone can't change the internals of the original
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
