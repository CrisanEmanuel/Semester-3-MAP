package com.example.laborator_7.Repository;

import com.example.laborator_7.Domain.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends Repository<UUID, User>{
    Optional<User> findUserByEmail(String email);

    Optional<User> deleteUserByEmail(String email);

    List<User> findAllFriends(User user);

    void addFriend(User u1, User u2);

    int numberOfUsers();

    int numberOfUserFriends(User user);
}
