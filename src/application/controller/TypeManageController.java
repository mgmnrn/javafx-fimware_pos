package application.controller;

import application.model.Product;
import application.model.ProductContainer;
import application.model.Type;
import application.model.TypeContainer;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import sample.MaskTextField;

import java.util.Arrays;
import java.util.Optional;

public class TypeManageController {
    @FXML
    private TableView<Type> typeTV;

    @FXML
    private TableColumn<Type, Integer> codeTC;

    @FXML
    private TableColumn<Type, String> nameTC;

    @FXML
    private TableColumn<Type, Void> editTC;

    @FXML
    private TableColumn<Type, Void> removeTC;

    @FXML
    private ComboBox<MaterialDesignIconView> iconCB;

    @FXML
    private MaskTextField nameTF;

    @FXML
    private Button addBtn;

    @FXML
    void initialize() {
        typeTV.setItems(TypeContainer.getTypes());
        codeTC.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameTC.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameTC.setCellFactory(e -> new TableCell<Type, String>() {
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
        setCell(editTC, true);
        setCell(removeTC, false);
        setAddBtn(addBtn);

        iconCB.setItems(getIcon());
        iconCB.getSelectionModel().select(0);
        iconCB.setCellFactory(e -> new ListCell<MaterialDesignIconView>() {
            @Override
            public void updateItem(MaterialDesignIconView item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    MaterialDesignIconView md = new MaterialDesignIconView();
                    md.setGlyphName(item.getGlyphName());
                    setText(item.getGlyphName());
                    setGraphic(md);
                }
            }
        });
        iconCB.setButtonCell(new ListCell<MaterialDesignIconView>() {
            @Override
            public void updateItem(MaterialDesignIconView item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(item.getGlyphName());
                    setGraphic(item);
                }
            }
        });
    }

    private void setAddBtn(Button btn) {
        FontAwesomeIconView fa = new FontAwesomeIconView(FontAwesomeIcon.PLUS_CIRCLE);
        btn.setGraphic(fa);
        btn.setText("Нэмэх");
        btn.setOnAction(e -> {
            if (checkValue()) {
                Type type = new Type(nameTF.getText(), iconCB.getSelectionModel().getSelectedItem().getGlyphName());
                TypeContainer.addType(type);
                clearValue();
            }
        });
    }

    private void setSaveBtn(Button btn) {
        FontAwesomeIconView fa = new FontAwesomeIconView(FontAwesomeIcon.FLOPPY_ALT);
        btn.setGraphic(fa);
        btn.setText("Хадгалах");
    }

    private void clearValue() {
        iconCB.setStyle("");
        nameTF.setText("");
        nameTF.setStyle("");
    }

    private boolean checkValue() {
        boolean value = true;
        if (nameTF.getText().isEmpty()) {
            nameTF.setStyle("-fx-border-color: red");
            value = false;
        } else nameTF.setStyle("");
        return value;
    }

    private void setCell(TableColumn<Type, Void> tableColumn, boolean isEdit) {
        tableColumn.setStyle("-fx-alignment: CENTER");
        tableColumn.setCellFactory(e -> new TableCell<Type, Void>() {
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                FontAwesomeIconView fa = new FontAwesomeIconView();
                Button newBtn = new Button();
                if (isEdit) {
                    fa.setGlyphName("PENCIL_SQUARE_ALT");
                    newBtn.setOnAction(e -> {
                        Type type = this.getTableView().getItems().get(getIndex());
                        MaterialDesignIconView md = new MaterialDesignIconView();
                        md.setGlyphName(type.getIcon());
                        nameTF.setText(type.getName());
                        iconCB.getSelectionModel().select(md);
                        setSaveBtn(addBtn);
                        addBtn.setOnAction(event -> {
                            type.setName(nameTF.getText());
                            type.setIcon(iconCB.getSelectionModel().getSelectedItem().getGlyphName());
                            getTableView().refresh();
                            clearValue();
                            setAddBtn(addBtn);
                        });
                    });
                } else {
                    fa.setGlyphName("TRASH");
                    newBtn.setOnAction(e -> {
                        Type type = this.getTableView().getItems().get(getIndex());
                        ObservableList<Product> pd = ProductContainer.getTypeProducts().get(type.getId());
                        if (pd == null)
                            TypeContainer.removeType(type);
                        else {
                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                            alert.setHeaderText("Тус төрөлд агуулагдаж байгаа бараа бүтээгдэхүүн хамт устахыг анхааруулж байна");
                            alert.setContentText("'" + type.getName() + "' төрөлд '" + pd.size() + "' бараа агуулж байна");
                            Optional<ButtonType> option = alert.showAndWait();
                            if (option.get() == ButtonType.OK)
                                TypeContainer.removeType(type);
                        }
                    });
                }
                newBtn.setGraphic(fa);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(newBtn);
                }
            }
        });
    }

    private ObservableList<MaterialDesignIconView> getIcon() {
        return FXCollections.observableArrayList(Arrays.asList(
                new MaterialDesignIconView(MaterialDesignIcon.CUBE_OUTLINE),
                new MaterialDesignIconView(MaterialDesignIcon.CAKE),
                new MaterialDesignIconView(MaterialDesignIcon.CARROT),
                new MaterialDesignIconView(MaterialDesignIcon.FOOD_APPLE),
                new MaterialDesignIconView(MaterialDesignIcon.GLASS_TULIP),
                new MaterialDesignIconView(MaterialDesignIcon.GLASS_MUG),
                new MaterialDesignIconView(MaterialDesignIcon.GIFT),
                new MaterialDesignIconView(MaterialDesignIcon.PIZZA)
        ));
    }
}
