package Tests;

import Domain.MessageTask;
import Domain.SortingTask;
import SortingMethods.BubbleSort;
import SortingMethods.QuickSort;

import java.time.LocalDateTime;

public class Tests {
    public void testMessageTask() {
        MessageTask m1 = new MessageTask("1", "message", "hello", "Mike", "Sarah", LocalDateTime.now());
        MessageTask m2 = new MessageTask("2", "message", "hi", "Sarah", "Mike", LocalDateTime.now());
        m1.execute();
        m2.execute();

        System.out.println();
    }
    public void testSortingTask() {
        Integer[] array1 = {4, 3, 10, 3, 2, 5, 1};
        SortingTask t1 = new SortingTask("1", "sort", array1, new BubbleSort());
        t1.execute();

        Integer[] array2 = {4, 3, 10, 3, 12, 5, 10};
        SortingTask t2 = new SortingTask("2", "sort", array2, new QuickSort());
        t2.execute();

        System.out.println();
    }

    public void testCreateArrayOfMessageTask() {
        MessageTask[] array = new MessageTask[5];
        array[0] = new MessageTask("1", "message", "Hi!", "Dom", "Brian", LocalDateTime.now());
        array[1] = new MessageTask("2", "message", "Hi!", "Dom", "Brian", LocalDateTime.now());
        array[2] = new MessageTask("3", "message", "Hi!", "Dom", "Brian", LocalDateTime.now());
        array[3] = new MessageTask("4", "message", "Hi!", "Dom", "Brian", LocalDateTime.now());
        array[4] = new MessageTask("5", "message", "Hi!", "Dom", "Brian", LocalDateTime.now());

        System.out.println("MessageTask list:");
        for(MessageTask messageTask : array) {
            System.out.println(messageTask);
        }
        System.out.println();
    }
}
