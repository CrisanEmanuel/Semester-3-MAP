package Containers;

import Domain.Task;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractTaskContainer implements Container{

    List<Task> list;
    public AbstractTaskContainer() {
        this.list = new ArrayList<>();
    }

    @Override
    public abstract Task remove();

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public void add(Task task) {
        list.add(task);
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }


}
