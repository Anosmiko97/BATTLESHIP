package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/* Clases propias */
import controlers.db.ConnectionDB;

public class MatchDAO {

    public void insertMatch(Match match) {
         try (Connection conn = ConnectionDB.createConnection();
             PreparedStatement pstmt = conn.prepareStatement("INSERT INTO user (name, flag) VALUES (?,?,?,?,?,?)")
        ) {
            pstmt.setBoolean(2, match.getVictory());
            pstmt.setInt(3, match.getSunkenBoats());
            pstmt.setInt(4, match.getScore());
            pstmt.setInt(5, match.getNumberOfShots());
            pstmt.setString(6, match.getOpponentName());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public List<Match> getAllMatches() {
        List<Match> matches = new ArrayList<>();
        
        String sql = "SELECT * FROM match";
        
        try (Connection conn = ConnectionDB.createConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()
        ) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String user_id = rs.getString("user_id");
                Boolean victory = rs.getBoolean("victory");
                int sunkenBoats = rs.getInt("sunken_boats");
                int score = rs.getInt("score");
                int numberOfShots = rs.getInt("number_of_shots");
                String opponentName = rs.getString("opponent_name");

                Match match = new Match(id, user_id, victory, sunkenBoats, score, numberOfShots, opponentName);
                matches.add(match);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return matches;
    }
}
