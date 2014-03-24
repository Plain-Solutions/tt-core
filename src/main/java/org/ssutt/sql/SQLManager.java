/*
* Copyright 2014 Plain Solutions. Licensed under MIT License.
* Authors:
*   Vlad Slepukhin <slp.vld@gmail.com>
*/
package org.ssutt.sql;

import java.sql.Connection;

/**
 * Manages data in h2 database and
 * executes queries
 */
public class SQLManager {
    private static Connection conn;

    /**
     * Connects to Tomcat DataSource
     * @throws java.lang.NullPointerException
     */
    private SQLManager() {
    }

    /**
     * Creates an instance of SQLManager for the library user
     * @throws java.lang.NullPointerException
     */
    public static Connection conn() {
        return null;
    }

}
