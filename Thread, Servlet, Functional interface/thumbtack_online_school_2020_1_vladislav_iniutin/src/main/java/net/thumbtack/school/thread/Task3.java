package net.thumbtack.school.thread;
public class Task3 {
    public static void main(String[] args){
        System.out.println("Main thread start");
        RunnableImpl runnable = new RunnableImpl();
        Thread threadOne = new Thread(runnable);
        Thread threadTwo = new Thread(runnable);
        Thread threadThree = new Thread(runnable);

        threadOne.start();
        threadTwo.start();
        threadThree.start();
        try {
            threadOne.join();
            threadTwo.join();
            threadThree.join();
        }catch (InterruptedException ex){
            ex.printStackTrace();
        }
        System.out.println("Main thread exit");
    }
}
