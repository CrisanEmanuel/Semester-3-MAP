package com.example.laborator_7.Repository;

import com.example.laborator_7.Domain.Friendship;
import com.example.laborator_7.Domain.User;

import java.util.Optional;
import java.util.UUID;

public interface FriendshipRepository extends Repository<UUID, Friendship>{

    Optional<Friendship> findFriendshipByEmail(String email1, String email2);

    Optional<Friendship> deleteFriendshipUsingEmail(String email1, String email2);

    Optional<User> findUserByEmail(String email);

    int numberOfFriendshipRequests();

    int numberOfUserFriendshipRequests(User user);
}
