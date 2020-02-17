package application.controller;

import application.database.DAO;
import application.model.*;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Optional;


public class UserController {
    @FXML
    private AnchorPane root;

    @FXML
    private AnchorPane midPane;

    @FXML
    private TableView<Type> typeTV;

    @FXML
    private TableColumn<Type, String> typeTC;

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
    private TableView<OrderItem> billTV;

    @FXML
    private TableColumn<OrderItem, String> billName;

    @FXML
    private TableColumn<OrderItem, Double> billPrice;

    @FXML
    private TableColumn<OrderItem, Integer> billQuantity;

    @FXML
    private TableColumn<OrderItem, Double> billSubPrice;

    @FXML
    private Button listView;

    @FXML
    private Button gridView;

    @FXML
    private Button confirmBtn;

    @FXML
    private TextField searchTF;

    @FXML
    private SplitMenuButton profile;

    @FXML
    private ImageView profileImage;

    @FXML
    private MenuItem user_info;

    @FXML
    private MenuItem change_password;

    @FXML
    private MenuItem close;

    @FXML
    private MenuItem sign_out;

    private ScrollPane productSP;
    private static TableView<OrderItem> staticOrderTv;

    @FXML
    void initialize() {
        DAO.init();
        Order.defaultOrder();
        setProfile();
        setType();
        setProduct();
        setBill();
        setKeyEvent();
        createGridView();
        changeGridView();

        staticOrderTv = billTV;

        gridView.setOnAction(e -> changeGridView());
        listView.setOnAction(e -> midPane.getChildren().set(0, productTV));
        confirmBtn.setOnAction(e -> confirmProduct());
        confirmBtn.textProperty().bindBidirectional(Order.cashAmountPropery());
    }

    private void setProfile() {
        Staff staff = StaffContainer.getActiveUser();
        profileImage.setImage(staff.getImage());
        profile.setText(staff.getlName() + " " + staff.getfName());

        change_password.setOnAction(e -> {
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
        });

        close.setOnAction(e -> Platform.exit());
        sign_out.setOnAction(e -> {
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

    private void setKeyEvent() {
        root.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER)
                confirmProduct();
        });
    }

    private void setTypeProperty() {
        typeTV.getSelectionModel().selectedItemProperty().addListener((ob, o, n) -> {
            try {
                ObservableList<Product> list = ProductContainer.getTypeProducts().get(n.getId());
                productTV.setItems(list);
                searchProduct(list);
            } catch (NullPointerException ignored) {
            }
        });
    }

    private void setType() {
        typeTV.setItems(TypeContainer.getTypes());
        setTypeProperty();
//        typeTV.focusedProperty().addListener((ob, o, n) -> {
//            typeTV.getSelectionModel().getSelectedItem()
//            DAO.downloadProduct();
//            productTV.setItems(ProductContainer.getProducts());
//            typeTV.setItems(TypeContainer.getTypes());
//        });
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
        productTV.setItems(ProductContainer.getProducts());
        productTV.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2)
                addOrderItem(productTV.getSelectionModel().getSelectedItem());
        });
        productTV.focusedProperty().addListener((ob, o, n) -> {
            DAO.downloadType();
            typeTV.setItems(TypeContainer.getTypes());
        });
        productName.setCellValueFactory(new PropertyValueFactory<>("name"));
        productId.setCellValueFactory(new PropertyValueFactory<>("id"));
        productId.setStyle("-fx-alignment: CENTER");
        productBarCode.setCellValueFactory(new PropertyValueFactory<>("barCode"));
        productBarCode.setStyle("-fx-alignment: CENTER");
        productWeight.setCellValueFactory(new PropertyValueFactory<>("weight"));
        productSize.setCellValueFactory(new PropertyValueFactory<>("size"));
        productPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
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
    }

    private void setBill() {
        billTV.setItems(Order.getStock());
        billTV.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                OrderItem orderItem = billTV.getSelectionModel().getSelectedItem();
                if (orderItem != null)
                    Order.removeItem(orderItem);
            }
        });
        billName.setCellValueFactory(new PropertyValueFactory<>("name"));
        billPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        billPrice.setCellFactory(e -> new TableCell<OrderItem, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) setText(null);
                else setText(item + "₮");
            }
        });
        billQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        billQuantity.setCellFactory(e -> new TableCell<OrderItem, Integer>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) setText(null);
                else setText(item + "ш");
            }
        });
        billSubPrice.setCellValueFactory(new PropertyValueFactory<>("subPrice"));
        billSubPrice.setCellFactory(e -> new TableCell<OrderItem, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) setText(null);
                else setText(item + "₮");
            }
        });
    }

    private void searchProduct(ObservableList<Product> list) {
        if (list != null) {
            FilteredList<Product> filteredList = new FilteredList<>(list, b -> true);
            searchTF.textProperty().addListener((observable, oldValue, newValue) -> filteredList.setPredicate(product -> {
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

    private void confirmProduct() {
        try {
            Parent newRoot = FXMLLoader.load(this.getClass().getResource("../view/calculator.fxml"));
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.setScene(new Scene(newRoot));
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    //Grid view button дарах үед
    private void changeGridView() {
        midPane.getChildren().set(0, productSP);
        AnchorPane.setBottomAnchor(productSP, 0.0);
        AnchorPane.setTopAnchor(productSP, 0.0);
        AnchorPane.setLeftAnchor(productSP, 0.0);
        AnchorPane.setRightAnchor(productSP, 0.0);
    }

    private void createGridView() {
        TilePane tilePane = new TilePane();
        tilePane.setHgap(10);
        tilePane.setVgap(10);
        tilePane.setPadding(new Insets(10.0));

        ProductContainer.getProducts().forEach(e -> {
            ImageView imageView = new ImageView(e.getImage());
            imageView.setFitWidth(100);
            imageView.setFitHeight(100);
            Label size = new Label(e.getSize() + "ш");
            size.setStyle("-fx-background-color: red; -fx-background-radius: 8; -fx-text-fill: white");
            size.setTranslateX(40.0);
            size.setTranslateY(-40.0);
            StackPane stackPane = new StackPane(imageView, size);
            Label label = new Label(e.getName() + " " + e.getWeight() + e.getMeasureUnit());
            Button button = new Button(e.getUnitPrice() + "₮");
            button.setPrefWidth(100);
            button.setOnAction(event -> addOrderItem(e));
            VBox vBox = new VBox(stackPane, label, button);
            vBox.setStyle("-fx-background-color:  #a5d6a7");
            vBox.setAlignment(Pos.CENTER);
            vBox.setSpacing(5);
            tilePane.getChildren().add(vBox);

        });

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.widthProperty().addListener((observable, oldValue, newValue) -> {
            tilePane.setPrefColumns(newValue.intValue() / 110);
            tilePane.setPrefWidth(newValue.doubleValue());
        });
        scrollPane.setContent(tilePane);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        productSP = scrollPane;
    }

    public void addOrderItem(Product e) {
        TextInputDialog dialog = new TextInputDialog("1");
        dialog.setHeaderText("Хэдэн ширхэгийг нэмэх вэ?");
        dialog.setContentText("Тоо");
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(num -> {
            try {
                int quantity = Integer.parseInt(num);
                if (quantity <= e.getSize() && quantity > 0) {
                    OrderItem orderItem = new OrderItem(e.getName(), e.getBarCode(), e.getId(), e.getUnitPrice(), e.getType(), e.getWeight(), e.getMeasureUnit(), e.getSize(), e.getImage(), Integer.valueOf(num));
                    Order.addItem(orderItem);
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setHeaderText("Барааны үлдэгдэл хүрэлцэхгүй байна!");
                    alert.show();
                }
            } catch (NumberFormatException ignored) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setHeaderText("Буруу тоо оруулсан байна!");
                alert.show();
            }
        });
    }

    public static void defaultValue() {
        staticOrderTv.setItems(Order.getStock());
        staticOrderTv.refresh();
    }
}