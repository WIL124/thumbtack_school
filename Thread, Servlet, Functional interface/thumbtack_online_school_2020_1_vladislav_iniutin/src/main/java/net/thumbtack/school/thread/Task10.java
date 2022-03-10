package net.thumbtack.school.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class AdderLock extends Thread {
    private List<Integer> list;
    private Lock lock;

    AdderLock(List<Integer> arg, Lock lock) {
        this.lock = lock;
        list = arg;
    }

    @Override
    public void run() {
        int i = 0;
        while (i != 10000) {
            lock.lock();
            try {
                int d = (int) (Math.random() * 100);
                System.out.println("add elem " + d + " at index " + list.size());
                list.add(d);
                i++;
            } finally {
                lock.unlock();
            }
        }
    }
}

class RemoverLock extends Thread {
    private List<Integer> list;
    private Lock lock;

    RemoverLock(List<Integer> arg, Lock lock) {
        this.lock = lock;
        list = arg;
    }

    @Override
    public void run() {
        int i = 0;
        while (i != 10000) {
            lock.lock();
            try {
                int index = (int) (Math.random() * 10000);
                System.out.println("try to remove elem at index: " + index);
                if (list.size() > index) {
                    list.remove(index);
                    System.out.println("elem at index: " + index + " was removed");
                }
                i++;
            } finally {
                lock.unlock();
            }
        }
    }
}

public class Task10 {
    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>();
        Lock lock = new ReentrantLock();

        new AdderLock(list, lock).start();
        new RemoverLock(list, lock).start();
        new AdderLock(list, lock).start();
        new RemoverLock(list, lock).start();
    }
}
