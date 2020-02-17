package application.controller;

import application.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.MaskTextField;

import java.io.*;
import java.util.Arrays;

public class ProductAddController {
    @FXML
    private StackPane root;

    @FXML
    private MaskTextField nameTF;

    @FXML
    private MaskTextField barcodeTF;

    @FXML
    private MaskTextField unitPriceTF;

    @FXML
    private ComboBox<Type> typeCB;

    @FXML
    private Button typeAddBtn;

    @FXML
    private Button imageChooseBtn;

    @FXML
    private ComboBox<String> unitCB;

    @FXML
    private MaskTextField idTF;

    @FXML
    private MaskTextField weightTF;

    @FXML
    private MaskTextField sizeTF;

    @FXML
    private Button addBtn;

    private File imagePath = null;

    @FXML
    void initialize() {
        unitCB.setItems(getUnits());
        typeCB.setItems(TypeContainer.getTypes());
        imageChooseBtn.setOnAction(e -> {
            Stage stage = (Stage) root.getScene().getWindow();
            FileChooser fileChooser = new FileChooser();
            configuringFileChooser(fileChooser);
            imagePath = fileChooser.showOpenDialog(stage);
        });

        addBtn.setOnAction(e -> {

//            Popup popup = new Popup();
//            Label label = new Label("Бүтээгдэхүүн нэмэгдлээ");
//            label.setStyle("-fx-background-color: white");
//            popup.getContent().add(label);
//            popup.setAutoHide(true);
//            Stage stage = (Stage) root.getScene().getWindow();
//            popup.show(stage);

            if (checkValue()) {
                String name = nameTF.getText();
                Integer barcode = Integer.parseInt(barcodeTF.getText());
                Integer id = Integer.parseInt(idTF.getText());
                String[] priceArray = unitPriceTF.getText().split("₮");
                Double unitPrice = Double.parseDouble(priceArray[0]);
                Type type = typeCB.getValue();
                Integer weight = Integer.parseInt(weightTF.getText());
                String unit = unitCB.getValue();
                String[] sizeArray = sizeTF.getText().split(" ");
                Integer size = Integer.parseInt(sizeArray[0]);
                Product product = null;
                if (imagePath != null) {
                    try {
                        InputStream is = new FileInputStream(imagePath);
                        product = new Product(name, barcode, id, unitPrice, type, weight, unit, size, new Image(is));
                    } catch (FileNotFoundException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    product = new Product(name, barcode, id, unitPrice, type, weight, unit, size);
                }
                ProductContainer.addProduct(product);
                Stage stage = (Stage) root.getScene().getWindow();
                stage.close();
            }
        });

        unitPriceTF.focusedProperty().addListener((ov, oldNode, newNode) -> {
            if (newNode) {
                unitPriceTF.setText(unitPriceTF.getText().replace("₮", ""));
            } else if (!unitPriceTF.getText().isEmpty()) {
                unitPriceTF.setText(unitPriceTF.getText().concat("₮"));
            }
        });

        sizeTF.focusedProperty().addListener((ov, oldNode, newNode) -> {
            if (newNode) {
                sizeTF.setText(sizeTF.getText().replace(" ширхэг", ""));
            } else if (!sizeTF.getText().isEmpty()) {
                sizeTF.setText(sizeTF.getText().concat(" ширхэг"));
            }
        });

        typeAddBtn.setOnAction(e -> {
            try {
                Parent root = FXMLLoader.load(this.getClass().getResource("../view/type_manage.fxml"));
                Stage stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setTitle("Fimware кассын систем - Төрөл нэмэх");
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }

    private boolean checkValue() {
        boolean value = true;
        if (nameTF.getText().isEmpty()) {
            nameTF.setStyle("-fx-border-color: red");
            value = false;
        } else nameTF.setStyle("");

        if (barcodeTF.getText().isEmpty()) {
            barcodeTF.setStyle("-fx-border-color: red");
            value = false;
        } else barcodeTF.setStyle("");

        if (idTF.getText().isEmpty() || Integer.parseInt(idTF.getText()) < 1000 || ProductContainer.isContain(Integer.parseInt(idTF.getText()))) {
            idTF.setStyle("-fx-border-color: red");
            value = false;
        } else idTF.setStyle("");

        if (unitPriceTF.getText().isEmpty()) {
            unitPriceTF.setStyle("-fx-border-color: red");
            value = false;
        } else unitPriceTF.setStyle("");

        if (typeCB.getSelectionModel().getSelectedItem() == null) {
            typeCB.setStyle("-fx-border-color: red");
            value = false;
        } else typeCB.setStyle("");

        if (weightTF.getText().isEmpty()) {
            weightTF.setStyle("-fx-border-color: red");
            value = false;
        } else weightTF.setStyle("");

        if (unitCB.getSelectionModel().getSelectedItem() == null) {
            unitCB.setStyle("-fx-border-color: red");
            value = false;
        } else unitCB.setStyle("");

        if (sizeTF.getText().isEmpty()) {
            sizeTF.setStyle("-fx-border-color: red");
            value = false;
        } else sizeTF.setStyle("");
        return value;
    }

    private ObservableList<String> getUnits() {
        return FXCollections.observableArrayList(Arrays.asList("г", "кг", "мл", "л"));
    }

    private void configuringFileChooser(FileChooser fileChooser) {
        fileChooser.setTitle("Бүтээгдэхүүний зураг сонгох");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("PNG", "*.png")
        );
    }
}

