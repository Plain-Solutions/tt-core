/**
 * Copyright 2014 Plain Solutions
 *
 * Authors:
 * Vlad Slepukhin <slp.vld@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.ssutt.core.testing;

import org.junit.Assert;
import org.junit.Test;
import org.ssutt.core.sql.SQLManager;
import org.ssutt.core.sql.SSUSQLManager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class TestDB {
    private Connection conn;
    private SQLManager sqlm;
    @Test
    public void TestSQLConnection() throws SQLException, ClassNotFoundException {
        Class.forName("org.h2.Driver");
        this.conn = DriverManager.
                getConnection("jdbc:h2:mem:test", "sa", "");
        Assert.assertNotNull("Testing JDBC connection to Tomcat timetable.h2.db - failed",this.conn);
    }

    @Test
    public void TestSSUSQLManager(){
        this.sqlm = new SSUSQLManager(conn);
        Assert.assertNotNull("Testing SQLManager(SSU) - failed", this.sqlm);
    }
}
