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
        return ssudm;
    }
    @Override
    public String putDepartmentsToDB() {
        Map<String, String> fetchedData = df.getDepartments();

        return null;
    }
}
