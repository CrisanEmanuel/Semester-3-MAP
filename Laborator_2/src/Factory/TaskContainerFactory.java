package Factory;

import Containers.Container;
import Containers.QueueContainer;
import Containers.StackContainer;

public class TaskContainerFactory implements Factory {

    private static TaskContainerFactory instance;

    private TaskContainerFactory(){};
    @Override
    public Container createContainer(Strategy strategy) {
        if(strategy == Strategy.LIFO)
            return new StackContainer();
        else if (strategy == Strategy.FIFO)
            return new QueueContainer();
        return null;
    }

    public static TaskContainerFactory getInstance() {
        if(instance == null) {
            instance = new TaskContainerFactory();
        }
        return instance;
    }
}
