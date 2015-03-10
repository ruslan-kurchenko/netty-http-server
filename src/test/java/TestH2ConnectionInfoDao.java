import com.henko.server.dao.ConnectDao;
import com.henko.server.dao.impl.DaoFactory;
import com.henko.server.db.DBManager;
import com.henko.server.db.HikariConnPool;
import com.henko.server.domain.UniqueRequest;
import com.henko.server.model.Connect;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static com.henko.server.dao.impl.DaoFactory.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TestH2ConnectionInfoDao {

    HikariConnPool pool = HikariConnPool.getConnPool();
    DaoFactory daoFactory = getDaoFactory(H2);
    ConnectDao dao = daoFactory.getConnectionDao();

    @Before
    public void setUp() throws SQLException {
        DBManager dbManager = new DBManager();
        dbManager.dropTables();
        dbManager.initialiseDB();
        initialiseDBData();
    }

    long firstRowTime = System.currentTimeMillis();
    long secondRowTime = System.currentTimeMillis();
    long thirdRowTime = System.currentTimeMillis();
    private void initialiseDBData() throws SQLException {
        java.sql.Connection conn = pool.getConnection();
        Statement stmt = conn.createStatement();

        String insert1DataSQL = "INSERT INTO " +
                "CONNECTIONS(SRC_IP, URI, TIME_STAMP, SEND_B, RECEIVED_B, SPEED) " +
                "VALUES ('111.11.11.11', '/hello', "+ firstRowTime + ", 100, 100, 200);";

        String insert2DataSQL = "INSERT INTO " +
                "CONNECTIONS(SRC_IP, URI, TIME_STAMP, SEND_B, RECEIVED_B, SPEED) " +
                "VALUES ('222.22.22.22', '/hello',"+ secondRowTime + ", 100, 100, 200);";

        String insert3DataSQL = "INSERT INTO " +
                "CONNECTIONS(SRC_IP, URI, TIME_STAMP, SEND_B, RECEIVED_B, SPEED) " +
                "VALUES ('222.22.22.22', '/status', "+ thirdRowTime + ", 100, 100, 200);";

        stmt.executeUpdate(insert1DataSQL);
        stmt.executeUpdate(insert2DataSQL);
        stmt.executeUpdate(insert3DataSQL);
    }

    @Test
    public void testSelectById() {
        Connect expected = new Connect(1, "111.11.11.11", "/hello", firstRowTime, 100, 100, 200);
        Connect actual = dao.getById(1);

        assertEquals(expected, actual);
    }

    @Test
    public void testSelectAll() {
        List<Connect> expected = new ArrayList<Connect>(){{
            add(new Connect(1, "111.11.11.11", "/hello", firstRowTime, 100, 100, 200));
            add(new Connect(2, "222.22.22.22", "/hello", secondRowTime, 100, 100, 200));
            add(new Connect(3, "222.22.22.22", "/status", thirdRowTime, 100, 100, 200));
        }};

        List<Connect> actual = dao.getAll();

        assertEquals(expected, actual);
    }

    @Test
    public void testUniqueRequestInfo() {
        List<UniqueRequest> expected = new ArrayList<UniqueRequest>(){{
            add(new UniqueRequest("222.22.22.22", 2, thirdRowTime));
            add(new UniqueRequest("111.11.11.11", 1, firstRowTime));
        }};

        List<UniqueRequest> actual = dao.getNUniqueRequest(3);

        assertEquals(expected, actual);
    }

    @Test
    public void testInsertConnectionInfo() {
        long timeStamp = 555555555;
        Connect expected = new Connect(4, "555.55.55.55", "/status", timeStamp, 500, 500, 200);

        assertNull(dao.getById(4));

        dao.insertConnect(expected);
        Connect actual = dao.getById(4);

        assertEquals(expected, actual);
    }

    @Test
    public void testSelectNumberOfUniqueRequest() {
        int expected = 2;
        int actual = dao.getNumOfUniqueConn();

        assertEquals(expected, actual);
    }
}
