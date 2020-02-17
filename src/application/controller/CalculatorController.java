package application.controller;

import application.database.DAO;
import application.model.Order;
import application.model.Report;
import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import mn.mta.pos.exam.BridgePosAPI;
import org.json.JSONObject;

public class CalculatorController {
    @FXML
    private AnchorPane root;

    @FXML
    private Label payLbl;

    @FXML
    private Label paidLbl;

    @FXML
    private Label returnLbl;

    @FXML
    private JFXButton btn1;

    @FXML
    private JFXButton btn2;

    @FXML
    private JFXButton btn3;

    @FXML
    private JFXButton btnX;

    @FXML
    private JFXButton btn4;

    @FXML
    private JFXButton btn5;

    @FXML
    private JFXButton btn6;

    @FXML
    private JFXButton btnAC;

    @FXML
    private JFXButton btn7;

    @FXML
    private JFXButton btn8;

    @FXML
    private JFXButton btn9;

    @FXML
    private JFXButton btn00;

    @FXML
    private JFXButton btn0;

    @FXML
    private JFXButton btnPrint;

    @FXML
    void initialize() {
        setButton();
        setKeyEvent();
    }

    private void setButton() {
        btnPrint.setOnAction(e -> printOrder());
        btnAC.setOnAction(e -> defaultPriceValue());
        btnX.setOnAction(e -> removePriceValue());
        btn00.setOnAction(e -> {
            addPriceValue(0);
            addPriceValue(0);
        });
        btn0.setOnAction(e -> addPriceValue(0));
        btn1.setOnAction(e -> addPriceValue(1));
        btn2.setOnAction(e -> addPriceValue(2));
        btn3.setOnAction(e -> addPriceValue(3));
        btn4.setOnAction(e -> addPriceValue(4));
        btn5.setOnAction(e -> addPriceValue(5));
        btn6.setOnAction(e -> addPriceValue(6));
        btn7.setOnAction(e -> addPriceValue(7));
        btn8.setOnAction(e -> addPriceValue(8));
        btn9.setOnAction(e -> addPriceValue(9));
        payLbl.textProperty().bindBidirectional(Order.cashAmountPropery());
        paidLbl.textProperty().addListener((observable, oldValue, newValue) -> {
            String[] paid = newValue.split("₮");
            double paidPrice = Double.parseDouble(paid[0]);
            returnLbl.setText(paidPrice - Order.getCashAmount() + "₮");
        });
    }

    private void setKeyEvent() {
        root.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER)
                printOrder();
            else if (e.getCode() == KeyCode.BACK_SPACE)
                removePriceValue();
            else if (e.getCode() == KeyCode.DELETE)
                defaultPriceValue();

            else if (e.getCode() == KeyCode.DIGIT0)
                addPriceValue(0);
            else if (e.getCode() == KeyCode.DIGIT1)
                addPriceValue(1);
            else if (e.getCode() == KeyCode.DIGIT2)
                addPriceValue(2);
            else if (e.getCode() == KeyCode.DIGIT3)
                addPriceValue(3);
            else if (e.getCode() == KeyCode.DIGIT4)
                addPriceValue(4);
            else if (e.getCode() == KeyCode.DIGIT5)
                addPriceValue(5);
            else if (e.getCode() == KeyCode.DIGIT6)
                addPriceValue(6);
            else if (e.getCode() == KeyCode.DIGIT7)
                addPriceValue(7);
            else if (e.getCode() == KeyCode.DIGIT8)
                addPriceValue(8);
            else if (e.getCode() == KeyCode.DIGIT9)
                addPriceValue(9);
        });
    }

    private void removePriceValue() {
        String[] paid = paidLbl.getText().split("₮");
        int paidPrice = Integer.parseInt(paid[0]);
        paidLbl.setText(paidPrice / 10 + "₮");
    }

    private void defaultPriceValue() {
        paidLbl.setText("0₮");
    }

    private void addPriceValue(Integer price) {
        String[] paid = paidLbl.getText().split("₮");
        int paidPrice = Integer.parseInt(paid[0]);

        if (price == 0) {
            if (paidPrice != 0)
                paidLbl.setText(paidPrice + String.valueOf(price) + "₮");
        } else {
            if (paidPrice != 0)
                paidLbl.setText(paidPrice + String.valueOf(price) + "₮");
            else
                paidLbl.setText(price + "₮");
        }
    }

    private void printOrder() {
        //BridgePosAPI.sendData();
        String result = BridgePosAPI.put(Order.toJSON().toString());
        JSONObject jsonObject = new JSONObject(result);
        if (jsonObject.getBoolean("success")) {
            if (!jsonObject.getString("lottery").equals("")) {
                Order.setQrData(jsonObject.getString("qrData"));
                Order.setBillId(jsonObject.getString("billId"));
                Order.setDateTime(jsonObject.getString("date"));
                String[] regex = jsonObject.getString("date").split(" ");
                Order.setDate(regex[0]);
                Order.setLottery(jsonObject.getString("lottery"));
                DAO.writeOrder();
                new Report().eBarimt();
                Order.defaultOrder();
                UserController.defaultValue();
                ((Stage) root.getScene().getWindow()).close();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Алдаа");
                alert.setHeaderText("Баримтыг бүртгэж чадсангүй");
                alert.setContentText(jsonObject.getString("warningMsg"));
                alert.setHeight(300);
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Алдаа");
            alert.setHeaderText("Баримтыг бүртгэж чадсангүй");
            alert.setContentText(jsonObject.getString("message"));
            alert.showAndWait();
        }
    }
}
