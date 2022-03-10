package net.thumbtack.school.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

// Реализовать очередь задач. Задача - экземпляр класса Task или его наследника,
// имплементирующего Executable - свой интерфейс с единственным методом void execute().
// Потоки - разработчики ставят в очередь экземпляры Task аналогично (15),
// потоки - исполнители берут их из очереди и исполняют.
// Количество тех и других потоков может быть любым и определяется в main
interface Executable {
    void execute();
}

class Developer implements Runnable {
    private BlockingQueue<Task> queue;
    private List<Task> tasks;

    public Developer(BlockingQueue queue, List<Task> tasks) {
        this.queue = queue;
        this.tasks = tasks;
    }

    @Override
    public void run() {
        try {
            for (Task task : tasks) {
                queue.put(task);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class Executor implements Runnable {
    private BlockingQueue<Task> queue;

    public Executor(BlockingQueue queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Task task = queue.take();
                if (task.getNumber() == -1) {
                    break;
                }
                task.execute();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class Task implements Executable {
    private int number;

    public Task(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }


    @Override
    public void execute() {
        System.out.println("...Executing task number " + getNumber() + " in thread " + Thread.currentThread().getName());
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Task number " + getNumber() + " executed by thread " + Thread.currentThread().getName());
    }
}

public class Task16 {
    public static void main(String[] args) {
        final int NUMBER_OF_DEVELOPERS = 5;
        final int NUMBER_OF_EXECUTORS = 5;
        final int NUMBER_OF_TASKS = 10;
        final Task END_MARKER = new Task(-1);

        BlockingQueue<Task> queue = new LinkedBlockingQueue<>();
        List<Task> taskList = new ArrayList<>();

        for (int i = 0; i < NUMBER_OF_TASKS; i++) {
            taskList.add(new Task(i));
        }

        Executor executor = new Executor(queue);
        Developer developer = new Developer(queue, taskList);

        Thread[] developerThreads = new Thread[NUMBER_OF_DEVELOPERS];

        for (int i = 0; i < NUMBER_OF_DEVELOPERS; i++) {
            developerThreads[i] = new Thread(developer);
            developerThreads[i].start();
        }
        for (int i = 0; i < NUMBER_OF_EXECUTORS; i++) {
            new Thread(executor).start();
        }
        try {
            for (Thread thread: developerThreads){
                thread.join();
            }
            for (int i = 0; i < NUMBER_OF_EXECUTORS; i++){
                queue.put(END_MARKER);
            }
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
