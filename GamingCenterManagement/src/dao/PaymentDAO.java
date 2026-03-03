package dao;

import db.DBConnection;
import model.Payment;
import java.sql.*;

public class PaymentDAO {

    public void insertPayment(Payment p) throws Exception {

        Connection con = DBConnection.getConnection();
        String sql = "INSERT INTO payments VALUES(?,?,?)";
        PreparedStatement ps = con.prepareStatement(sql);

        ps.setInt(1, p.getPaymentId());
        ps.setInt(2, p.getBookingId());
        ps.setDouble(3, p.getAmount());

        ps.executeUpdate();
        con.close();
    }
}