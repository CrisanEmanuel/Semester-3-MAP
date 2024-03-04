package Domain;

import java.util.*;

public class User extends  Entity<UUID>{

    private String firstName;

    private String lastName;

    private String email;

    private final Map<UUID, User> friends = new HashMap<>();

    public User(String firstName, String lastName, String email) {
        this.id = UUID.randomUUID();
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void addFriendUser(User user) {
        friends.put(user.getId(), user);
    }

    public boolean removeFriendUser(User u){
        return friends.remove(u.getId(),u);
    }

    public Map<UUID, User> getFriends() {
        return friends;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if(!(o instanceof User that)) return false;
        return getFirstName().equals(that.getFirstName()) &&
                getLastName().equals(that.getLastName()) &&
                getFriends().equals(that.getFriends());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFirstName(), getLastName());
    }

    @Override
    public String toString() {
        return "User{" +
                "firstName='" + firstName + '\'' +
                //", lastName='" + lastName + '\'' +
                //", email='" + email + '\''+
                //", friends=" + friends +
                '}';
    }
}
