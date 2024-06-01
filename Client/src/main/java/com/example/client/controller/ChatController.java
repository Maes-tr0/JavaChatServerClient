package com.example.client.controller;

import com.example.client.model.User;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;

import java.io.IOException;

public class ChatController {
    @FXML
    private TextField messageField;
    @FXML
    private TextArea chatArea;
    @FXML
    private Button sendButton;

    private ServerConnection serverConnection;
    private User currentUser;

    @FXML
    public void initialize() {
        serverConnection = new ServerConnection();
    }

    @FXML
    public void connectToServer(User user) throws IOException {
        this.currentUser = user;
        serverConnection.connect(user.getEmail(), user.getPassword(), user.getFullname());
        serverConnection.receiveMessages(this);
    }

    @FXML
    public void sendMessage() {
        String message = messageField.getText();
        if (!message.isEmpty()) {
            try {
                serverConnection.sendMessage(message);
                chatArea.appendText("You: " + message + "\n");
                messageField.clear();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void addMessageToChat(String message) {
        if (message.startsWith(currentUser.getFullname())) {
            chatArea.appendText("You: " + message.substring(currentUser.getFullname().length() + 2) + "\n");
        } else {
            chatArea.appendText(message + "\n");
        }
    }
}
