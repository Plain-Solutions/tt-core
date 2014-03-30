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
import org.ssutt.core.dm.SSUDataManager;
import org.ssutt.core.dm.TTDataManager;
import org.ssutt.core.fetch.SSUDataFetcher;
import org.ssutt.core.sql.H2Queries;
import org.ssutt.core.sql.SSUSQLManager;
import org.ssutt.core.sql.ex.EmptyTableException;
import org.ssutt.core.sql.ex.NoSuchDepartmentException;
import org.ssutt.core.sql.ex.NoSuchGroupException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestDM {

    @Test
    public void aObjectCreation(){
        TTDataManager dm = null;
        try {
            dm = createInstance();

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        Assert.assertNotNull("DataManager(SSU) init - failed",dm);
    }

    @Test
    public void bTestProviders(){
        TTDataManager dm = null;

        try {
            dm = createInstance();
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
        TTDataManager dm = null;
        try {
            dm = createInstance();
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

        Map<String, String> result = null;
        try {
            result = dm.getDepartments();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Assert.assertNotNull(result);
    }


    @Test
    public void dTestGroupsAndTTs() {
        TTDataManager dm = null;
        try {
            dm = createInstance();
            assert (true);
        } catch (SQLException e) {
            e.printStackTrace();
            assert (false);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            assert (false);
        }

        try {
            dm.putAllGroups();
            dm.putTT("knt", dm.getGroupID("knt", "151"));
            dm.getTT(dm.getGroupID("knt", "151"));
            assert(true);
        } catch (SQLException e) {
            e.printStackTrace();
            assert (false);
        } catch (NoSuchDepartmentException e) {
            e.printStackTrace();
            assert (false);
        } catch (NoSuchGroupException e) {
            e.printStackTrace();
            assert (false);
       } catch (IOException e) {
            e.printStackTrace();
            assert (false);
        } catch (EmptyTableException e) {
            e.printStackTrace();
        }

        removeTestDB();
    }

    private TTDataManager createInstance() throws SQLException, ClassNotFoundException {
        TTDataManager dm = new SSUDataManager();

        dm.deliverDataFetcherProvider(new SSUDataFetcher());

        dm.deliverDBProvider(new SSUSQLManager(createConnection()), new H2Queries());
        return dm;
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
