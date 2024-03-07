package me.croshaw.ess.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class CompanySettingsApplication extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("company-settings-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Настройки компаний");
        stage.setScene(scene);
        stage.show();
    }
}