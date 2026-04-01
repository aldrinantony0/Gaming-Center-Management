package dao;

import db.DBConnection;
import model.Game;
import view.Login;
import java.sql.*;

public class GameDAO {

    public void insertGame(Game g) throws Exception {

        Connection con = DBConnection.getConnection();
        String sql = "INSERT INTO games VALUES(?,?,?)";
        PreparedStatement ps = con.prepareStatement(sql);

        ps.setInt(1, g.getGameId());
        ps.setString(2, g.getGameName());
        ps.setDouble(3, g.getPricePerHour());

        ps.executeUpdate();
        con.close();
    }
}
