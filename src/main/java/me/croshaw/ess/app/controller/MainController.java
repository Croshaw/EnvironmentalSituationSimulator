package me.croshaw.ess.app.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import me.croshaw.ess.app.SettingsApplication;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    private final SettingsApplication settingsApplication = new SettingsApplication();
    public static final Stage STAGE = new Stage();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
    @FXML
    private void onStartSimulationButtonClick() {

    }
    @FXML
    private void onSettingsButtonClick() {
        try {
            settingsApplication.start(STAGE);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
