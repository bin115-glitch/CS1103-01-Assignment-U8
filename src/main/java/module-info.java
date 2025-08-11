module com.example.cs110301assignmentu8 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires kotlin.stdlib;
    
    // HTTP client and JSON parsing
    requires okhttp3;
    requires com.google.gson;

    opens com.example.cs110301assignmentu8 to javafx.fxml, com.google.gson;
    exports com.example.cs110301assignmentu8;
}