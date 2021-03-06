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

import org.quartz.*;
import org.tt.core.dm.TTUpdateManager;

import java.util.TimeZone;

/**
 * AbstractJob is an abstraction which contains main code of timer jobs
 *
 * @see org.quartz.Job
 * @author Avetisyan Sevak
 * @since 2.0.0
 */
public abstract class AbstractJob implements Job {
    public static final TimeZone TIME_ZONE = TimeZone.getTimeZone("UTC+4");
    private JobDetail jobDetail;
    private Trigger trigger;

    private static TTUpdateManager updm;

    public Trigger getTrigger() {
        return trigger;
    }

    public JobDetail getJobDetail() {
        return jobDetail;
    }

    public void setJobDetail(JobDetail jobDetail) {
        this.jobDetail = jobDetail;
    }

    public void setTrigger(Trigger trigger) {
        this.trigger = trigger;
    }

    public static void setUpdateManager(TTUpdateManager updm) {
        AbstractJob.updm = updm;
    }

    public static TTUpdateManager getUpdm() {
        return updm;
    }
}
