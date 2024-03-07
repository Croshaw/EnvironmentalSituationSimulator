package me.croshaw.ess.app.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import me.croshaw.ess.controller.SimulationController;
import me.croshaw.ess.app.SettingsApplication;
import me.croshaw.ess.model.Company;
import me.croshaw.ess.settings.SimulationSettings;
import me.croshaw.ess.util.RandomUtils;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MainController {
    private final SettingsApplication settingsApplication = new SettingsApplication();
    public static final Stage STAGE = new Stage();
    @FXML
    private void onStartSimulationButtonClick() {
        SimulationController simulationController = SimulationSettings.GET_CONTROLLER();
        try {
            simulationController.start();
//            var temp = simulationController.getJournal().values().stream().map(x-> x.getCompanyController().getCompanies()).flatMap(List::stream).filter(x-> !x.isWork()).toList();
//            var temp2 = simulationController.getJournal().values().stream().map(x-> x.getCompanyController().getCompanies()).flatMap(List::stream).filter(Company::isWork).toList();
//            System.out.println("");
        } catch (CloneNotSupportedException e) {
            System.err.println(e.getMessage());
        }
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
