module me.croshaw.ess {
    requires javafx.controls;
    requires javafx.fxml;

    opens me.croshaw.ess.app to javafx.fxml;
    opens me.croshaw.ess.app.controller to javafx.fxml;
    exports me.croshaw.ess.app;
}