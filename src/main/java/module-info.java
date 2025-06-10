module com.example.comp242project4 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.example.comp242project4 to javafx.fxml;
    exports project4;

}