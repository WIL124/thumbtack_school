package net.thumbtack.school.thread;

import java.util.ArrayList;
import java.util.List;

enum WhatToDo {
    ADD_TO_LIST,
    REMOVE_FROM_LIST;
}

class Caller extends Thread {
    private WhatToDo whatToDo;
    private ListHolder listHolder;

    Caller(WhatToDo whatToDo, ListHolder argList) {
        super();
        this.whatToDo = whatToDo;
        listHolder = argList;
    }

    @Override
    public void run() {
        if (whatToDo.equals(WhatToDo.ADD_TO_LIST)) {
            for (int i = 0; i < 10000; i++) listHolder.addToList();
        } else {
            for (int i = 0; i < 10000; i++) listHolder.removeFromList();
        }
    }
}

class ListHolder {
    private List<Integer> list;

    public ListHolder(List<Integer> list) {
        this.list = list;
    }

    public synchronized void addToList() {
        int d = (int) (Math.random() * 100);
        System.out.println("add elem " + d + " at index " + list.size());
        list.add(d);
    }

    public synchronized void removeFromList() {
        int index = (int) (Math.random() * 10000);
        System.out.println("try to remove elem at index: " + index);
        if (list.size() > index) {
            list.remove(index);
            System.out.println("elem at index: " + index + " was removed");
        }
    }
}

public class Task5 {
    public static void main(String[] args) {
        ArrayList<Integer> list = new ArrayList<>();
        ListHolder listHolder = new ListHolder(list);
        new Caller(WhatToDo.ADD_TO_LIST, listHolder).start();
        new Caller(WhatToDo.REMOVE_FROM_LIST, listHolder).start();
    }
}
