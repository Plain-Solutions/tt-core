package org.tt.core.dm;

import org.tt.core.fetch.AbstractDataFetcher;
import org.tt.core.sql.AbstractSQLManager;

/**
 * Created by fau on 27/04/14.
 */
public class UpdateManager extends SSUDataManager {
    private static AbstractSQLManager usqlm;
    private static AbstractDataFetcher udf;
    public UpdateManager() {
    }

    public UpdateManager(AbstractSQLManager usqlm, AbstractDataFetcher udf) {
        this.usqlm = usqlm;
        this.udf = udf;
    }


}
