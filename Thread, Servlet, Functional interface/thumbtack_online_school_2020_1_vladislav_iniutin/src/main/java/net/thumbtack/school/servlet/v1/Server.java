package net.thumbtack.school.servlet.v1;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private static final int port = 6666;

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        System.out.println("Server started");
        int id = 0;
        try (ServerSocket socket = new ServerSocket(port)) {
            while (true) {
                Socket clientSocket = socket.accept();
                executorService.execute(new ClientServiceThread(id++, clientSocket));
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
