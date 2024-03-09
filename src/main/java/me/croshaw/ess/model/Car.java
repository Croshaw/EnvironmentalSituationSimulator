package me.croshaw.ess.model;

import java.io.Serializable;

public class Car implements Serializable {
    private final int number;

    public Car(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }
}
