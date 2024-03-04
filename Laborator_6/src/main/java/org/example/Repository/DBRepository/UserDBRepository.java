package org.example.Repository.DBRepository;

import org.example.Domain.Friendship;
import org.example.Domain.User;
import org.example.Repository.Repository;

import java.sql.*;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;


public class UserDBRepository implements Repository<UUID, User> {
    private final String url;
    private final String username;
    private final String password;

//    private Validator<User> validator;

    public UserDBRepository(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
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
                User user = new User(firstName, lastName, email);
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
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String email = resultSet.getString("email");
                User user = new User(firstName, lastName, email);
                users.add(user);
            }
            return users;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<User> save(User entity) {
        String insertSQL = "insert into users (first_name, last_name, email) values(?, ?, ?)";
        try (var connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement=connection.prepareStatement(insertSQL))
        {
            statement.setString(1,entity.getFirstName());
            statement.setString(2,entity.getLastName());
            statement.setString(3,entity.getEmail());
            int response = statement.executeUpdate();
            return response == 0 ? Optional.of(entity) : Optional.empty();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<User> deleteUserByEmail(String email) {
        Optional<User> user = findUserByEmail(email);
        String deleteSQL = "delete from users where email = ?";
        try (var connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement(deleteSQL))
        {
            statement.setString(1, email);
            int response = statement.executeUpdate();
            return response == 0 ? user : Optional.empty();
        }
        catch (SQLException e) {
                throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Friendship> findFriendshipByEmail(String email1, String email2) {
        return Optional.empty();
    }

    @Override
    public Optional<Friendship> deleteFriendshipUsingEmail(String email1, String email2) {
        return Optional.empty();
    }

    @Override
    public Optional<User> delete(UUID id) {
        return Optional.empty();
    }

    @Override
    public Optional<User> update(User entity) {
        return Optional.empty();
    }

    @Override
    public Optional<User> findOne(UUID id) {
        return Optional.empty();
    }
}