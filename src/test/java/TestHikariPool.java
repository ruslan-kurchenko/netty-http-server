import com.henko.server.db.connectionpool.HikariConnPool;
import org.junit.Test;

import java.sql.*;

import static org.junit.Assert.assertNotNull;

public class TestHikariPool {

    @Test
    public void testGetConnection() throws SQLException {
        assertNotNull("must be not null", HikariConnPool.getConnPool().getConnection());
    }

    @Test
    public void testQueryToDB() throws SQLException {
        Connection conn = HikariConnPool.getConnPool().getConnection();
        Statement stmt = conn.createStatement();

        String dropTableSQL = "DROP TABLE IF EXISTS REDIRECTS";
        String createTableSQL = "CREATE TABLE REDIRECTS (ID_REDIRECT INT PRIMARY KEY, URL VARCHAR(100), COUNT INT);";

        String insert1DataSQL = "INSERT INTO REDIRECTS (ID_REDIRECT, URL, COUNT) VALUES (1, \'vk.com\', 10)";
        String insert2DataSQL = "INSERT INTO REDIRECTS (ID_REDIRECT, URL, COUNT) VALUES (2, \'facebook.com\', 20)";
        String insert3DataSQL = "INSERT INTO REDIRECTS (ID_REDIRECT, URL, COUNT) VALUES (3, \'google.com\', 30)";

        stmt.execute(dropTableSQL);
        stmt.executeUpdate(createTableSQL);
        stmt.executeUpdate(insert1DataSQL);
        stmt.executeUpdate(insert2DataSQL);
        stmt.executeUpdate(insert3DataSQL);

        String selectQuery = "SELECT * FROM REDIRECTS;";
        ResultSet ps = stmt.executeQuery(selectQuery);

        while(ps.next()) {
            System.out.println(ps.getInt(1) + " " + ps.getString(2) + " " + ps.getInt(3));
        }
    }
}
