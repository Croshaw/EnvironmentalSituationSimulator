package me.croshaw.ess.model;

import java.io.Serializable;

public record Weather(String name, float multiplier, float chanceOfChange) implements Serializable {
    @Override
    public String toString() {
        return name;
    }
}
