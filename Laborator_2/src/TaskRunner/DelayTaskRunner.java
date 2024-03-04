package TaskRunner;

public class DelayTaskRunner extends AbstractTaskRunner{

    TaskRunner taskRunner;
    public DelayTaskRunner(TaskRunner taskRunner) {
        super(taskRunner);
    }

    @Override
    public void executeAll() {
        while (super.hasTask()) {
        try {
            Thread.sleep(1800);
            super.executeOneTask();
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    }
}
