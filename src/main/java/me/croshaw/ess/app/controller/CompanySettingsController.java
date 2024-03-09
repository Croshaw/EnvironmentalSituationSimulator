package me.croshaw.ess.app.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.VBox;
import me.croshaw.ess.settings.CompanySettings;
import me.croshaw.ess.settings.SimulationSettings;
import me.croshaw.ess.util.Alerts;
import me.croshaw.ess.util.Filters;
import me.croshaw.ess.util.RandomUtils;

import java.net.URL;
import java.util.ResourceBundle;

public class CompanySettingsController implements Initializable {
    @FXML
    TextField taxField;
    @FXML
    TextField maxEmissionsField;
    @FXML
    TextField fineField;
    @FXML
    Slider countSlider;
    @FXML
    Label countInfo;
    @FXML
    Slider emissionDistributionSlider;
    @FXML
    Label emissionDistributionInfo;
    @FXML
    Slider emissionFluctuationsSlider;
    @FXML
    Label emissionFluctuationsInfo;
    @FXML
    VBox emissionBox;
    CompanySettings companySettings;

    void load() {
        taxField.setText(Double.toString(companySettings.getTax()));
        maxEmissionsField.setText(Double.toString(companySettings.getMaxEmissions()));
        fineField.setText(Double.toString(companySettings.getFine()));
        countSlider.setValue(companySettings.getEmissions().size());
        emissionDistributionSlider.valueProperty().setValue(companySettings.getEmissionDistribution());
        emissionFluctuationsSlider.valueProperty().setValue(companySettings.getEmissionFluctuations());
        while(emissionBox.getChildren().size() < companySettings.getEmissions().size()) {
            int id = emissionBox.getChildren().size();
            TextField textField = new TextField(Double.toString(companySettings.getEmissions().get(id)));
            textField.setTextFormatter(new TextFormatter<>(Filters.doubleFilter));
            textField.textProperty().addListener((obs, oldValue, newValue) -> {
                companySettings.getEmissions().set(id, textField.getText().isEmpty() ? 0 : Double.parseDouble(textField.getText()));
            });
            emissionBox.getChildren().add(textField);
        }
    }
    @FXML
    void onSaveButtonClick() {
        if(companySettings.getEmissions().size() < 5 || companySettings.getEmissions().size() > 12) {
            Alerts.WARNING.setTitle("Внимание");
            Alerts.WARNING.setHeaderText("Кол-во компаний должно быть 5 <= C <= 12");
            Alerts.WARNING.show();
            return;
        }
        SimulationSettings.COMPANY.setEmissionFluctuations(companySettings.getEmissionFluctuations());
        SimulationSettings.COMPANY.setFine(companySettings.getFine());
        SimulationSettings.COMPANY.setMaxEmissions(companySettings.getMaxEmissions());
        SimulationSettings.COMPANY.setTax(companySettings.getTax());
        SimulationSettings.COMPANY.setEmissionDistribution(companySettings.getEmissionDistribution());
        SimulationSettings.COMPANY.setEmissions(companySettings.getEmissions());
    }
    @FXML
    void onRandomFillButtonClick() {
        companySettings.fillRandomly(RandomUtils.RANDOM);
        emissionBox.getChildren().clear();
        load();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        companySettings = SimulationSettings.COMPANY.clone();
        taxField.setTextFormatter(new TextFormatter<>(Filters.doubleFilter));
        taxField.textProperty().addListener((obs, oldValue, newValue)
                -> companySettings.setTax(taxField.getText().isEmpty() ? 0 : Double.parseDouble(taxField.getText())));
        maxEmissionsField.setTextFormatter(new TextFormatter<>(Filters.doubleFilter));
        maxEmissionsField.textProperty().addListener((obs, oldValue, newValue)
                -> companySettings.setMaxEmissions(maxEmissionsField.getText().isEmpty() ? 0 : Double.parseDouble(maxEmissionsField.getText())));
        fineField.setTextFormatter(new TextFormatter<>(Filters.doubleFilter));
        fineField.textProperty().addListener((obs, oldValue, newValue)
                -> companySettings.setFine(fineField.getText().isEmpty() ? 0 : Double.parseDouble(fineField.getText())));
        countSlider.valueProperty().addListener((obs, oldValue, newValue) -> {
            countSlider.valueProperty().setValue(newValue.intValue());
            countInfo.setText(Integer.toString(newValue.intValue()));
            int count = newValue.intValue();
            if(companySettings.getEmissions().size() < count)
                while(companySettings.getEmissions().size() < count)
                    companySettings.getEmissions().add(1d);
            else if(companySettings.getEmissions().size() > count)
                while(companySettings.getEmissions().size() > count)
                    companySettings.getEmissions().removeLast();

            if(emissionBox.getChildren().size() < count) {
                while (emissionBox.getChildren().size() < count) {
                    int ind = emissionBox.getChildren().size();
                    TextField text = new TextField(Double.toString(companySettings.getEmissions().get(ind)));
                    text.setTextFormatter(new TextFormatter<>(Filters.doubleFilter));
                    text.textProperty().addListener((obs1, oldValue1, newValue1) -> {
                        companySettings.getEmissions().set(ind, newValue1.isEmpty() ? 0 : Double.parseDouble(newValue1));
                    });
                    emissionBox.getChildren().add(text);
                }
            }
            else if(emissionBox.getChildren().size() > count) {
                while (emissionBox.getChildren().size() > count) {
                    emissionBox.getChildren().removeLast();
                }
            }
        });
        emissionDistributionSlider.valueProperty().addListener((observableValue, oldValue, newValue) -> {
            float val = (float)(int)(newValue.floatValue() * 100) / 100;
            emissionDistributionInfo.setText(Float.toString(val));
            emissionDistributionSlider.valueProperty().setValue(val);
            companySettings.setEmissionDistribution(val);
        });
        emissionFluctuationsSlider.valueProperty().addListener((observableValue, oldValue, newValue) -> {
            float val = (float)(int)(newValue.floatValue() * 100) / 100;
            emissionFluctuationsInfo.setText(Float.toString(val));
            emissionFluctuationsSlider.valueProperty().setValue(val);
            companySettings.setEmissionFluctuations(val);
        });
        load();
    }
}
