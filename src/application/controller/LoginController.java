package application.controller;

import application.database.DAO;
import application.model.HashPass;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {
    @FXML
    private StackPane rootPane;

    @FXML
    private TextField username;

    @FXML
    private Label status;

    @FXML
    private PasswordField password;

    @FXML
    private Button loginBtn;

    @FXML
    void initialize() {
        loginBtn.setOnAction(e -> checkLogin());
        rootPane.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER)
                checkLogin();
        });
    }

    private void checkLogin() {
        status.setText("");
        username.setStyle("");
        password.setStyle("");
        if (username.getText().isEmpty())
            username.setStyle("-fx-border-color: red");
        else if (password.getText().isEmpty())
            password.setStyle("-fx-border-color: red");
        else {
            int role = DAO.checkUser(username.getText(), HashPass.hash(password.getText()));
            switch (role) {
                case -1:
                    status.setText("Нэвтрэх нэр эсвэл нууц үг буруу байна.");
                    break;
                case 0:
                    status.setText(" Таны эрх хаагдсан байна. Та админд хандана уу.");
                    break;
                case 1:
                case 2:
                    callPage("manager");
                    break;
                case 3:
                    callPage("user");
                    break;
            }
        }
    }

    private void callPage(String name) {
        try {
            Stage newStage = ((Stage) rootPane.getScene().getWindow());
            Parent root = FXMLLoader.load(getClass().getResource("../view/" + name + ".fxml"));
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            newStage.setScene(new Scene(root, screenBounds.getWidth(), screenBounds.getHeight()));
            newStage.setTitle("Fimware кассын систем");
            newStage.setFullScreen(true);
            newStage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}