package application.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class ChangePasswordController {
    @FXML
    private AnchorPane root;

    @FXML
    private JFXPasswordField oldPassword;

    @FXML
    private JFXPasswordField newPassword;

    @FXML
    private JFXPasswordField confirmPassword;

    @FXML
    private JFXButton confirmBtn;

    @FXML
    private MaterialDesignIconView close;

    @FXML
    void initialize() {
        close.setOnMouseClicked(event -> {
            Stage stage = (Stage) root.getScene().getWindow();
            stage.close();
        });

        confirmBtn.setOnAction(event -> {
            if (checkValue(oldPassword) & checkValue(newPassword) & checkValue(confirmPassword)){
                System.out.println("ok");
                //DAO.checkUser(StaffContainer.getActiveUser().get)
            }
        });
    }

    private boolean checkValue(JFXPasswordField pass){
        return !pass.getText().isEmpty();
    }
}
