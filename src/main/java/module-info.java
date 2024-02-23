module ad.apppuntuaciones {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;
    requires com.fasterxml.jackson.databind;

    opens ad.apppuntuaciones to javafx.fxml;
    exports ad.apppuntuaciones;
}