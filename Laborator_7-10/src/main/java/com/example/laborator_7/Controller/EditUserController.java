package com.example.laborator_7.Controller;

import com.example.laborator_7.Domain.User;
import com.example.laborator_7.Domain.Validators.ValidationException;
import com.example.laborator_7.Repository.DBRepository.UserDBRepository;
import com.example.laborator_7.Service.Service;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EditUserController {
    @FXML
    private TextField textFieldFirstName;
    @FXML
    private TextField textFieldLastName;
    @FXML
    private TextField textFieldEmail;

    private Service service;
    Stage dialogStage;
    User user;

    @FXML
    private void initialize() {
    }

    public void setService(Service service, Stage stage, User u) {
        this.service = service;
        this.dialogStage = stage;
        this.user = u;
        if (null != u) {
            setFields(u);
            textFieldEmail.setEditable(false);
        }
    }

    @FXML
    public void handleSave() {
        String firstName = textFieldFirstName.getText();
        String lastName = textFieldLastName.getText();
        String email = textFieldEmail.getText();
        User u = new User (firstName, lastName, email);
        if (null == this.user)
            saveUser(u);
        else
            updateUser(u);
    }

    private void updateUser(User u) {
        try {
            this.service.updateUser(u);
            UserAlert.showMessage(null, Alert.AlertType.INFORMATION,"Update user","User has been updated");
        } catch (ValidationException | IllegalArgumentException e) {
            UserAlert.showErrorMessage(null, e.getMessage());
        }
        dialogStage.close();
    }


    private void saveUser(User u) {
        try {
            this.service.addUser(u);
            if (u == null)  {
                dialogStage.close();
            }
            UserAlert.showMessage(null, Alert.AlertType.INFORMATION,"User saved","The user has been saved");
        } catch (RuntimeException e) {
            UserAlert.showErrorMessage(null, e.getMessage());
        }
        dialogStage.close();
    }

    private void clearFields() {
        textFieldEmail.clear();
        textFieldLastName.clear();
        textFieldFirstName.clear();
    }
    private void setFields(User u) {
        textFieldFirstName.setText(u.getFirstName());
        textFieldLastName.setText(u.getLastName());
        textFieldEmail.setText(u.getEmail());
    }

    @FXML
    public void handleCancel() {
        dialogStage.close();
    }
}
