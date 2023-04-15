module kernel {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;
    requires org.controlsfx.controls;
//    requires jdk.hotspot.agent;
    opens kernel to javafx.fxml;
    exports kernel;
    opens kernel.views to javafx.fxml;
    exports kernel.views;
    exports kernel.test;
    opens kernel.test to javafx.fxml;
}