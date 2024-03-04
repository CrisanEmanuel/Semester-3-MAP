package org.example.Service;

import org.example.Domain.Friendship;
import org.example.Domain.User;
import org.example.Repository.Repository;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static org.example.Domain.FriendshipRequest.ACCEPTED;
import static org.example.Domain.FriendshipRequest.REJECTED;

public class Service implements ServiceInterface {
    private final Repository<UUID, User> repo_user;

    private final Repository<UUID, Friendship> repo_friendship;

    public Service(Repository<UUID, User> repo1, Repository<UUID, Friendship> repo2) {
        this.repo_friendship = repo2;
        this.repo_user = repo1;
    }

    @Override
    public void addUser(User user) {
        try {
            repo_user.save(user);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void deleteUser(String email) {

        if (email == null) {
            throw new IllegalArgumentException("Email must be not null!");
        }

        User u = getUserUsingEmail(email);
        if (u == null) {
            throw new IllegalArgumentException("User does not exist!");
        } else {
            // delete user
            //repo_user.delete(u.getId());
            repo_user.deleteUserByEmail(email);

            // delete all friendships that contains the deleted user
            Iterable<Friendship> friendships = repo_friendship.findAll();
            Iterator<Friendship> friendshipIterator = friendships.iterator();
            while (friendshipIterator.hasNext()) {
                Friendship friendship = friendshipIterator.next();
                if (friendship.getUser1().getId() == u.getId()) friendshipIterator.remove();
                if (friendship.getUser2().getId() == u.getId()) friendshipIterator.remove();
            }

            // delete the user from all users friend list
            Iterable<User> users = repo_user.findAll();
            for (User user : users) {
                Map<UUID, User> userFriends = user.getFriends();
                userFriends.remove(u.getId());
            }
        }
    }

    @Override
    public void createFriendship(String email1, String email2) {
        User u1, u2;
        u1 = getUserUsingEmail(email1);
        u2 = getUserUsingEmail(email2);
        if (u1 == null || u2 == null) {
            throw new IllegalArgumentException("User does not exist!");
        } else {
            Friendship prietenie = new Friendship(u1, u2);
            repo_friendship.save(prietenie);
        }
    }

    @Override
    public void deleteFriendship(String email1, String email2) {
        User u1, u2;
        u1 = getUserUsingEmail(email1);
        u2 = getUserUsingEmail(email2);
        if (email1 == null || email2 == null) {
            throw new IllegalArgumentException("Email must be not null!");
        }
        if (u1 == null || u2 == null) {
            throw new IllegalArgumentException("User does not exist!");
        } else {
            repo_friendship.deleteFriendshipUsingEmail(email1, email2);
//            Iterable<Friendship> lista = repo_friendship.findAll();
//            lista.forEach(friendship -> {
//                if (friendship.getUser1() == u1 && friendship.getUser2() == u2)
//                    repo_friendship.delete(friendship.getId());
//            });
////            for(Friendship p : lista) {
////                if(p.getUser1() == u1 && p.getUser2() == u2){
////                    repo_friendship.delete(p.getId());
////              }
////           }
        }
    }

    @Override
    public Iterable<User> getAllUsers() {
        return repo_user.findAll();
    }

    @Override
    public Iterable<Friendship> getAllFriendships() {
        return repo_friendship.findAll();
    }

    @Override
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
//        for(User utilizator : users){
//            if(!set.contains(utilizator)){
//                nr++;
//                DFS(utilizator,set);
//            }
//        }
        return nr.get();
    }

    @Override
    public void acceptFriendRequest(String email1, String email2) {
        User u1, u2;
        if (email1 == null || email2 == null) {
            throw new IllegalArgumentException("Email must be not null!");
        }
        u1 = getUserUsingEmail(email1);
        u2 = getUserUsingEmail(email2);

        if (u1 == null || u2 == null) {
            throw new IllegalArgumentException("User does not exist!");
        } else {
            Iterable<Friendship> lista = repo_friendship.findAll();
            lista.forEach(friendship -> {
                if (friendship.getUser1() == u1 && friendship.getUser2() == u2) {
                    friendship.set_request(ACCEPTED);
                }
            });
//            for(Friendship p : lista){
//                if(p.getUser1() == u1 && p.getUser2() == u2) {
//                    p.set_request(ACCEPTED);
//                }
//            }
        }
    }

    @Override
    public void declineFriendRequest(String email1, String email2) {
        User u1, u2;
        u1 = getUserUsingEmail(email1);
        u2 = getUserUsingEmail(email2);
        if (u1 == null || u2 == null) {
            throw new IllegalArgumentException("User does not exist!");
        } else {
            Iterable<Friendship> lista = repo_friendship.findAll();
            lista.forEach(friendship -> {
                if (friendship.getUser1() == u1 && friendship.getUser2() == u2) {
                    friendship.set_request(REJECTED);
                }
            });
//            for(Friendship p : lista){
//                if(p.getUser1()==u1 && p.getUser2()==u2){
//                    p.set_request(FriendshipRequest.REJECTED);
//                    break;
//                }
//            }
        }
    }


    @Override
    public User getUserUsingEmail(String email) {
        Iterable<User> lista = repo_user.findAll();
        AtomicReference<User> result = new AtomicReference<>();
        lista.forEach(user -> {
            if (Objects.equals(user.getEmail(), email)) {
                result.set(user);
            }
        });
//        for(User u : lista){
//            if(Objects.equals(u.getEmail(), email)){
//                return u;
//            }
//        }
        return result.get();
    }

    @Override
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
//        for (User u : it)
//            if (!set.contains(u)) {
//                List<User> aux = DFS(u, set);
//                int l = longestPath(aux);
//                if (l > max) {
//                    list = aux;
//                    max = l;
//                }
//            }
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
//                for (User x : l) {  am inlocuit cu list.addAll(l);
//                    list.add(x);
//                }
            }
        }
        return list;
    }

    public Set<Friendship> DBShowFriendshipsMadeInACertainDate(int monthNumber, String userEmail) {
        Set<Friendship> requiredFriendships = new HashSet<>();
        Iterable<Friendship> allFriendships = repo_friendship.findAll();
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

    @Override
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
