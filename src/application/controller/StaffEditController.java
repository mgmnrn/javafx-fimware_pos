package application.controller;

import application.database.DAO;
import application.database.DBConnection;
import application.model.Staff;
import application.model.StaffContainer;
import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.controlsfx.control.Notifications;
import sample.MaskTextField;

import java.io.File;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class StaffEditController {
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
    private PasswordField confirmTF;

    @FXML
    private ImageView staffImageView;

    @FXML
    private JFXButton imageChooseBtn;

    @FXML
    private Button saveBtn;

    private static Staff activeEmployee = new Staff();

    public static void updateActiveEmployee(Staff employee) {
        activeEmployee = employee;
    }

    private File imagePath = null;

    @FXML
    void initialize() {
        staffIdTF.setText(String.valueOf(activeEmployee.getId()));
        usernameTF.setText(activeEmployee.getUsername());
        lastNameTF.setText(activeEmployee.getlName());
        firstNameTF.setText(activeEmployee.getfName());
        registerTF.setText(activeEmployee.getRegister());
        emailTF.setText(activeEmployee.getEmail());
        phoneTF.setText(String.valueOf(activeEmployee.getPhone()));
        staffIdTF.setDisable(true);
        usernameTF.setDisable(true);
        staffImageView.setImage(activeEmployee.getImage());

        imageChooseBtn.setOnAction(e -> {
            Stage stage = (Stage) rootPane.getScene().getWindow();
            FileChooser fileChooser = new FileChooser();
            configuringFileChooser(fileChooser);
            imagePath = fileChooser.showOpenDialog(stage);
            try {
                imageChooseBtn.setStyle("");
                staffImageView.setImage(new Image(imagePath.toURI().toString()));
            } catch (NullPointerException ignored) {
                imageChooseBtn.setStyle("-fx-border-color: red");
            }
        });

        saveBtn.setOnAction(e -> {
            if (checkValue()) {
                if (DAO.checkPassword(StaffContainer.getActiveUser().getId(), confirmTF.getText())) {
                    if (!checkUsername(usernameTF.getText(), activeEmployee.getId())) {
                        activeEmployee.setlName(lastNameTF.getText());
                        activeEmployee.setfName(firstNameTF.getText());
                        activeEmployee.setRegister(registerTF.getText());
                        activeEmployee.setEmail(emailTF.getText());
                        activeEmployee.setPhone(phoneTF.getText());

                        String register = registerTF.getText();

                        String gender;
                        if (register.charAt(8) % 2 == 0) {
                            gender = "Эмэгтэй";
                        } else {
                            gender = "Эрэгтэй";
                        }
                        activeEmployee.setGender(gender);

                        StringBuilder stringBuilder = new StringBuilder();
                        if (register.charAt(4) == '2' || register.charAt(4) == '3') {
                            stringBuilder.append(Integer.parseInt(20 + register.substring(2, 4)));
                            stringBuilder.append("-");
                            final int i = Integer.parseInt(register.substring(4, 6)) - 20;
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
                        activeEmployee.setBirthDate(Date.valueOf(birthDate));
                        if (imagePath != null)
                            activeEmployee.setImage(new Image(imagePath.toURI().toString()));
                        DAO.updateStaff(activeEmployee);
                        DAO.notification(Pos.BOTTOM_RIGHT, "Ажилтаны мэдээллийг амжилттай заслаа.");
                        Stage stage = (Stage) rootPane.getScene().getWindow();
                        stage.close();
                    } else {
                        usernameTF.setStyle("-fx-border-color: red");
                        DAO.notification(Pos.TOP_RIGHT, "Ажилтаны хэрэглэгчийн нэр давхцаж байна.");
                    }
                } else {
                    confirmTF.setStyle("-fx-border-color: red");
                    DAO.notification(Pos.TOP_RIGHT, "Ажилтаны нууц үг буруу байна");
                }
            }
        });
    }

    private boolean checkUsername(String username, Integer id) {
        Connection conn = DBConnection.getConnection();
        try {
            Statement statement = conn.createStatement();

            ResultSet rsUsers = statement.executeQuery("SELECT username, staff_id FROM accounts");
            while (rsUsers.next()) {
                if (rsUsers.getString("username").toLowerCase().equals(username.toLowerCase()) && rsUsers.getInt("staff_id") != id) {
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean checkValue() {
        boolean value = true;

        if (lastNameTF.getText().isEmpty()) {
            lastNameTF.setStyle("-fx-border-color: red");
            value = false;
        } else lastNameTF.setStyle("");

        if (firstNameTF.getText().isEmpty()) {
            firstNameTF.setStyle("-fx-border-color: red");
            value = false;
        } else firstNameTF.setStyle("");

        if (registerTF.getText().isEmpty() || registerTF.getText().length() < 10) {
            registerTF.setStyle("-fx-border-color: red");
            value = false;
        } else registerTF.setStyle("");

        if (emailTF.getText().isEmpty() || isValid(emailTF.getText())) {
            emailTF.setStyle("-fx-border-color: red");
            value = false;
        } else emailTF.setStyle("");

        if (phoneTF.getText().isEmpty() || phoneTF.getText().length() < 8) {
            phoneTF.setStyle("-fx-border-color: red");
            value = false;
        } else phoneTF.setStyle("");

        if (confirmTF.getText().isEmpty()) {
            confirmTF.setStyle("-fx-border-color: red");
            value = false;
        } else confirmTF.setStyle("");

        return value;
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
