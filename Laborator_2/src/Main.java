import Domain.MessageTask;
import Domain.Task;
import Factory.Strategy;
import TaskRunner.StrategyTaskRunner;
import TaskRunner.PrinterTaskRunner;
import TaskRunner.DelayTaskRunner;
import TaskRunner.TaskRunner;
import Tests.Tests;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        System.out.println("Choose FIFO/LIFO");
        String strategy = sc.nextLine();

        Tests tests = new Tests();
        tests.testMessageTask();
        tests.testSortingTask();
        tests.testCreateArrayOfMessageTask();

        MessageTask m0 = new MessageTask("1","message1","Hi!","Me","You", LocalDateTime.now());
        MessageTask m1 = new MessageTask("2","message2","Hi!","Me","You", LocalDateTime.now());
        MessageTask m2 = new MessageTask("3","message3","Hi!","Me","You", LocalDateTime.now());
        MessageTask m3 = new MessageTask("4","message4","Hi!","Me","You", LocalDateTime.now());
        MessageTask m4 = new MessageTask("5","message5","Hi!","Me","You", LocalDateTime.now());

        List<Task> list = Arrays.asList(m0,m1,m2,m3,m4);

        StrategyTaskRunner strategyTaskRunner = new StrategyTaskRunner(Strategy.valueOf(strategy.toUpperCase()));
        System.out.println("Strategy Task Runner:");
        for (Task t: list)
           strategyTaskRunner.addTask(t);
        strategyTaskRunner.executeAll();
        System.out.println();

        System.out.println("Printer Task Runner");
        TaskRunner printerTaskRunner = new PrinterTaskRunner(strategyTaskRunner);
        for (Task t: list)
            printerTaskRunner.addTask(t);
        printerTaskRunner.executeAll();
        System.out.println();

        System.out.println("Delay Task Runner");
        TaskRunner delayTaskRunner = new DelayTaskRunner(strategyTaskRunner);
        for (Task t: list)
            delayTaskRunner.addTask(t);
        delayTaskRunner.executeAll();
        System.out.println();
    }
}