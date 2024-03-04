package com.example.laborator_7.Controller;

import com.example.laborator_7.Domain.Message;
import com.example.laborator_7.Service.Service;
import com.example.laborator_7.Utils.Events.MessageChangeEvent;
import com.example.laborator_7.Utils.Events.UserChangeEvent;
import com.example.laborator_7.Utils.Observer.Observer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class MessageController implements Observer<MessageChangeEvent> {
    Service service;
    ObservableList<Message> model = FXCollections.observableArrayList();

    @FXML
    TableView<Message> tableViewMessages;
    @FXML
    TableColumn<Message, String> tableColumnFrom;
    @FXML
    TableColumn<Message, String[]> tableColumnTo;
    @FXML
    TableColumn<Message, String> tableColumnMessage;
    @FXML
    TableColumn<Message, LocalDateTime> tableColumnDate;
    @FXML
    TableColumn<Message, LocalDateTime> tableColumnReply;

    @FXML
    private TextField textEmail1;
    @FXML
    private TextField textEmail2;
    Stage dialogStage;

    public void setService(Service service, Stage stage) {
        this.dialogStage = stage;
        this.service = service;
        this.service.addMessageObserver(this);
        initModel();
    }

    @FXML
    public void initialize() {
        tableColumnFrom.setCellValueFactory(new PropertyValueFactory<>("from"));
        tableColumnTo.setCellValueFactory(new PropertyValueFactory<>("to"));
        tableColumnMessage.setCellValueFactory(new PropertyValueFactory<>("messageText"));
        tableColumnDate.setCellValueFactory(new PropertyValueFactory<>("dateTime"));
        tableColumnReply.setCellValueFactory(new PropertyValueFactory<>("replyMessage"));
        tableViewMessages.setItems(model);
    }

    private void initModel() {
        Iterable<Message> allMessages = service.getAllMessages();
        List<Message> messageList = StreamSupport.stream(allMessages.spliterator(), false)
                .collect(Collectors.toList());
        model.setAll(messageList);
    }

    public void handleSearchConversation () {
        String email1 = textEmail1.getText();
        String email2 = textEmail2.getText();
        textEmail1.clear();
        textEmail2.clear();
        List<Message> allMessages = service.conversationBetweenTwoUsers(email1, email2);
        model.setAll(allMessages);
    }

    public void handleViewAll() {
        initModel();
    }

    @Override
    public void update(MessageChangeEvent messageChangeEvent) {
        initModel();
    }
}
