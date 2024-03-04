package TaskRunner;

import Containers.Container;
import Domain.Task;
import Factory.Factory;
import Factory.Strategy;
import Factory.TaskContainerFactory;

public class StrategyTaskRunner implements TaskRunner {

    private Container container;

    public StrategyTaskRunner(Strategy strategy) {
        Factory factory = TaskContainerFactory.getInstance();
        container = factory.createContainer(strategy);
    }
    @Override
    public void executeOneTask() {
        if(!container.isEmpty())
            container.remove().execute();
    }

    @Override
    public void executeAll() {
        while(!container.isEmpty())
            executeOneTask();
    }

    @Override
    public void addTask(Task t) {
        container.add(t);
    }

    @Override
    public boolean hasTask() {
        return !container.isEmpty();
    }
}
