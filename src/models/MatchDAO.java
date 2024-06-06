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
             PreparedStatement pstmt = conn.prepareStatement("INSERT INTO match_party (victory,\r\n" + //
                                  "                    sunken_boats,\r\n" + //
                                  "                    score,\r\n" + //
                                  "                    number_of_shots,\r\n" + //
                                  "                    opponent_name) VALUES (?,?,?,?,?)")
        ) {
            pstmt.setBoolean(1, match.getVictory());
            pstmt.setInt(2, match.getSunkenBoats());
            pstmt.setInt(3, match.getScore());
            pstmt.setInt(4, match.getNumberOfShots());
            pstmt.setString(5, match.getOpponentName());
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
                Boolean victory = rs.getBoolean("victory");
                int sunkenBoats = rs.getInt("sunken_boats");
                int score = rs.getInt("score");
                int numberOfShots = rs.getInt("number_of_shots");
                String opponentName = rs.getString("opponent_name");

                Match match = new Match(victory, sunkenBoats, score, numberOfShots, opponentName);
                matches.add(match);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return matches;
    }
}
