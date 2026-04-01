package dao;

import db.DBConnection;
import model.Booking;
import java.sql.*;

public class BookingDAO {

    public void insertBooking(Booking b) throws Exception {

        Connection con = DBConnection.getConnection();
        String sql = "INSERT INTO bookings VALUES(?,?,?,?)";
        PreparedStatement ps = con.prepareStatement(sql);

        ps.setInt(1, b.getBookingId());
        ps.setInt(2, b.getCustomerId());
        ps.setInt(3, b.getGameId());
        ps.setInt(4, b.getHours());

        ps.executeUpdate();
        con.close();
    }
}