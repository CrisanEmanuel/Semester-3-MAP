package com.example.laborator_7.Controller;

import com.example.laborator_7.Domain.Friendship;
import com.example.laborator_7.Domain.FriendshipRequest;
import com.example.laborator_7.Domain.Message;
import com.example.laborator_7.Domain.User;
import com.example.laborator_7.Domain.Validators.ValidationException;
import com.example.laborator_7.Repository.Paging.Page;
import com.example.laborator_7.Repository.Paging.Pageable;
import com.example.laborator_7.Repository.Paging.PageableImplementation;
import com.example.laborator_7.Repository.Paging.Paginator;
import com.example.laborator_7.Service.Service;
import com.example.laborator_7.Utils.Events.Event;
import com.example.laborator_7.Utils.Events.FriendshipChangeEvent;
import com.example.laborator_7.Utils.Events.MessageChangeEvent;
import com.example.laborator_7.Utils.Events.UserChangeEvent;
import com.example.laborator_7.Utils.Observer.Observer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class UserController implements Observer<Event> {
    private Service service;
    private String userEmail;
    protected User selectedFriend;
    Stage userStage;
    ObservableList<Friendship> modelFriendship = FXCollections.observableArrayList();
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

    ObservableList<User> modelUser = FXCollections.observableArrayList();
    @FXML
    public TableView<User> usersTableView;
    @FXML
    public TableColumn<User, String> tableColumnFirstName;
    @FXML
    public TableColumn<User, String> tableColumnLastName;
    @FXML
    public TableColumn<User, String> tableColumnEmail;

    ObservableList<Message> modelMessage = FXCollections.observableArrayList();
    @FXML
    public TableView<Message> messagesTableView;
    @FXML
    public TableColumn<Message, String> tableColumnFrom;
    @FXML
    public TableColumn<Message, String> tableColumnTo;
    @FXML
    public TableColumn<Message, String> tableColumnMessage;
    @FXML
    public TableColumn<Message, String> tableColumnDateMessage;

    @FXML
    TextField emailUserText;
    @FXML
    TextField textUsersEmail;
    @FXML
    TextField messageText;
    @FXML
    TextField textFriendsPageSize;
    @FXML
    Label currentFriendsPageLabel;
    @FXML
    TextField textFriendshipPageSize;
    @FXML
    Label currentFriendshipPageLabel;
    @FXML
    TextField textMessagesPageSize;

    private int friendship_current_page = 1;
    private int friendship_size = 5;
    private int numberOfFriendshipRequests;

    private int friends_current_page = 1;
    private int friends_size = 5;
    private int numberOfUserFriends;

    private int numberOfMessagesToShow = 10;

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    public void setService(Service service, String userEmail, Stage userStage) {
        this.service = service;
        this.userEmail = userEmail;
        this.userStage = userStage;
        this.service.addUserObserver(this::update);
        this.service.addFriendshipObserver(this::update);
        this.service.addMessageObserver(this::update);

        this.numberOfUserFriends = service.getNumberOfUserFriends(service.findUserByEmail(userEmail).get());
        this.currentFriendsPageLabel.setText(friends_current_page + "/" + ((numberOfUserFriends + friends_size - 1) / friends_size));
        this.service.setUserPageSize(friends_size);

        this.numberOfFriendshipRequests = service.getTheNumberOfUserRequests(service.findUserByEmail(userEmail).get());
        this.currentFriendshipPageLabel.setText(friendship_current_page + "/" + ((numberOfFriendshipRequests + friendship_size - 1) / friendship_size));
        this.service.setFriendshipPageSize(friendship_size);

        initModelFriendships();
        initModelFriends();
    }

    public void handleAcceptButton() {
        Friendship selectedFriendship = friendshipTableView.getSelectionModel().getSelectedItem();
        if (selectedFriendship != null) {
            if (selectedFriendship.getRequest().equals(FriendshipRequest.ACCEPTED) || selectedFriendship.getRequest().equals(FriendshipRequest.REJECTED)) {
                UserAlert.showErrorMessage(null, "Request already handled");
                return;
            }
            service.acceptFriendRequest(selectedFriendship.getUser1().getEmail(), selectedFriendship.getUser2().getEmail());
            UserAlert.showMessage(null, Alert.AlertType.INFORMATION, "Added", "Friend added");
        } else {
            UserAlert.showErrorMessage(null, "You must select a friend request!");
        }
    }

    public void handleRejectButton() {
        Friendship selectedFriendship = friendshipTableView.getSelectionModel().getSelectedItem();
        if (selectedFriendship != null) {
            if (selectedFriendship.getRequest().equals(FriendshipRequest.ACCEPTED) || selectedFriendship.getRequest().equals(FriendshipRequest.REJECTED)) {
                UserAlert.showErrorMessage(null, "Request already handled");
                return;
            }
            service.declineFriendRequest(selectedFriendship.getUser1().getEmail(), selectedFriendship.getUser2().getEmail());
            UserAlert.showMessage(null, Alert.AlertType.INFORMATION, "Rejected", "Friend rejected");
        } else {
            UserAlert.showErrorMessage(null, "You must select a friend request!");
        }
    }

    public void handleAddFriendButton() {
        String userToSendRequest = emailUserText.getText();
        try {
            service.createFriendship(userEmail, userToSendRequest);
            UserAlert.showMessage(null, Alert.AlertType.INFORMATION, "Sent", "Friend request sent");
            emailUserText.clear();
        } catch (IllegalArgumentException | ValidationException e) {
            UserAlert.showErrorMessage(null, e.getMessage());
        }
    }

    @FXML
    public void initialize() {
        tableColumnFirstUser.setCellValueFactory(new PropertyValueFactory<>("user1"));
        tableColumnSecondUser.setCellValueFactory(new PropertyValueFactory<>("user2"));
        tableColumnDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        tableColumnStatus.setCellValueFactory(new PropertyValueFactory<>("request"));
        friendshipTableView.setItems(modelFriendship);

        tableColumnFirstName.setCellValueFactory(new PropertyValueFactory<>("FirstName"));
        tableColumnLastName.setCellValueFactory(new PropertyValueFactory<>("LastName"));
        tableColumnEmail.setCellValueFactory(new PropertyValueFactory<>("Email"));
        usersTableView.setItems(modelUser);

        tableColumnFrom.setCellValueFactory(new PropertyValueFactory<>("from"));
        tableColumnMessage.setCellValueFactory(new PropertyValueFactory<>("messageText"));
        tableColumnDateMessage.setCellValueFactory(new PropertyValueFactory<>("dateTime"));
        tableColumnTo.setCellValueFactory(new PropertyValueFactory<>("to"));
        messagesTableView.setItems(modelMessage);
    }

    private void initModelFriendships() {
        Optional<User> user = service.findUserByEmail(userEmail);
        if (user.isPresent()) {
            Iterable<Friendship> allFriendships = service.getAllFriendships();
            List<Friendship> friendshipsList = StreamSupport.stream(allFriendships.spliterator(), false)
                    .filter(friendship -> friendship.getUser2().getEmail().equals(userEmail))
                    .collect(Collectors.toList());
            Pageable pageable = new PageableImplementation(friendship_current_page, friendship_size);
            Paginator<Friendship> paginator = new Paginator<>(pageable, friendshipsList);
            Page<Friendship> friendshipsPage = paginator.paginate();
            List<Friendship> paginatedFriendships = friendshipsPage.getContent().toList();
            modelFriendship.setAll(paginatedFriendships);
            numberOfFriendshipRequests = service.getTheNumberOfUserRequests(user.get());
        }
    }

    private void initModelFriends() {
        Optional<User> user = service.findUserByEmail(userEmail);
        if (user.isPresent()) {
//            Iterable<User> allFriends = service.getAllFriends(user.get());
//            List<User> usersList = StreamSupport.stream(allFriends.spliterator(), false)
//                    .collect(Collectors.toList());
            Pageable pageable = new PageableImplementation(friends_current_page, friends_size);
            Paginator<User> paginator = new Paginator<>(pageable, service.getAllFriends(user.get()));
            Page<User> friendsPage = paginator.paginate();
            List<User> paginatedFriends = friendsPage.getContent().toList();
            modelUser.setAll(paginatedFriends);
            numberOfUserFriends = service.getNumberOfUserFriends(user.get());
        }
    }

    private void initModelMessages() {
        Iterable<Message> allMessages = service.getAllMessages();
        List<Message> messagesList = StreamSupport.stream(allMessages.spliterator(), false)
                .filter(this::isMessageForUser)
                .limit(numberOfMessagesToShow)
                .collect(Collectors.toList());
        modelMessage.setAll(messagesList);
    }

    private boolean isMessageForUser(Message message) {
        List<User> recipients = message.getTo();
        Optional<User> user = service.findUserByEmail(this.userEmail);
        return user.filter(value -> (recipients.contains(value) && message.getFrom().equals(selectedFriend)) ||
                (message.getFrom().equals(value) && recipients.contains(selectedFriend))).isPresent();
    }

    public void handleSendButton() {
        User selectedUser = usersTableView.getSelectionModel().getSelectedItem();
        Message selectedMessage = messagesTableView.getSelectionModel().getSelectedItem();
        if (selectedUser != null && selectedMessage == null) {
            if (messageText.getText().isEmpty()) {
                UserAlert.showErrorMessage(null, "Cannot send an empty message!");
                return;
            }
            Optional<User> fromUser = service.findUserByEmail(userEmail);
            List<User> toUsers = new ArrayList<>();
            toUsers.add(selectedFriend);

            if (fromUser.isPresent()) {
                Message messageToBeSend = new Message(fromUser.get(), toUsers, messageText.getText(), LocalDateTime.now());
                service.addMessage(messageToBeSend);
                messageText.clear();
            }
        } else if (selectedUser != null) {
            if (messageText.getText().isEmpty()) {
                UserAlert.showErrorMessage(null, "Cannot send an empty message!");
                return;
            }
            Optional<User> fromUser = service.findUserByEmail(userEmail);
            List<User> toUsers = new ArrayList<>();
            toUsers.add(selectedMessage.getFrom());

            if (fromUser.isPresent()) {
                Message messageToBeSend = new Message(fromUser.get(), toUsers, messageText.getText(), LocalDateTime.now());
                messageToBeSend.setReplyMessage(selectedMessage);
                service.addMessage(messageToBeSend);
                messageText.clear();
            }
        } else {
            UserAlert.showErrorMessage(null, "Choose a message to reply or a user to send a message");
        }
    }

    public void handleMouseClicked() {
        this.selectedFriend = usersTableView.getSelectionModel().getSelectedItem();
        initModelMessages();
    }

    public void handleMultipleSendButton() {
        if (textUsersEmail.getText().isEmpty()) {
            UserAlert.showErrorMessage(null, "Give the email of users!");
        } else {
            if (messageText.getText().isEmpty()) {
                UserAlert.showErrorMessage(null, "Cannot send an empty message!");
                return;
            }

            Optional<User> fromUser = service.findUserByEmail(userEmail);
            String arrayOfEmails = textUsersEmail.getText();
            List<String> toUsersEmail;

            String[] items = arrayOfEmails.split("\\s+"); // emailurile se dau cu spatiu
            toUsersEmail = Arrays.asList(items);
            List<User> toUsers = new ArrayList<>();

            toUsersEmail.forEach(email -> {
                Optional<User> user = service.findUserByEmail(email);
                user.ifPresent(toUsers::add);
            });
            if (fromUser.isPresent()) {
                Message messageToBeSend = new Message(fromUser.get(), toUsers, messageText.getText(), LocalDateTime.now());
                service.addMessage(messageToBeSend);
                textUsersEmail.clear();
            }
        }
    }

    public void handleLogOutButton() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout Confirmation");
        alert.setHeaderText("Are you sure you want to log out?");
        alert.setContentText("Click OK to confirm, or Cancel to stay logged in.");

        alert.showAndWait().ifPresent(response -> {
            if (response == javafx.scene.control.ButtonType.OK) {
                userStage.close();
            }
        });
    }

    @Override
    public void update(Event event) {
        if (event instanceof UserChangeEvent) {
            initModelFriends();
        } else if (event instanceof FriendshipChangeEvent) {
            initModelFriendships();
        } else if (event instanceof MessageChangeEvent) {
            initModelMessages();
        }
    }

    public void handleSetFriendsPageSizeButton() {
        if (this.textFriendsPageSize.getText().isEmpty()) {
            UserAlert.showErrorMessage(null, "Invalid size");
            return;
        }
        this.friends_size = Integer.parseInt(this.textFriendsPageSize.getText());
        this.service.setUserPageSize(this.friends_size);
        if (this.friends_size > 0) {
            if (friends_current_page > ((this.numberOfUserFriends + this.friends_size - 1) / this.friends_size)) {
                friends_current_page = 1;
            }
            initModelFriends();
            this.currentFriendsPageLabel.setText(this.friends_current_page + "/" + ((this.numberOfUserFriends + this.friends_size - 1) / this.friends_size));
        } else {
            UserAlert.showErrorMessage(null, "Invalid size");
        }
    }

    public void handleNextPageUserButton() {
        if (this.friends_current_page < ((this.numberOfUserFriends + this.friends_size - 1) / this.friends_size)) {
            this.friends_current_page += 1;
            this.currentFriendsPageLabel.setText(this.friends_current_page + "/" + ((this.numberOfUserFriends + this.friends_size - 1) / this.friends_size));
            initModelFriends();
        }
    }
    public void handlePreviousPageUserButton () {
        if (this.friends_current_page > 1) {
            this.friends_current_page -= 1;
            this.currentFriendsPageLabel.setText(this.friends_current_page + "/" + ((this.numberOfUserFriends + this.friends_size - 1) / this.friends_size));
            initModelFriends();
        }
    }

    public void handleSetFriendshipPageSizeButton() {
        if (this.textFriendshipPageSize.getText().isEmpty()) {
            UserAlert.showErrorMessage(null, "Invalid size");
            return;
        }
        this.friendship_size = Integer.parseInt(this.textFriendshipPageSize.getText());
        this.service.setFriendshipPageSize(this.friendship_size);
        if (this.friends_size > 0) {
            if (friendship_current_page > ((this.numberOfFriendshipRequests + this.friendship_size - 1) / this.friendship_size)) {
                friendship_current_page = 1;
            }
            initModelFriendships();
            this.currentFriendshipPageLabel.setText(this.friendship_current_page + "/" + ((this.numberOfFriendshipRequests + this.friendship_size - 1) / this.friendship_size));
        } else {
            UserAlert.showErrorMessage(null, "Invalid size");
        }
    }

    public void handleNextPageFriendshipButton() {
        if (this.friendship_current_page < ((this.numberOfFriendshipRequests + this.friendship_size - 1) / this.friendship_size)) {
            this.friendship_current_page += 1;
            this.currentFriendshipPageLabel.setText(this.friendship_current_page + "/" + ((this.numberOfFriendshipRequests + this.friendship_size - 1) / this.friendship_size));
            initModelFriendships();
        }
    }

    public void handlePreviousPageFriendshipButton() {
        if (this.friendship_current_page > 1) {
            this.friendship_current_page -= 1;
            this.currentFriendshipPageLabel.setText(this.friendship_current_page + "/" + ((this.numberOfFriendshipRequests + this.friendship_size - 1) / this.friendship_size));
            initModelFriendships();
        }
    }

    public void handleSetMessagesPageSizeButton() {
        this.numberOfMessagesToShow = Integer.parseInt(textMessagesPageSize.getText());
        initModelMessages();
    }

}
