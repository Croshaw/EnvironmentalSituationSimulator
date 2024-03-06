package me.croshaw.ess.settings;

import java.util.Random;

public interface ISettings {
    void fillRandomly(Random random);
    void fillDefault();
}
