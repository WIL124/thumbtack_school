package net.thumbtack.school.thread;

import java.util.Arrays;
import java.util.concurrent.*;
//Реализовать очередь данных. Данные - экземпляр класса Data с единственным методом int[] get().
//Потоки-писатели ставят в очередь экземпляры Data, количество экземпляров Data, которое ставит в очередь каждый писатель,
//определяется в его конструкторе. Потоки - читатели берут их из очереди и распечатывают,
//и в конечном итоге должны взять все экземпляры Data, которые записали все писатели вместе взятые.
//Количество тех и других потоков может быть любым и определяется в main.
class Data {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Data)) return false;
        Data data = (Data) o;
        return Arrays.equals(numbs, data.numbs);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(numbs);
    }

    private int[] numbs;

    public Data(int[] numbs) {
        this.numbs = numbs;
    }

    public int[] get() {
        return numbs;
    }
}

class Writer implements Runnable {
    private final BlockingQueue<Data> queue;
    private final int countOfData;

    public Writer(BlockingQueue<Data> queue, int countOfData) {
        this.queue = queue;
        this.countOfData = countOfData;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < countOfData; i++) {
                queue.put(new Data(new int[]{i, i + 1, i + 2}));
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class Reader implements Runnable {
    private final BlockingQueue<Data> queue;
    private final Data END_MARKER;

    public Reader(BlockingQueue<Data> queue, Data END_MARKER) {
        this.queue = queue;
        this.END_MARKER = END_MARKER;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Data data = queue.take();
                if (data.equals(END_MARKER)) {
                    break;
                }
                System.out.println(Arrays.toString(data.get()));
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

public class Task15 {
    public static void main(String[] args) {
        final int NUM_OF_WRITERS = 2;
        final int NUM_OF_READERS = 2;
        final int COUNT_OF_DATA = 3;
        final Data END_MARKER = new Data(new int[]{-1, -1, -1});

        BlockingQueue<Data> queue = new LinkedBlockingQueue<>();
        Writer writer = new Writer(queue,COUNT_OF_DATA);
        Thread[] threadsOfWriters = new Thread[NUM_OF_WRITERS];

        for (int i = 0; i < NUM_OF_WRITERS; i++){    //запускаем потоки-писатели, оставляем на них ссылку
            threadsOfWriters[i] = new Thread(writer);
            threadsOfWriters[i].start();
        }
        for (int i = 0; i < NUM_OF_READERS; i++){   //запускаем потоки-читатели
            new Thread(new Reader(queue, END_MARKER)).start();
        }

        try {
            for(Thread thread: threadsOfWriters){   // ждём пока все писатели "допишут"
                thread.join();
            }
            for (int i = 0; i < NUM_OF_READERS; i++){ //ставим end-markers столько, сколько потоков-читателей
                queue.put(END_MARKER);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
