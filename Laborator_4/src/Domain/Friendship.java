package Domain;

import java.time.LocalDateTime;
import java.util.UUID;

public class Friendship extends Entity<UUID> {

    private final User user1;

    private final User user2;
    LocalDateTime date;

    FriendshipRequest request;

    public Friendship(User u1, User u2) {
        this.user1 = u1;
        this.user2 = u2;
        this.request = FriendshipRequest.PENDING;
        this.setId(UUID.randomUUID());
        this.date = LocalDateTime.now();
    }

    public Friendship(User u1, User u2, FriendshipRequest req) {
        this.user1 = u1;
        this.user2 = u2;
        this.request = req;
        this.setId(UUID.randomUUID());
        this.date = LocalDateTime.now();
    }

    public void set_request(FriendshipRequest req) {
        this.request = req;
        if (req  == FriendshipRequest.ACCEPTED) {
            addFriend();
        }
    }

    public void addFriend() {
        // Map<UUID,Utilizator> lista_prieteni = user1.getFriends();
        // if(lista_prieteni!=null && lista_prieteni.containsKey(user2.getId())){
        //  return;
        //}
        user1.addFriendUser(user2);
        user2.addFriendUser(user1);
    }

    public User getUser1() {
        return user1;
    }

    public User getUser2() {
        return user2;
    }

    @Override
    public String toString() {
        return "Friendship{" +
                "user1=" + user1 +
                ", user2=" + user2 +
                ", date=" + date +
                ", request=" + request +
                '}';
    }

    /**
     * @return the date when friendship was created
     */
    public LocalDateTime getDate() {
        return date;
    }

}
