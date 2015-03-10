import com.henko.server.dao.RedirectDao;
import com.henko.server.dao.exception.PersistException;
import com.henko.server.db.HikariConnPool;
import com.henko.server.dao.impl.DaoFactory;
import com.henko.server.db.DBManager;
import com.henko.server.model.Redirect;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static com.henko.server.dao.impl.DaoFactory.*;
import static com.henko.server.db.DBUtil.close;
import static org.junit.Assert.*;

public class TestH2RedirectDao {
    private HikariConnPool _pool = HikariConnPool.getConnPool();
    private DaoFactory _daoFactory = getDaoFactory(H2);
    private RedirectDao _redirectDao = _daoFactory.getRedirectDao();

    private final static List<Redirect> _testData = _generateTestData();

    @Before
    public void setUp() throws SQLException {
        DBManager dbManager = new DBManager();
        dbManager.dropTables();
        dbManager.initialiseDB();
        initialiseDBData();
    }

    @Test
    public void testSelectByUrl() {
        Redirect expected = _testData.get(0);
        Redirect actual = _redirectDao.getByUrl("google.com");

        assertEquals(expected, actual);
    }

    @Test
    public void testSelectAll() {
        List<Redirect> actual = _redirectDao.getAll();

        assertEquals(_testData, actual);
    }

    @Test
    public void testGetNRedirect() {
        List<Redirect> expected = new ArrayList<Redirect>(){{
            add(new Redirect(_testData.get(2).getId(), _testData.get(2).getUrl(), _testData.get(2).getCount()));
            add(new Redirect(_testData.get(1).getId(), _testData.get(1).getUrl(), _testData.get(1).getCount()));
        }};

        List<Redirect> actual = _redirectDao.getNRedirect(2);

        assertEquals(expected, actual);
    }

    @Test
    public void testAddOrIncrementRedirect() throws PersistException {
        String testUrl = "http://test.url.com";

        assertNull(_redirectDao.getByUrl(testUrl));

        _redirectDao.addOrIncrementCount(testUrl);

        Redirect expected = new Redirect(4, testUrl, 1);
        Redirect actual = _redirectDao.getByUrl(testUrl);

        assertEquals(expected, actual);
    }

    private void initialiseDBData() throws SQLException {
        Connection conn = _pool.getConnection();
        Statement stmt = conn.createStatement();

        String insert1DataSQL = "INSERT INTO REDIRECTS (URL, R_COUNT) VALUES('google.com', 1);";
        String insert2DataSQL = "INSERT INTO REDIRECTS (URL, R_COUNT) VALUES('vk.com', 2);";
        String insert3DataSQL = "INSERT INTO REDIRECTS (URL, R_COUNT) VALUES('facebook.com', 3);";

        stmt.executeUpdate(insert1DataSQL);
        stmt.executeUpdate(insert2DataSQL);
        stmt.executeUpdate(insert3DataSQL);

        close(stmt);
        close(conn);
    }

    private static ArrayList<Redirect> _generateTestData() {
        return new ArrayList<Redirect>(){{
            add(new Redirect(1, "google.com", 1));
            add(new Redirect(2, "vk.com", 2));
            add(new Redirect(3, "facebook.com", 3));
        }};
    }

}
