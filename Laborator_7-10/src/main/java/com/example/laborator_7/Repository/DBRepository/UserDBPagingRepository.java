package com.example.laborator_7.Repository.DBRepository;

import com.example.laborator_7.Domain.User;
import com.example.laborator_7.Domain.Validators.UserValidator;
import com.example.laborator_7.Repository.Paging.Page;
import com.example.laborator_7.Repository.Paging.PageImplementation;
import com.example.laborator_7.Repository.Paging.Pageable;
import com.example.laborator_7.Repository.Paging.PagingRepository;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class UserDBPagingRepository extends UserDBRepository implements PagingRepository<UUID, User> {

    public UserDBPagingRepository(String url, String username, String password, UserValidator validator) {
        super(url, username, password, validator);
    }

    @Override
    public Page<User> findAll(Pageable pageable) {
        Set<User> users = new HashSet<>();

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("select * from users limit ? offset ?")
        ) {
            statement.setInt(1, pageable.getPageSize());
            statement.setInt(2, (pageable.getPageNumber() - 1) * pageable.getPageSize());
            ResultSet resultSet = statement.executeQuery();
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
            return new PageImplementation<>(pageable, users.stream());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
