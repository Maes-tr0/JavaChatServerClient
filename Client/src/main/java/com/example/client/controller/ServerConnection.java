package com.example.client.controller;

import java.io.*;
import java.net.Socket;

public class ServerConnection {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private String full_name;

    public void connect(String email, String password, String full_name) throws IOException {
        socket = new Socket("localhost", 8080);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        this.full_name = full_name;

        out.println(email);
        out.println(password);
        out.println(full_name);  // Надсилаємо повне ім'я

        String response = in.readLine();
        if (!response.equals("Login successful")) {
            throw new IOException("Invalid login credentials");
        }
    }

    public void sendMessage(String message) throws IOException {
        out.println(full_name + ": " + message);
    }

    public void receiveMessages(ChatController chatController) {
        new Thread(() -> {
            String message;
            try {
                while ((message = in.readLine()) != null) {
                    chatController.addMessageToChat(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
