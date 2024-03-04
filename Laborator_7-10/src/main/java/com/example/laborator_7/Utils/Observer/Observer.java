package com.example.laborator_7.Utils.Observer;

import com.example.laborator_7.Utils.Events.Event;

public interface Observer<E extends Event> {
    void update(E e);
}

