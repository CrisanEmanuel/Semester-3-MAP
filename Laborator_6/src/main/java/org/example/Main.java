package org.example;
import org.example.Domain.Friendship;
import org.example.Domain.User;
import org.example.Domain.Validators.FriendshipValidator;
import org.example.Domain.Validators.UserValidator;
import org.example.Repository.DBRepository.FriendshipDBRepository;
import org.example.Repository.InMemoryRepository.InMemoryRepository;
import org.example.Repository.Repository;
import org.example.Repository.DBRepository.UserDBRepository;
import org.example.Service.Service;

import java.util.UUID;

public class Main {
    public static void main(String[] args) {
        UserValidator vu = new UserValidator();
        FriendshipValidator vp = new FriendshipValidator();

        InMemoryRepository<UUID, User> repo_utilizator = new InMemoryRepository<>(vu);
        InMemoryRepository<UUID, Friendship> repo_prietenie = new InMemoryRepository<>(vp);

        String username = "postgres";
        String password = "pinguin";
        String url = "jdbc:postgresql://localhost:5432/socialnetwork";
        Repository<UUID, User> dbUserRepository = new UserDBRepository(url, username, password);
        Repository<UUID, Friendship> dbFriendshipRepository = new FriendshipDBRepository(url, username, password);

        //Service service = new Service(repo_utilizator, repo_prietenie);
        Service serviceDB = new Service(dbUserRepository, dbFriendshipRepository);
        Console console = new Console(serviceDB);
        console.run();

//        Tests tests = new Tests(repo_utilizator, repo_prietenie, dbUserRepository, dbFriendshipRepository);
//        tests.runTests();
    }
}