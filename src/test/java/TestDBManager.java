import com.henko.server.db.DBManager;
import com.henko.server.db.HikariConnPool;
import org.junit.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.Assert.*;

public class TestDBManager {

    @Test
    public void testInitBD() throws SQLException {
        DBManager manager = new DBManager();
        manager.initialiseDB();

        HikariConnPool pool = HikariConnPool.getConnPool();
        testRedirectsTable(pool);
        testConnectionsTable(pool);
    }

    private void testRedirectsTable(HikariConnPool pool) throws SQLException {
        Connection conn = pool.getConnection();
        Statement stmt = conn.createStatement();

        String insertDataSQL = "INSERT INTO REDIRECTS (URL, R_COUNT) VALUES('google.com', 20);";
        stmt.executeUpdate(insertDataSQL);

        String selectQuery = "SELECT * FROM REDIRECTS;";
        ResultSet rs = stmt.executeQuery(selectQuery);
        while(rs.next()) {
            assertEquals(1, rs.getInt(1));
            assertEquals("google.com", rs.getString(2));
            assertEquals(20, rs.getInt(3));
        }
    }

    private void testConnectionsTable(HikariConnPool pool) throws SQLException {
        Connection conn = pool.getConnection();
        Statement stmt = conn.createStatement();

        String insertDataSQL = "INSERT INTO " +
                "CONNECTIONS (SRC_IP, URI, TIME_STAMP, SEND_B, RECEIVED_B, SPEED)  " +
                "VALUES ('111.11.11.11', '/hello', '1000000', '1111', '1111', '1000');";
        stmt.executeUpdate(insertDataSQL);

        String selectQuery = "SELECT * FROM `CONNECTIONS`;";
        ResultSet rs = stmt.executeQuery(selectQuery);
        while(rs.next()) {
            assertEquals(1, rs.getInt(1));
            assertEquals("111.11.11.11", rs.getString(2));
            assertEquals("/hello", rs.getString(3));
            assertEquals(1000000, rs.getLong(4));
            assertEquals(1111, rs.getLong(5));
            assertEquals(1111, rs.getLong(6));
            assertEquals(1000, rs.getLong(7));
        }
    }
}
