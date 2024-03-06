package me.croshaw.ess.app.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import me.croshaw.ess.app.MapSettingsApplication;
import me.croshaw.ess.model.Weather;
import me.croshaw.ess.settings.*;
import me.croshaw.ess.util.Filters;

import java.io.*;
import java.net.URL;
import java.time.Duration;
import java.util.ResourceBundle;

public class SettingsController implements Initializable {
    private final FileChooser fileChooser = new FileChooser();
    private final FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("DAT files (*.dat)", "*.dat");
    private final Alert alert = new Alert(Alert.AlertType.WARNING);
    private final MapSettingsApplication mapSettingsApplication = new MapSettingsApplication();
    private final Stage stage = new Stage();
    //region CarSettings
    @FXML
    HBox carSettingsBox;
    @FXML
    CheckBox randomCarSettings;
    @FXML
    Slider countCarSlider;
    @FXML
    Label countCarInfo;
    @FXML
    TextField carExhaustField;
    //endregion
    //region CompanySettings
    @FXML
    HBox companySettingsBox;
    @FXML
    CheckBox randomCompanySettings;
    @FXML
    TextField taxField;
    @FXML
    TextField companyMaxEmissionsField;
    //endregion
    //region MapSettings
    @FXML
    CheckBox randomMapSettings;
    @FXML
    Button mapSettingsButton;
    //endregion
    //region FilterSettings
    @FXML
    HBox filterSettingsBox;
    @FXML
    CheckBox randomFilterSettings;
    @FXML
    TextField filterPriceField;
    @FXML
    Slider filterDurationInstallationSlider;
    @FXML
    Label durationInstallationInfo;
    @FXML
    Slider filterEmissionReductionSlider;
    @FXML
    Label emissionReductionInfo;
    //endregion
    //region CitySettings
    @FXML
    HBox citySettingsBox;
    @FXML
    CheckBox randomCitySettings;
    @FXML
    ComboBox<Weather> weatherCB;
    @FXML
    TextField startCityFundField;
    //endregion
    //region SimulationSettings
    @FXML
    HBox simulationSettingsBox;
    @FXML
    CheckBox randomSimulationSettings;
    @FXML
    Slider simulationDurationSlider;
    @FXML
    Label simulationDurationInfo;
    @FXML
    Slider simulationStepSlider;
    @FXML
    Label simulationStepInfo;
    //endregion
    private void loadSettings() {
        //? Авто
        countCarSlider.valueProperty().setValue(SimulationSettings.CAR.getCarCount());
        carExhaustField.setText(Double.toString(SimulationSettings.CAR.getExhaust()));
        //? Компания
        taxField.setText(Double.toString(SimulationSettings.COMPANY.getTax()));
        companyMaxEmissionsField.setText(Double.toString(SimulationSettings.COMPANY.getMaxEmissions()));
        //? Фильтры
        filterPriceField.setText(Double.toString(SimulationSettings.FILTER.getPrice()));
        filterDurationInstallationSlider.valueProperty().setValue(SimulationSettings.FILTER.getInstallationDuration().toDays());
        filterEmissionReductionSlider.valueProperty().setValue(SimulationSettings.FILTER.getEmissionReduction());
        //? Город
        startCityFundField.setText(Double.toString(SimulationSettings.CITY.getStartCityFund()));
        weatherCB.setValue(SimulationSettings.CITY.getStartWeather());
        //? Симуляция
        simulationDurationSlider.valueProperty().setValue(SimulationSettings.SIMULATION_DURATION.toDays());
        simulationStepSlider.valueProperty().setValue(SimulationSettings.SIMULATION_STEP);
    }
    private boolean isCarSettingsFilled() {
        return randomCarSettings.selectedProperty().getValue() || !carExhaustField.getText().isEmpty();
    }
    private boolean isCompanySettingsFilled() {
        return randomCompanySettings.selectedProperty().getValue() || (!taxField.getText().isEmpty() && !companyMaxEmissionsField.getText().isEmpty());
    }
    private boolean isMapSettingsFilled() {
        //! ВНИМАНИЕ
        return randomMapSettings.selectedProperty().getValue() || SimulationSettings.MAP.getCompanyCount() >= 5;
    }
    private boolean isFilterSettingsFilled() {
        return randomFilterSettings.selectedProperty().getValue() || !filterPriceField.getText().isEmpty();
    }
    private boolean isCitySettingsFilled() {
        return randomCitySettings.selectedProperty().getValue() || (weatherCB.getValue() != null && !startCityFundField.getText().isEmpty());
    }
    private boolean isSettingsFilled() {
        return isCarSettingsFilled() && isCompanySettingsFilled() && isMapSettingsFilled()
                && isFilterSettingsFilled() && isCitySettingsFilled();
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fileChooser.getExtensionFilters().add(extFilter);
        //? Авто
        randomCarSettings.selectedProperty().addListener((obs, oldValue, newValue) -> {
            carSettingsBox.getChildren().forEach(node -> node.disableProperty().setValue(newValue));
        });
        countCarSlider.valueProperty().addListener((obs, oldValue, newValue) -> {
            countCarSlider.setValue(newValue.intValue());
            countCarInfo.setText(Integer.toString(newValue.intValue()));
        });
        carExhaustField.setTextFormatter(new TextFormatter<>(Filters.doubleFilter));
        //? Компания
        randomCompanySettings.selectedProperty().addListener((obs, oldValue, newValue) -> {
            companySettingsBox.getChildren().forEach(node -> node.disableProperty().setValue(newValue));
        });
        taxField.setTextFormatter(new TextFormatter<>(Filters.doubleFilter));
        companyMaxEmissionsField.setTextFormatter(new TextFormatter<>(Filters.doubleFilter));
        //? Карта
        randomMapSettings.selectedProperty().addListener((obs, oldValue, newValue) -> {
            mapSettingsButton.disableProperty().setValue(newValue);
        });
        mapSettingsButton.setOnAction(actionEvent -> {
            try {
                mapSettingsApplication.start(stage);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        //? Фильтры
        randomFilterSettings.selectedProperty().addListener((obs, oldValue, newValue) -> {
            filterSettingsBox.getChildren().forEach(node -> node.disableProperty().setValue(newValue));
        });
        filterDurationInstallationSlider.valueProperty().addListener((obs, oldValue, newValue) -> {
            filterDurationInstallationSlider.setValue(newValue.intValue());
            durationInstallationInfo.setText(newValue.intValue() + " дней");
        });
        filterEmissionReductionSlider.valueProperty().addListener((obs, oldValue, newValue) -> {
            float val = (float) (int) (newValue.floatValue() * 100) / 100;
            filterEmissionReductionSlider.setValue(val);
            emissionReductionInfo.setText(Float.toString(val));
        });
        filterPriceField.setTextFormatter(new TextFormatter<>(Filters.doubleFilter));
        //? Город
        randomCitySettings.selectedProperty().addListener((obs, oldValue, newValue) -> {
            citySettingsBox.getChildren().forEach(node -> node.disableProperty().setValue(newValue));
        });
        startCityFundField.setTextFormatter(new TextFormatter<>(Filters.doubleFilter));
        ObservableList<Weather> weathers = FXCollections.observableArrayList();
        weathers.addAll(SimulationSettings.WEATHERS);
        weatherCB.setItems(weathers);
        //? Симуляция
        randomSimulationSettings.selectedProperty().addListener((obs, oldValue, newValue) -> {
            simulationSettingsBox.getChildren().forEach(node -> node.disableProperty().setValue(newValue));
        });
        simulationDurationSlider.valueProperty().addListener((obs, oldValue, newValue) -> {
            simulationDurationSlider.setValue(newValue.intValue());
            simulationDurationInfo.setText(Integer.toString(newValue.intValue()));
        });
        simulationStepSlider.valueProperty().addListener((obs, oldValue, newValue) -> {
            simulationStepSlider.setValue(newValue.intValue());
            simulationStepInfo.setText(Integer.toString(newValue.intValue()));
        });
        loadSettings();
    }
    @FXML
    private void onSaveButtonClick() {
        File file = fileChooser.showSaveDialog(stage);
        if(file == null)
            return;
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file)))
        {
            oos.writeObject(SimulationSettings.CAR);
            oos.writeObject(SimulationSettings.COMPANY);
            oos.writeObject(SimulationSettings.MAP);
            oos.writeObject(SimulationSettings.CITY);
            oos.writeObject(SimulationSettings.FILTER);
            oos.writeObject(SimulationSettings.SIMULATION_DURATION);
            oos.writeObject(SimulationSettings.SIMULATION_STEP);
        }
        catch(Exception ex){
            System.err.println(ex.getMessage());
        }
    }
    @FXML
    private void onLoadButtonClick() {
        File file = fileChooser.showOpenDialog(stage);
        if (file == null)
            return;
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file)))
        {
            SimulationSettings.CAR =(CarSettings) ois.readObject();
            SimulationSettings.COMPANY = (CompanySettings) ois.readObject();
            SimulationSettings.MAP = (MapSettings) ois.readObject();
            SimulationSettings.CITY = (CitySettings) ois.readObject();
            SimulationSettings.CITY.setStartWeather(SimulationSettings.WEATHERS.stream()
                    .filter(weather -> weather.name().equals(SimulationSettings.CITY.getStartWeather().name()))
                    .findFirst().orElse(SimulationSettings.WEATHERS.getFirst()));
            SimulationSettings.FILTER = (FilterSettings) ois.readObject();
            SimulationSettings.SIMULATION_DURATION = (Duration) ois.readObject();
            SimulationSettings.SIMULATION_STEP = (int) ois.readObject();
            loadSettings();
        }
        catch(Exception ex) {
            System.err.println(ex.getMessage());
        }
    }
    @FXML
    private void onApplyButtonClick() {
        if(!isSettingsFilled()) {
            alert.setTitle("Ошибка");
            alert.setHeaderText("Заполните поля!");
            alert.setContentText("");
            alert.show();
            return;
        }

        //? Авто
        if(randomCarSettings.selectedProperty().getValue()) {
            SimulationSettings.CAR.fillRandomly(SimulationSettings.RANDOM);
        } else {
            SimulationSettings.CAR.setCarCount(countCarSlider.valueProperty().intValue());
            SimulationSettings.CAR.setExhaust(Double.parseDouble(carExhaustField.getText()));
        }
        //? Компания
        if(randomCompanySettings.selectedProperty().getValue()) {
            SimulationSettings.COMPANY.fillRandomly(SimulationSettings.RANDOM);
        } else {
            SimulationSettings.COMPANY.setTax(Double.parseDouble(taxField.getText()));
            SimulationSettings.COMPANY.setMaxEmissions(Double.parseDouble(companyMaxEmissionsField.getText()));
        }
        //? Карта
        if(randomMapSettings.selectedProperty().getValue()) {
            SimulationSettings.MAP.fillRandomly(SimulationSettings.RANDOM);
        }
        //? Фильтры
        if(randomFilterSettings.selectedProperty().getValue()) {
            SimulationSettings.FILTER.fillRandomly(SimulationSettings.RANDOM);
        } else {
            SimulationSettings.FILTER.setPrice(Double.parseDouble(filterPriceField.getText()));
            SimulationSettings.FILTER.setInstallationDuration(Duration.ofDays(filterDurationInstallationSlider.valueProperty().intValue()));
            SimulationSettings.FILTER.setEmissionReduction(filterEmissionReductionSlider.valueProperty().floatValue());
        }
        //? Город
        if(randomCitySettings.selectedProperty().getValue()) {
            SimulationSettings.CITY.fillRandomly(SimulationSettings.RANDOM);
        } else {
            SimulationSettings.CITY.setStartCityFund(Double.parseDouble(startCityFundField.getText()));
            SimulationSettings.CITY.setStartWeather(weatherCB.getValue());
        }
        //? Симуляция
        if(randomSimulationSettings.selectedProperty().getValue()) {
            SimulationSettings.SIMULATION_DURATION = Duration.ofDays(SimulationSettings.RANDOM.nextInt(15, 365));
            SimulationSettings.SIMULATION_STEP = SimulationSettings.RANDOM.nextInt(1, 5);
        } else {
            SimulationSettings.SIMULATION_DURATION = Duration.ofDays(simulationDurationSlider.valueProperty().intValue());
            SimulationSettings.SIMULATION_STEP = simulationStepSlider.valueProperty().intValue();
        }
        loadSettings();
    }
}
