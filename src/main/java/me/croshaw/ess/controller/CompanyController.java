package me.croshaw.ess.controller;

import me.croshaw.ess.model.Company;
import me.croshaw.ess.settings.CompanySettings;
import me.croshaw.ess.settings.FilterSettings;
import me.croshaw.ess.settings.MapSettings;
import me.croshaw.ess.util.NumberHelper;
import me.croshaw.ess.util.Pair;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class CompanyController implements Cloneable {
    private final ArrayList<Company> companies;
    private final CompanySettings companySettings;
    private final MapSettings mapSettings;
    public CompanyController(CompanySettings companySettings, MapSettings mapSettings, FilterSettings filterSettings) {
        companies = new ArrayList<>();
        var emissions = companySettings.getEmissions();
        var dic = mapSettings.getCompanyPriorities();
        var keys = dic.keySet().stream().sorted().toArray(Integer[]::new);
        for(int i = 0; i < emissions.size(); i++) {
            companies.add(new Company(emissions.get(i), dic.get(keys[i]), companySettings, filterSettings,  mapSettings));
        }
        this.companySettings = companySettings;
        this.mapSettings = mapSettings;
    }
    public double[][] getPollutionMap() {
        var t = companies.stream().filter(Company::isWork).map(Company::getPollutionMap).toArray(double[][][]::new);
        if(t.length == 0)
            return new double[mapSettings.getRows()][mapSettings.getColumns()];
        return NumberHelper.merge(t);
    }
    public double[][] getPollutionMapWithFluctuation(Random random) {
        var t = companies.stream().filter(Company::isWork).map(company -> company.getPollutionMapWithFluctuation(random)).toArray(double[][][]::new);
        if(t.length == 0)
            return new double[mapSettings.getRows()][mapSettings.getColumns()];
        return NumberHelper.merge(t);
    }
    public double getTaxes() {
        return companySettings.getTax() * companies.size();
    }
    private Company getNearestCompanyToPoint(int x, int y) {
        Company nearestCompany = null;
        double minDistance = Double.MAX_VALUE;
        for (Company company : companies) {
            var companyCoordinates = company.getCoordinates();
            double distance = Math.sqrt(Math.pow(x - companyCoordinates.getFirst(), 2) + Math.pow(y - companyCoordinates.getSecond(), 2));
            if (distance < minDistance) {
                minDistance = distance;
                nearestCompany = company;
            }
        }
        return nearestCompany;
    }
    public Pair<Double, HashSet<Company>> getFinesAndBadCompanies(double[][] map) {
        double result = 0;
        HashSet<Company> badCompanies = new HashSet<>();
        for(int i = 0; i < map.length; i++) {
            for(int j = 0 ; j < map[i].length; j++) {
                if(map[i][j] > companySettings.getMaxEmissions()) {
                    if(badCompanies.add(getNearestCompanyToPoint(i, j)))
                        result += companySettings.getFine();
                }
            }
        }
        return new Pair<Double, HashSet<Company>>(result, badCompanies);
    }

    public ArrayList<Company> getCompanies() {
        return companies;
    }

    public void reduceSanctions() {
        companies.forEach(company -> {
            if(!company.isWork()) {
                company.reduceSanction();
            }
        });
    }

    @Override
    public CompanyController clone() {
        try {
            return (CompanyController) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
