package org.example.Repository.DBRepository;

import org.example.Domain.Friendship;
import org.example.Domain.User;
import org.example.Domain.Validators.Validator;
import org.example.Repository.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class FriendshipDBRepository implements Repository<UUID, Friendship> {
    private final String url;
    private final String username;
    private final String password;
    private Validator<Friendship> validator;

    public FriendshipDBRepository(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public Optional<Friendship> findFriendshipByEmail(String email1, String email2) {
        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement("select f.date, f.user1, f.user2, " +
                    "u1.first_name as u1FirstName, u1.last_name as u1LastName, " +
                    "u2.first_name as u2FirstName, u2.last_name as u2LastName " +
                    "from friendships f " +
                    "inner join users u1 ON f.user1 = u1.email " +
                    "inner join users u2 ON f.user2 = u2.email " +
                    "where u1.email = ? and u2.email = ?");
        ) {
            statement.setObject(1, email1);
            statement.setObject(2, email2);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()) {
                Date sqlDate = resultSet.getDate("date");
                LocalDate javaDate = sqlDate.toLocalDate();

                String user1Email = resultSet.getString("user1");
                String user1FirstName = resultSet.getString("u1FirstName");
                String user1LastName = resultSet.getString("u1LastName");
                User user1 = new User(user1FirstName, user1LastName, user1Email);

                String user2Email = resultSet.getString("user2");
                String user2FirstName = resultSet.getString("u2FirstName");
                String user2LastName = resultSet.getString("u2LastName");
                User user2 = new User(user2FirstName, user2LastName, user2Email);

                Friendship friendship = new Friendship(user1, user2, javaDate);
                return Optional.of(friendship);
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Friendship> deleteFriendshipUsingEmail(String email1, String email2) {
        Optional<Friendship> friendship = findFriendshipByEmail(email1, email2);
        String deleteSQL ="delete from friendships where user1 = ? and user2 = ?";
        try (var connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement=connection.prepareStatement(deleteSQL))
        {
            statement.setString(1, email1);
            statement.setString(2, email2);
            int response = statement.executeUpdate();
            return response == 0 ? friendship : Optional.empty();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Iterable<Friendship> findAll() {
        Set<Friendship> friendships = new HashSet<>();

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("select f.date, f.user1, f.user2, " +
                     "u1.first_name as u1FirstName, u1.last_name as u1LastName, " +
                     "u2.first_name as u2FirstName, u2.last_name as u2LastName " +
                     "from friendships f " +
                     "inner join users u1 ON f.user1 = u1.email " +
                     "inner join users u2 ON f.user2 = u2.email");
             ResultSet resultSet = statement.executeQuery()
        ) {
            while (resultSet.next())
            {
                Date sqlDate = resultSet.getDate("date");
                LocalDate javaDate = sqlDate.toLocalDate();

                String user1Email = resultSet.getString("user1");
                String user1FirstName = resultSet.getString("u1FirstName");
                String user1LastName = resultSet.getString("u1LastName");
                User user1 = new User(user1FirstName, user1LastName, user1Email);

                String user2Email = resultSet.getString("user2");
                String user2FirstName = resultSet.getString("u2FirstName");
                String user2LastName = resultSet.getString("u2LastName");
                User user2 = new User(user2FirstName, user2LastName, user2Email);

                Friendship friendship = new Friendship(user1, user2, javaDate);
                friendships.add(friendship);
            }
            return friendships;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Friendship> save(Friendship entity) {
        String insertSQL="insert into friendships(date, user1, user2) values(?, ?, ?)";
        try (var connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement=connection.prepareStatement(insertSQL))
        {
            statement.setDate(1, Date.valueOf(entity.getDate()));
            statement.setString(2, entity.getUser1().getEmail());
            statement.setString(3, entity.getUser2().getEmail());
            int response=statement.executeUpdate();
            return response == 0 ? Optional.of(entity) : Optional.empty();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Friendship> delete(UUID id) {
        return Optional.empty();
    }

    @Override
    public Optional<Friendship> update(Friendship entity) {
        return Optional.empty();
    }

    @Override
    public Optional<User> findUserByEmail(String email) {
        return Optional.empty();
    }

    @Override
    public Optional<User> deleteUserByEmail(String email) {
        return Optional.empty();
    }

    @Override
    public Optional<Friendship> findOne(UUID id) {
        return Optional.empty();
    }
}
