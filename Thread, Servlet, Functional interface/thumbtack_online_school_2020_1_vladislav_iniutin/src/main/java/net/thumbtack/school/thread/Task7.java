package net.thumbtack.school.thread;

import java.util.concurrent.Semaphore;

class Printer {
    static Semaphore semPong = new Semaphore(0);
    static Semaphore semPing = new Semaphore(1);

    public void printPing() {
        try {
            semPing.acquire();
            System.out.println("ping");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            semPong.release();
        }
    }
    public void printPong() {
        try {
            semPong.acquire();
            System.out.println("pong");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            semPing.release();
        }
    }
}
class Ping extends Thread{
    private Printer printer;

    Ping(Printer printer){
        this.printer = printer;
    }
    @Override
    public void run() {
        while (true){
            printer.printPing();
        }
    }
}
class Pong extends Thread{
    private Printer printer;

    Pong(Printer printer){
        this.printer = printer;
    }
    @Override
    public void run() {
        while (true){
            printer.printPong();
        }
    }
}
class Task7 {
    public static void main(String[] args) {
        Printer printer = new Printer();
        new Ping(printer).start();
        new Pong(printer).start();
    }
}
