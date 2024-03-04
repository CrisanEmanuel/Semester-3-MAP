package com.example.laborator_7.Repository.DBRepository;

import com.example.laborator_7.Domain.Friendship;
import com.example.laborator_7.Domain.Message;
import com.example.laborator_7.Domain.User;
import com.example.laborator_7.Repository.MessageRepository;
import com.example.laborator_7.Repository.Repository;
import com.example.laborator_7.Utils.Constants;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

public class MessageDBRepository implements MessageRepository {

    private final String url;
    private final String username;
    private final String password;

    public MessageDBRepository(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public Optional<Message> findOne(UUID uuid) {
        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement("select * from messages where \"messageID\" = ?")
        ) {
            statement.setObject(1, uuid);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()) {
                UUID messageID = (UUID) resultSet.getObject("messageID");
                String from = resultSet.getString("from");
                Optional<User> user = findUserByEmail(from);

                Array to = resultSet.getArray("to");
                String[] toUserEmail = (String[]) to.getArray();
                List<String> toListOfUserEmail = new ArrayList<>(List.of(toUserEmail));
                List<User> users = new ArrayList<>();
                toListOfUserEmail.forEach(userEmail -> {
                    Optional<User> u = findUserByEmail(userEmail);
                    u.ifPresent(users::add);
                });

                String messageText = resultSet.getString("messageText");
                LocalDateTime date = resultSet.getTimestamp("date").toLocalDateTime();

                UUID replyMessageID = (UUID) resultSet.getObject("replyMessage");
                Optional<Message> replyMessage = this.findOne(replyMessageID);

                if (user.isPresent()) {
                    Message message = new Message(messageID, user.get(), users, messageText, date);
                    replyMessage.ifPresent(message::setReplyMessage);
                    return Optional.of(message);
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Iterable<Message> findAll() {
        List<Message> messages = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("select * from messages");
             ResultSet resultSet = statement.executeQuery()
        ) {
            while (resultSet.next()) {
                UUID id = (UUID) resultSet.getObject("messageID");

                String from = resultSet.getString("from");
                Optional<User> user = findUserByEmail(from);

                Array to = resultSet.getArray("to");
                String[] toUserEmail = (String[]) to.getArray();
                List<String> toListOfUserEmail = new ArrayList<>(List.of(toUserEmail));
                List<User> users = new ArrayList<>();
                toListOfUserEmail.forEach(userEmail -> {
                    Optional<User> u = findUserByEmail(userEmail);
                    u.ifPresent(users::add);
                });

                String messageText = resultSet.getString("messageText");
                LocalDateTime date = resultSet.getTimestamp("date").toLocalDateTime();

                UUID replyMessageID = (UUID) resultSet.getObject("replyMessage");
                Optional<Message> replyMessage = this.findOne(replyMessageID);

                if (user.isPresent()) {
                    Message message = new Message(id, user.get(), users, messageText, date);
                    replyMessage.ifPresent(message::setReplyMessage);
                    messages.add(message);
                }
            }
            return messages;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<Message> save(Message entity) {
        String insertSQL = "insert into messages(\"messageID\", \"from\", \"to\", \"messageText\", date, \"replyMessage\") values (?, ?, ?, ?, ?, ?)";

        try (var connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(insertSQL)) {

            statement.setObject(1, entity.getId());
            statement.setString(2, entity.getFrom().getEmail());

            List<String> usersEmail = new ArrayList<>();
            entity.getTo().forEach(user -> usersEmail.add(user.getEmail()));
            Object[] to = usersEmail.toArray();

            statement.setArray(3, connection.createArrayOf("VARCHAR", to));
            statement.setString(4, entity.getMessageText());

            LocalDateTime javaDateTime = entity.getDateTime();
            String formattedDate = javaDateTime.format(Constants.DATE_TIME_FORMATTER);
            Timestamp sqlDate = Timestamp.valueOf(formattedDate);
            statement.setObject(5, sqlDate);

            if (entity.getReplyMessage() != null) {
                statement.setObject(6, entity.getReplyMessage().getId());
            } else {
                statement.setObject(6, null);
            }

            int response = statement.executeUpdate();
            return response == 0 ? Optional.of(entity) : Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Message> findMessagesBetweenUsers(String userEmail1, String userEmail2) {
        List<Message> messagesBetweenUsers = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(
                     "select * from messages where (\"from\" = ? and ? = any(\"to\")) or (? = any(\"to\") and \"from\" = ?) order by date"
             )
        ) {
            statement.setString(1, userEmail1);
            statement.setString(2, userEmail2);
            statement.setString(3, userEmail1);
            statement.setString(4, userEmail2);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                UUID id = (UUID) resultSet.getObject("messageID");

                String from = resultSet.getString("from");
                Optional<User> user = findUserByEmail(from);

                Array to = resultSet.getArray("to");
                String[] toUserEmail = (String[]) to.getArray();
                List<String> toListOfUserEmail = new ArrayList<>(List.of(toUserEmail));
                List<User> users = new ArrayList<>();
                toListOfUserEmail.forEach(userEmail -> {
                    Optional<User> u = findUserByEmail(userEmail);
                    u.ifPresent(users::add);
                });

                String messageText = resultSet.getString("messageText");
                LocalDateTime date = resultSet.getTimestamp("date").toLocalDateTime();

                UUID replyMessageID = (UUID) resultSet.getObject("replyMessage");
                Optional<Message> replyMessage = this.findOne(replyMessageID);

                if (user.isPresent()) {
                    Message message = new Message(id, user.get(), users, messageText, date);
                    replyMessage.ifPresent(message::setReplyMessage);
                    messagesBetweenUsers.add(message);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return messagesBetweenUsers;
    }

    @Override
    public Optional<Message> update(Message entity) {
        return Optional.empty();
    }

    @Override
    public Optional<Message> delete(UUID uuid) {
        Optional<Message> deletedMessage = findOne(uuid);
        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement("delete from messages where \"messageID\" = ?")
        ) {
            statement.setObject(1, uuid);
            int response = statement.executeUpdate();
            return response == 0 ? Optional.empty() : deletedMessage;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<User> findUserByEmail(String email) {
        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement("select * from users where email = ?")
        ) {
            statement.setObject(1, email);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()) {
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String password = resultSet.getString("password");
                User user = new User(firstName, lastName, email);
                user.setPassword(password);
                return Optional.of(user);
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
