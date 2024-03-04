package com.example.laborator_7.Utils.Events;

import com.example.laborator_7.Domain.User;
import com.example.laborator_7.Utils.Events.ChangeEventType;
import com.example.laborator_7.Utils.Events.Event;

public class UserChangeEvent implements Event {
    private final ChangeEventType type;
    private final User data;
    private User oldData;
    public UserChangeEvent(ChangeEventType type, User data) {
        this.type = type;
        this.data = data;
    }
    public UserChangeEvent(ChangeEventType type, User data, User oldData) {
        this.type = type;
        this.data = data;
        this.oldData = oldData;
    }

    public ChangeEventType getType() {
        return type;
    }

    public User getData() {
        return data;
    }

    public User getOldData() {
        return oldData;
    }
}
