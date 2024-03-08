package me.croshaw.ess.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import me.croshaw.ess.app.controller.SimulationController;

public class SimulationApplication extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(SettingsApplication.class.getResource("simulation-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Симуляция");
        stage.setScene(scene);
        stage.show();
        var controller = fxmlLoader.<SimulationController>getController();
        stage.setOnHiding(windowEvent -> {
            controller.stopSimulation();
        });
    }
}
