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
package org.ssutt.core.dm;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.ssutt.core.fetch.DataFetcher;
import org.ssutt.core.fetch.SSUDataFetcher;
import org.ssutt.core.sql.SQLManager;
import org.ssutt.core.sql.SSUSQLManager;

import java.sql.Connection;
import java.util.Map;

public class SSUDataManager implements DataManager {
    private static final Logger logger = LogManager.getLogger(SSUDataManager.class.getName());

    private static String[] exclusions = {"kgl","cre","el"} ;
    private static  String globalScheduleURL = "http://www.sgu.ru/schedule";

    private static DataFetcher df;
    private static SQLManager sqlm;

    public SSUDataManager() {
         logger.info("DataManager (SSU) was created");
    }

    @Override
    public void deliverDBProvider(Connection conn) {
            sqlm = new SSUSQLManager(conn);
        logger.info("DataManager: DBManager: SSUSQLManager");
    }

    @Override
    public void deliverDataFetcherProvider() {
        df = new SSUDataFetcher(globalScheduleURL, exclusions);
        logger.info("DataManager: DataFetcher: SSUDataFetcher");
    }

    @Override
    public String putDepartments() {
        Map<String, String> res = df.getDepartments();

        return null;
    }
}
