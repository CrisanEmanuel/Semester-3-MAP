package com.example.laborator_7.Repository;

import com.example.laborator_7.Domain.Message;
import com.example.laborator_7.Domain.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageRepository extends Repository<UUID, Message>{
    List<Message> findMessagesBetweenUsers(String user1Email, String user2Email);

    Optional<User> findUserByEmail(String email);

}
