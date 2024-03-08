package me.croshaw.ess.view;

import javafx.geometry.BoundingBox;
import javafx.scene.canvas.Canvas;
import me.croshaw.ess.model.City;
import me.croshaw.ess.settings.CitySettings;

public class SimulationView {
    private final MapView cityView;
    private final Canvas canvas;
    public SimulationView(City city, CitySettings citySettings, Canvas canvas) {
        cityView = new MapView(city, new BoundingBox(5, 5, canvas.getWidth() - 10, canvas.getHeight() - 10), citySettings.getPermissibleConcentration());
        this.canvas = canvas;
    }
    public void draw() {
        cityView.draw(canvas.getGraphicsContext2D());
    }
}
