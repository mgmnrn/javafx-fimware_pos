package application.database;

import application.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DAO {
    private static Connection conn = DBConnection.getConnection();

    public static void init() {
        int role = StaffContainer.getActiveUser().getRole();
        if (role != 3)
            downloadStaff();
        downloadType();
        downloadProduct();
    }

    public static void addType(Type type) {
        try (Statement statement = conn.createStatement()) {
            statement.executeUpdate("insert into types values (" + type.getId() + ", N'" + type.getName() + "', '" + type.getIcon() + "') ");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void removeType(Type type) {
        try (Statement statement = conn.createStatement()) {
            statement.executeUpdate("DELETE FROM types WHERE id = " + type.getId() + " ");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateType(Type type) {
        try (Statement statement = conn.createStatement()) {
            statement.executeUpdate("update types set name = '" + type.getName() + "', icon = '" + type.getIcon() + "' where id = " + type.getId() + " ");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addProduct(Product product) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            PreparedStatement ps = conn.prepareStatement("insert into products values ('" + product.getName() + "', " + product.getBarCode() + ", " + product.getId() + ", " + product.getUnitPrice() + ", " + product.getType().getId() + ", " + product.getWeight() + ", '" + product.getMeasureUnit() + "', " + product.getSize() + ", ?)");
            if (product.getImage() != null) {
                BufferedImage bImage = SwingFXUtils.fromFXImage(product.getImage(), null);
                ImageIO.write(bImage, "png", outputStream);
                byte[] res = outputStream.toByteArray();
                InputStream inputStream = new ByteArrayInputStream(res);
                ps.setBlob(1, inputStream);
            }
            ps.execute();
            ps.close();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    public static void removeProduct(Product product) {
        try (Statement statement = conn.createStatement()) {
            statement.executeUpdate("DELETE FROM products WHERE id = " + product.getId() + " ");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateProduct(Product product) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            BufferedImage bImage = SwingFXUtils.fromFXImage(product.getImage(), null);
            ImageIO.write(bImage, "png", outputStream);
            byte[] res = outputStream.toByteArray();
            InputStream inputStream = new ByteArrayInputStream(res);
            PreparedStatement ps = conn.prepareStatement("update products set name = '" + product.getName() + "', bar_code = " + product.getBarCode() + ", id = " + product.getId() + ", unit_price = " + product.getUnitPrice() + ", type_id = " + product.getType().getId() + ", weight = " + product.getWeight() + ", measure_unit = '" + product.getMeasureUnit() + "', size = " + product.getSize() + ", image = ? where id = " + product.getId() + " ");
            ps.setBlob(1, inputStream);
            ps.execute();
            ps.close();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }
    public static void addStaff(Staff staff) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BufferedImage bImage = SwingFXUtils.fromFXImage(staff.getImage(), null);
            ImageIO.write(bImage, "png", outputStream);
            byte[] res = outputStream.toByteArray();
            InputStream inputStream = new ByteArrayInputStream(res);

            Statement statement = conn.createStatement();
            statement.executeUpdate("insert into staffs values (" + staff.getId() + ", N'" + staff.getlName() + "', N'" + staff.getfName() + "', N'" + staff.getRegister() + "', N'" + staff.getEmail() + "', '" + staff.getPhone() + "', '" + staff.getBirthDate() + "',  N'" + staff.getGender() + "', '" + inputStream + "', '" + 3 + "')");
            statement.executeUpdate("insert into accounts values (N'" + staff.getUsername() + "', '" + HashPass.hash(staff.getRegister().substring(6, 10)) + "', '" + staff.getId() + "', '2')");
            statement.close();

            notification(Pos.BOTTOM_RIGHT, "Ажилтаны мэдээллийг амжилттай хадгаллаа.");
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    public static void removeStaff(Staff staff) {
        try {
            Statement statement = conn.createStatement();
            statement.executeUpdate("DELETE FROM staffs WHERE id = '" + staff.getId() + "' ");
            statement.executeUpdate("DELETE FROM accounts WHERE staff_id = '" + staff.getId() + "' ");
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateStaff(Staff staff) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BufferedImage bImage = SwingFXUtils.fromFXImage(staff.getImage(), null);
            ImageIO.write(bImage, "png", outputStream);
            byte[] res = outputStream.toByteArray();
            InputStream inputStream = new ByteArrayInputStream(res);
            PreparedStatement psEmployee = conn.prepareStatement("UPDATE staffs SET lname=?, fname=?, register=?, email=?, phone=?, birthdate=?, gender=?, role_id=?, image=? WHERE id=?");
            psEmployee.setString(1, staff.getlName());
            psEmployee.setString(2, staff.getfName());
            psEmployee.setString(3, staff.getRegister());
            psEmployee.setString(4, staff.getEmail());
            psEmployee.setString(5, staff.getPhone());
            psEmployee.setString(6, staff.getBirthDate().toString());
            psEmployee.setString(7, staff.getGender());
            psEmployee.setInt(8, staff.getRole());
            psEmployee.setBlob(9, inputStream);
            psEmployee.setInt(10, staff.getId());
            psEmployee.executeUpdate();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeOrder() {
        try (Statement statement = conn.createStatement()) {
            statement.executeUpdate("insert into orders values ('" + Order.getBillId() + "', '" + Order.getCashAmount() + "', '" + Order.getVat() + "', '" + Order.getAmount() + "', '" + Order.getDate() + "', '" + Order.getDateTime() + "', '" + Order.getLottery() + "', '" + Order.getQrData() + "', 1234)");
            Order.getStock().forEach(e -> {
                try {
                    statement.executeUpdate("insert into order_details values ('" + Order.getBillId() + "', " + e.getId() + ", " + e.getQuantity() + ", " + e.getSubPrice() + ") ");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean isValidUser(String username) {
        try {
            Statement statement = conn.createStatement();
            ResultSet rsUsers = statement.executeQuery("SELECT username FROM accounts");
            while (rsUsers.next()) {
                if (rsUsers.getString("username").toLowerCase().equals(username.toLowerCase())) {
                    return false;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public static boolean checkPassword(Integer id, String password) {
        try {
            Statement statement = conn.createStatement();

            ResultSet rsUsers = statement.executeQuery("SELECT * FROM accounts");
            while (rsUsers.next()) {
                if (rsUsers.getInt("staff_id") == id && rsUsers.getString("password").equals(HashPass.hash(password))) {
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static int checkUser(String text, String hash) {
        try (Statement statement = conn.createStatement()) {
            ResultSet passwordCheck = statement.executeQuery("SELECT * \n" +
                    "FROM accounts a\n" +
                    "INNER JOIN staffs s ON s.id = a.staff_id \n" +
                    "WHERE username = '" + text + "' AND password = '" + hash + "'");
            if (passwordCheck.next()) {
                int state = passwordCheck.getInt("state");
                if (state != 0) {
                    Image image;
                    try {
                        InputStream inputStream = passwordCheck.getBinaryStream("image");
                        image = new Image(inputStream);
                    } catch (NullPointerException e) {
                        System.out.println("ilee");
                        image = new Image(DAO.class.getResourceAsStream("../../Resource/Images/no-product-found.jpg"));
                    }
                    Staff staff = new Staff(passwordCheck.getInt("id"), passwordCheck.getString("username"), passwordCheck.getString("lname"), passwordCheck.getString("fname"), passwordCheck.getString("register"), passwordCheck.getString("email"), passwordCheck.getString("phone"), passwordCheck.getDate("birthDate"), passwordCheck.getString("gender"), passwordCheck.getInt("role_id"), image);
                    StaffContainer.setActiveUser(staff);
                    int role = passwordCheck.getInt("role_id");
                    switch (role) {
                        case 1:
                            return 1;
                        case 2:
                            return 2;
                        case 3:
                            return 3;
                        default:
                            return 0;
                    }
                } else return 0;
            } else return -1;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    private static void downloadStaff() {
        try {
            StaffContainer.init();
            Statement statement = conn.createStatement();
            ResultSet employeeResult = statement.executeQuery("SELECT s.id, a.username, s.lname, s.fname, s.register, s.email, s.phone, s.birthdate AS date, s.gender, s.role_id, s.image FROM staffs s, accounts a WHERE s.id = a.staff_id");
            while (employeeResult.next()) {
                Integer id = employeeResult.getInt(1);
                String username = employeeResult.getString(2);
                String lastName = employeeResult.getString(3);
                String firstName = employeeResult.getString(4);
                String registerNumber = employeeResult.getString(5);
                String email = employeeResult.getString(6);
                String phoneNumber = employeeResult.getString(7);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate birthDate = LocalDate.parse(employeeResult.getString(8), formatter);
                String gender = employeeResult.getString(9);
                Integer role = employeeResult.getInt(10);
                Image image;
                try {
                    InputStream inputStream = employeeResult.getBinaryStream(11);
                    image = new Image(inputStream);
                } catch (NullPointerException e) {
                    image = new Image(DAO.class.getResourceAsStream("../../Resource/Images/no-product-found.jpg"));
                }
                Staff staff = new Staff(id, username, lastName, firstName, registerNumber, email, phoneNumber, Date.valueOf(birthDate), gender, role, image);
                StaffContainer.addStaff(staff);
            }
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void downloadType() {
        try (Statement statement = conn.createStatement()) {
            ResultSet typeResult = statement.executeQuery("SELECT * FROM types");
            TypeContainer.init();
            while (typeResult.next()) {
                Type type = new Type(typeResult.getInt(1), typeResult.getString(2), typeResult.getString(3));
                TypeContainer.addType(type);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void downloadProduct() {
        try (Statement statement = conn.createStatement()) {
            ProductContainer.init();
            ResultSet rs = statement.executeQuery("SELECT p.name, p.bar_code, p.id, p.unit_price, p.type_id, p.weight, p.measure_unit, p.size, p.image \n" +
                    "FROM products p, types t \n" +
                    "WHERE p.type_id = t.id");
            while (rs.next()) {
                String name = rs.getString(1);
                Integer barCode = rs.getInt(2);
                Integer id = rs.getInt(3);
                Double unitPrice = rs.getDouble(4);
                Type type = TypeContainer.getType(rs.getInt(5));
                Integer weight = rs.getInt(6);
                String measureUnit = rs.getString(7);

                Integer size = rs.getInt(8);
                InputStream inputStream = rs.getBinaryStream(9);
                Image image = new Image(inputStream);
                if (image.getHeight() == 0) {
                    image = new Image(DAO.class.getResourceAsStream("../../Resource/Images/no-product-found.jpg"));
                }
                Product product = new Product(name, barCode, id, unitPrice, type, weight, measureUnit, size, image);
                ProductContainer.addProduct(product);
                if (ProductContainer.getTypeProducts().containsKey(product.getType().getId()))
                    ProductContainer.getTypeProducts().get(product.getType().getId()).add(product);
                else {
                    ObservableList<Product> tempList = FXCollections.observableArrayList();
                    tempList.add(product);
                    ProductContainer.getTypeProducts().put(product.getType().getId(), tempList);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void notification(Pos pos, String Text) {
        Notifications notificationBuilder = Notifications.create()
                .text(Text)
                .hideAfter(Duration.seconds(3))
                .position(pos);
        notificationBuilder.darkStyle();
        notificationBuilder.showWarning();
    }
}
