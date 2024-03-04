package com.example.laborator_7.Controller;

import com.example.laborator_7.Domain.User;
import com.example.laborator_7.Service.Service;
import com.example.laborator_7.Utils.Events.UserChangeEvent;
import com.example.laborator_7.Utils.Observer.Observer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class AdminController implements Observer<UserChangeEvent> {

    Service service;
    ObservableList<User> model = FXCollections.observableArrayList();

    private int current_page = 1;
    private int size = 6;

    private int numberOfUsers;

    @FXML
    TableView<User> userTableView;
    @FXML
    TableColumn<User, String> tableColumnFirstName;
    @FXML
    TableColumn<User, String> tableColumnLastName;
    @FXML
    TableColumn<User, String> tableColumnEmail;
    @FXML
    TextField textSearchedEmail;
    @FXML
    TextField textPageSize;
    @FXML
    Label currentPageLabel;

    public void setService(Service service) {
        this.service = service;
        this.service.addUserObserver(this);
        this.numberOfUsers = service.getNumberOfUsers();
        this.currentPageLabel.setText(current_page + "/" + ((numberOfUsers + size - 1) / size));
        this.service.setUserPageSize(size);
        initModel();
    }

    @FXML
    public void initialize() {
        tableColumnFirstName.setCellValueFactory(new PropertyValueFactory<>("FirstName"));
        tableColumnLastName.setCellValueFactory(new PropertyValueFactory<>("LastName"));
        tableColumnEmail.setCellValueFactory(new PropertyValueFactory<>("Email"));
        userTableView.setItems(model);
    }

    private void initModel() {
        Iterable<User> allUsers = service.getUsersOnPage(current_page);
        List<User> usersList = StreamSupport.stream(allUsers.spliterator(), false)
                .collect(Collectors.toList());
        model.setAll(usersList);
        numberOfUsers = service.getNumberOfUsers();
    }

    @Override
    public void update(UserChangeEvent userChangeEvent) {
        initModel();
    }

    public void handleDeleteUser() {
        User selected = userTableView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                service.deleteUser(selected.getEmail());
                UserAlert.showMessage(null, Alert.AlertType.INFORMATION, "Delete", "User deleted successfully!");
            } catch (IllegalArgumentException e) {
                UserAlert.showErrorMessage(null, e.getMessage());
            }
        } else UserAlert.showErrorMessage(null, "You must select an user!");
    }

    public void handleAddUser() throws IOException {
        showUserEditDialog(null);
    }

    public void handleUpdateUser() throws IOException {
        User selected = userTableView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            showUserEditDialog(selected);
        } else
            UserAlert.showErrorMessage(null, "You have not selected any user!");
    }

    public void handleSearchUser() throws IOException {
        Optional<User> searchedUser = service.findUserByEmail(textSearchedEmail.getText());
        if (searchedUser.isEmpty()) {
            UserAlert.showErrorMessage(null, "User doesn't exist!");
        } else {
            showUserEditDialog(searchedUser.get());
        }
    }

    public void handleMessageButton() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/com/example/laborator_7/messages-view.fxml"));

        SplitPane root = loader.load();

        Stage dialoStage = new Stage();
        dialoStage.setTitle("Messages");
        dialoStage.initModality(Modality.WINDOW_MODAL);

        Scene scene = new Scene(root);
        dialoStage.setScene(scene);

        MessageController messageController = loader.getController();
        messageController.setService(this.service, dialoStage);
        dialoStage.show();

    }

    public void handleFriendshipsButton() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/com/example/laborator_7/friendship-view.fxml"));

        SplitPane root = loader.load();

        Stage dialoStage = new Stage();
        dialoStage.setTitle("Friendships");
        dialoStage.initModality(Modality.WINDOW_MODAL);

        Scene scene = new Scene(root);
        dialoStage.setScene(scene);

        FriendshipController friendshipController = loader.getController();
        friendshipController.setService(this.service, dialoStage);
        dialoStage.show();
    }

    public void showUserEditDialog(User user) throws IOException {
        // create a new stage for the popup dialog.
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/com/example/laborator_7/edit-user.fxml"));


        AnchorPane root = loader.load();

        // Create the dialog Stage.
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Edit User");
        dialogStage.initModality(Modality.WINDOW_MODAL);
        //dialogStage.initOwner(primaryStage);
        Scene scene = new Scene(root);
        dialogStage.setScene(scene);

        EditUserController editMessageViewController = loader.getController();
        editMessageViewController.setService(this.service, dialogStage, user);

        dialogStage.show();
    }

    public void handleSetPageSizeButton() {
        if (this.textPageSize.getText().isEmpty()) {
            UserAlert.showErrorMessage(null, "Invalid size");
            return;
        }
        this.size = Integer.parseInt(this.textPageSize.getText());
        this.service.setUserPageSize(this.size);
        if (this.size > 0) {
            if (current_page > ((this.numberOfUsers + this.size - 1) / this.size)) {
                current_page = 1;
            }
            initModel();
            this.currentPageLabel.setText(this.current_page + "/" + ((this.numberOfUsers + this.size - 1) / this.size));
        } else {
            UserAlert.showErrorMessage(null, "Invalid size");
        }
    }

    public void handleNextPageUserButton() {
        if (this.current_page < ((this.numberOfUsers + this.size - 1) / this.size)) {
            this.current_page += 1;
            this.currentPageLabel.setText(this.current_page + "/" + ((this.numberOfUsers + this.size - 1) / this.size));
            initModel();
        }
    }

    public void handlePreviousPageUserButton() {
        if (this.current_page > 1) {
            this.current_page -= 1;
            this.currentPageLabel.setText(this.current_page + "/" + ((this.numberOfUsers + this.size - 1) / this.size));
            initModel();
        }
    }
}

