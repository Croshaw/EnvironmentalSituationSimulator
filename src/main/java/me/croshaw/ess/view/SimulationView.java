package me.croshaw.ess.view;

import javafx.geometry.BoundingBox;
import javafx.scene.canvas.Canvas;
import me.croshaw.ess.model.City;
import me.croshaw.ess.settings.CitySettings;
import me.croshaw.ess.util.Pair;

public class SimulationView {
    private final MapView cityView;
    private final Canvas canvas;
    public SimulationView(City city, CitySettings citySettings, Canvas canvas) {
        cityView = new MapView(city, new BoundingBox(5, 5, 10, 10), citySettings.getPermissibleConcentration());
        this.canvas = canvas;
    }
    public void draw() {
        cityView.draw(canvas.getGraphicsContext2D());
    }
    public void select(Pair<Integer, Integer> p) {
        cityView.select(p);
    }

    public void clear() {
        canvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }
}
