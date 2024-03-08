package me.croshaw.ess.app.controller;

import javafx.fxml.FXML;
import javafx.stage.Stage;
import me.croshaw.ess.app.SimulationApplication;
import me.croshaw.ess.app.SettingsApplication;

public class MainController {
    private final SettingsApplication settingsApplication = new SettingsApplication();
    private final SimulationApplication simulationApplication = new SimulationApplication();
    @FXML
    private void onStartSimulationButtonClick() {
        try {
            simulationApplication.start(new Stage());
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    @FXML
    private void onSettingsButtonClick() {
        try {
            settingsApplication.start(new Stage());
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
