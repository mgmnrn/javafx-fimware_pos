package application.model;

import application.database.DAO;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.util.stream.IntStream;

public class TypeContainer {
    static private ObservableList<Type> types;

    public static void init() {
        types = FXCollections.observableArrayList(
                param -> new Observable[]{
                        param.idProperty(),
                        param.nameProperty(),
                        param.iconProperty()
                });
    }

    public static void addListener() {
        types.addListener((ListChangeListener<Type>) c -> {
            while (c.next()) {
                if (c.wasAdded()) {
                    for (Type type : c.getAddedSubList())
                        DAO.addType(type);
                } else if (c.wasRemoved()) {
                    for (Type type : c.getRemoved())
                        DAO.removeType(type);
                } else if (c.wasUpdated()) {
                    for (int i = c.getFrom(); i < c.getTo(); ++i)
                        DAO.updateType(types.get(i));
                }
            }
        });
    }

    public static ObservableList<Type> getTypes() {
        return types;
    }

    public static void addType(Type type) {
        types.add(type);
    }

    public static void removeType(Type type) {
        types.remove(type);
    }

    public static Type getType(Integer id) {
        for (Type loopType : types)
            if (loopType.getId().equals(id))
                return loopType;
        return null;
    }

    public static int getNewId() {
        return IntStream.range(100, 1000).filter(i -> types.stream().noneMatch(e -> e.getId() == i)).findFirst().getAsInt();
    }
}
