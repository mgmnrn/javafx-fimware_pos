package application.model;

import javafx.scene.image.Image;

public class OrderItem extends Product {
    private Integer quantity;
    private Double subPrice;

    public OrderItem(String name, Integer barCode, Integer id, Double unitPrice, Type type, Integer weight, String measureUnit, Integer size, Image image, Integer quantity) {
        super(name, barCode, id, unitPrice, type, weight, measureUnit, size, image);
        this.quantity = quantity;
        this.subPrice = unitPrice * quantity;
    }

    public Double getSubPrice() {
        return subPrice;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
        subPrice = super.getUnitPrice() * quantity;
    }
}
