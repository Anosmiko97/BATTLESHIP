package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import com.mysql.cj.xdevapi.PreparableStatement;

/* Clases propias */
import controlers.db.ConnectionDB;

public class UserDAO {

    public void insertUser(User user) {
        try (Connection conn = ConnectionDB.createConnection();
             PreparedStatement pstmt = conn.prepareStatement("INSERT INTO user (name, flag) VALUES (?,?)")
        ) {
            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getFlag());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteUser() {
        try (Connection conn = ConnectionDB.createConnection();
            PreparedStatement stmt =  conn.prepareStatement("DELETE FROM user");
        ) {
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    } 

    public User getUser() {
        User user = null;

        try (Connection conn = ConnectionDB.createConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM user");
        ) {
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    String flag = rs.getString("flag");
                    user = new User(name);
                    user.setId(id);
                    user.setFlag(flag);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }

    public boolean isEmptyTable() {
        try (Connection conn = ConnectionDB.createConnection();
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery("SELECT COUNT(*) FROM user");
        ) {
            if (result.next()) {
                int count = result.getInt(1);
                return count == 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}