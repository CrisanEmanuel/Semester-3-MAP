package Service;

import Domain.Friendship;
import Domain.FriendshipRequest;
import Domain.User;
import Repository.Repository;

import java.util.*;

import static Domain.FriendshipRequest.ACCEPTED;

public class Service implements ServiceInterface {
    private Repository repo_user;

    private Repository repo_friendship;

    public Service(Repository repo1, Repository repo2) {
        this.repo_friendship = repo2;
        this.repo_user = repo1;
    }

    @Override
    public boolean addUser(User user) {
        try {
            return repo_user.save(user);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public void deleteUser(String email) {
        if(email == null) {
            throw new IllegalArgumentException("Email must be not null!");
        }
        User u = getUserUsingEmail(email);
        if(u == null) {
            throw new IllegalArgumentException("User does not exist!");
        }
        else repo_user.delete(u.getId());
    }

    @Override
    public void createFriendship(String email1, String email2) {
        User u1,u2;
        u1 = getUserUsingEmail(email1);
        u2 = getUserUsingEmail(email2);
        if(u1 == null || u2 == null){
            throw new IllegalArgumentException("User does not exist!");
        }
        else {
            Friendship prietenie = new Friendship(u1,u2);
            repo_friendship.save(prietenie);
        }
    }

    @Override
    public boolean deleteFriendship(String email1, String email2) {
        User u1,u2;
        u1 = getUserUsingEmail(email1);
        u2 = getUserUsingEmail(email2);
        if(email1 == null || email2 == null) {
            throw new IllegalArgumentException("Email must be not null!");
        }
        if(u1 == null || u2 == null) {
            throw new IllegalArgumentException("User does not exist!");
        }
        else {
            Iterable<Friendship> lista = repo_friendship.findAll();
            for(Friendship p : lista) {
                if(p.getUser1() == u1 && p.getUser2() == u2){
                    repo_friendship.delete(p.getId());
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public Iterable getAllUsers() {
        return repo_user.findAll();
    }

    @Override
    public Iterable getAllFriendships() {
        return repo_friendship.findAll();
    }

    @Override
    public int communitiesNumber() {
        int nr=0;
        Set<User> set = new HashSet<>();
        Iterable<User> u = getAllUsers();
        for( User utilizator : u){
            if(!set.contains(utilizator)){
                nr++;
                DFS(utilizator,set);
            }
        }
        return nr;
    }

    @Override
    public void acceptFriendRequest(String email1, String email2) {
        User u1,u2;
        if(email1 == null || email2 == null) {
            throw new IllegalArgumentException("Email must be not null!");
        }
        u1 = getUserUsingEmail(email1);
        u2 = getUserUsingEmail(email2);

        if(u1 == null || u2 == null) {
            throw new IllegalArgumentException("User does not exist!");
        }
        else {
            Iterable<Friendship> lista = repo_friendship.findAll();
            for(Friendship p : lista){
                if(p.getUser1() == u1 && p.getUser2() == u2) {
                    p.set_request(ACCEPTED);
                    //break;
                    //System.out.println(u1.getId());
                }
                //System.out.println(p);
            }
        }
    }

    @Override
    public void declineFriendRequest(String email1, String email2) {
        User u1,u2;
        u1 = getUserUsingEmail(email1);
        u2 = getUserUsingEmail(email2);
        if(u1 == null || u2 == null){
            throw new IllegalArgumentException("User does not exist!");
        }
        else{
            Iterable<Friendship> lista = repo_friendship.findAll();
            for(Friendship p : lista){
                if(p.getUser1()==u1 && p.getUser2()==u2){
                    p.set_request(FriendshipRequest.REJECTED);
                    break;
                }
            }
        }
    }

    @Override
    public void addData() {
        User u1 = new User("u1FirstName", "u1LastName","e1"); u1.setId(UUID.randomUUID());
        User u2 = new User("u2FirstName", "u2LastName","e2"); u2.setId(UUID.randomUUID());
        User u3 = new User("u3FirstName", "u3LastName","e3"); u3.setId(UUID.randomUUID());
        User u4 = new User("u4FirstName", "u4LastName","e4"); u4.setId(UUID.randomUUID());
        User u5 = new User("u5FirstName", "u5LastName","e5"); u5.setId(UUID.randomUUID());
        User u6 = new User("u6FirstName", "u6LastName","e6"); u6.setId(UUID.randomUUID());
        User u7 = new User("u7FirstName", "u7LastName","e7"); u7.setId(UUID.randomUUID());
        repo_user.save(u1);
        repo_user.save(u2);
        repo_user.save(u3);
        repo_user.save(u4);
        repo_user.save(u5);
        repo_user.save(u6);
        repo_user.save(u7);
//        createFriendship(u1.getEmail(),u2.getEmail());
//        createFriendship(u2.getEmail(),u3.getEmail());
//        createFriendship(u4.getEmail(),u5.getEmail());
//        createFriendship(u6.getEmail(),u5.getEmail());
//        createFriendship(u4.getEmail(),u6.getEmail());
//        createFriendship(u1.getEmail(),u3.getEmail());
//        createFriendship(u1.getEmail(),u7.getEmail());
//        acceptFriendRequest(u1.getEmail(),u2.getEmail());
//        acceptFriendRequest(u2.getEmail(),u3.getEmail());
//        acceptFriendRequest(u4.getEmail(),u5.getEmail());
//        acceptFriendRequest(u6.getEmail(),u5.getEmail());
//        acceptFriendRequest(u4.getEmail(),u6.getEmail());
//        acceptFriendRequest(u1.getEmail(),u3.getEmail());
//        acceptFriendRequest(u1.getEmail(),u7.getEmail());
    }

    @Override
    public User getUserUsingEmail(String email) {
        Iterable<User> lista = repo_user.findAll();
        for(User u : lista){
            if(Objects.equals(u.getEmail(), email)){
                return u;
            }
        }
        return null;
    }

    @Override
    public Iterable<User> mostSociableCommunity() {
        List<User> list = new ArrayList<>();
        Iterable<User> it = repo_user.findAll();
        Set<User> set = new HashSet<>();

        int max = -1;
        for(User u : it)
            if(!set.contains(u))
            {
                List<User> aux = DFS(u, set);
                int l = longestPath(aux);
                if(l > max) {
                    list=aux;
                    max = l;
                }
            }
        return list;
    }

    private int longestPath(List<User> nodes){
        int max = 0;
        for(User u : nodes) {
            int l = longestPathFromSource(u);
            if(max < l)
                max = l;
        }
        return max;
    }

    private int longestPathFromSource(User source){
        Set<User> set = new HashSet<>();
        return BFS(source, set);
    }

    private int BFS(User source, Set<User> set){
        int max = -1;
        for(User f : source.getFriends().values())
            if(!set.contains(f)) {
                set.add(f);
                int l = BFS(f, set);
                if(l > max)
                    max = l;
                set.remove(f);
            }
        return max + 1;
    }

    private List<User> DFS(User u, Set<User> set){
        List <User> list = new ArrayList<>();
        list.add(u);
        set.add(u);
        for(User f : u.getFriends().values()){
            if(!set.contains(f)){
                List<User> l = DFS(f, set);
                for(User x : l){
                    list.add(x);
                }
            }
        }
        return list;
    }
}
