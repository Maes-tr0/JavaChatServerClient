package com.example.chatapp.client.main;

import java.io.*;
import java.net.Socket;

public class Client {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 8080;
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private BufferedReader userInput;
    private String name;

    public Client() {
        try {
            socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            userInput = new BufferedReader(new InputStreamReader(System.in));

            System.out.print("Enter your name: ");
            name = userInput.readLine();
            out.println(name);

            // Thread for receiving messages from the server
            new Thread(() -> {
                String serverMessage;
                try {
                    while ((serverMessage = in.readLine()) != null) {
                        System.out.println(serverMessage);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

            // Main loop for sending messages to the server
            String userMessage;
            while ((userMessage = userInput.readLine()) != null) {
                if (userMessage.equalsIgnoreCase("disconnect")) {
                    out.println("disconnect");
                    break;
                }
                out.println(name + ": " + userMessage);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (socket != null) socket.close();
                if (in != null) in.close();
                if (out != null) out.close();
                if (userInput != null) userInput.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public BufferedReader getBufferedReader() {
        return in;
    }

    public PrintWriter getBufferedWriter() {
        return out;
    }
}
