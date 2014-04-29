package org.tt.core.timer;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;

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

public class TimerMain {
    private Scheduler scheduler;

    public TimerMain() throws SchedulerException {
        this.scheduler = StdSchedulerFactory.getDefaultScheduler();
        init();
    }

    private void init() throws SchedulerException {
        JobUpdate jobUpdate = new JobUpdate();
        scheduler.scheduleJob(jobUpdate.getJobDetail(), jobUpdate.getTrigger());

        JobDrop jobDrop = new JobDrop();
        scheduler.scheduleJob(jobDrop.getJobDetail(), jobDrop.getTrigger());
    }

    public void start() throws SchedulerException {
        scheduler.start();
    }
}
