package dao;

import db.DBConnection;
import model.Customer;
import java.sql.*;

public class CustomerDAO {

    public void insertCustomer(Customer c) throws Exception {

        Connection con = DBConnection.getConnection();

        String sql = "INSERT INTO customers VALUES(?,?,?)";
        PreparedStatement ps = con.prepareStatement(sql);

        ps.setInt(1, c.getId());
        ps.setString(2, c.getName());
        ps.setString(3, c.getPhone());

        ps.executeUpdate();
        con.close();
    }
}