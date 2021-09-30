package com.eg.quartzybatchy.misc;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

public class Tasklet1 implements Tasklet {

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
        System.out.println("tasklet1 started");
        Thread.sleep(5000);
        System.out.println("tasklet1 finished");

        return RepeatStatus.FINISHED;
    }
}
