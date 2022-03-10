package net.thumbtack.school.thread;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

// Реализовать очередь многостадийных задач. Многостадийная задача - экземпляр класса MultistageTask,
// имеющего список из стадий - List<Executable>, где Executable - интерфейс из задания 16.
// Потоки - разработчики ставят в очередь экземпляры Stage аналогично (15), потоки - исполнители берут из очереди задачу,
// исполняют очередную ее стадию, после чего, если это не последняя стадия, ставят задачу обратно в очередь.
// Количество тех и других потоков может быть любым и определяется в main.

//считать кол-во запущенных/закончившихся developers, кол-во запущенных/закончившихся задач

//Developer создаёт случайный список задач. Main-функция о кол-ве задач не знает
enum Event {
    DEVELOPER_STARTED,
    DEVELOPER_FINISHED,
    TASK_STARTED,
    TASK_FINISHED
}

@Getter
class Stage implements Executable {
    private final int stageNumber;

    public Stage(int stageNumber) {
        this.stageNumber = stageNumber;
    }

    @Override
    public void execute() {
        System.out.println("...Executing stage number " + getStageNumber() + " in thread " + Thread.currentThread().getName());
        System.out.println("Stage number " + getStageNumber() + " executed by thread " + Thread.currentThread().getName());
    }
}

@Getter
class MultistageTask implements Executable {
    private List<Executable> executableList;
    private final int taskNumber;
    private int currentStage = 0;

    public MultistageTask(List<Executable> executableList, int taskNumber) {
        this.taskNumber = taskNumber;
        this.executableList = executableList;
    }

    public MultistageTask(int taskNumber) {
        this.taskNumber = taskNumber;
    }

    @Override
    public void execute() {
        System.out.println("...Task number " + getTaskNumber() + " stage number " + currentStage + " run by " + Thread.currentThread().getName());
        if (executableList.size() > currentStage) {
            executableList.get(currentStage).execute();
            currentStage++;
        } else {
            System.out.println("Task number " + getTaskNumber() + " stage number " + currentStage + " executed by " + Thread.currentThread().getName());
        }
    }
}

class MultistageDeveloper implements Runnable {
    private final BlockingQueue<MultistageTask> tasksQueue;
    private final BlockingQueue<Event> eventsQueue;

    public MultistageDeveloper(BlockingQueue<MultistageTask> tasksQueue, BlockingQueue<Event> eventsQueue) {
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
                eventsQueue.put(Event.TASK_STARTED);
                tasksQueue.put((new MultistageTask(stageList, i)));
            }
            eventsQueue.put(Event.DEVELOPER_FINISHED);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class MultistageExecutor implements Runnable {
    private final BlockingQueue<MultistageTask> tasksQueue;
    private final MultistageTask END_MARKER = new MultistageTask(-1);
    private final BlockingQueue<Event> eventsQueue;

    public MultistageExecutor(BlockingQueue<MultistageTask> tasksQueue, BlockingQueue<Event> eventsQueue) {
        this.eventsQueue = eventsQueue;
        this.tasksQueue = tasksQueue;
    }

    @Override
    public void run() {
        while (true) {
            try {
                MultistageTask task = tasksQueue.take();
                if (task.getTaskNumber() == END_MARKER.getTaskNumber()) {
                    break;
                }
                task.execute();
                if (task.getExecutableList().size() != task.getCurrentStage()) {
                    tasksQueue.put(task);
                } else {
                    eventsQueue.put(Event.TASK_FINISHED);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

public class Task17 {
    public static void main(String[] args) {
        final int NUMBER_OF_DEVELOPERS = 3;
        final int NUMBER_OF_EXECUTORS = 2;
        final MultistageTask END_MARKER = new MultistageTask(-1);

        BlockingQueue<Event> eventsQueue = new LinkedBlockingQueue<>();
        BlockingQueue<MultistageTask> tasksQueue = new LinkedBlockingQueue<>();
        MultistageExecutor executor = new MultistageExecutor(tasksQueue, eventsQueue);
        MultistageDeveloper developer = new MultistageDeveloper(tasksQueue, eventsQueue);

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
