package net.thumbtack.school.servlet.v1;

import java.io.*;
import java.net.Socket;

public class ClientServiceThread extends Thread {
    private final int clientID;
    private final Socket clientSocket;

    public ClientServiceThread(int clientID, Socket clientSocket) {
        this.clientID = clientID;
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        System.out.println("Accepted Client: ID = " + clientID + " IP: " + clientSocket.getInetAddress().getHostAddress());
        try (DataInputStream in = new DataInputStream(clientSocket.getInputStream());
             DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream())) {

            ThreadLocal<Integer> answer = new ThreadLocal<>();
            ThreadLocal<Integer> countOfAttempts = new ThreadLocal<>();
            countOfAttempts.set(3);
            answer.set((int) (Math.random() * 10000));
            while (countOfAttempts.get() != 0) {
                countOfAttempts.set(countOfAttempts.get() - 1);
                int clientNumber = in.readInt();
                if (clientNumber < answer.get()) {
                    out.writeUTF("Мало!");
                }
                if (clientNumber > answer.get()) {
                    out.writeUTF("Много!");
                }
                if (clientNumber == answer.get()) {
                    out.writeUTF("Угадал!");
                }
                out.flush();
            }
            out.writeUTF("Попытки закончились");
            out.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }finally {
            System.out.println("Closing socket");
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
