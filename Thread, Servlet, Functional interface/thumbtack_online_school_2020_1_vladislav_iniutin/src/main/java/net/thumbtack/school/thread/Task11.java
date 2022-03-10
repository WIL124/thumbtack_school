package net.thumbtack.school.thread;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class PrinterLock {
    private Lock lock = new ReentrantLock();
    private Condition pingPrinted = lock.newCondition();
    private Condition pongPrinted = lock.newCondition();
    private boolean printedPong = false;

    public void printPong() {
        lock.lock();
        try {
            while (printedPong) {
                pingPrinted.await();
            }
            System.out.println("Pong");
            pongPrinted.signal();
            printedPong = true;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void printPing() {
        lock.lock();
        try {
            while (!printedPong) {
                pongPrinted.await();
            }
            System.out.println("Ping");
            pingPrinted.signal();
            printedPong = false;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}

class PingLock extends Thread {
    private PrinterLock printer;

    PingLock(PrinterLock printer) {
        this.printer = printer;
    }

    @Override
    public void run() {
        while (true) {
            printer.printPing();
        }
    }
}

class PongLock extends Thread {
    private PrinterLock printer;

    PongLock(PrinterLock printer) {
        this.printer = printer;
    }

    @Override
    public void run() {
        while (true) {
            printer.printPong();
        }
    }
}

public class Task11 {
    public static void main(String[] args) {
        PrinterLock printerLock = new PrinterLock();
        new PongLock(printerLock).start();
        new PingLock(printerLock).start();
    }
}
