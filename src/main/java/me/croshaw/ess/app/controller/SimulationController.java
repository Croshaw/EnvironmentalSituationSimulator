package me.croshaw.ess.app.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import me.croshaw.ess.model.Car;
import me.croshaw.ess.model.Company;
import me.croshaw.ess.settings.SimulationSettings;
import me.croshaw.ess.util.NumberHelper;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class SimulationController implements Initializable {
    @FXML
    Canvas canvas;
    @FXML
    TreeView simulationTreeView;
    @FXML
    Slider speedSlider;
    @FXML
    Label speedInfo;
    @FXML
    Button startButton;
    @FXML
    Button pauseButton;
    me.croshaw.ess.controller.SimulationController simulationController;
    TreeItem<String> root;
    TreeItem<String> city;
    TreeItem<String> weatherTreeItem;
    TreeItem<String> cityFundItem;
    TreeItem<String> cityMax;
    TreeItem<String> cityBadCompanies;
    TreeItem<String> cityCurCars;
    TreeItem<String> cars;
    ArrayList<TreeItem<String>> companyItems = new ArrayList<>();
    private void updateCity() {
        weatherTreeItem.setValue("Погода: %s".formatted(simulationController.getCity().getCurrentWeather().name()));
        cityFundItem.setValue("Казна: %s".formatted(simulationController.getCity().getCityFund()));
        cityMax.setValue("Допустимое загрязнение: %s".formatted(simulationController.getCity().getPermissibleConcentration()));

        var badCom = simulationController.getBadCompany();
        cityBadCompanies.getChildren().clear();
        cityBadCompanies.setValue("Нарушители [%s]".formatted(badCom.size()));
        for(Company company : badCom) {
            TreeItem<String> com = new TreeItem<>("Компания %s".formatted(simulationController.getCompanyManager().getCompanies().indexOf(company)+1));
            cityBadCompanies.getChildren().add(com);
        }


        var currentCars = simulationController.getCarManager().getCurrentCars();
        cityCurCars.getChildren().clear();
        cityCurCars.setValue("Текущие машины на дорогах [%s]".formatted(currentCars.size()));
        for(Car car : currentCars) {
            TreeItem<String> tCar = new TreeItem<>(Integer.toString(car.getNumber()));
            cityCurCars.getChildren().add(tCar);
        }
    }
    private void updateCompany() {
        var companies = simulationController.getCompanyManager().getCompanies();
        int i = 0;
        for(Company company : companies) {
            TreeItem<String> companyItem = companyItems.get(i++);
            companyItem.getChildren().clear();
            TreeItem<String> companyEmissions = new TreeItem<>("Выброс: %s".formatted(company.getEmission()));
            TreeItem<String> filters = new TreeItem<>("Фильтров: %s".formatted(company.getCountFilters()));
            TreeItem<String> work = new TreeItem<>(company.isWork() ? "Работает" : "Не работает");
            companyItem.getChildren().add(companyEmissions);
            companyItem.getChildren().add(filters);
            companyItem.getChildren().add(work);
            if(!company.isWork()) {
                TreeItem<String> workDuration = new TreeItem<>(company.getSanctionDuration().toString());
                companyItem.getChildren().add(workDuration);
            }
        }
    }
    private void updateCar() {
        var carManager = simulationController.getCarManager();
        var carl = carManager.getCars();
        TreeItem<String> carList = new TreeItem<>("Список машин [%s]".formatted(carl.size()));
        for(Car car : carl) {
            TreeItem<String> tCar = new TreeItem<>(Integer.toString(car.getNumber()));
            carList.getChildren().add(tCar);
        }
        TreeItem<String> avgExhaust = new TreeItem<>("AVG EXHAUST: %s".formatted(carManager.getAvgExhaust()));
        cars.getChildren().add(carList);
        cars.getChildren().add(avgExhaust);
    }
    public void update() {
        root.setValue("Симуляция [%s день]".formatted(simulationController.getCurrentDay()));
        updateCity();
        updateCompany();
    }
    @FXML
    void onStartButtonClick() {
        if(startButton.getText().equals("Старт")) {
            simulationController = SimulationSettings.GET_CONTROLLER(canvas);
            root = new TreeItem<>("Симуляция");
            city = new TreeItem<>("Город");

            weatherTreeItem = new TreeItem<>();
            cityFundItem = new TreeItem<>();
            cityMax = new TreeItem<>();
            cityBadCompanies = new TreeItem<>();
            cityCurCars = new TreeItem<>();
            city.getChildren().add(weatherTreeItem);
            city.getChildren().add(cityFundItem);
            city.getChildren().add(cityMax);
            city.getChildren().add(cityBadCompanies);
            city.getChildren().add(cityCurCars);

            TreeItem<String> companyTree = new TreeItem<>("Компании");
            var companies = simulationController.getCompanyManager().getCompanies();
            int i = 1;
            for (Company company : companies) {
                TreeItem<String> companyItem = new TreeItem<>("Компания %s".formatted(i++));
                companyItems.add(companyItem);
                companyTree.getChildren().add(companyItem);
            }
            cars = new TreeItem<>("Машины");
            root.getChildren().add(city);
            root.getChildren().add(companyTree);
            root.getChildren().add(cars);
            update();
            updateCar();
            simulationTreeView.setRoot(root);
            simulationController.start(this::update);
            startButton.setText("Стоп");
            pauseButton.disableProperty().setValue(false);
        }
        else {
            startButton.setText("Старт");
            pauseButton.disableProperty().setValue(true);
            simulationController.stop();
        }
    }
    public void stopSimulation() {
        if(simulationController != null)
            simulationController.stop();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        speedSlider.valueProperty().addListener((observableValue, number, t1) -> {
            float round = NumberHelper.round(t1.floatValue(), 2);
            if(simulationController != null) {
                simulationController.setSpeed(round);
            }
            speedInfo.setText(Float.toString(round));
            speedSlider.valueProperty().setValue(round);
        });
        pauseButton.setOnAction(actionEvent -> {
            if(simulationController == null)
                return;
            if(pauseButton.getText().equals("Пауза")) {
                pauseButton.setText("Возобновить");
                simulationController.pause();
            } else {
                pauseButton.setText("Пауза");
                simulationController.resume();
            }
        });
    }
}