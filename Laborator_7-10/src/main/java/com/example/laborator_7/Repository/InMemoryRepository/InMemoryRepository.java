//package com.example.laborator_7.Repository.InMemoryRepository;
//
//import com.example.laborator_7.Domain.Entity;
//import com.example.laborator_7.Domain.Friendship;
//import com.example.laborator_7.Domain.Message;
//import com.example.laborator_7.Domain.User;
//import com.example.laborator_7.Domain.Validators.Validator;
//import com.example.laborator_7.Repository.Repository;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Optional;
//
//public class InMemoryRepository<ID, E extends Entity<ID>> implements Repository<ID, E> {
//
//    private final Validator<E> validator;
//
//    Map<ID, E> entities;
//
//    public InMemoryRepository(Validator<E> validator) {
//        this.validator = validator;
//        entities = new HashMap<>();
//    }
//
//    @Override
//    public Optional<E> save(E entity)  {
//        if (entity == null)
//            throw new IllegalArgumentException("Entity must be not null");
//        validator.validate(entity);
//        return Optional.ofNullable(entities.putIfAbsent(entity.getId(), entity));
//    }
//
//    @Override
//    public Optional<E> findOne(ID id) {
//        if (id == null)
//            throw new IllegalArgumentException("Id must be not null");
//        return Optional.ofNullable(entities.get(id));
//    }
//
//    @Override
//    public Optional<E> update(E entity) {
//        if (entity == null)
//            throw new IllegalArgumentException("Entity must be not null");
//        validator.validate(entity);
//
//        // entities.put(entity.getId(), entity);
//
//        if (entities.get(entity.getId()) != null) {
//            entities.put(entity.getId(), entity);
//            return Optional.empty();
//        }
//        return Optional.of(entity);
//    }
//
//    @Override
//    public Optional<User> findUserByEmail(String email) {
//        return entities.values().stream()
//                .filter(user -> user instanceof User && ((User) user).getEmail().equals(email))
//                .map(user -> (User) user)
//                .findFirst();
//    }
//
//    @SuppressWarnings("unchecked")
//    private ID castId(Object id) {
//        return (ID) id;
//    }
//
//    @Override
//    public Optional<User> deleteUserByEmail(String email) {
//        Optional<User> userToDelete = findUserByEmail(email);
//        userToDelete.ifPresent(user -> entities.remove(castId(user.getId())));
//        return userToDelete;
//    }
//
//    @Override
//    public Optional<Friendship> findFriendshipByEmail(String email1, String email2) {
//        return entities.values().stream()
//                .filter(friendship -> friendship instanceof Friendship &&
//                        ((Friendship) friendship).getUser1().getEmail().equals(email1) &&
//                        ((Friendship) friendship).getUser2().getEmail().equals(email2))
//                .map(friendship -> (Friendship) friendship)
//                .findFirst();
//    }
//
//    @Override
//    public Optional<Friendship> deleteFriendshipUsingEmail(String email1, String email2) {
////        Optional<Friendship> friendshipToDelete = findFriendshipByEmail(email1, email2);
////        friendshipToDelete.ifPresent(friendship -> entities.remove(castId(friendship.getId())));
////        return friendshipToDelete;
//        return Optional.empty();
//    }
//
//    @Override
//    public List<Message> findMessagesBetweenUsers(String user1Email, String user2Email) {
//        return null;
//    }
//
//    @Override
//    public void addFriend(User u1, User u2) {
//
//    }
//
//    @Override
//    public Iterable<User> findAllFriends(User user) {
//        return null;
//    }
//
//    @Override
//    public Optional<E> delete(ID id) {
//        if (id == null)
//            throw new IllegalArgumentException("Id must be not null");
//        return Optional.ofNullable(entities.remove(id));
//    }
//
//    @Override
//    public Iterable<E> findAll() {
//        return entities.values();
//    }
//}
