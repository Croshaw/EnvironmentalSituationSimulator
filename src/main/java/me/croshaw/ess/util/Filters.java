package me.croshaw.ess.util;

import javafx.scene.control.TextFormatter;

import java.util.function.UnaryOperator;

public class Filters {
    public static final UnaryOperator<TextFormatter.Change> integerFilter = change -> {
        String input = change.getText();

        if ((input.matches("[\\d]+")) || change.isDeleted()) {
            return change;
        }
        return null;
    };
    public static final UnaryOperator<TextFormatter.Change> doubleFilter = change -> {
        String input = change.getText();

        if ((input.matches("[\\d\\.]+")) || change.isDeleted()) {
            return change;
        }
        return null;
    };
}
