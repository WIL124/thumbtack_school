package net.thumbtack.school.thread;

import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//Написать класс Message, содержащий 4 текстовых поля: emailAddress, sender, subject, body, и класс Transport, с методом send (Message message),
// отсылающий письмо на некий SMTP-сервер. Реализовать массовую рассылку одного и того же текста, email адреса берутся из текстового файла.
// Имейте в виду, что отправка одного письма требует довольно много времени (установление соединения с сервером, отсылка, получение ответа),
// поэтому последовательная отправка писем не является хорошим решением. Порядок отправки писем произвольный и не обязан совпадать с порядком email адресов в файле.
class Message {
    private String emailAddress;
    private String sender;
    private String subject;
    private String body;

    public String getEmailAddress() {
        return emailAddress;
    }

    public Message(String emailAddress, String sender, String subject, String body) {
        this.emailAddress = emailAddress;
        this.sender = sender;
        this.subject = subject;
        this.body = body;
    }

    @Override
    public String toString() {
        return "Message{" +
                "emailAddress='" + emailAddress + '\'' +
                ", sender='" + sender + '\'' +
                ", subject='" + subject + '\'' +
                ", body='" + body + '\'' +
                '}';
    }
}

class Transport {
    public void send(Message msg) {
        try (FileWriter bw = new FileWriter("sentEmails.txt", true)) {
            bw.write(msg.toString() + "\n");
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Message to " + msg.getEmailAddress() + " sent");
    }
}

public class Task14 {
    private static void createEmails() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("emails.txt"))) {
            for (int i = 0; i < 100; i++) {
                bw.write("emailNumber" + i + "@gmail.com");
                bw.newLine();
            }
            bw.flush();
            System.out.println("Emails are ready");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static ArrayList<String> readEmails() {
        ArrayList<String> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("emails.txt"))) {
            for (int i = 0; i < 100; i++) {
                String s = br.readLine();
                list.add(s);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static void main(String[] args) {
        createEmails();   // Создаём файл с email-адресами
        ArrayList<String> emails = readEmails(); // получаем email-адреса из файла
        Transport transport = new Transport();
        ExecutorService es = Executors.newCachedThreadPool();
        for (String email : emails) {
            es.execute(() -> transport.send(new Message(email, "Inyutin Vladislav", "ttschool", "Task14")));
        }
        es.shutdown();
    }
}
