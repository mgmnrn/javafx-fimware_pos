package application.model;

import javafx.beans.property.*;
import javafx.scene.image.Image;

import java.sql.Date;

public class Staff {
    private IntegerProperty id;
    private StringProperty username;
    private StringProperty lName;
    private StringProperty fName;
    private StringProperty register;
    private StringProperty email;
    private StringProperty phone;
    private ObjectProperty<Date> birthDate;
    private StringProperty gender;
    private IntegerProperty role;
    private ObjectProperty<Image> image;

    public Staff() {
    }

    public Staff(Integer id, String username, String lName, String fName, String register, String email, String phone, Date birthDate, String gender, Integer role, Image image) {
        this.id = new SimpleIntegerProperty(id);
        this.username = new SimpleStringProperty(username);
        this.lName = new SimpleStringProperty(lName);
        this.fName = new SimpleStringProperty(fName);
        this.register = new SimpleStringProperty(register);
        this.email = new SimpleStringProperty(email);
        this.phone = new SimpleStringProperty(phone);
        this.birthDate = new SimpleObjectProperty<>(birthDate);
        this.gender = new SimpleStringProperty(gender);
        this.role = new SimpleIntegerProperty(role);
        this.image = new SimpleObjectProperty<>(image);
    }

    public Integer getId() {
        return id.get();
    }

    public void setId(Integer id) {
        this.id.set(id);
    }

    public String getUsername() {
        return username.get();
    }

    public void setId(String username) {
        this.username.set(username);
    }

    public StringProperty usernameProperty() {
        return username;
    }

    public void setUsername(String username) {
        this.username.set(username);
    }

    public String getlName() {
        return lName.get();
    }

    public void setlName(String lName) {
        this.lName.set(lName);
    }

    public String getfName() {
        return fName.get();
    }

    public void setfName(String fName) {
        this.fName.set(fName);
    }

    public String getRegister() {
        return register.get();
    }

    public void setRegister(String register) {
        this.register.set(register);
    }

    public String getEmail() {
        return email.get();
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public String getPhone() {
        return phone.get();
    }

    public void setPhone(String phone) {
        this.phone.set(phone);
    }

    public Date getBirthDate() {
        return birthDate.get();
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate.set(birthDate);
    }

    public String getGender() {
        return gender.get();
    }

    public void setGender(String gender) {
        this.gender.set(gender);
    }

    public int getRole() {
        return role.get();
    }

    public IntegerProperty roleProperty() {
        return role;
    }

    public void setRole(int role) {
        this.role.set(role);
    }

    public Image getImage() {
        return image.get();
    }

    public void setImage(Image image) {
        this.image.set(image);
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public StringProperty lNameProperty() {
        return lName;
    }

    public StringProperty fNameProperty() {
        return fName;
    }

    public StringProperty registerProperty() {
        return register;
    }

    public StringProperty emailProperty() {
        return email;
    }

    public StringProperty phoneProperty() {
        return phone;
    }

    public ObjectProperty<Date> birthDateProperty() {
        return birthDate;
    }

    public StringProperty genderProperty() {
        return gender;
    }

    public ObjectProperty<Image> imageProperty() {
        return image;
    }
}
