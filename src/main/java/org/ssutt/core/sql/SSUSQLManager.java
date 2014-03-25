/**
 * Copyright 2014 Plain Solutions
 *
 * Authors:
 *  Vlad Slepukhin <slp.vld@gmail.com>
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
package org.ssutt.core.sql;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;


/**
 * Manages data in h2 database and
 * executes queries
 */
public class SSUSQLManager implements SQLManager {
    static final Logger logger = LogManager.getLogger(SQLManager.class.getName());
    private static Connection conn;

    public SSUSQLManager(Connection conn) {
        SSUSQLManager.conn = conn;
        logger.info("SSU SQLManager initialized with JNDI DB (H2) correctly.");
    }


    @Override
    public void putDepartments(Map<String, String> departments) {

    }

    @Override
    public String getDepartmentTag(String name) {
        return null;
    }

    @Override
    public String getDepartmentName(String tag) {
        return null;
    }

    @Override
    public HashMap<String, String> getDepartments() {
        return null;
    }

}
