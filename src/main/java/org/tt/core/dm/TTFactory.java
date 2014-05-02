package org.tt.core.dm;

import org.tt.core.fetch.AbstractDataFetcher;
import org.tt.core.sql.AbstractQueries;
import org.tt.core.sql.AbstractSQLManager;

public class TTFactory {
    private static AbstractSQLManager sqlm;
    private static AbstractDataFetcher df;
    private static TTFactory ttf;

    private TTFactory() {
    }

    public static TTFactory getInstance() {
        if (ttf == null) {
            ttf = new TTFactory();
        }

        return ttf;
    }

    public void setSQLManager(AbstractSQLManager sqlm, AbstractQueries qrs) {
        this.sqlm = sqlm;
        this.sqlm.setQueries(qrs);
    }

    public void setDataFetcher(AbstractDataFetcher df) {
        this.df = df;
    }

    public TTUpdateManager produceUpdateManager() {
        return new TTUpdateManager(sqlm, df);
    }

    public TTDeliveryManager produceDeliveryManager() {
        return new TTDeliveryManager(sqlm);
    }


}
