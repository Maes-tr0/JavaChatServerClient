package com.example.chatapp.client.service;

import com.example.chatapp.client.main.Client;
import com.example.chatapp.client.model.Message;

import java.io.IOException;

public class MessageHandler {
    private Client client;

    public MessageHandler(Client client) {
        this.client = client;
    }

    public void sendMessage(String content, String sender) {
        Message message = new Message(content, sender);
        client.getBufferedWriter().println(message.getContent());
    }

    public String receiveMessage() {
        try {
            return client.getBufferedReader().readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
