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
package org.ssutt.core.dm;

import org.ssutt.core.fetch.DataFetcher;
import org.ssutt.core.fetch.SSUDataFetcher;
import org.ssutt.core.sql.SQLManager;

import java.util.Map;

/**
 * Created by fau on 24/03/14.
 */
public class SSUDataManager implements DataManager {
    private static String[] exclusions = {"kgl","cre","el"} ;
    private static  String globalScheduleURL = "http://www.sgu.ru/schedule";


    private static SSUDataManager ssudm;
    private static DataFetcher df;
    private static SQLManager sqlm;
    private SSUDataManager(){
        df = SSUDataFetcher.getInstance(globalScheduleURL, exclusions);
    }

    public static SSUDataManager getInstance(){
        if (ssudm == null) {
            ssudm = new SSUDataManager();
        }
        logger.info("SSU DataManager Instance created.");
        return ssudm;
    }
    @Override
    public String putDepartmentsToDB() {
        Map<String, String> fetchedData = df.getDepartments();

        return null;
    }
}
