package me.croshaw.ess.model;

import me.croshaw.ess.controller.CarController;
import me.croshaw.ess.controller.CompanyController;

public record SimulationSummary(City city, CompanyController companyController, CarController carController,
                                CarSpecialDrivingMode specialDrivingMode) {
    public SimulationSummary(City city, CompanyController companyController, CarController carController, CarSpecialDrivingMode specialDrivingMode) {
        this.city = city.clone();
        this.companyController = companyController.clone();
        this.carController = carController.clone();
        this.specialDrivingMode = specialDrivingMode.clone();
    }
}
