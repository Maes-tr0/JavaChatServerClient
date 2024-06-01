package com.example.server.handler;

import com.example.server.ServerApplication;

import java.io.*;
import java.net.Socket;

public class ClientHandler extends Thread {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private String fullname;

    public ClientHandler(Socket socket) throws IOException {
        this.socket = socket;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
    }

    @Override
    public void run() {
        try {
            String email = in.readLine();
            String password = in.readLine();
            fullname = in.readLine(); // Отримуємо повне ім'я

            // Assuming validation is already done
            out.println("Login successful");
            String message;
            while ((message = in.readLine()) != null) {
                System.out.println("Received message from " + email + ": " + message);
                // Broadcasting to other clients can be implemented here
                broadcastMessage(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendMessage(String message) {
        out.println(message);
    }

    private void broadcastMessage(String message) {
        // Implement broadcasting to all connected clients
        ServerApplication.broadcast(message, this);
    }
}
