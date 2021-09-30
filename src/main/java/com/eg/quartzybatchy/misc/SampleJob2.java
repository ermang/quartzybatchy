package com.eg.quartzybatchy.misc;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

@Component
public class SampleJob2 implements Job {

    private final SampleService sampleService;

    public SampleJob2(SampleService sampleService) {
        this.sampleService = sampleService;
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobDataMap map = jobExecutionContext.getMergedJobDataMap();
        String param1 = map.getString("param1");
        System.out.println("SampleJob instance started");
        System.out.println("param1=" + param1);
        System.out.println("SampleJob instance stopped");
    }
}
