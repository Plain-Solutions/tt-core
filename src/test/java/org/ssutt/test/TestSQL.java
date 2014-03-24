package org.ssutt.test;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.ssutt.core.sql.SQLManager;

import javax.naming.NamingException;
import java.sql.SQLException;

/**
 * Created by fau on 24/03/14.
 */
public class TestSQL {
    @Rule
    public ExpectedException sql= ExpectedException.none();
    @Rule
    public ExpectedException ne= ExpectedException.none();

    public TestSQL() {
    }
    @Test
    public void testConnection(){
        try {
            SQLManager sqlm = SQLManager.getInstance();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NamingException e) {
            e.printStackTrace();
        }
        sql.expect(NamingException.class);
        ne.expect(SQLException.class);
    }
}
