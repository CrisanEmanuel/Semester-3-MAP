package com.example.laborator_7.Repository.DBRepository;

import com.example.laborator_7.Domain.*;
import com.example.laborator_7.Domain.Validators.FriendshipValidator;
import com.example.laborator_7.Domain.Validators.ValidationException;
import com.example.laborator_7.Domain.Validators.Validator;
import com.example.laborator_7.Repository.FriendshipRepository;
import com.example.laborator_7.Repository.Repository;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

public class FriendshipDBRepository implements FriendshipRepository {
    protected final String url;
    protected final String username;
    protected final String password;
    protected final Validator<Friendship> validator;

    public FriendshipDBRepository(String url, String username, String password, FriendshipValidator validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
    }

    @Override
    public Optional<Friendship> findOne(UUID id) {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("select * from friendships where friendshipid = ?")
        ) {
            statement.setObject(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                UUID friendshipID = (UUID) resultSet.getObject("friendshipid");
                UUID user1ID = (UUID) resultSet.getObject("user1");
                UUID user2ID = (UUID) resultSet.getObject("user2");
                Date sqlDate = resultSet.getDate("date");
                LocalDate javaDate = sqlDate.toLocalDate();
                FriendshipRequest request = FriendshipRequest.valueOf(resultSet.getString("status"));
                return Optional.of(new Friendship(friendshipID, user1ID, user2ID, javaDate, request));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Iterable<Friendship> findAll() {
        Set<Friendship> friendshipsdb = new HashSet<>();

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("select * from friendships");
             ResultSet resultSet = statement.executeQuery()
        ) {
            while (resultSet.next())
            {
                UUID friendshipID = (UUID) resultSet.getObject("friendshipid");
                UUID user1ID = (UUID) resultSet.getObject("user1");
                UUID user2ID = (UUID) resultSet.getObject("user2");
                Date sqlDate = resultSet.getDate("date");
                LocalDate javaDate = sqlDate.toLocalDate();
                FriendshipRequest request = FriendshipRequest.valueOf(resultSet.getString("status"));

                Friendship friendship = new Friendship(friendshipID, user1ID, user2ID, javaDate, request);
                friendshipsdb.add(friendship);
            }
            return friendshipsdb;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<Friendship> findFriendshipByEmail(String email1, String email2) {
        Optional<User> u1 = findUserByEmail(email1);
        Optional<User> u2 = findUserByEmail(email2);
        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement("select * from friendships " +
                    "where user1 = ? and user2 = ?"
    )
        ) {
            if (u1.isPresent() && u2.isPresent()) {
                statement.setObject(1, u1.get().getId());
                statement.setObject(2, u2.get().getId());

                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    UUID uuid = (UUID) resultSet.getObject("friendshipid");

                    Date sqlDate = resultSet.getDate("date");
                    LocalDate javaDate = sqlDate.toLocalDate();

                    FriendshipRequest request = FriendshipRequest.valueOf(resultSet.getString("status"));

                    Friendship friendship = new Friendship(uuid, u1.get(), u2.get(), javaDate, request);
                    return Optional.of(friendship);
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<Friendship> deleteFriendshipUsingEmail(String email1, String email2) {
        Optional<Friendship> friendship = this.findFriendshipByEmail(email1, email2);
        Optional<User> u1 = findUserByEmail(email1);
        Optional<User> u2 = findUserByEmail(email2);
        String deleteSQL ="delete from friendships where user1 = ? and user2 = ?";
        try (var connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement=connection.prepareStatement(deleteSQL))
        {
            if (u1.isPresent() && u2.isPresent()) {
                statement.setObject(1, u1.get().getId());
                statement.setObject(2, u2.get().getId());
            }
                int response = statement.executeUpdate();
                return response == 0 ? Optional.empty()  : friendship;

        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Friendship> save(Friendship entity) {
        String insertSQL="insert into friendships(friendshipid, user1, user2, date, status) values(?, ?, ?, ?, ?)";
        try (var connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement=connection.prepareStatement(insertSQL))
        {
            validator.validate(entity);
            statement.setObject(1, entity.getId());
            statement.setObject(2, entity.getUser1().getId());
            statement.setObject(3, entity.getUser2().getId());
            statement.setDate(4, Date.valueOf(entity.getDate()));
            statement.setString(5, entity.getRequest().toString());
            int response=statement.executeUpdate();
            return response == 0 ?  Optional.of(entity) : Optional.empty() ;
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
        catch (ValidationException e) {
            throw new ValidationException(e);
        }
    }

    @Override
    public Optional<Friendship> update(Friendship entity) {
        String updateSQL = "update friendships " +
                "set status = ? " +
                "where friendshipid  = ?";
        try (var connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(updateSQL))
        {
            statement.setString(1, entity.getRequest().toString());
            statement.setObject(2, entity.getId());
            int response = statement.executeUpdate();
            return response == 0 ? Optional.of(entity) : Optional.empty();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
        catch (ValidationException e) {
            throw new ValidationException(e);
        }
    }

    @Override
    public Optional<Friendship> delete(UUID id) {
        Optional<Friendship> deletedFriendship = this.findOne(id);
        String deleteSQL ="delete from friendships where friendshipid = ?";
        try (var connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement=connection.prepareStatement(deleteSQL))
        {
            statement.setObject(1, id);
            int response = statement.executeUpdate();
            return response == 0 ? Optional.empty() : deletedFriendship;
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<User> findUserByEmail(String email) {
        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement("select * from users where email = ?")
        ) {
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()) {
                UUID uuid = (UUID) resultSet.getObject("userid");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String password = resultSet.getString("password");
                User user = new User(uuid, firstName, lastName, email);
                user.setPassword(password);
                return Optional.of(user);
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int numberOfFriendshipRequests() {
        int numberOfFriendshipRequests = 0;
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("select count(*) as request_count from friendships")
        ) {
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                numberOfFriendshipRequests = resultSet.getInt("request_count");
            }
            return numberOfFriendshipRequests;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int numberOfUserFriendshipRequests(User user) {
        int numberOfUserFriendsRequests = 0;
        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement("select count(*) as req_count from friendships " +
                    "where user2 = ?")
        ) {
                statement.setObject(1, user.getId());
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    numberOfUserFriendsRequests = resultSet.getInt("req_count");
                }
            return numberOfUserFriendsRequests;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
