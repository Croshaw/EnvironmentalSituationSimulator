package me.croshaw.ess.model;

import me.croshaw.ess.controller.CarManager;
import me.croshaw.ess.controller.CompanyManager;

public record SimulationSummary(City city, CompanyManager companyController, CarManager carController) {
}
