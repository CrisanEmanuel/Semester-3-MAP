package com.example.laborator_7.Console;

import com.example.laborator_7.Console.Console;
import com.example.laborator_7.Domain.Friendship;
import com.example.laborator_7.Domain.Message;
import com.example.laborator_7.Domain.User;
import com.example.laborator_7.Domain.Validators.FriendshipValidator;
import com.example.laborator_7.Domain.Validators.UserValidator;
import com.example.laborator_7.Repository.DBRepository.FriendshipDBRepository;
import com.example.laborator_7.Repository.DBRepository.MessageDBRepository;
import com.example.laborator_7.Repository.DBRepository.UserDBPagingRepository;
import com.example.laborator_7.Repository.DBRepository.UserDBRepository;
//import com.example.laborator_7.Repository.InMemoryRepository.InMemoryRepository;
import com.example.laborator_7.Repository.Paging.PagingRepository;
import com.example.laborator_7.Repository.Repository;
import com.example.laborator_7.Service.Service;
import com.example.laborator_7.Tests;

import java.util.UUID;

public class Main {
    public static void main(String[] args) {
        UserValidator vu = new UserValidator();
        FriendshipValidator vp = new FriendshipValidator();

//        InMemoryRepository<UUID, User> repo_utilizator = new InMemoryRepository<>(vu);
//        InMemoryRepository<UUID, Friendship> repo_prietenie = new InMemoryRepository<>(vp);

        String username = "postgres";
        String password = "pinguin";
        String url = "jdbc:postgresql://localhost:5432/socialnetwork";
        Repository<UUID, User> dbUserRepository = new UserDBRepository(url, username, password, vu);
        Repository<UUID, Friendship> dbFriendshipRepository = new FriendshipDBRepository(url, username, password, vp);
        Repository<UUID, Message> dbMessageRepository = new MessageDBRepository(url, username, password);
        PagingRepository<UUID, User> userDBpaging = new UserDBPagingRepository(url, username, password, vu);

        //Service service = new Service(repo_utilizator, repo_prietenie);
//        Service serviceDB = new Service(dbUserRepository, dbFriendshipRepository);
//        Console console = new Console(serviceDB);
//        console.run();

        Tests tests = new Tests(dbUserRepository, dbFriendshipRepository, dbMessageRepository, userDBpaging);
        tests.runTests();
    }
}