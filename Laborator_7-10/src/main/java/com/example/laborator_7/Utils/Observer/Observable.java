package com.example.laborator_7.Utils.Observer;
import com.example.laborator_7.Utils.Events.FriendshipChangeEvent;
import com.example.laborator_7.Utils.Events.MessageChangeEvent;
import com.example.laborator_7.Utils.Events.UserChangeEvent;

public interface Observable {
    void addUserObserver(Observer<UserChangeEvent> e);
    void removeUserObserver(Observer<UserChangeEvent> e);
    void notifyUserObservers(UserChangeEvent t);

    void addFriendshipObserver(Observer<FriendshipChangeEvent> e);
    void removeFriendshipObserver(Observer<FriendshipChangeEvent> e);
    void notifyFriendshipObservers(FriendshipChangeEvent t);

    void addMessageObserver(Observer<MessageChangeEvent> e);
    void removeMessageObserver(Observer<MessageChangeEvent> e);
    void notifyMessageObservers(MessageChangeEvent t);
}