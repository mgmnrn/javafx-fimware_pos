package application.model;

import application.database.DAO;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

public class ProductContainer {
    static private ObservableList<Product> products;
    static private ObservableMap<Integer, ObservableList<Product>> typeProducts;

    public static void init(){
        products = FXCollections.observableArrayList(
                param -> new Observable[]{
                        param.nameProperty(),
                        param.barCodeProperty(),
                        param.idProperty(),
                        param.unitPriceProperty(),
                        param.typeProperty(),
                        param.weightProperty(),
                        param.measureUnitProperty(),
                        param.sizeProperty(),
                        param.imageProperty()
                });
        typeProducts = FXCollections.observableHashMap();
    }

    public static ObservableMap<Integer, ObservableList<Product>> getTypeProducts() {
        return typeProducts;
    }

    public static ObservableList<Product> getProducts() {
        return products;
    }

    public static void addProduct(Product product) {
        products.add(product);
    }

    public static void removeProduct(Product product) {
        products.remove(product);
    }

    public static boolean isContain(Integer id) {
        return products.stream().anyMatch(e -> e.getId().equals(id));
    }

    public static void addListener() {
        products.addListener((ListChangeListener<Product>) c -> {
            while (c.next()) {
                if (c.wasAdded()) {
                    for (Product product : c.getAddedSubList()) {
                        DAO.addProduct(product);
                        if (typeProducts.containsKey(product.getType().getId()))
                            typeProducts.get(product.getType().getId()).add(product);
                        else {
                            typeProducts.put(product.getType().getId(), FXCollections.observableArrayList(product));
                        }
                    }
                } else if (c.wasRemoved()) {
                    for (Product product : c.getRemoved()) {
                        DAO.removeProduct(product);
                        typeProducts.get(product.getType().getId()).remove(product);
                    }
                } else if (c.wasUpdated()) {
                    for (int i = c.getFrom(); i < c.getTo(); ++i) {
                        DAO.updateProduct(products.get(i));
                        System.out.println(products.get(i) + " irlee");
                    }
                }
            }
        });
    }
}
