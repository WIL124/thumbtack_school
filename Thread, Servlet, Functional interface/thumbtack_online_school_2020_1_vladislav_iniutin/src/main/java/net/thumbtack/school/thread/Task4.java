package net.thumbtack.school.thread;

import java.util.ArrayList;
import java.util.List;

class Adder extends Thread {
    private final List<Integer> list;

    Adder(List<Integer> arg) {
        list = arg;
    }

    @Override
    public void run() {
        int i = 0;
        while (i != 10000) {
            synchronized (list) {
                int d = (int) (Math.random() * 100);
                System.out.println("add elem " + d + " at index " + list.size());
                list.add(d);
                i++;
            }
        }
    }
}

class Remover extends Thread {
    private final List<Integer> list;

    Remover(List<Integer> arg) {
        list = arg;
    }

    @Override
    public void run() {
        int i = 0;
        while (i != 10000) {
            synchronized (list) {
                int index = (int) (Math.random() * 10000);
                System.out.println("try to remove elem at index: " + index);
                if (list.size() > index) {
                    list.remove(index);
                    System.out.println("elem at index: " + index + " was removed");
                }
                i++;
            }
        }
    }
}

public class Task4 {
    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>();
        Adder adder = new Adder(list);
        Remover remover = new Remover(list);
        adder.start();
        remover.start();
        try {
            adder.join();
            remover.join();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }
}
