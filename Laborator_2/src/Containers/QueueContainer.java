package Containers;

import Domain.Task;

public class QueueContainer extends AbstractTaskContainer {
    public QueueContainer() {
        super();
    }
    @Override
    public Task remove() {
        return list.remove(0);
    }
}
