package com.example.laborator_7.Repository.DBRepository;

import com.example.laborator_7.Domain.User;
import com.example.laborator_7.Domain.Validators.UserValidator;
import com.example.laborator_7.Domain.Validators.ValidationException;
import com.example.laborator_7.Repository.UserRepository;

import java.sql.*;
import java.util.*;


public class UserDBRepository implements UserRepository {
    protected final String url;
    protected final String username;
    protected final String password;
    protected final UserValidator validator;

    public UserDBRepository(String url, String username, String password, UserValidator validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
    }

    @Override
    public Optional<User> findOne(UUID id) {
        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement("select * from users where userid = ?")
        ) {
            statement.setObject(1, id);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()) {
                UUID userid = (UUID) resultSet.getObject("userid");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String email = resultSet.getString("email");
                String password = resultSet.getString("password");
                User user = new User(userid, firstName, lastName, email);
                user.setPassword(password);
                return Optional.of(user);
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

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
    public Iterable<User> findAll() {
        Set<User> users = new HashSet<>();

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("select * from users");
             ResultSet resultSet = statement.executeQuery()
        ) {
            while (resultSet.next())
            {
                UUID userid = (UUID) resultSet.getObject("userid");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String email = resultSet.getString("email");
                String password = resultSet.getString("password");
                User user = new User(userid, firstName, lastName, email);
                user.setPassword(password);
                users.add(user);
            }
            return users;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<User> save(User entity) {
        String insertSQL = "insert into users (userid, first_name, last_name, email, password) values(?, ?, ?, ?, ?)";
        try (var connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement=connection.prepareStatement(insertSQL))
        {
            validator.validate(entity);
            statement.setObject(1,entity.getId());
            statement.setString(2,entity.getFirstName());
            statement.setString(3,entity.getLastName());
            statement.setString(4,entity.getEmail());
            statement.setString(5,entity.getPassword());
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
    public Optional<User> delete(UUID id) {
        Optional<User> user = findOne(id);
        String deleteSQL = "delete from users where userid = ?";
        try (var connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(deleteSQL))
        {
            statement.setObject(1, id);
            int response = statement.executeUpdate();
            return response == 0 ? Optional.empty() : user;
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<User> deleteUserByEmail(String email) {
        Optional<User> user = findUserByEmail(email);
        String deleteSQL = "delete from users where email = ?";
        try (var connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement(deleteSQL))
        {
            statement.setString(1, email);
            int response = statement.executeUpdate();
            return response == 0 ? Optional.empty() : user;
        }
        catch (SQLException e) {
                throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<User> update(User entity) {
        Optional<User> user = findOne(entity.getId());
        String updateSQL = "update users " +
                "set first_name = ?, last_name = ? " +
                "where email = ?";
        try (var connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(updateSQL))
        {
            validator.validate(entity);
            statement.setString(1, entity.getFirstName());
            statement.setString(2, entity.getLastName());
            statement.setString(3, entity.getEmail());
            int response = statement.executeUpdate();
            return response == 0 ? user : Optional.empty();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
        catch (ValidationException e) {
            throw new ValidationException(e);
        }
    }

    public void addFriend(User u1, User u2) {
        String insertSQL = "insert into userfriends (user1id, user2id) values(?, ?)";
        try (var connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement=connection.prepareStatement(insertSQL))
        {
            statement.setObject(1,u1.getId());
            statement.setObject(2,u2.getId());
            statement.executeUpdate();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
        catch (ValidationException e) {
            throw new ValidationException(e);
        }
    }

    public List<User> findAllFriends(User user) {
        List<User> friends = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("select u.userid, u.first_name, u.last_name, u.email " +
                     "from users u " +
                     "inner join userfriends uf on u.userid = uf.user1id or u.userid = uf.user2id " +
                     "where (uf.user1id = ? or uf.user2id = ?) and (u.userid != ?)")
        ) {
            statement.setObject(1, user.getId());
            statement.setObject(2, user.getId());
            statement.setObject(3, user.getId());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next())
            {
                UUID userid = (UUID) resultSet.getObject("userid");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String email = resultSet.getString("email");
                User friend = new User(userid, firstName, lastName, email);
                friends.add(friend);
            }
            return friends;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int numberOfUsers() {
        int numberOfUsers = 0;
        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement("select count(*) as user_count from users")
        ) {
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()) {
                numberOfUsers = Integer.parseInt(resultSet.getString("user_count"));
            }
            return numberOfUsers;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public int numberOfUserFriends(User user) {
        int numberOfFriends = 0;
        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement("select count(*) as friend_count " +
                    "from users u " +
                    "inner join userfriends uf on u.userid = uf.user1id or u.userid = uf.user2id " +
                    "where (uf.user1id = ? or uf.user2id = ?) and (u.userid != ?)")
        ) {
            statement.setObject(1, user.getId());
            statement.setObject(2, user.getId());
            statement.setObject(3, user.getId());
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()) {
                numberOfFriends = Integer.parseInt(resultSet.getString("friend_count"));
            }
            return numberOfFriends;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}