package Containers;

import Domain.Task;
public class StackContainer  extends AbstractTaskContainer{
    public StackContainer() {
        super();
    }
    @Override
    public Task remove() {
        return list.remove(list.size() - 1);
    }
}
