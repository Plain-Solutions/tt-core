package org.tt.core.timer;

import org.quartz.*;

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
public abstract class AbstractJob implements Job {
    private JobDetail jobDetail;
    private Trigger trigger;

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
}
