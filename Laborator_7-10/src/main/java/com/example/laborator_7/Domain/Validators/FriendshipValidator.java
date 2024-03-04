package com.example.laborator_7.Domain.Validators;

import com.example.laborator_7.Domain.Friendship;

public class FriendshipValidator implements Validator<Friendship> {
    @Override
    public void validate(Friendship entity) throws ValidationException {
        String email1 = entity.getUser1().getEmail();
        String email2 = entity.getUser2().getEmail();
        if(email1.equals(email2)) {
            throw new ValidationException("Cannot add yourself to friends!");
        }
    }
}
