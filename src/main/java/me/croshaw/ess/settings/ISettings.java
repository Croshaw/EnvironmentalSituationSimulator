package me.croshaw.ess.settings;

import me.croshaw.ess.exception.WrongCoordinatesException;

import java.util.Random;

public interface ISettings {
    void fillRandomly(Random random);
    void fillDefault();
}
