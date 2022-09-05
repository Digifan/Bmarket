package db;

import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static db.Rundb.saveToDb;
import static org.junit.jupiter.api.Assertions.*;

class RundbTest {

    @Test
    void saveToDbTest() throws SQLException {
        assertTrue(saveToDb());
    }
}