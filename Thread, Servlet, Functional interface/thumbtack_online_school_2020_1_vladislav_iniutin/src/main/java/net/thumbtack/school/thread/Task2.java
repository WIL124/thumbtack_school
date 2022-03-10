package net.thumbtack.school.thread;

public class Task2 {

    public static void main(String[] args){
        System.out.println("Main thread start");
        Runnable runnable = new RunnableImpl();
        Thread thread = new Thread(runnable);
        thread.start();
        try {
            thread.join();
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Main thread exit");
    }
}
