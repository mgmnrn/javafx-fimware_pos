package application.model;

import javafx.beans.property.*;

public class Type {
    private IntegerProperty id;
    private StringProperty name;
    private StringProperty icon;

    public Type(Integer id, String name, String icon) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.icon = new SimpleStringProperty(icon);
    }

    public Type(String name, String icon) {
        this.id = new SimpleIntegerProperty(TypeContainer.getNewId());
        this.name = new SimpleStringProperty(name);
        this.icon = new SimpleStringProperty(icon);
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public Integer getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public StringProperty nameProperty() {
        return name;
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getIcon() {
        return icon.get();
    }

    public StringProperty iconProperty() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon.set(icon);
    }

    @Override
    public String toString() {
        return this.name.get();
    }
}
