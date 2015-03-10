import com.henko.server.dao.ConnectDao;
import com.henko.server.dao.impl.DaoFactory;
import com.henko.server.db.DBManager;
import com.henko.server.db.HikariConnPool;
import com.henko.server.domain.UniqueRequest;
import com.henko.server.model.Connect;
import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static com.henko.server.dao.impl.DaoFactory.*;
import static com.henko.server.db.DBUtil.close;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TestH2ConnectDao {

    private HikariConnPool _pool = HikariConnPool.getConnPool();
    private DaoFactory _daoFactory = getDaoFactory(H2);
    private ConnectDao _connectDao = _daoFactory.getConnectionDao();


    private final static List<Connect> _testData = new ArrayList<Connect>(){{
        add(new Connect(1, "111.11.11.11", "/hello", 1, 1, 1, 1));
        add(new Connect(2, "222.22.22.22", "/redirect", 2, 2, 2, 2));
        add(new Connect(3, "222.22.22.22", "/status", 3, 3, 3, 3));
    }};

    @Before
    public void setUp() throws SQLException {
        DBManager dbManager = new DBManager();
        dbManager.dropTables();
        dbManager.initialiseDB();
        initialiseDBData();
    }

    private void initialiseDBData() throws SQLException {
        Connection conn = _pool.getConnection();
        Statement stmt = conn.createStatement();

        String insert1DataSQL = "INSERT INTO " +
                "CONNECTIONS(SRC_IP, URI, TIME_STAMP, SEND_B, RECEIVED_B, SPEED) " +
                "VALUES ('111.11.11.11', '/hello', 1, 1, 1, 1);";

        String insert2DataSQL = "INSERT INTO " +
                "CONNECTIONS(SRC_IP, URI, TIME_STAMP, SEND_B, RECEIVED_B, SPEED) " +
                "VALUES ('222.22.22.22', '/redirect', 2, 2, 2, 2);";

        String insert3DataSQL = "INSERT INTO " +
                "CONNECTIONS(SRC_IP, URI, TIME_STAMP, SEND_B, RECEIVED_B, SPEED) " +
                "VALUES ('222.22.22.22', '/status', 3, 3, 3, 3);";

        stmt.executeUpdate(insert1DataSQL);
        stmt.executeUpdate(insert2DataSQL);
        stmt.executeUpdate(insert3DataSQL);

        close(stmt);
        close(conn);
    }

    @Test
    public void testSelectById() {
        Connect expected = _testData.get(0);
        Connect actual = _connectDao.getById(1);

        assertEquals(expected, actual);
    }

    @Test
    public void testSelectAll() {
        List<Connect> actual = _connectDao.getAll();

        assertEquals(_testData, actual);
    }

    @Test
    public void testGetLastNConn(){
        assertEquals(new ArrayList<>(asList(_testData.get(1), _testData.get(2))), _connectDao.getLastNConn(2));
    }

    @Test
    public void testGetNUniqueRequests() {
        List<UniqueRequest> expected = new ArrayList<UniqueRequest>(){{
            add(new UniqueRequest(_testData.get(2).getIp(), 2, _testData.get(2).getTimestamp()));
            add(new UniqueRequest(_testData.get(0).getIp(), 1, _testData.get(0).getTimestamp()));
        }};

        List<UniqueRequest> actual = _connectDao.getNUniqueRequest(2);

        assertEquals(expected, actual);
    }

    @Test
    public void testUniqueRequest() {
        List<UniqueRequest> expected = new ArrayList<UniqueRequest>(){{
            add(new UniqueRequest("222.22.22.22", 2, 3));
            add(new UniqueRequest("111.11.11.11", 1, 1));
        }};

        List<UniqueRequest> actual = _connectDao.getNUniqueRequest(2);
        assertEquals(expected, actual);
    }

    @Test
    public void testInsertConnect() {
        Connect expected = new Connect(4, "444.44.44.44", "/status", 4, 4, 4, 4);

        assertNull(_connectDao.getById(4));

        _connectDao.insertConnect(expected);
        Connect actual = _connectDao.getById(4);

        assertEquals(expected, actual);
    }
}
