package net.thumbtack.school.thread;

public class RunnableImpl implements Runnable{
    @Override
    public void run() {
        try {
            System.out.println("Child thread " + Thread.currentThread() +  "run");
            Thread.sleep(2000);
            System.out.println("Child thread"+ Thread.currentThread() + " exit");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
