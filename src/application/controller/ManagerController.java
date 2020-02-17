package application.controller;

import application.database.DAO;
import application.model.*;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;

import java.io.IOException;
import java.util.Arrays;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXPasswordField;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ManagerController {

    @FXML
    private BorderPane root;

    @FXML
    private AnchorPane userPane;

    @FXML
    private TableView<Type> typeTV;

    @FXML
    private TableColumn<Type, String> typeTC;

    @FXML
    private TableView<Staff> userTV;

    @FXML
    private TableColumn<Staff, Integer> userId;

    @FXML
    private TableColumn<Staff, String> userLName;

    @FXML
    private TableColumn<Staff, String> userFName;

    @FXML
    private TableColumn<Staff, String> userRegister;

    @FXML
    private TableColumn<Staff, String> userGender;

    @FXML
    private TableColumn<Staff, String> userEmail;

    @FXML
    private TableColumn<Staff, String> userPhone;

    @FXML
    private TableColumn<Staff, Image> userImage;

    @FXML
    private Button userAdd;

    @FXML
    private Button userEdit;

    @FXML
    private Button userRemove;

    @FXML
    private TextField userSearch;

    @FXML
    private AnchorPane productPane;

    @FXML
    private TableView<Product> productTV;

    @FXML
    private TableColumn<Product, String> productName;

    @FXML
    private TableColumn<Product, Integer> productId;

    @FXML
    private TableColumn<Product, Integer> productBarCode;

    @FXML
    private TableColumn<Product, Integer> productWeight;

    @FXML
    private TableColumn<Product, Integer> productSize;

    @FXML
    private TableColumn<Product, Double> productPrice;

    @FXML
    private Button productAdd;

    @FXML
    private Button productEdit;

    @FXML
    private Button productRemove;

    @FXML
    private TextField productSearch;

    @FXML
    private AnchorPane reportPane;

    @FXML
    private Circle profileImage;

    @FXML
    private JFXComboBox<Label> profile;

    @FXML
    private JFXButton usersBtn;

    @FXML
    private JFXButton productBtn;

    @FXML
    private JFXButton reportBtn;

    @FXML
    private JFXButton exitBtn;

    @FXML
    private JFXButton reportSale;

    @FXML
    private JFXButton reportOrder;

    @FXML
    private JFXButton reportMax;

    @FXML
    private JFXButton reportUserSales;

    @FXML
    private JFXButton reportUserMax;

    @FXML
    private JFXDatePicker startDate;

    @FXML
    private JFXDatePicker endDate;

    @FXML
    void initialize() {
        DAO.init();
        TypeContainer.addListener();
        ProductContainer.addListener();
        setStaff();
        setProduct();
        setType();
        setReport();

        Label userLbl = new Label("Хэрэглэгчийн мэдээлэл");
        Label passwordLbl = new Label("Нууц үг солих");

        profileImage.setFill(new ImagePattern(StaffContainer.getActiveUser().getImage()));

        profile.setItems(FXCollections.observableArrayList(Arrays.asList(userLbl, passwordLbl)));
        profile.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.intValue() == 1) {
                try {
                    Parent root = FXMLLoader.load(this.getClass().getResource("../view/change_password.fxml"));
                    Stage stage = new Stage();
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.initStyle(StageStyle.TRANSPARENT);
                    Scene scene = new Scene(root);
                    scene.setFill(Color.TRANSPARENT);
                    stage.setScene(scene);
                    stage.show();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        userPane.toFront();
        usersBtn.setOnAction(e -> userPane.toFront());
        productBtn.setOnAction(e -> productPane.toFront());
        reportBtn.setOnAction(e -> reportPane.toFront());

        exitBtn.setOnAction(e -> {
            try {
                Stage newStage = ((Stage) root.getScene().getWindow());
                Parent root = FXMLLoader.load(getClass().getResource("../view/login.fxml"));
                Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
                newStage.setScene(new Scene(root, screenBounds.getWidth(), screenBounds.getHeight()));
                newStage.setTitle("Fimware кассын систем - Нэвтрэх");
                newStage.setFullScreen(true);
                newStage.show();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }

    private void setReport() {
        reportOrder.setOnAction(event -> {
            if (checkDate())
                new Report().showReport(reportOrder.getId(), startDate.getValue(), endDate.getValue());
        });
        reportMax.setOnAction(event -> {
            if (checkDate())
                new Report().showReport(reportOrder.getId(), startDate.getValue(), endDate.getValue());
        });
        reportSale.setOnAction(event -> {
            if (checkDate())
                new Report().showReport(reportOrder.getId(), startDate.getValue(), endDate.getValue());
        });
        reportUserMax.setOnAction(event -> {

        });
        reportUserSales.setOnAction(event -> {

        });
    }

    private boolean checkDate() {
        boolean value = true;
        if (startDate.getValue() == null) {
            startDate.setStyle("-fx-border-color: red");
            value = false;
        } else startDate.setStyle("");
        if (endDate.getValue() == null) {
            endDate.setStyle("-fx-border-color: red");
            value = false;
        } else endDate.setStyle("");
        return value;
    }

    private void setStaff() {
        userAdd.setOnAction(e -> addStaff());
        userEdit.setOnAction(e -> editStaff());
        userRemove.setOnAction(e -> removeStaff());

        userTV.setItems(StaffContainer.getStaffs());
        userId.setCellValueFactory(new PropertyValueFactory<>("id"));
        userImage.setCellValueFactory(new PropertyValueFactory<>("image"));
        userLName.setCellValueFactory(new PropertyValueFactory<>("lName"));
        userFName.setCellValueFactory(new PropertyValueFactory<>("fName"));
        userRegister.setCellValueFactory(new PropertyValueFactory<>("register"));
        userEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        userPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        userGender.setCellValueFactory(new PropertyValueFactory<>("gender"));

        userImage.setCellFactory(e -> new TableCell<Staff, Image>() {
            @Override
            protected void updateItem(Image item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    ImageView imageView = new ImageView(item);
                    imageView.setFitWidth(20);
                    imageView.setFitHeight(25);
                    setGraphic(imageView);
                }
            }
        });
//        searchProduct(StaffContainer.getStaffs());
    }

    private void setType() {
        typeTV.setItems(TypeContainer.getTypes());
        typeTV.getSelectionModel().selectedItemProperty().addListener((ob, o, n) -> productTV.setItems(ProductContainer.getTypeProducts().get(n.getId())));
        typeTC.setCellValueFactory(new PropertyValueFactory<>("name"));
        typeTC.setCellFactory(e -> new TableCell<Type, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                    setText(null);
                } else {
                    Type type = this.getTableView().getItems().get(getIndex());
                    MaterialDesignIconView md = new MaterialDesignIconView();
                    md.setGlyphName(type.getIcon());
                    setGraphic(md);
                    setText(item);
                }
            }
        });
    }

    private void setProduct() {
        productAdd.setOnAction(e -> addProduct());
        productEdit.setOnAction(e -> editProduct());
        productRemove.setOnAction(e -> removeProduct());

        productTV.setItems(ProductContainer.getProducts());
        productName.setCellValueFactory(new PropertyValueFactory<>("name"));
        productId.setCellValueFactory(new PropertyValueFactory<>("id"));
        productBarCode.setCellValueFactory(new PropertyValueFactory<>("barCode"));
        productWeight.setCellValueFactory(new PropertyValueFactory<>("weight"));
        productSize.setCellValueFactory(new PropertyValueFactory<>("size"));
        productPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));

        productBarCode.setStyle("-fx-alignment: CENTER");
        productId.setStyle("-fx-alignment: CENTER");

        productName.setCellFactory(e -> new TableCell<Product, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                    setText(null);
                } else {
                    Product product = this.getTableView().getItems().get(getIndex());
                    ImageView imageView = new ImageView(product.getImage());
                    imageView.setFitWidth(20);
                    imageView.setFitHeight(25);
                    setGraphic(imageView);
                    setText(item);
                }
            }
        });
        productWeight.setCellFactory(e -> new TableCell<Product, Integer>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) setText(null);
                else {
                    Product product = this.getTableView().getItems().get(getIndex());
                    setText(item + product.getMeasureUnit());
                }
            }
        });
        productSize.setCellFactory(e -> new TableCell<Product, Integer>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) setText(null);
                else {
                    setText(item + "ш");
                }
            }
        });
        productPrice.setCellFactory(e -> new TableCell<Product, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) setText(null);
                else {
                    setText(item + "₮");
                }
            }
        });

        searchProduct(ProductContainer.getProducts());
    }

    private void addStaff() {
        try {
            Parent newRoot = FXMLLoader.load(this.getClass().getResource("../view/staff_add.fxml"));
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Fimware кассын систем - Ажилчин нэмэх");
            stage.setScene(new Scene(newRoot));
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void editStaff() {
        Staff staff = userTV.getSelectionModel().getSelectedItem();
        if (staff != null) {
            try {
                StaffEditController.updateActiveEmployee(staff);
                Parent newRoot = FXMLLoader.load(this.getClass().getResource("../view/staff_edit.fxml"));
                Stage stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setTitle("Fimware кассын систем - Ажилчин засах");
                stage.setScene(new Scene(newRoot));
                stage.show();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Засах бүтээгдэхүүн сонгоогүй байна!");
            alert.show();
        }
    }

    private void removeStaff() {
        Staff staff = userTV.getSelectionModel().getSelectedItem();
        if (staff != null) {
            StaffContainer.removeStaff(staff);
            DAO.removeStaff(staff);
            userTV.refresh();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Устгах бүтээгдэхүүн сонгоогүй байна!");
            alert.show();
        }
    }

    private void addProduct() {
        try {
            Parent newRoot = FXMLLoader.load(this.getClass().getResource("../view/product_add.fxml"));
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Fimware кассын систем - Бүтээгдэхүүн нэмэх");
            stage.setScene(new Scene(newRoot));
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void editProduct() {
        Product product = productTV.getSelectionModel().getSelectedItem();
        if (product != null) {
            try {
                ProductEditController.updateProduct(product);
                Parent newRoot = FXMLLoader.load(this.getClass().getResource("../view/product_edit.fxml"));
                Stage stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setTitle("Fimware кассын систем - Бүтээгдэхүүн засах");
                stage.setScene(new Scene(newRoot));
                stage.show();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Засах бүтээгдэхүүн сонгоогүй байна!");
            alert.show();
        }
    }

    private void removeProduct() {
        Product product = productTV.getSelectionModel().getSelectedItem();
        if (product != null) {
            ProductContainer.removeProduct(product);
            productTV.refresh();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Устгах бүтээгдэхүүн сонгоогүй байна!");
            alert.show();
        }
    }

    private void searchProduct(ObservableList<Product> list) {
        if (list != null) {
            FilteredList<Product> filteredList = new FilteredList<>(list, b -> true);
            productSearch.textProperty().addListener((observable, oldValue, newValue) -> filteredList.setPredicate(product -> {
                if (newValue == null || newValue.isEmpty())
                    return true;
                String lower = newValue.toLowerCase();
                if (product.getName().toLowerCase().contains(lower))
                    return true;
                else if (product.getId().toString().contains(lower))
                    return true;
                else return product.getBarCode().toString().contains(lower);
            }));
            SortedList<Product> sortedList = new SortedList<>(filteredList);
            sortedList.comparatorProperty().bind(productTV.comparatorProperty());
            productTV.setItems(sortedList);
        }
    }
}
