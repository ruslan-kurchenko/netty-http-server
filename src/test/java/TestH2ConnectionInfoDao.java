import com.henko.server.dao.ConnectionInfoDao;
import com.henko.server.dao.impl.DaoFactory;
import com.henko.server.db.DBManager;
import com.henko.server.db.HikariConnPool;
import com.henko.server.domain.UniqueRequestInfo;
import com.henko.server.model.ConnectionInfo;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.henko.server.dao.impl.DaoFactory.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TestH2ConnectionInfoDao {

    HikariConnPool pool = HikariConnPool.getConnPool();
    DaoFactory daoFactory = getDaoFactory(H2);
    ConnectionInfoDao dao = daoFactory.getConnectionInfoDao();

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
        Connection conn = pool.getConnection();
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
        ConnectionInfo expected = new ConnectionInfo(1, "111.11.11.11", "/hello", firstRowTime, 100, 100, 200);
        ConnectionInfo actual = dao.selectById(1);

        assertEquals(expected, actual);
    }

    @Test
    public void testSelectAll() {
        List<ConnectionInfo> expected = new ArrayList<ConnectionInfo>(){{
            add(new ConnectionInfo(1, "111.11.11.11", "/hello", firstRowTime, 100, 100, 200));
            add(new ConnectionInfo(2, "222.22.22.22", "/hello", secondRowTime, 100, 100, 200));
            add(new ConnectionInfo(3, "222.22.22.22", "/status", thirdRowTime, 100, 100, 200));
        }};

        List<ConnectionInfo> actual = dao.selectAll();

        assertEquals(expected, actual);
    }

    @Test
    public void testUniqueRequestInfo() {
        List<UniqueRequestInfo> expected = new ArrayList<UniqueRequestInfo>(){{
            add(new UniqueRequestInfo("222.22.22.22", 2, new Date(thirdRowTime)));
            add(new UniqueRequestInfo("111.11.11.11", 1, new Date(firstRowTime)));
        }};

        List<UniqueRequestInfo> actual = dao.selectUniqueRequestInfo();

        assertEquals(expected, actual);
    }

    @Test
    public void testInsertConnectionInfo() {
        long timeStamp = 555555555;
        ConnectionInfo expected = new ConnectionInfo(4, "555.55.55.55", "/status", timeStamp, 500, 500, 200);

        assertNull(dao.selectById(4));

        dao.insertConnectionInfo(expected);
        ConnectionInfo actual = dao.selectById(4);

        assertEquals(expected, actual);
    }

//    @Test
//    public void testSelectByTimeStamp() {
//        long timestamp =  0;
//        ConnectionInfo expected = null;
//        for (int i = 0; i < 100; i++) {
//            timestamp = System.currentTimeMillis();
//            expected = new ConnectionInfo("111.11.11.11", "/hello", timestamp, 100, 100, 200);
//
//            dao.insertConnectionInfo(expected);
//
//            System.out.println(dao.selectByTimeStamp(timestamp));
//        }
//        expected.setId(103);
//        ConnectionInfo actual = dao.selectByTimeStamp(timestamp);
//
//        assertEquals(expected, actual);
//    }

    @Test
    public void testSelectNumberOfUniqueRequest() {
        int expected = 2;
        int actual = dao.selectNumberOfUniqueRequest();

        assertEquals(expected, actual);
    }

    @Test
    public void testSelectNumberOfAllRequests(){
        int expected = 3;
        int actual = dao.selectNumberOfAllRequests();

        assertEquals(expected, actual);
    }
}
