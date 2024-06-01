package com.example.client.controller;

import com.example.client.model.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.util.Objects;
import java.util.regex.Pattern;

public class AuthController {
    private Stage stage;
    private Parent root;
    private Scene scene;

    private ServerConnection serverConnection;

    private final UserManager userManager = new UserManager();

    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField loginEmailField;
    @FXML
    private PasswordField loginPasswordField;

    @FXML
    private void handleSignUpLink() {
        try {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/example/client/auth/RegistrationForm.fxml")));
            stage = getCurrentStage();
            scene = new Scene(root);
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/example/client/auth/styles.css")).toExternalForm());
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSignInLink() {
        try {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/example/client/auth/LoginForm.fxml")));
            stage = getCurrentStage();
            scene = new Scene(root);
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/example/client/auth/styles.css")).toExternalForm());
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void check_information() {
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();

        if (!isValidEmail(email)) {
            showAlert(Alert.AlertType.ERROR, "Form Error!", "Please enter a valid email address");
            return;
        }

        if (userManager.isUserRegistered(email)) {
            showAlert(Alert.AlertType.ERROR, "Form Error!", "This email is already registered. Please use another email.");
            return;
        }

        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Form Error!", "Please fill in all fields");
            return;
        }

        if (!isValidPassword(password)) {
            showAlert(Alert.AlertType.ERROR, "Form Error!", "Password must be: \n" +
                    "- at least 8 characters\n" +
                    "- must contain at least 1 uppercase letter, 1 lowercase letter, and 1 number\n" +
                    "- Can contain special characters ");
            return;
        }

        // Save user information here
        saveUserInformation(firstName, lastName, email, password);
        handleSignInLink();
        showAlert(Alert.AlertType.INFORMATION, "Registration Successful!", "Now sign in to your account " + firstName + "!");
    }

    @FXML
    private void check_login_information() {
        String email = loginEmailField.getText();
        String password = loginPasswordField.getText();
        User user = userManager.getUserByEmail(email);

        if (email.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Form Error!", "Please fill in all fields");
            return;
        }

        if (user == null) {
            showAlert(Alert.AlertType.ERROR, "Form Error!", "No account found with this email");
            return;
        }

        if (!user.getPassword().equals(password)) {
            showAlert(Alert.AlertType.ERROR, "Form Error!", "Incorrect password");
            return;
        }

        try {
            serverConnection = new ServerConnection();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/client/chat/ChatForm.fxml"));
            Parent root = loader.load();
            ChatController chatController = loader.getController();
            chatController.initialize();
            chatController.connectToServer(user);  // Передаємо об'єкт користувача

            Stage stage = new Stage();
            stage.setTitle("Chat");
            stage.setScene(new Scene(root));
            stage.show();

            // Close login form
            Stage loginStage = (Stage) loginEmailField.getScene().getWindow();
            loginStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        showAlert(Alert.AlertType.INFORMATION, "Login Successful!", "Welcome back, " + user.getFirstName() + "!");
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^([A-Z|a-z|0-9](\\.|_){0,1})+[A-Z|a-z|0-9]\\@([A-Z|a-z|0-9])+((\\.){0,1}[A-Z|a-z|0-9]){2}\\.[a-z]{2,3}$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }

    private boolean isValidPassword(String password) {
        String passwordRegex = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$";
        Pattern pattern = Pattern.compile(passwordRegex);
        return pattern.matcher(password).matches();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.show();
    }

    private void saveUserInformation(String firstName, String lastName, String email, String password) {
        User user = new User(firstName, lastName, email, password);
        userManager.saveUser(user);
    }

    private Stage getCurrentStage() {
        return (Stage) Stage.getWindows().stream().filter(Window::isShowing).findFirst().orElse(null);
    }
}
