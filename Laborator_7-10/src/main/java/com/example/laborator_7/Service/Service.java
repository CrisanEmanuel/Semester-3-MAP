package com.example.laborator_7.Service;

import com.example.laborator_7.Domain.Friendship;
import com.example.laborator_7.Domain.FriendshipRequest;
import com.example.laborator_7.Domain.Message;
import com.example.laborator_7.Domain.User;
import com.example.laborator_7.Domain.Validators.ValidationException;
import com.example.laborator_7.Repository.DBRepository.FriendshipDBPagingRepository;
import com.example.laborator_7.Repository.DBRepository.UserDBPagingRepository;
import com.example.laborator_7.Repository.FriendshipRepository;
import com.example.laborator_7.Repository.MessageRepository;
import com.example.laborator_7.Repository.Paging.Page;
import com.example.laborator_7.Repository.Paging.Pageable;
import com.example.laborator_7.Repository.Paging.PageableImplementation;
import com.example.laborator_7.Repository.UserRepository;
import com.example.laborator_7.Utils.Events.ChangeEventType;
import com.example.laborator_7.Utils.Events.FriendshipChangeEvent;
import com.example.laborator_7.Utils.Events.MessageChangeEvent;
import com.example.laborator_7.Utils.Events.UserChangeEvent;
import com.example.laborator_7.Utils.Observer.Observable;
import com.example.laborator_7.Utils.Observer.Observer;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static com.example.laborator_7.Domain.FriendshipRequest.*;

public class Service implements Observable {
    private final UserRepository repo_user;

    private final FriendshipRepository repo_friendship;

    private final MessageRepository repo_messages;

    private final UserDBPagingRepository userDBPagingRepository;

    private final FriendshipDBPagingRepository friendshipDBPagingRepository;

    private int numberOfUsers;
    private int numberOfFriendshipRequests;

    public Service(UserRepository repo1, FriendshipRepository repo2, MessageRepository repo3, UserDBPagingRepository repo4, FriendshipDBPagingRepository repo5) {
        this.repo_friendship = repo2;
        this.repo_user = repo1;
        this.repo_messages = repo3;
        this.userDBPagingRepository = repo4;
        this.friendshipDBPagingRepository = repo5;
        this.numberOfUsers = repo_user.numberOfUsers();
        this.numberOfFriendshipRequests = repo_friendship.numberOfFriendshipRequests();
    }

    // (START) USERS USERS USERS USERS USERS USERS USERS USERS USERS USERS USERS USERS USERS USERS USERS USERS USERS USERS
    public void addUser(User user) {
        try {
            Optional<User> userAdded = repo_user.save(user);
            if (userAdded.isEmpty()) {
                notifyUserObservers(new UserChangeEvent(ChangeEventType.ADD, user));
                this.numberOfUsers++;
            }
        } catch (ValidationException e) {
            throw new ValidationException(e);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteUser(String email) {

        if (email == null) {
            throw new IllegalArgumentException("Email must be not null!");
        }
        Optional<User> userDeleted = repo_user.deleteUserByEmail(email);
        if (userDeleted.isPresent()) {
            notifyUserObservers(new UserChangeEvent(ChangeEventType.DELETE, userDeleted.get()));
            this.numberOfUsers--;
        } else {
            throw new IllegalArgumentException("User does not exist!");
        }
    }

    public void updateUser(User newUser) {
        Optional<User> oldUser = repo_user.findOne(newUser.getId());
        if(oldUser.isPresent()) {
            try {
                Optional<User> res = repo_user.update(newUser);
                if (res.isEmpty()) {
                    notifyUserObservers(new UserChangeEvent(ChangeEventType.UPDATE, newUser, oldUser.get()));
                } else {
                    throw new IllegalArgumentException("User does not exist!");
                }
            } catch (ValidationException e) {
                throw new ValidationException(e);
            }
        }
    }

    public Optional<User> findUserByEmail(String email) {
        try {
            return repo_user.findUserByEmail(email);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }
    public Iterable<User> getAllUsers() {
        return repo_user.findAll();
    }

    public Iterable<User> getAllFriends(User user) {
        return repo_user.findAllFriends(user);
    }

    public int getNumberOfUsers() {
        return this.numberOfUsers;
    }
    public int getNumberOfUserFriends(User user) {
        return repo_user.numberOfUserFriends(user);
    }
    private int user_page = 0;
    private int user_size = 1;
    public void setUserPageSize(int user_size) {
        this.user_size = user_size;
    }

    public Set<User> getNextUsers() {
        this.user_page++;
        return getUsersOnPage(this.user_page);
    }

    public Set<User> getUsersOnPage(int page) {
        this.user_page = page;
        Pageable pageable = new PageableImplementation(page, this.user_size);
        Page<User> userPage = userDBPagingRepository.findAll(pageable);
        return userPage.getContent().collect(Collectors.toSet());
    }

    // (END) USERS USERS USERS USERS USERS USERS USERS USERS USERS USERS USERS USERS USERS USERS USERS USERS USERS USERS

    // (START) FRIENDSHIPS FRIENDSHIPS FRIENDSHIPS FRIENDSHIPS FRIENDSHIPS FRIENDSHIPS FRIENDSHIPS FRIENDSHIPS FRIENDSHIPS
    public void createFriendship(String email1, String email2) {
        Optional<User> u1 = findUserByEmail(email1);
        Optional<User> u2 = findUserByEmail(email2);

        Optional<Friendship> optionalFriendship1 = repo_friendship.findFriendshipByEmail(email1, email2);
        Optional<Friendship> optionalFriendship2 = repo_friendship.findFriendshipByEmail(email2, email1);

        if (optionalFriendship1.isPresent() && optionalFriendship1.get().getRequest().equals(PENDING)) {
            throw new IllegalArgumentException("Request already sent!");
        }
        if (optionalFriendship1.isPresent() && optionalFriendship1.get().getRequest().equals(ACCEPTED)) {
            throw new IllegalArgumentException("You and " + email2 + " are already friends!");
        }
        if (optionalFriendship2.isPresent() && optionalFriendship2.get().getRequest().equals(PENDING)) {
            throw new IllegalArgumentException("You already got a request from " + email2);
        }
        if (optionalFriendship2.isPresent() && optionalFriendship2.get().getRequest().equals(ACCEPTED)) {
            throw new IllegalArgumentException("You and " + email2 + " are already friends!");
        }
        if (u1.isPresent() && u2.isPresent()) {
            Friendship prietenie = new Friendship(u1.get(), u2.get());
            try {
                repo_friendship.save(prietenie);
                notifyFriendshipObservers(new FriendshipChangeEvent(ChangeEventType.ADD, prietenie));
                this.numberOfFriendshipRequests++;
            } catch (ValidationException e) {
                throw new ValidationException(e.getMessage());
            }
        }
    }

    public void deleteFriendship(String email1, String email2) {
        Optional<User> u1 = findUserByEmail(email1);
        Optional<User> u2 = findUserByEmail(email2);
        if (email1 == null || email2 == null) {
            throw new IllegalArgumentException("Email must be not null!");
        }
        if (u1.isEmpty()|| u2.isEmpty()) {
            throw new IllegalArgumentException("User does not exist!");
        } else {
            Optional<Friendship> friendship = repo_friendship.findFriendshipByEmail(email1, email2);
            repo_friendship.deleteFriendshipUsingEmail(email1, email2);
            friendship.ifPresent(value -> notifyFriendshipObservers(new FriendshipChangeEvent(ChangeEventType.DELETE, value)));
            this.numberOfFriendshipRequests--;
          }
    }

    public Iterable<Friendship> getAllFriendships() {
        List<Friendship> friendshipsJava = new ArrayList<>();

        Iterable<Friendship> friendshipsDB = repo_friendship.findAll();

        friendshipsDB.forEach(dbFriendship -> {
            Optional<User> user1 = repo_user.findOne(dbFriendship.getUser1ID());
            Optional<User> user2 = repo_user.findOne(dbFriendship.getUser2ID());
            if (user1.isPresent() && user2.isPresent()) {
                LocalDate date = dbFriendship.getDate();
                FriendshipRequest request = dbFriendship.getRequest();
                UUID uuid = dbFriendship.getId();
                Friendship javaFriendship = new Friendship(uuid, user1.get(), user2.get(), date, request);
                friendshipsJava.add(javaFriendship);
            }
        });
        return friendshipsJava;
    }

    public void acceptFriendRequest(String email1, String email2) {
        Optional<User> u1, u2;
        if (email1 == null || email2 == null) {
            throw new IllegalArgumentException("Email must be not null!");
        }
        u1 = findUserByEmail(email1);
        u2 = findUserByEmail(email2);

        if (u1.isEmpty() || u2.isEmpty()) {
            throw new IllegalArgumentException("User does not exist!");
        } else {
            Iterable<Friendship> lista = this.getAllFriendships();
            lista.forEach(friendship -> {
                if (Objects.equals(friendship.getUser1(), u1.get()) && Objects.equals(friendship.getUser2(), u2.get())) {
                    friendship.set_request(ACCEPTED);
                    repo_friendship.update(friendship);
                    notifyFriendshipObservers(new FriendshipChangeEvent(ChangeEventType.UPDATE, friendship));
                    //repo_friendship.deleteFriendshipUsingEmail(u1.getEmail(), u2.getEmail());
                    repo_user.addFriend(u1.get(), u2.get());
                    notifyUserObservers(new UserChangeEvent(ChangeEventType.ADD, u2.get()));
                }
            });
        }
    }

    public void declineFriendRequest(String email1, String email2) {
        Optional<User> u1, u2;
        u1 = findUserByEmail(email1);
        u2 = findUserByEmail(email2);
        if (u1.isEmpty() || u2.isEmpty()) {
            throw new IllegalArgumentException("User does not exist!");
        } else {
            Iterable<Friendship> lista = this.getAllFriendships();
            lista.forEach(friendship -> {
                if (Objects.equals(friendship.getUser1(), u1.get()) && Objects.equals(friendship.getUser2(), u2.get())) {
                    friendship.set_request(REJECTED);
                    repo_friendship.update(friendship);
                    notifyFriendshipObservers(new FriendshipChangeEvent(ChangeEventType.UPDATE, friendship));
                    //repo_friendship.deleteFriendshipUsingEmail(u1.getEmail(), u2.getEmail());
                }
            });
        }
    }

    public Set<Friendship> DBShowFriendshipsMadeInACertainDate(int monthNumber, String userEmail) {
        Set<Friendship> requiredFriendships = new HashSet<>();
        Iterable<Friendship> allFriendships = this.getAllFriendships();
        allFriendships.forEach(friendship -> {
            if (Objects.equals(friendship.getUser1().getEmail(), userEmail) && friendship.getDate().getMonthValue() == monthNumber) {
                requiredFriendships.add(friendship);
            }
            if (Objects.equals(friendship.getUser2().getEmail(), userEmail) && friendship.getDate().getMonthValue() == monthNumber) {
                requiredFriendships.add(friendship);
            }
        });
        return requiredFriendships;
    }

    public int getNumberOfFriendshipRequests() {
        return this.numberOfFriendshipRequests;
    }

    public int getTheNumberOfUserRequests(User user) {
        return repo_friendship.numberOfUserFriendshipRequests(user);
    }

    private int friendship_request_page = 0;
    private int friendship_request_size = 1;
    public void setFriendshipPageSize(int friendship_request_size) {
        this.friendship_request_size = friendship_request_size;
    }

    public Set<Friendship> getNextFriendshipRequests() {
        this.friendship_request_page++;
        return getFriendshipRequestsOnPage(this.friendship_request_page);
    }

    public Set<Friendship> getFriendshipRequestsOnPage(int page) {
        this.friendship_request_page = page;
        Pageable pageable = new PageableImplementation(page, this.friendship_request_size);
        Page<Friendship> friendshipPage = friendshipDBPagingRepository.findAll(pageable);
        return friendshipPage.getContent().collect(Collectors.toSet());
    }

    // (END) FRIENDSHIPS FRIENDSHIPS FRIENDSHIPS FRIENDSHIPS FRIENDSHIPS FRIENDSHIPS FRIENDSHIPS FRIENDSHIPS FRIENDSHIPS

    // (START) MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES
    public List<Message> conversationBetweenTwoUsers(String user1Email, String user2Email) {
        return repo_messages.findMessagesBetweenUsers(user1Email, user2Email);
    }

    public Iterable<Message> getAllMessages() {
        return repo_messages.findAll();
    }

    public void addMessage(Message message) {
        repo_messages.save(message);
        notifyMessageObservers(new MessageChangeEvent(ChangeEventType.ADD, message));
    }
    // (END) MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES

    public int communitiesNumber() {
        AtomicInteger nr = new AtomicInteger();
        Set<User> set = new HashSet<>();
        Iterable<User> users = getAllUsers();
        users.forEach(user -> {
            if (!set.contains(user)) {
                nr.getAndIncrement();
                DFS(user, set);
            }
        });
        return nr.get();
    }

    public Iterable<User> mostSociableCommunity() {
        AtomicReference<List<User>> list = new AtomicReference<>(new ArrayList<>());
        Iterable<User> it = repo_user.findAll();
        Set<User> set = new HashSet<>();

        AtomicInteger max = new AtomicInteger(-1);
        it.forEach(user -> {
            if(!set.contains(user)) {
                List<User> aux = DFS(user, set);
                int l = longestPath(aux);
                if (l > max.get()) {
                    list.set(aux);
                    max.set(l);
                }
            }
        });
        return list.get();
    }

    private int longestPath(List<User> nodes) {
        int max = 0;
        for (User u : nodes) {
            int l = longestPathFromSource(u);
            if (max < l)
                max = l;
        }
        return max;
    }

    private int longestPathFromSource(User source) {
        Set<User> set = new HashSet<>();
        return BFS(source, set);
    }

    private int BFS(User source, Set<User> set) {
        int max = -1;
        for (User f : source.getFriends().values())
            if (!set.contains(f)) {
                set.add(f);
                int l = BFS(f, set);
                if (l > max)
                    max = l;
                set.remove(f);
            }
        return max + 1;
    }

    private List<User> DFS(User u, Set<User> set) {
        List<User> list = new ArrayList<>();
        list.add(u);
        set.add(u);
        for (User f : u.getFriends().values()) {
            if (!set.contains(f)) {
                List<User> l = DFS(f, set);
                list.addAll(l);
            }
        }
        return list;
    }

    private final List<Observer<UserChangeEvent>> userObserver = new ArrayList<>();
    @Override
    public void addUserObserver(Observer<UserChangeEvent> e) {
        userObserver.add(e);
    }
    @Override
    public void removeUserObserver(Observer<UserChangeEvent> e) {
        userObserver.remove(e);
    }
    @Override
    public void notifyUserObservers(UserChangeEvent u) {
        userObserver.forEach(x->x.update(u));
    }



    private final List<Observer<FriendshipChangeEvent>> friendshipObserver = new ArrayList<>();
    @Override
    public void addFriendshipObserver(Observer<FriendshipChangeEvent> e) {
        friendshipObserver.add(e);
    }
    @Override
    public void removeFriendshipObserver(Observer<FriendshipChangeEvent> e) {
        friendshipObserver.remove(e);
    }
    @Override
    public void notifyFriendshipObservers(FriendshipChangeEvent u) {
        friendshipObserver.forEach(x->x.update(u));
    }



    private final List<Observer<MessageChangeEvent>> messageObserver = new ArrayList<>();
    @Override
    public void addMessageObserver(Observer<MessageChangeEvent> e) {
        messageObserver.add(e);
    }
    @Override
    public void removeMessageObserver(Observer<MessageChangeEvent> e) {
        messageObserver.remove(e);
    }
    @Override
    public void notifyMessageObservers(MessageChangeEvent u) {
        messageObserver.forEach(x->x.update(u));
    }



    public void addData() {
        User u1 = new User("u1FirstName", "u1LastName", "e1");
        User u2 = new User("u2FirstName", "u2LastName", "e2");
        User u3 = new User("u3FirstName", "u3LastName", "e3");
        User u4 = new User("u4FirstName", "u4LastName", "e4");
        User u5 = new User("u5FirstName", "u5LastName", "e5");
        User u6 = new User("u6FirstName", "u6LastName", "e6");
        User u7 = new User("u7FirstName", "u7LastName", "e7");
        User u8 = new User("u8FirstName", "u8LastName", "e8");
        repo_user.save(u1);
        repo_user.save(u2);
        repo_user.save(u3);
        repo_user.save(u4);
        repo_user.save(u5);
        repo_user.save(u6);
        repo_user.save(u7);
        repo_user.save(u8);
        createFriendship(u1.getEmail(), u2.getEmail());
        createFriendship(u2.getEmail(), u3.getEmail());
        createFriendship(u4.getEmail(), u5.getEmail());
        createFriendship(u6.getEmail(), u5.getEmail());
        createFriendship(u4.getEmail(), u6.getEmail());
        createFriendship(u1.getEmail(), u3.getEmail());
        createFriendship(u1.getEmail(), u7.getEmail());
        acceptFriendRequest(u1.getEmail(), u2.getEmail());
        acceptFriendRequest(u2.getEmail(), u3.getEmail());
        acceptFriendRequest(u4.getEmail(), u5.getEmail());
        acceptFriendRequest(u6.getEmail(), u5.getEmail());
        acceptFriendRequest(u4.getEmail(), u6.getEmail());
        acceptFriendRequest(u1.getEmail(), u3.getEmail());
        acceptFriendRequest(u1.getEmail(), u7.getEmail());
    }

}
