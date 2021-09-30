package com.eg.quartzybatchy.misc;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class SampleJob implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobDataMap map = jobExecutionContext.getMergedJobDataMap();
        String param1 = map.getString("param1");
        System.out.println("SampleJob instance started");
        System.out.println("param1=" + param1);
        System.out.println("SampleJob instance stopped");
    }
}
