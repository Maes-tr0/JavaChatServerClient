package com.example.chatapp.server.service;

import com.example.chatapp.server.main.Server;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private String clientName;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            clientName = in.readLine();
            Server.broadcastMessage(clientName + " has joined the chat.");
            System.out.println(clientName + " has joined the chat.");  // Додаємо логування

            String message;
            while ((message = in.readLine()) != null) {
                if (message.equalsIgnoreCase("disconnect")) {
                    Server.broadcastMessage(clientName + " has left the chat.");
                    System.out.println(clientName + " has left the chat.");  // Додаємо логування
                    break;
                }
                Server.broadcastMessage(message);
                System.out.println(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Server.removeClient(this);
        }
    }

    public void sendMessage(String message) {
        out.println(message);
    }
}
