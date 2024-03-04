package Domain;

import java.util.Arrays;

public class SortingTask extends Task{
    private Integer[] array;

    private AbstractSorter sorter;

    public SortingTask(String taskID, String description, Integer[] array, AbstractSorter sorter) {
        super(taskID, description);
        this.array = array;
        this.sorter = sorter;
    }

    @Override
    public void execute() {
        System.out.println("Sorting using " + sorter.getClass().getSimpleName() + " strategy:");
        sorter.sort(array);
        System.out.println(Arrays.toString(array));
    }
}

