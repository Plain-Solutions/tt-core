/**
 * Copyright 2014 Plain Solutions
 * <p/>
 * Authors:
 * Vlad Slepukhin <slp.vld@gmail.com>
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.ssutt.core.testing;

import junit.framework.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.ssutt.core.dm.AbstractDataManager;
import org.ssutt.core.dm.SSUDataManager;
import org.ssutt.core.dm.TTData;
import org.ssutt.core.dm.convert.json.JSONConverter;
import org.ssutt.core.fetch.SSUDataFetcher;
import org.ssutt.core.sql.H2Queries;
import org.ssutt.core.sql.SSUSQLManager;
import org.ssutt.core.sql.ex.NoSuchDepartmentException;
import org.ssutt.core.sql.ex.NoSuchGroupException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestDM {

    @Test
    public void aObjectCreation() throws SQLException, ClassNotFoundException {
        AbstractDataManager dm = null;
        dm = new SSUDataManager();

        Assert.assertNotNull("DataManager(SSU) init - failed",dm);
    }

    @Test
    public void bTestProviders(){
        AbstractDataManager dm = null;

        try {
            dm = new SSUDataManager(new SSUSQLManager(createConnection()), new H2Queries(), new SSUDataFetcher(),
                    new JSONConverter());
            assert(true);
        } catch (SQLException e) {
            e.printStackTrace();
            assert(false);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            assert(false);
        }
    }

    @Test
    public void cTestDepartments(){
        AbstractDataManager dm = null;
        try {
            dm = new SSUDataManager(new SSUSQLManager(createConnection()), new H2Queries(), new SSUDataFetcher()
                    , new JSONConverter());
            assert(true);
        } catch (SQLException e) {
            e.printStackTrace();
            assert(false);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            assert(false);
        }

        try {
            dm.putDepartments();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        TTData result = dm.getDepartments();

        System.out.println(result.getMessage());

        Assert.assertEquals(200, result.getHttpCode());
    }


    @Test
    public void dTestGroups() throws NoSuchDepartmentException, SQLException, IOException, NoSuchGroupException {
        AbstractDataManager dm = null;
        try {
            dm = new SSUDataManager(new SSUSQLManager(createConnection()), new H2Queries(), new SSUDataFetcher(),
                    new JSONConverter());
            assert (true);
        } catch (SQLException e) {
            e.printStackTrace();
            assert (false);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            assert (false);
        }

        dm.putAllGroups();
        System.out.println(dm.getGroups("knt").getMessage());
        dm.putTT("knt", Integer.parseInt(dm.getGroupID("knt", "151").getMessage()));

    }

    @Test
    public void eTestTTs() {
        AbstractDataManager dm = null;
        try {
            dm = new SSUDataManager(new SSUSQLManager(createConnection()), new H2Queries(), new SSUDataFetcher(),
                    new JSONConverter());
            assert (true);
        } catch (SQLException e) {
            e.printStackTrace();
            assert (false);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            assert (false);
        }
        System.out.println(dm.getTT(Integer.parseInt(dm.getGroupID("knt","151").getMessage())).getMessage());

        removeTestDB();
    }

    private Connection createConnection() throws SQLException, ClassNotFoundException {
        Class.forName("org.h2.Driver");
        return DriverManager.
                getConnection("jdbc:h2:file:localtest;INIT=RUNSCRIPT FROM './src/main/resources/initTT.sql'", "sa", "");
    }

    private void removeTestDB() {
        try {
            Files.delete(Paths.get("localtest.h2.db"));
            Files.delete(Paths.get("localtest.trace.db"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
