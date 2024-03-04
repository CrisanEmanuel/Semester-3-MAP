package org.example.Domain.Validators;

import org.example.Domain.User;

public class UserValidator implements Validator<User> {
    @Override
    public void validate(User entity) throws ValidationException {
        if(entity.getEmail().isEmpty() ||
                entity.getFirstName().isEmpty() ||
                entity.getLastName().isEmpty())
            throw new ValidationException("Validate error: The user cannot be null");
    }
}
