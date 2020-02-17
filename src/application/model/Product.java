package application.model;

import javafx.beans.property.*;
import javafx.scene.image.Image;

public class Product {
    private StringProperty name;
    private IntegerProperty barCode;
    private IntegerProperty id;
    private DoubleProperty unitPrice;
    private ObjectProperty<Type> type;
    private IntegerProperty weight;
    private StringProperty measureUnit;
    private IntegerProperty size;
    private ObjectProperty<Image> image;

    public Product(String name, Integer barCode, Integer id, Double unitPrice, Type type, Integer weight, String measureUnit, Integer size, Image image) {
        this.name = new SimpleStringProperty(name);
        this.barCode = new SimpleIntegerProperty(barCode);
        this.id = new SimpleIntegerProperty(id);
        this.unitPrice = new SimpleDoubleProperty(unitPrice);
        this.type = new SimpleObjectProperty<>(type);
        this.weight = new SimpleIntegerProperty(weight);
        this.measureUnit = new SimpleStringProperty(measureUnit);
        this.size = new SimpleIntegerProperty(size);
        this.image = new SimpleObjectProperty<>(image);
    }

    public Product(String name, Integer barCode, Integer id, Double unitPrice, Type type, Integer weight, String measureUnit, Integer size) {
        this.name = new SimpleStringProperty(name);
        this.barCode = new SimpleIntegerProperty(barCode);
        this.id = new SimpleIntegerProperty(id);
        this.unitPrice = new SimpleDoubleProperty(unitPrice);
        this.type = new SimpleObjectProperty<>(type);
        this.weight = new SimpleIntegerProperty(weight);
        this.measureUnit = new SimpleStringProperty(measureUnit);
        this.size = new SimpleIntegerProperty(size);
        this.image = new SimpleObjectProperty<>();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public IntegerProperty barCodeProperty() {
        return barCode;
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public DoubleProperty unitPriceProperty() {
        return unitPrice;
    }

    public ObjectProperty<Type> typeProperty() {
        return type;
    }

    public IntegerProperty weightProperty() {
        return weight;
    }

    public StringProperty measureUnitProperty() {
        return measureUnit;
    }

    public IntegerProperty sizeProperty() {
        return size;
    }

    public ObjectProperty<Image> imageProperty() {
        return image;
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public Integer getBarCode() {
        return barCode.get();
    }

    public void setBarCode(Integer barCode) {
        this.barCode.set(barCode);
    }

    public Integer getId() {
        return id.get();
    }

    public void setId(Integer id) {
        this.id.set(id);
    }

    public Double getUnitPrice() {
        return unitPrice.get();
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice.set(unitPrice);
    }

    public Type getType() {
        return type.get();
    }

    public void setType(Type type) {
        this.type.set(type);
    }

    public Integer getWeight() {
        return weight.get();
    }

    public void setWeight(Integer weight) {
        this.weight.set(weight);
    }

    public String getMeasureUnit() {
        return measureUnit.get();
    }

    public void setMeasureUnit(String measureUnit) {
        this.measureUnit.set(measureUnit);
    }

    public Integer getSize() {
        return size.get();
    }

    public void setSize(Integer size) {
        this.size.set(size);
    }

    public Image getImage() {
        return image.get();
    }

    public void setImage(Image image) {
        this.image.set(image);
    }
}
