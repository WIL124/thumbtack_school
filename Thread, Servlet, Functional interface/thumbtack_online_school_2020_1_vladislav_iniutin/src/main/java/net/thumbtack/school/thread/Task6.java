package net.thumbtack.school.thread;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


class AdderTask6 extends Thread {
    private List<Integer> list;

    AdderTask6(List<Integer> arg) {
        list = arg;
    }

    @Override
    public void run() {
        int i = 0;
        while (i != 10000) {
            int d = (int) (Math.random() * 100);
            System.out.println("add elem " + d + " at index " + list.size());
            list.add(d);
            i++;
        }
    }
}

class RemoverTask6 extends Thread {
    private List<Integer> list;

    RemoverTask6(List<Integer> arg) {
        list = arg;
    }

    @Override
    public void run() {
        int i = 0;
        while (i != 10000) {
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

public class Task6 {
    public static void main(String[] args) {
        List<Integer> list = Collections.synchronizedList(new ArrayList<>());
        new AdderTask6(list).start();
        new RemoverTask6(list).start();
    }
}
