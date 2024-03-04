package Service;

import Domain.Friendship;
import Domain.User;

public interface ServiceInterface {

    boolean addUser(User user);

    void deleteUser(String email);

    void createFriendship(String email1, String email2);

    boolean deleteFriendship(String email1, String email2);

    Iterable<User> getAllUsers();

    Iterable<Friendship> getAllFriendships();

    int communitiesNumber();

    void acceptFriendRequest(String email1, String email2);

    void declineFriendRequest(String email1, String email2);

    void addData();

    User getUserUsingEmail(String email);

    Iterable<User> mostSociableCommunity();
}
