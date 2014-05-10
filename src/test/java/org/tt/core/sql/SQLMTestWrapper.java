/*
* Copyright 2014 Plain Solutions
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
package org.tt.core.sql;

import org.h2.jdbcx.JdbcDataSource;
import org.h2.tools.RunScript;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.sql.SQLException;

public abstract class SQLMTestWrapper {
    private static final SecureRandom random = new SecureRandom();
    private static final AbstractQueries qrs = new H2Queries();

    private static final String JDBCDRIVER = org.h2.Driver.class.getName();
    private static final String JDBCPATH = "jdbc:h2:mem:%s;DB_CLOSE_DELAY=-1;";
    private static final String DBLOGIN = "sa";
    private static final String DBPASS = "";
    private static final String DBSCHEME = "./src/test/resources/initTT.sql";
    private static final String DBTESTSCHEME = "./src/test/resources/testAdjustment.sql";
    private static final Charset DBCS = StandardCharsets.UTF_8;

    public static AbstractSQLManager produceNewManager() {
        try {
            Class.forName(JDBCDRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        String localPath = String.format(JDBCPATH, nextName());
        JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setURL(localPath);
        dataSource.setUser(DBLOGIN);
        dataSource.setPassword(DBPASS);

        System.out.println(dataSource.getURL());
        try {
            RunScript.execute(localPath, DBLOGIN, DBPASS, DBSCHEME, DBCS, false);
            RunScript.execute(localPath, DBLOGIN, DBPASS, DBTESTSCHEME, DBCS, false);
            return new SSUSQLManager(dataSource.getConnection(),qrs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;

    }

    private static String nextName() {
        return new BigInteger(130, random).toString(32);
    }

}
