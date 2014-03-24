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
