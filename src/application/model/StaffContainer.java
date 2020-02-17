package application.model;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.stream.IntStream;

public class StaffContainer {
    static private ObservableList<Staff> staffs;
    public static void init() {
        staffs = FXCollections.observableArrayList(
                param -> new Observable[]{
                        param.idProperty(),
                        param.lNameProperty(),
                        param.fNameProperty(),
                        param.registerProperty(),
                        param.emailProperty(),
                        param.phoneProperty(),
                        param.birthDateProperty(),
                        param.genderProperty(),
                        param.imageProperty()
                });
    }

    static private Staff activeUser;

    public static ObservableList<Staff> getStaffs() {
        return staffs;
    }

    public static Staff getActiveUser() {
        return activeUser;
    }

    public static void setActiveUser(Staff activeUser) {
        StaffContainer.activeUser = activeUser;
    }

    public static int getNewId() {
        return IntStream.range(1000, 10000).filter(i -> staffs.stream().noneMatch(e -> e.getId() == i)).findFirst().getAsInt();
    }

    public static void addStaff(Staff staff) {
        staffs.add(staff);
    }

    public static void removeStaff(Staff staff) {
        staffs.remove(staff);
    }
}
