module com.example.laborator_sapte {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;

    opens com.example.laborator_7 to javafx.fxml;
    exports com.example.laborator_7;

    opens com.example.laborator_7.Service to javafx.fxml;
    exports com.example.laborator_7.Service;

    exports com.example.laborator_7.Controller;
    opens com.example.laborator_7.Controller to javafx.fxml;

    opens com.example.laborator_7.Repository to javafx.fxml;
    exports com.example.laborator_7.Repository;

    opens com.example.laborator_7.Domain to javafx.fxml;
    exports com.example.laborator_7.Domain;

    opens com.example.laborator_7.Utils.Observer to javafx.fxml;
    exports com.example.laborator_7.Utils.Observer;

    exports com.example.laborator_7.Utils.Events;
    opens com.example.laborator_7.Utils.Events to javafx.fxml;

    exports com.example.laborator_7.Repository.DBRepository;
    opens com.example.laborator_7.Repository.DBRepository to javafx.fxml;

    exports com.example.laborator_7.Repository.Paging;
    exports com.example.laborator_7.Domain.Validators;
}