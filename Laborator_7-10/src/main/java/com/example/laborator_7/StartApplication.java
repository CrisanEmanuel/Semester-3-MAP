package com.example.laborator_7;
import com.example.laborator_7.Controller.StartController;
import com.example.laborator_7.Domain.Validators.FriendshipValidator;
import com.example.laborator_7.Domain.Validators.UserValidator;
import com.example.laborator_7.Repository.DBRepository.*;
import com.example.laborator_7.Service.Service;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class StartApplication extends Application {

    private Service service;

    @Override
    public void start(Stage primaryStage) throws IOException {

        String username = "postgres";
        String password = "pinguin";
        String url = "jdbc:postgresql://localhost:5432/socialnetwork";
        UserValidator validatorUser = new UserValidator();
        FriendshipValidator validatorFriendship = new FriendshipValidator();
        UserDBRepository userDBRepository = new UserDBRepository(url, username, password, validatorUser);
        FriendshipDBRepository friendshipDBRepository = new FriendshipDBRepository(url, username, password, validatorFriendship);
        MessageDBRepository messageDBRepository = new MessageDBRepository(url, username, password);
        UserDBPagingRepository userDBPagingRepository = new UserDBPagingRepository(url, username, password, validatorUser);
        FriendshipDBPagingRepository friendshipDBPagingRepository = new FriendshipDBPagingRepository(url, username, password, validatorFriendship);

        service = new Service(userDBRepository, friendshipDBRepository, messageDBRepository, userDBPagingRepository, friendshipDBPagingRepository);

        initView(primaryStage);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    private void initView(Stage primaryStage) throws IOException {

        FXMLLoader userLoader = new FXMLLoader();
        userLoader.setLocation(getClass().getResource("startApp-view.fxml"));
        AnchorPane userLayout = userLoader.load();
        primaryStage.setTitle("Welcome!");
        primaryStage.setScene(new Scene(userLayout));

        StartController userController = userLoader.getController();
        userController.setService(service);
    }
}
