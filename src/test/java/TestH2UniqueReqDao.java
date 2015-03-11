import com.henko.server.dao.UniqueReqDao;
import com.henko.server.dao.impl.DaoFactory;
import com.henko.server.db.DBManager;
import com.henko.server.db.HikariConnPool;
import com.henko.server.model.UniqueReq;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static com.henko.server.dao.impl.DaoFactory.H2;
import static com.henko.server.dao.impl.DaoFactory.getDaoFactory;
import static com.henko.server.db.DBUtil.close;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TestH2UniqueReqDao {
    private HikariConnPool _pool = HikariConnPool.getConnPool();
    private DaoFactory _daoFactory = getDaoFactory(H2);
    private UniqueReqDao _requestDao = _daoFactory.getUniqueReqDao();

    private final static List<UniqueReq> _testData = _generateTestData();

    @Before
    public void setUp() throws SQLException {
        DBManager dbManager = new DBManager();
        dbManager.dropTables();
        dbManager.initialiseDB();
        _initialiseDBData();
    }

    @Test
    public void testGetByIP() {
        UniqueReq expected = _testData.get(0);
        UniqueReq actual = _requestDao.getByIp("111.11.11.11");

        assertEquals(expected, actual);
    }

    @Test
    public void testGetNUniqueReq() {
        List<UniqueReq> expected = new ArrayList<UniqueReq>(){{
            add(_testData.get(2));
            add(_testData.get(1));
        }};

        List<UniqueReq> actual = _requestDao.getNUniqueReq(2);

        assertEquals(expected, actual);
    }

    @Test
    public void testAddOrIncrementCount() {
        String testIp = "444.44.44.44";
        long testLastConn = System.currentTimeMillis();

        assertNull(_requestDao.getByIp(testIp));

        _requestDao.addOrIncrementCount(new UniqueReq(testIp, testLastConn));

        assertEquals(testIp, _requestDao.getByIp(testIp).getIp());
        assertEquals(1, _requestDao.getByIp(testIp).getCount());

        _requestDao.addOrIncrementCount(new UniqueReq(testIp, testLastConn));

        assertEquals(2, _requestDao.getByIp(testIp).getCount());
    }

    private static List<UniqueReq> _generateTestData() {
        return new ArrayList<UniqueReq>(){{
            add(new UniqueReq(1, "111.11.11.11", 1, 1));
            add(new UniqueReq(2, "222.22.22.22", 2, 2));
            add(new UniqueReq(3, "333.33.33.33", 3, 3));
        }};
    }

    private void _initialiseDBData() throws SQLException {
        Connection conn = _pool.getConnection();
        Statement stmt = conn.createStatement();

        String insert1DataSQL = "INSERT INTO UNIQUE_REQUESTS(IP, COUNT, LAST_CONN) VALUES('111.11.11.11', 1, 1);";
        String insert2DataSQL = "INSERT INTO UNIQUE_REQUESTS(IP, COUNT, LAST_CONN) VALUES('222.22.22.22', 2, 2);";
        String insert3DataSQL = "INSERT INTO UNIQUE_REQUESTS(IP, COUNT, LAST_CONN) VALUES('333.33.33.33', 3, 3);";

        stmt.executeUpdate(insert1DataSQL);
        stmt.executeUpdate(insert2DataSQL);
        stmt.executeUpdate(insert3DataSQL);

        close(stmt);
        close(conn);
    }


}
