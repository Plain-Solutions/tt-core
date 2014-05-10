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
package org.tt.core.timer.jobs;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.tt.core.dm.TTUpdateManager;
import org.tt.core.timer.AbstractJob;

import java.sql.SQLException;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * JobDrop is a class which extends AbstractJob class and implements a logic of dropping database
 *
 * @see org.tt.core.timer.AbstractJob
 * @author Avetisyan Sevak
 * @since 2.0.0
 */
public class JobDrop extends AbstractJob {
    public JobDrop() {
        setJobDetail(newJob(getClass())
                .withIdentity(getClass().getName())
                .build());

        setTrigger(newTrigger()
                .withIdentity(getClass().getName())
                .startNow()
                .withSchedule(
                        cronSchedule("0 0 0 1 1,8 ?")
                        .inTimeZone(TIME_ZONE))
                .build());
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println("DROP-DROP-KLADBISHE!");
        TTUpdateManager updm = getUpdm();
        try {
            updm.flushDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
