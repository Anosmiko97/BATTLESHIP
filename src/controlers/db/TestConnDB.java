package controlers.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/* Clases propias */

public class TestConnDB {
    public static void main(String[] args) {
        Connection conn = ConnectionDB.createConnection();
    }   
}
