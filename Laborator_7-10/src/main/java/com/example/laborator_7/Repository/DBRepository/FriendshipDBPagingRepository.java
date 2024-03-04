package com.example.laborator_7.Repository.DBRepository;

import com.example.laborator_7.Domain.Friendship;
import com.example.laborator_7.Domain.FriendshipRequest;
import com.example.laborator_7.Domain.User;
import com.example.laborator_7.Domain.Validators.FriendshipValidator;
import com.example.laborator_7.Repository.Paging.Page;
import com.example.laborator_7.Repository.Paging.PageImplementation;
import com.example.laborator_7.Repository.Paging.Pageable;
import com.example.laborator_7.Repository.Paging.PagingRepository;

import java.sql.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class FriendshipDBPagingRepository extends FriendshipDBRepository implements PagingRepository<UUID, Friendship> {

    public FriendshipDBPagingRepository(String url, String username, String password, FriendshipValidator validator) {
        super(url, username, password, validator);
    }

    @Override
    public Page<Friendship> findAll(Pageable pageable) {
        Set<Friendship> friendships = new HashSet<>();

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("select * from friendships limit ? offset ?")
        ) {
            statement.setInt(1, pageable.getPageSize());
            statement.setInt(2, (pageable.getPageNumber() - 1) * pageable.getPageSize());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next())
            {
                UUID friendshipID = (UUID) resultSet.getObject("friendshipid");
                UUID user1ID = (UUID) resultSet.getObject("user1");
                User user1 = findUserById(user1ID);

                UUID user2ID = (UUID) resultSet.getObject("user2");
                User user2 = findUserById(user2ID);

                Date sqlDate = resultSet.getDate("date");
                LocalDate javaDate = sqlDate.toLocalDate();
                FriendshipRequest request = FriendshipRequest.valueOf(resultSet.getString("status"));

                Friendship friendship = new Friendship(friendshipID, user1, user2, javaDate, request);
                friendships.add(friendship);
            }
            return new PageImplementation<>(pageable, friendships.stream());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private User findUserById(UUID uuid) {
        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement("select * from users where userid = ?")
        ) {
            statement.setObject(1, uuid);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()) {
                UUID userid = (UUID) resultSet.getObject("userid");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String email = resultSet.getString("email");
                return new User(userid, firstName, lastName, email);
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
