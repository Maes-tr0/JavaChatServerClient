package com.example.chatapp.client.util;

import com.example.chatapp.client.main.Client;

public class ClientUtils {
    public static String formatMessage(String sender, String message) {
        return sender + ": " + message;
    }

    public static boolean isConnectionActive(Client client) {
        return client.getBufferedReader() != null && client.getBufferedWriter() != null;
    }
}
