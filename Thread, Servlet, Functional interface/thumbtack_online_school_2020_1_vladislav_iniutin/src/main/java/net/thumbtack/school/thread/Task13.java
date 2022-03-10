package net.thumbtack.school.thread;

import java.text.FieldPosition;
import java.text.SimpleDateFormat;
import java.util.Date;

class Formatter {
    static ThreadLocal<SimpleDateFormat> threadLocal = new ThreadLocal<>();

    public void format(Date date) {
        threadLocal.set(new SimpleDateFormat("y-M-d H:m:s"));
        StringBuffer stringBuffer = new StringBuffer();
        SimpleDateFormat simpleDateFormat =  threadLocal.get();
        simpleDateFormat.format(date, stringBuffer, new FieldPosition(0));
        System.out.println(stringBuffer);
    }
}

public class Task13 {


    public static void main(String[] args) {
        Formatter formatter = new Formatter();
        Date date = new Date();
        Runnable runnable = () -> formatter.format(date);
        new Thread(runnable).start();
        new Thread(runnable).start();
        new Thread(runnable).start();
        new Thread(runnable).start();
        new Thread(runnable).start();
    }
}
