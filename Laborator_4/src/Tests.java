import Domain.Friendship;
import Domain.User;
import Repository.Repository;
import Service.Service;
import java.util.*;

public class Tests {
    private final Service service;
    private final Repository<UUID, User> repo_user;
    private final Repository<UUID, Friendship> repo_friendship;

    public Tests(Service service, Repository<UUID, User> repo_user, Repository<UUID, Friendship> repo_friendship){
        this.repo_friendship = repo_friendship;
        this.repo_user = repo_user;
        this.service = service;
    }

    void runTests() {
        User u1 = new User("u1FirstName", "u1LastName", "e1");
        User u2 = new User("u2FirstName", "u2LastName", "e2");
        User u3 = new User("u3FirstName", "u3LastName", "e3");
        User u4 = new User("u4FirstName", "u4LastName", "e4");
        User u5 = new User("u5FirstName", "u5LastName", "e5");
        User u6 = new User("u6FirstName", "u6LastName", "e6");
        User u7 = new User("u7FirstName", "u7LastName", "e7");
        User u8 = new User("u8FirstName", "u8LastName", "e8");

        service.addUser(u1);
        service.addUser(u2);
        service.addUser(u3);
        service.addUser(u4);
        service.addUser(u5);
        service.addUser(u6);
        service.addUser(u7);
        service.addUser(u8);

        service.createFriendship(u1.getEmail(), u2.getEmail());
        service.createFriendship(u1.getEmail(), u3.getEmail());
        service.createFriendship(u2.getEmail(), u3.getEmail());
        service.createFriendship(u3.getEmail(), u7.getEmail());
        service.createFriendship(u4.getEmail(), u5.getEmail());
        service.createFriendship(u4.getEmail(), u6.getEmail());
        service.createFriendship(u5.getEmail(), u6.getEmail());

        service.acceptFriendRequest(u1.getEmail(), u2.getEmail());
        service.acceptFriendRequest(u1.getEmail(), u3.getEmail());
        service.acceptFriendRequest(u2.getEmail(), u3.getEmail());
        service.acceptFriendRequest(u3.getEmail(), u7.getEmail());
        service.acceptFriendRequest(u4.getEmail(), u5.getEmail());
        service.acceptFriendRequest(u4.getEmail(), u6.getEmail());
        service.acceptFriendRequest(u5.getEmail(), u6.getEmail());

        System.out.println("preteni user 1");
        for (User user: u1.getFriends().values()) {
            System.out.println(user);
        }

        System.out.println();

        System.out.println("____AM STERS USER2____");
        service.deleteUser("e2");
        System.out.println();

        System.out.println("preteni user 1");
        for (User user: u1.getFriends().values()) {
            System.out.println(user);
        }

        System.out.println();
        System.out.println("_______ALL USERS______");
        for (Object user : service.getAllUsers()) {
            System.out.println(user);
        }
        System.out.println();

        System.out.println("_______ALL FRIENDSHIPS_______");
        for (Object friendship: service.getAllFriendships()) {
            System.out.println(friendship);
        }




        System.out.println("All good!");
    }
}
