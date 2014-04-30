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
package org.tt.core.timer;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;

/**
 * TTTimer is a class which doing some the work on a schedule
 *
 * @author Avetisyan Sevak
 * @see org.tt.core.timer.jobs.JobDrop
 * @see org.tt.core.timer.jobs.JobUpdate
 * @see org.tt.core.timer.AbstractJob
 * @since 2.0.0
 */

public class TTTimer {
    private static TTTimer tm;
    private Scheduler scheduler;
    private AbstractJob[] jobs;
    private boolean hasStarted;

    /**
     * Constructor of TTTimer class, where initializing class-fields
     *
     * @param jobs list of jobs
     * @throws SchedulerException
     */
    private TTTimer(AbstractJob... jobs) throws SchedulerException {
        this.scheduler = StdSchedulerFactory.getDefaultScheduler();
        this.jobs = jobs;
        hasStarted = false;
    }

    public static TTTimer getInstance(AbstractJob... jobs) throws SchedulerException {
        if (tm == null) {
            tm = new TTTimer(jobs);
        }

        return tm;
    }

    /**
     * This method starts scheduler and add all jobs to schedule
     *
     * @throws SchedulerException
     */
    public void start() throws SchedulerException {
        if (!hasStarted) {
            hasStarted = true;
            scheduler.start();
            for (AbstractJob job : jobs) {
                scheduler.scheduleJob(job.getJobDetail(), job.getTrigger());
            }
        }
    }
}
