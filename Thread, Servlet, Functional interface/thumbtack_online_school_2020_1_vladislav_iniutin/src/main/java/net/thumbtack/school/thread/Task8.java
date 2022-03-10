package net.thumbtack.school.thread;

import java.util.concurrent.Semaphore;

public class Task8 {
    static private int num;
    static Semaphore semRead = new Semaphore(0);
    static Semaphore semWrite = new Semaphore(1);

    public static void main(String[] args) {
        Runnable read = () -> {
            try {
                semRead.acquire();
                System.out.println("Прочитали " + num);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                semWrite.release();
            }
        };
        Runnable write = () -> {
            try {
                semWrite.acquire();
                num++;
                System.out.println("Записали " + num);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                semRead.release();
            }
        };
        new Thread(read).start();
        new Thread(write).start();
        new Thread(read).start();
        new Thread(write).start();
        new Thread(read).start();
        new Thread(write).start();
        new Thread(read).start();
        new Thread(write).start();
        new Thread(read).start();
        new Thread(write).start();
        new Thread(read).start();
        new Thread(write).start();
        new Thread(read).start();
        new Thread(write).start();
    }
}
