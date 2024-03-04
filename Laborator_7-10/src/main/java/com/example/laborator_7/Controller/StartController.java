package com.example.laborator_7.Controller;

import com.example.laborator_7.Domain.User;
import com.example.laborator_7.Domain.Validators.ValidationException;
import com.example.laborator_7.Service.Service;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static com.example.laborator_7.Utils.Password.Password.*;

public class StartController {
    Service service;
    @FXML
    TextField firstNameSignUpText;
    @FXML
    TextField lastNameSignUpText;
    @FXML
    TextField emailSignUpText;
    @FXML
    TextField emailLogInText;
    @FXML
    PasswordField passwordSignUpText;
    @FXML
    PasswordField repeatPasswordSignUpText;
    @FXML
    PasswordField passwordLogInText;

    public void setService(Service service) {
        this.service = service;
    }

    public void handleAdminButton() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/com/example/laborator_7/admin-view.fxml"));

        AnchorPane root = loader.load();

        Stage adminStage = new Stage();
        adminStage.setTitle("Admin");
        adminStage.initModality(Modality.WINDOW_MODAL);

        Scene scene = new Scene(root);
        adminStage.setScene(scene);

        AdminController userController = loader.getController();
        userController.setService(service);
        adminStage.show();
    }

    public void handleLogInButton() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/com/example/laborator_7/user-view.fxml"));

        SplitPane root = loader.load();

        if (emailLogInText.getText().isEmpty() || passwordLogInText.getText().isEmpty()) {
            UserAlert.showErrorMessage(null, "Invalid email or password");
            return;
        }

        // checks if the user email exists
        Iterable<User> allUsers = service.getAllUsers();
        Optional<User> userOptional = StreamSupport.stream(allUsers.spliterator(), false)
                .filter(user -> user.getEmail().equals(emailLogInText.getText().trim()))
                .findFirst();
        if (userOptional.isEmpty()) {
            UserAlert.showErrorMessage(null, "User " + emailLogInText.getText().trim() + " doesn't exist!");
            return;
        }

        // checks if user provided the right password
        if (!verifyPassword(passwordLogInText.getText().trim(), userOptional.get().getPassword()) ||
                !userOptional.get().getEmail().equals(emailLogInText.getText().trim())) {
            UserAlert.showErrorMessage(null, "Incorrect email or password");
            return;
        }

        Stage userStage = new Stage();
        userStage.setTitle("Logged in as " + emailLogInText.getText().trim());
        userStage.initModality(Modality.WINDOW_MODAL);

        Scene scene = new Scene(root);
        userStage.setScene(scene);

        UserController userController = loader.getController();
        userController.setService(service, emailLogInText.getText().trim(), userStage);
        userStage.show();

        emailLogInText.clear();
        passwordLogInText.clear();
    }

    public void handleSignUpButton() {
        String firstName = firstNameSignUpText.getText().trim();
        String lastName = lastNameSignUpText.getText().trim();
        String email = emailSignUpText.getText().trim();
        String password = passwordSignUpText.getText().trim();
        String repeatedPassword = repeatPasswordSignUpText.getText().trim();

        if (!password.equals(repeatedPassword)) {
            UserAlert.showErrorMessage(null, "Passwords doesn't match");
            return;
        }

        if (!checkPasswordFormat(password)) {
            UserAlert.showErrorMessage(null, """
                    Password must be at least 8 characters long and contain at least one of the following characters:
                    @#$%^&+=!
                    capital letter
                    number""");
            return;
        }

        String hashedPassword = hashPassword(password);
        User newUser = new User(firstName, lastName, email);
        newUser.setPassword(hashedPassword);

        try {
            service.addUser(newUser);
            UserAlert.showMessage(null, Alert.AlertType.INFORMATION, "Account created", "Account created successfully. Use the email to log in");
            firstNameSignUpText.clear();
            lastNameSignUpText.clear();
            emailSignUpText.clear();
            passwordSignUpText.clear();
            repeatPasswordSignUpText.clear();
            emailLogInText.setText(email);
        } catch (ValidationException e) {
            UserAlert.showErrorMessage(null, e.getMessage());
        } catch (RuntimeException e) {
            UserAlert.showErrorMessage(null, "User email already in use!");
        }
    }
}
