module me.croshaw.ess {
    requires javafx.controls;
    requires javafx.fxml;


    opens me.croshaw.ess to javafx.fxml;
    exports me.croshaw.ess;
}