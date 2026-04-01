package db;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

    public static Connection getConnection() {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");

           Connection con = DriverManager.getConnection(
     "jdbc:oracle:thin:@//localhost:1521/FREE",
            "C##ALDRIN",
            "1234"
);

            return con;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}