/*
* Copyright 2014 Plain Solutions
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package org.tt.core.sql;

import org.dbunit.IDatabaseTester;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.h2.command.dml.RunScriptCommand;
import org.h2.jdbcx.JdbcDataSource;
import org.h2.tools.RunScript;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.tt.core.entity.datafetcher.Department;
import org.tt.core.entity.datafetcher.Group;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class SSUSQLManagerTest {
    private static final String JDBCDRIVER = org.h2.Driver.class.getName();
    private static final String JDBCPATH = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;";
    private static final String DBLOGIN = "sa";
    private static final String DBPASS = "";
    private static final String DBSCHEME = "./src/test/resources/initTT.sql";
    private static final String DBTESTSCHEME = "./src/test/resources/testAdjustment.sql";
    private static final String DBDATASET = "./src/test/resources/dataset.xml";
    private static final Charset DBCS = StandardCharsets.UTF_8;

    @BeforeClass
    public static void createSchema() throws Exception {
        RunScript.execute(JDBCPATH, DBLOGIN, DBPASS, DBSCHEME, DBCS, false);
    }

    @Before
    public void importDataSet() throws Exception {
        IDataSet dataSet = readDataSet();
        cleanlyInsert(dataSet);
    }

    @Test
    public void testUpdateDepartmentMessage() throws Exception {

    }

    @Test
    public void testUpdateDepartmentInfo() throws Exception {

    }

    @Test
    public void testGetDepartments() throws Exception {
        AbstractSQLManager sqlm = new SSUSQLManager(getConnection(), new H2Queries());
        List<Department> result = sqlm.getDepartments();
        List<Department> expected = java.util.Arrays.asList(new Department("Biology House", "bf", "Crucial Information!"),
                new Department("Geography House", "gf", ""));

        assertTrue("Departments getting failed" +
                        "\n  'result'        = " + result +
                        "\n  'expected' = " + expected,
                expected.equals(result)
        );

    }

    @Test
    public void testGetDepartmentTags() throws Exception {
        AbstractSQLManager sqlm = new SSUSQLManager(getConnection(), new H2Queries());
        List<String> result = sqlm.getDepartmentTags();
        List<String> expected = Arrays.asList("bf", "gf");

        assertTrue("Department tags getting failed" +
                        "\n  'result'        = " + result +
                        "\n  'expected' = " + expected,
                expected.equals(result)
        );

    }

    @Test
    public void testGetNonEmptyDepartmentMessage() throws Exception {
        AbstractSQLManager sqlm = new SSUSQLManager(getConnection(), new H2Queries());
        String result = sqlm.getDepartmentMessage("bf");
        String expected = "Crucial Information!";

        assertEquals("Department message getting failed.", expected, result);
    }

    @Test
    public void testGetEmptyDepartmentMessage() throws Exception {
        AbstractSQLManager sqlm = new SSUSQLManager(getConnection(), new H2Queries());
        /*trim to fix the problem with FlatXmlDataSetBuilder() and null columns
        other way is:
        String result = sqlm.getDepartmentMessage("gf").equals("null") ? "" : sqlm.getDepartmentMessage("gf");
        */
        String result = sqlm.getDepartmentMessage("gf").trim();
        String expected = "";

        assertEquals("Department (empty) message getting failed.", expected, result);
    }


   // @Test
    public void testGetGroups() throws Exception {
        AbstractSQLManager sqlm = new SSUSQLManager(getConnection(), new H2Queries());
        List<Group> result = sqlm.getGroups("bf");
        List<Group> expected = Arrays.asList(new Group("111"), new Group("String name"));

        assertTrue("Groups (bf, not gf) getting failed" +
                        "\n  'result'        = " + result +
                        "\n  'expected' = " + expected,
                expected.equals(result)
        );


    }

    @Test
    public void testGetNonEmptyGroups() throws Exception {

    }

    @Test
    public void testGetTT() throws Exception {

    }

    @Test
    public void testGetLessonList() throws Exception {

    }

    @Test
    public void testDeleteDepartment() throws Exception {

    }

    @Test
    public void testDeleteGroupFromDepartment() throws Exception {

    }

    @Test
    public void testDeleteLessons() throws Exception {

    }

    @Test
    public void testDeleteSubGroups() throws Exception {

    }

    @Test
    public void testDeleteLesson() throws Exception {

    }

    @Test
    public void testFlushDatabase() throws Exception {

    }

    private Connection getConnection() {
        JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setURL(JDBCPATH);
        dataSource.setUser(DBLOGIN);
        dataSource.setPassword(DBPASS);
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void cleanlyInsert(IDataSet dataSet) throws Exception {
        IDatabaseTester databaseTester = new JdbcDatabaseTester(JDBCDRIVER, JDBCPATH, DBLOGIN, DBPASS);
        databaseTester.setSetUpOperation(DatabaseOperation.CLEAN_INSERT);
        databaseTester.setDataSet(dataSet);
        //RunScript.execute(JDBCPATH, DBLOGIN, DBPASS, "./src/test/resources/testAdjustment.sql", DBCS, false);
        databaseTester.onSetup();
    }


    private IDataSet readDataSet() throws Exception {
        return new FlatXmlDataSetBuilder().build(new File(DBDATASET));
    }


}
