package com.example.laborator_7.Controller;

import com.example.laborator_7.Domain.Friendship;
import com.example.laborator_7.Domain.FriendshipRequest;
import com.example.laborator_7.Service.Service;
import com.example.laborator_7.Utils.Events.FriendshipChangeEvent;
import com.example.laborator_7.Utils.Observer.Observer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class FriendshipController implements Observer<FriendshipChangeEvent> {

    Service service;
    ObservableList<Friendship> model = FXCollections.observableArrayList();

    @FXML
    TableView<Friendship> friendshipTableView;
    @FXML
    public TableColumn<Friendship, String> tableColumnFirstUser;
    @FXML
    public TableColumn<Friendship, String> tableColumnSecondUser;
    @FXML
    public TableColumn<Friendship, LocalDate> tableColumnDate;
    @FXML
    public TableColumn<Friendship, FriendshipRequest> tableColumnStatus;

    Stage dialogStage;

    @FXML
    TextField textPageSize;
    @FXML
    Label currentPageLabel;

    private int current_page = 1;
    private int size = 5;
    private int numberOfFriendshipRequests;

    public void setService(Service service, Stage stage) {
        this.dialogStage = stage;
        this.service = service;
        this.service.addFriendshipObserver(this);
        this.numberOfFriendshipRequests = service.getNumberOfFriendshipRequests();
        this.currentPageLabel.setText(current_page + "/" + ((numberOfFriendshipRequests + size - 1) / size));
        this.service.setFriendshipPageSize(size);
        initModel();
    }

    @FXML
    public void initialize() {
        tableColumnFirstUser.setCellValueFactory(new PropertyValueFactory<>("user1"));
        tableColumnSecondUser.setCellValueFactory(new PropertyValueFactory<>("user2"));
        tableColumnDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        tableColumnStatus.setCellValueFactory(new PropertyValueFactory<>("request"));
        friendshipTableView.setItems(model);
    }

    private void initModel() {
        Iterable<Friendship> allFriendships = service.getFriendshipRequestsOnPage(current_page);
        List<Friendship> friendshipsList = StreamSupport.stream(allFriendships.spliterator(), false)
                .collect(Collectors.toList());
        model.setAll(friendshipsList);
        numberOfFriendshipRequests = service.getNumberOfFriendshipRequests();
    }

    @Override
    public void update(FriendshipChangeEvent friendshipChangeEvent) {
        initModel();
    }

    public void handleAcceptButton() {
        Friendship friendship = friendshipTableView.getSelectionModel().getSelectedItem();
        if (friendship != null) {
            if (friendship.getRequest().equals(FriendshipRequest.ACCEPTED) || friendship.getRequest().equals(FriendshipRequest.REJECTED)) {
                UserAlert.showErrorMessage(null, "Request already handled");
                return;
            }
            service.acceptFriendRequest(friendship.getUser1().getEmail(), friendship.getUser2().getEmail());
            UserAlert.showMessage(null, Alert.AlertType.INFORMATION, "Accept", "Friendship status changed to: " + FriendshipRequest.ACCEPTED);
        } else UserAlert.showErrorMessage(null, "You must select a friendship!");
    }

    public void handleRejectButton() {
        Friendship friendship = friendshipTableView.getSelectionModel().getSelectedItem();
        if (friendship != null) {
            if (friendship.getRequest().equals(FriendshipRequest.ACCEPTED) || friendship.getRequest().equals(FriendshipRequest.REJECTED)) {
                UserAlert.showErrorMessage(null, "Request already handled");
                return;
            }
            service.declineFriendRequest(friendship.getUser1().getEmail(), friendship.getUser2().getEmail());
            UserAlert.showMessage(null, Alert.AlertType.INFORMATION, "Accept", "Friendship status changed to: " + FriendshipRequest.REJECTED);
        } else UserAlert.showErrorMessage(null, "You must select a friendship!");
    }

    public void handleSetPageSizeButton() {
        if (this.textPageSize.getText().isEmpty()) {
            UserAlert.showErrorMessage(null, "Invalid size");
            return;
        }
        this.size = Integer.parseInt(this.textPageSize.getText());
        this.service.setFriendshipPageSize(this.size);
        if (this.size > 0) {
            if (current_page > ((this.numberOfFriendshipRequests + this.size - 1) / this.size)) {
                current_page = 1;
            }
            initModel();
            this.currentPageLabel.setText(this.current_page + "/" + ((this.numberOfFriendshipRequests + this.size - 1) / this.size));
        } else {
            UserAlert.showErrorMessage(null, "Invalid size");
        }
    }

    public void handleNextPageFriendshipButton() {
        if (this.current_page < ((this.numberOfFriendshipRequests + this.size - 1) / this.size)) {
            this.current_page += 1;
            this.currentPageLabel.setText(this.current_page + "/" + ((this.numberOfFriendshipRequests + this.size - 1) / this.size));
            initModel();
        }
    }

    public void handlePreviousPageFriendshipButton() {
        if (this.current_page > 1) {
            this.current_page -= 1;
            this.currentPageLabel.setText(this.current_page + "/" + ((this.numberOfFriendshipRequests + this.size - 1) / this.size));
            initModel();
        }
    }
}
