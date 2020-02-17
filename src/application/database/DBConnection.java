package application.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static Connection connection = null;
    private static String database = "pos";
    private static String username = "mgmnrn";
    private static String password = "88434030";

    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost/"+database+"?characterEncoding=UTF-8&serverTimezone=UTC", username, password);
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("DB not connected");
            e.printStackTrace();
        }
        return connection;
    }
}
