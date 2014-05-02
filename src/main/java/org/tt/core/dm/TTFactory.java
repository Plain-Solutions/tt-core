package org.tt.core.dm;

import org.tt.core.fetch.AbstractDataFetcher;
import org.tt.core.sql.AbstractQueries;
import org.tt.core.sql.AbstractSQLManager;

public class TTFactory {
    private AbstractSQLManager sqlm;
    private AbstractQueries qrs;
    private AbstractDataFetcher df;

    public TTFactory() {
    }

    public TTFactory(AbstractSQLManager sqlm, AbstractQueries qrs, AbstractDataFetcher df) {
        this.sqlm = sqlm;
        this.qrs = qrs;
        this.df = df;
    }

    public TTFactory(AbstractSQLManager sqlm, AbstractDataFetcher df) {
        this.sqlm = sqlm;
        this.df = df;
    }

    public void setSQLManager(AbstractSQLManager sqlm) {
        this.sqlm = sqlm;
    }

    public void setSQLQueries(AbstractQueries qrs) {
        this.qrs = qrs;
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
