package net.thumbtack.school.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

class MultistageDeveloperTask18 implements Runnable {
    private final BlockingQueue<MultistageTask> tasksQueue;
    private final BlockingQueue<Event> eventsQueue;

    public MultistageDeveloperTask18(BlockingQueue<MultistageTask> tasksQueue, BlockingQueue<Event> eventsQueue) {
        this.tasksQueue = tasksQueue;
        this.eventsQueue = eventsQueue;
    }

    @Override
    public void run() {
        try {
            eventsQueue.put(Event.DEVELOPER_STARTED);
            List<Executable> stageList = new ArrayList<>();
            final int NUMBER_OF_TASKS = (int) (Math.random() + 1) * 2;
            final int NUMBER_OF_STAGES = (int) (Math.random() + 1) * 3;
            for (int i = 0; i < NUMBER_OF_STAGES; i++) {  //случайное кол-во стадий
                stageList.add(new Stage(i));
            }
            for (int i = 0; i < NUMBER_OF_TASKS; i++) {  // случайное кол-во задач в очередь
                if (Math.random() < 0.5) {
                    eventsQueue.put(Event.TASK_STARTED);
                    tasksQueue.put((new MultistageTask(stageList, i)));
                } else {
                    new Thread(new MultistageDeveloperTask18(tasksQueue, eventsQueue)).start();
                }
            }
            eventsQueue.put(Event.DEVELOPER_FINISHED);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

public class Task18 {
    public static void main(String[] args) {
        final int NUMBER_OF_DEVELOPERS = 2;
        final int NUMBER_OF_EXECUTORS = 5;
        final MultistageTask END_MARKER = new MultistageTask(-1);

        BlockingQueue<Event> eventsQueue = new LinkedBlockingQueue<>();
        BlockingQueue<MultistageTask> tasksQueue = new LinkedBlockingQueue<>();
        MultistageExecutor executor = new MultistageExecutor(tasksQueue, eventsQueue);
        MultistageDeveloperTask18 developer = new MultistageDeveloperTask18(tasksQueue, eventsQueue);

        Thread[] developerThreads = new Thread[NUMBER_OF_DEVELOPERS];
        Thread[] executorThreads = new Thread[NUMBER_OF_EXECUTORS];

        for (int i = 0; i < NUMBER_OF_DEVELOPERS; i++) {
            developerThreads[i] = new Thread(developer);
            developerThreads[i].start();
        }
        for (int i = 0; i < NUMBER_OF_EXECUTORS; i++) {
            executorThreads[i] = new Thread(executor);
            executorThreads[i].start();
        }
        int tasksCount = 0;
        int developersCount = 0;
        while (true) {
            Event event = null;
            try {
                event = eventsQueue.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (event == Event.TASK_STARTED) {
                tasksCount++;
            }
            if (event == Event.TASK_FINISHED) {
                tasksCount--;
                if (tasksCount == 0 && developersCount == 0) {
                    break;
                }
            }
            if (event == Event.DEVELOPER_STARTED) {
                developersCount++;
            }
            if (event == Event.DEVELOPER_FINISHED) {
                developersCount--;
            }
        }
        for (int i = 0; i < NUMBER_OF_EXECUTORS; i++) {
            try {
                tasksQueue.put(END_MARKER);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
