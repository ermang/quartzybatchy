package com.eg.quartzybatchy.misc;


import com.eg.quartzybatchy.entity.FromTable;
import com.eg.quartzybatchy.entity.ToTable;
import org.quartz.*;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.DuplicateJobException;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.support.ReferenceJobFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManagerFactory;
import java.util.Map;

@Configuration
@EnableBatchProcessing
public class BatchConfig extends DefaultBatchConfigurer { //extends DefaultBatchConfigurer extends SimpleBatchConfiguration

    private final ApplicationContext applicationContext;
    private final EntityManagerFactory entityManagerFactory;
    //private final Scheduler scheduler;
   //private final DataSource dataSource;
    //private final JobRegistry jobRegistry;
//    private final JobBuilderFactory jobBuilders; //spring uses this name, dont change
//    private final StepBuilderFactory stepBuilders; //spring uses this name, dont change
//


    public BatchConfig(ApplicationContext applicationContext, EntityManagerFactory entityManagerFactory) {
        this.applicationContext = applicationContext;
        this.entityManagerFactory = entityManagerFactory;
       // this.scheduler = scheduler;
    }

    @PostConstruct //TODO: JobRegistryBeanPostProcessor should be used instead of this hacky
    public void fillJobRegistry() throws DuplicateJobException, SchedulerException {
        JobRegistry jobRegistry = applicationContext.getBean(JobRegistry.class);

        Map<String, Job> jobs = applicationContext.getBeansOfType(Job.class);
        for (Map.Entry<String, Job> item : jobs.entrySet())
            jobRegistry.register(new ReferenceJobFactory(item.getValue()));

        //scheduler.scheduleJob(jobDetail(), trigger());
    }

    //@Lazy
//    @Bean
//    public JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor(JobRegistry jobRegistry) {
//        JobRegistryBeanPostProcessor postProcessor = new JobRegistryBeanPostProcessor();
//        postProcessor.setJobRegistry(jobRegistry);
//        return postProcessor;
//    }

    @Bean
    public Scheduler scheduler(SchedulerFactoryBean factory)
            throws SchedulerException {
        Scheduler scheduler = factory.getScheduler();
        //scheduler.scheduleJob(jobDetail(), trigger());
        scheduler.start();
        return scheduler;
    }

    @Bean
    public Trigger trigger2() {
        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("trigger2", "defaultGroup")
                .startNow()
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInSeconds(3)
                        .withRepeatCount(4))
                .forJob("jobDetail2")
                .build();

        return trigger;
    }

    @Bean
    public JobDetailFactoryBean jobDetail2() {
        JobDetailFactoryBean jobDetailFactoryBean = new JobDetailFactoryBean();
        jobDetailFactoryBean.setJobClass(SampleJob2.class);
        jobDetailFactoryBean.setDurability(true);
//        JobDetail jobDetail = JobBuilder.newJob(SampleJob.class)
//                .withIdentity("sampleJob", "defaultGroup")
//                .usingJobData("param1", "value1")
//                .storeDurably()
//                .build();

        return jobDetailFactoryBean;
    }


//    @Bean
//    public Trigger trigger() {
//        Trigger trigger = TriggerBuilder.newTrigger()
//                .withIdentity("trigger1", "defaultGroup")
//                .startNow()
//                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
//                        .withIntervalInSeconds(3)
//                        .withRepeatCount(4))
//                .forJob(jobDetail())
//                .build();
//
//        return trigger;
//    }
//
//    @Bean
//    public JobDetail jobDetail() {
//        JobDetail jobDetail = JobBuilder.newJob(SampleJob.class)
//                .withIdentity("sampleJob", "defaultGroup")
//                .usingJobData("param1", "value1")
//                .storeDurably()
//                .build();
//
//        return jobDetail;
//    }



//    @Bean
//    public JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor(JobRegistry jobRegistry) {
//        JobRegistryBeanPostProcessor postProcessor = new JobRegistryBeanPostProcessor();
//        postProcessor.setJobRegistry(jobRegistry);
//        return postProcessor;
//    }


//    @Bean
//    public JobFactory jobFactory(JobRegistry jobRegistry, Job job1, Job job2) throws DuplicateJobException {
////        JobFactory factory = new ReferenceJobFactory(job1);
////        jobRegistry.register(factory);
//        //return factory;
//
//        //factory.
//        JobFactory factory2 = new ReferenceJobFactory(job2);
//        jobRegistry.register(factory2);
//        return  factory2;
//
//
//    }

//    @Override
//    protected JobRepository createJobRepository() throws Exception {
//        JobRepositoryFactoryBean factory = new JobRepositoryFactoryBean();
//        factory.setDataSource(this.dataSource);
//        //factory.setDatabaseType(DatabaseType.POSTGRES.getProductName());
//        factory.setTransactionManager(this.getTransactionManager());
//        factory.setIsolationLevelForCreate("ISOLATION_REPEATABLE_READ");
//        factory.setIncrementerFactory(new DefaultDataFieldMaxValueIncrementerFactory(this.dataSource));
//
//        factory.afterPropertiesSet();
//        return factory.getObject();
//    }

    @Override
    protected JobLauncher createJobLauncher() throws Exception {
        SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
        jobLauncher.setJobRepository(getJobRepository());
        jobLauncher.setTaskExecutor(new SimpleAsyncTaskExecutor());
        jobLauncher.afterPropertiesSet();
        return jobLauncher;
    }

    @Bean
    public Job job1(JobBuilderFactory jobBuilders, @Qualifier("step1") Step step1) {
        return jobBuilders.get("job1")
                .start(step1)
                .build();
    }

    @Bean
    protected Step step1(StepBuilderFactory stepBuilders) {
        return stepBuilders.get("step1")
                .tasklet(tasklet1())
                .build();
    }

    @Bean
    public Tasklet tasklet1() {
        Tasklet1 tasklet1 = new Tasklet1();

        return tasklet1;
    }

    @Bean
    public Job job2(JobBuilderFactory jobBuilders, Step step2) {
        return jobBuilders.get("job2")
                .start(step2)
                .build();
    }


    @Override
    public PlatformTransactionManager getTransactionManager() {
        //TransactionManager t = applicationContext.getBean(TransactionManager.class);
        //PlatformTransactionManager pt = new
        JpaTransactionManager tx = new JpaTransactionManager(entityManagerFactory);
        return tx;

        //return (PlatformTransactionManager)t;
    }

//    @Bean
//    public PlatformTransactionManager platformTransactionManager() {
//        JpaTransactionManager tx = new JpaTransactionManager(entityManagerFactory);
//        return tx;
//    }

    @Bean
    public Step step2(StepBuilderFactory stepBuilders) {
        return stepBuilders.get("step2")
                //.transactionManager(this.getTransactionManager())
                .<String, String>chunk(2)
                .reader(itemReader())
                .processor(processor())
                .writer(itemWriter())
                .build();
    }

    @Bean
    public JpaPagingItemReader itemReader() {
        return new JpaPagingItemReaderBuilder<FromTable>()
                .name("creditReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString("select f from FromTable f")
                .pageSize(2)
                .build();
    }

    @Bean
    public FromToProcessor processor() {
        return new FromToProcessor();
    }

    @Bean
    public JpaItemWriter itemWriter() {
        return new JpaItemWriterBuilder<ToTable>()
                .entityManagerFactory(entityManagerFactory)
                //.usePersist()
                //.
                .build();
//                .b
//                .name("creditReader")
//                .entityManagerFactory(entityManagerFactory)
//                .queryString("select f from FromTable f")
//                .pageSize(1000)
//                .build();
    }

    //------------------------------------------------

}
