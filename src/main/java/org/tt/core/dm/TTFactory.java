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
package org.tt.core.dm;

import org.tt.core.fetch.AbstractDataFetcher;
import org.tt.core.sql.AbstractQueries;
import org.tt.core.sql.AbstractSQLManager;

/**
 * TTFactory is a class, moved, actually from TT Platform, which produces instances of
 * {@link org.tt.core.dm.TTDeliveryManager} and {@link org.tt.core.dm.TTUpdateManager}
 *
 * @author Vlad Slepukhin
 * @since 2.1.0
 */
public class TTFactory {
    /**
     * An instance of {@link org.tt.core.sql.AbstractSQLManager}. Can be both configured with some
     * sort of {@link org.tt.core.sql.AbstractQueries} or not as they are configured as must in
     * the constructor.
     */
    private static AbstractSQLManager sqlm;
    /**
     * An instance of {@link org.tt.core.fetch.AbstractDataFetcher}. Should be already configured with
     * links or credentials.
     *
     * @see org.tt.core.fetch.lexx.LexxDataFetcher
     */
    private static AbstractDataFetcher df;
    /**
     * Supply for singleton. This instance is returned in TT Platform.
     */
    private static TTFactory ttf;

    /**
     * Constructor providing singleton architecture.
     *
     * @since 2.1.0
     */
    private TTFactory() {
    }

    /**
     * Returns a <<b>NOT CONFIGURED</b> instance of this class.
     *
     * @return this class entity.
     * @since 2.1.0
     */
    public static TTFactory getInstance() {
        if (ttf == null) {
            ttf = new TTFactory();
        }

        return ttf;
    }

    /**
     * Configures {@link org.tt.core.sql.AbstractSQLManager} for this factory. {@link org.tt.core.sql.AbstractQueries} are
     * passed in a obligatory way and configured obligatory as well.
     *
     * @param sqlm Instance of {@link org.tt.core.sql.AbstractSQLManager}
     * @param qrs  Instance of {@link org.tt.core.sql.AbstractQueries}
     * @since 2.1.0
     */
    public void setSQLManager(AbstractSQLManager sqlm, AbstractQueries qrs) {
        this.sqlm = sqlm;
        this.sqlm.setQueries(qrs);
    }

    /**
     * Configures {@link org.tt.core.fetch.AbstractDataFetcher} for this factory.
     *
     * @param df Instance of {@link org.tt.core.fetch.AbstractDataFetcher}
     * @since 2.1.0
     */
    public void setDataFetcher(AbstractDataFetcher df) {
        this.df = df;
    }

    /**
     * Creates an instance of {@link org.tt.core.dm.TTUpdateManager} with configured SQL manager
     * and Data fetcher.
     *
     * @return Instance of {@link org.tt.core.dm.TTUpdateManager}
     * @since 2.1.0
     */
    public TTUpdateManager produceUpdateManager() {
        return new TTUpdateManager(sqlm, df);
    }

    /**
     * Creates an instance of {@link org.tt.core.dm.TTDeliveryManager} with configured SQL manager.
     *
     * @return Insatnce of {@link org.tt.core.dm.TTDeliveryManager}
     * @since 2.1.0
     */
    public TTDeliveryManager produceDeliveryManager() {
        return new TTDeliveryManager(sqlm);
    }


}
