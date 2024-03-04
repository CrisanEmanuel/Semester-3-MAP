package com.example.laborator_7.Domain;

import java.time.LocalDate;
import java.util.UUID;

public class Friendship extends Entity<UUID> {

    private User user1;
    private User user2;
    private UUID user1ID;
    private UUID user2ID;
    LocalDate date;
    FriendshipRequest request;

    public Friendship(User u1, User u2) {
        this.user1 = u1;
        this.user2 = u2;
        this.request = FriendshipRequest.PENDING;
        this.setId(UUID.randomUUID());
        this.date = LocalDate.now();
    }

    public Friendship(UUID id, User u1, User u2, LocalDate date, FriendshipRequest request) {
        this.user1 = u1;
        this.user2 = u2;
        this.request = request;
        this.setId(id);
        this.date = date;
    }

    public Friendship(User u1, User u2, LocalDate date, FriendshipRequest request) {
        this.user1 = u1;
        this.user2 = u2;
        this.request = request;
        this.setId(UUID.randomUUID());
        this.date = date;
    }

    public Friendship(UUID friendshipID, UUID user1ID, UUID user2ID, LocalDate date, FriendshipRequest request) {
        this.setId(friendshipID);
        this.user1ID = user1ID;
        this.user2ID = user2ID;
        this.date = date;
        this.request = request;
    }

    public FriendshipRequest getRequest() {
        return request;
    }
    public void set_request(FriendshipRequest req) {
        this.request = req;
//        if (req  == FriendshipRequest.ACCEPTED) {
//            addFriend();
//        }
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
    public UUID getUser1ID() {
        return user1ID;
    }
    public UUID getUser2ID() {
        return user2ID;
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
    public LocalDate getDate() {
        return date;
    }

}
