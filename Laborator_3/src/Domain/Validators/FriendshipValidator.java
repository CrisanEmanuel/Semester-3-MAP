package Domain.Validators;

import Domain.Friendship;

import java.util.UUID;

public class FriendshipValidator implements Validator<Friendship> {
    @Override
    public void validate(Friendship entity) throws ValidationException {
        UUID id1 = entity.getUser1().getId();
        UUID id2 = entity.getUser2().getId();
        if(id1 == id2) {
            throw new ValidationException("Cannot add yourself to friends!");
        }
    }
}
