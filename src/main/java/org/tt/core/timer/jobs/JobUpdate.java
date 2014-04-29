package org.tt.core.timer.jobs;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.tt.core.timer.AbstractJob;

import java.util.TimeZone;

import static org.quartz.CronScheduleBuilder.dailyAtHourAndMinute;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * Copyright 2014 Plain Solutions
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
 * Author: Avetisyan Sevak
 * Date: 29.04.14.
 */
public class JobUpdate extends AbstractJob {
    public JobUpdate() {
        setJobDetail(newJob(getClass())
                .withIdentity(getClass().getName())
                .build());

        setTrigger(newTrigger()
                .withIdentity(getClass().getName())
                .startNow()
                .withSchedule(
                        dailyAtHourAndMinute(0, 0)
                        .inTimeZone(TimeZone.getTimeZone("UTC+4")))
                .build());
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println("UPDATING");
    }
}
