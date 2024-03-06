package me.croshaw.ess.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SettingsApplication extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(SettingsApplication.class.getResource("settings-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Настройки");
        stage.setScene(scene);
        stage.show();
    }
}
