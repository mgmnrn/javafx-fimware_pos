package application.controller;

import application.database.DAO;
import application.model.HashPass;
import application.model.Staff;
import application.model.StaffContainer;
import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.controlsfx.control.Notifications;
import sample.MaskTextField;

import java.io.File;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class StaffAddController {
    @FXML
    private AnchorPane rootPane;

    @FXML
    private MaskTextField staffIdTF;

    @FXML
    private MaskTextField usernameTF;

    @FXML
    private MaskTextField lastNameTF;

    @FXML
    private MaskTextField firstNameTF;

    @FXML
    private MaskTextField registerTF;

    @FXML
    private MaskTextField emailTF;

    @FXML
    private MaskTextField phoneTF;

    @FXML
    private JFXButton imageChooserBtn;

    @FXML
    private PasswordField confirmPassword;

    @FXML
    private Button staffRegister;

    private File imagePath = null;

    @FXML
    void initialize() {
        staffIdTF.setText(String.valueOf(StaffContainer.getNewId()));
        staffIdTF.setDisable(true);

        imageChooserBtn.setOnAction(e-> {
            Stage stage = (Stage) rootPane.getScene().getWindow();
            FileChooser fileChooser = new FileChooser();
            configuringFileChooser(fileChooser);
            imagePath = fileChooser.showOpenDialog(stage);
        });

        staffRegister.setOnAction(e -> {
            if (checkValue()) {
                if (DAO.isValidUser(usernameTF.getText())) {
                    if (DAO.checkPassword(StaffContainer.getActiveUser().getId(), confirmPassword.getText())) {
                        Integer id = Integer.parseInt(staffIdTF.getText());
                        String username = usernameTF.getText();
                        String lastName = lastNameTF.getText();
                        String firstName = lastNameTF.getText();
                        String register = registerTF.getText();
                        String email = emailTF.getText();
                        String phone = phoneTF.getText();
                        String gender = null;
                        if (register.charAt(8) % 2 == 0)
                            gender = "Эмэгтэй";
                        else
                            gender = "Эрэгтэй";
                        java.sql.Date birthDate = generateRegister(register);
                        Image image;
                        if(imagePath != null) {
                            image = new Image(imagePath.toURI().toString());
                        } else {
                            image = new Image(StaffAddController.class.getResourceAsStream("../../Resource/Images/no-product-found.jpg"));
                        }
                        Staff staff = new Staff(id, username, lastName, firstName, register, email, phone, birthDate, gender, 3, image);
                        DAO.addStaff(staff);
                        StaffContainer.addStaff(staff);
                        Stage stage = (Stage) rootPane.getScene().getWindow();
                        stage.close();
                    } else {
                        confirmPassword.setStyle("-fx-border-color: red");
                        DAO.notification(Pos.TOP_RIGHT, "Нууц үг буруу байна.");
                    }
                } else {
                    usernameTF.setStyle("-fx-border-color: red");
                    DAO.notification(Pos.TOP_RIGHT, "Ажилтаны нэвтрэх нэр давхцаж байна.");
                }
            }
        });
    }

    private boolean checkValue() {
        boolean value = true;
        if (staffIdTF.getText().isEmpty()) {
            staffIdTF.setStyle("-fx-border-color: red");
            value = false;
        } else staffIdTF.setStyle("");

        if (usernameTF.getText().isEmpty()) {
            usernameTF.setStyle("-fx-border-color: red");
            value = false;
        } else usernameTF.setStyle("");

        if (lastNameTF.getText().isEmpty()) {
            lastNameTF.setStyle("-fx-border-color: red");
            value = false;
        } else lastNameTF.setStyle("");

        if (firstNameTF.getText().isEmpty()) {
            firstNameTF.setStyle("-fx-border-color: red");
            value = false;
        } else firstNameTF.setStyle("");

        if (registerTF.getText().isEmpty() || registerTF.getText().length() <= 9) {
            registerTF.setStyle("-fx-border-color: red");
            value = false;
        } else registerTF.setStyle("");


        if (emailTF.getText().isEmpty() || isValid(emailTF.getText())) {
            emailTF.setStyle("-fx-border-color: red");
            value = false;
        } else emailTF.setStyle("");

        if (phoneTF.getText().isEmpty() || phoneTF.getText().length() <= 7) {
            phoneTF.setStyle("-fx-border-color: red");
            value = false;
        } else phoneTF.setStyle("");

        if (confirmPassword.getText().isEmpty()) {
            confirmPassword.setStyle("-fx-border-color: red");
            value = false;
        } else confirmPassword.setStyle("");

        return value;
    }

    private Date generateRegister(String register) {
        StringBuilder stringBuilder = new StringBuilder();
        if (register.charAt(4) == '2' || register.charAt(4) == '3') {
            stringBuilder.append(Integer.parseInt(20 + register.substring(2, 4)));
            stringBuilder.append("-");
            int i = Integer.parseInt(register.substring(4, 6)) - 20;
            if (i < 10)
                stringBuilder.append("0");
            stringBuilder.append(i);
        } else {
            stringBuilder.append(Integer.parseInt(19 + register.substring(2, 4)));
            stringBuilder.append("-");
            if (Integer.parseInt(register.substring(4, 6)) < 10)
                stringBuilder.append("0");
            stringBuilder.append(Integer.parseInt(register.substring(4, 6)));
        }
        stringBuilder.append("-");
        if (Integer.parseInt(register.substring(6, 8)) < 10)
            stringBuilder.append("0");
        stringBuilder.append(Integer.parseInt(register.substring(6, 8)));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate birthDate = LocalDate.parse(stringBuilder.toString(), formatter);
        return java.sql.Date.valueOf(birthDate);
    }

    static boolean isValid(String email) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return !email.matches(regex);
    }

    private void configuringFileChooser(FileChooser fileChooser) {
        fileChooser.setTitle("Бүтээгдэхүүний зураг сонгох");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("PNG", "*.png")
        );
    }
}
