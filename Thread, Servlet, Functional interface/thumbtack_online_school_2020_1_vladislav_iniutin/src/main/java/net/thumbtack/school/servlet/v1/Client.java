package net.thumbtack.school.servlet.v1;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
    public static void main(String[] args) {
        final int serverPort = 6666;
        final String address = "localhost";
        InetAddress ipAddress;
        try {
            ipAddress = InetAddress.getByName(address);
        } catch (UnknownHostException e) {
            System.out.println("Host unknown, exit.");
            return;
        }
        try (Socket socket = new Socket(ipAddress, serverPort);
             DataInputStream in = new DataInputStream(socket.getInputStream());
             DataOutputStream out = new DataOutputStream(socket.getOutputStream());
             BufferedReader keyboardReader = new BufferedReader(new InputStreamReader(System.in, "CP866"))) {
            System.out.println("Сервер загадал число от 1 до 10000. Попробуй его угадать! У тебя 3 попытки");
            while (socket.isConnected()) {
                String numAsString = keyboardReader.readLine();
                int num = Integer.parseInt(numAsString);
                out.writeInt(num);
                out.flush();
                String response = in.readUTF();
                System.out.println(response);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
