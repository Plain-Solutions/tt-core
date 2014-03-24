/*
Copyright 2014 Plain Solutions

Authors:
    Vlad Slepukhin <slp.vld@gmail.com>

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package org.ssutt.core.sql;


import javax.naming.NamingException;
import java.sql.Connection;
import java.sql.SQLException;


/**
 * Manages data in h2 database and
 * executes queries
 */
public class SQLManager {
    private static SQLManager sqlm;
    private static Connection conn;
    /**
     * Generates singleton instance
     */
    private SQLManager()  {
    }

    /**
     * Creates an instance of SQLManager for the library user
     * @param java.sql.Connection conn - connection from JNDI from API module
     * @return org.ssutt.core.sql.SQLManager instance (should be singleton some day)
     */
    public static SQLManager getInstance(Connection conn) throws SQLException, NamingException {
        if (sqlm == null) {
            sqlm =new SQLManager();
            SQLManager.conn = conn;
        }
        return sqlm;
    }

}
