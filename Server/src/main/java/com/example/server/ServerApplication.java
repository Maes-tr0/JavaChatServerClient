package com.example.server;

import com.example.server.handler.ClientHandler;
import com.example.server.module.User;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerApplication {
    private static final int PORT = 8080;
    private List<User> users;
    private static List<ClientHandler> clients = new ArrayList<>(); // Статичний список клієнтів

    public ServerApplication() {
        users = new ArrayList<>();
        loadUsers();
    }

    public static void main(String[] args) {
        new ServerApplication().start();
    }

    private void start() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server started on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(clientSocket);
                clients.add(clientHandler);
                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadUsers() {
        try (BufferedReader reader = new BufferedReader(new FileReader("F:\\ProjectsOn(Java)\\Messenger\\users.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    users.add(new User(parts[0], parts[1], parts[2], parts[3]));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void broadcast(String message, ClientHandler excludeClient) {
        for (ClientHandler client : clients) {
            if (client != excludeClient) {
                client.sendMessage(message);
            }
        }
    }
}
