module com.example.currencyapi {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    
    opens com.example.currencyapi to javafx.fxml;
    exports com.example.currencyapi;
}