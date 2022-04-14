package com.alibaba.chaosblade.box.dao.scheduler.quartz;

import com.alibaba.chaosblade.box.dao.model.SchedulerJobDO;
import com.alibaba.chaosblade.box.dao.repository.SchedulerJobRepository;
import com.alibaba.chaosblade.box.dao.scheduler.BaseSchedulerService;
import com.alibaba.chaosblade.box.dao.scheduler.SchedulerJob;
import com.alibaba.chaosblade.box.dao.scheduler.SchedulerJobService;
import com.google.common.base.Strings;
import org.apache.commons.lang3.RandomUtils;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;

import java.text.ParseException;
import java.util.*;

import static com.alibaba.chaosblade.box.dao.scheduler.SchedulerConstant.SCHEDULER_PARAM_KET_JOB_ID;

/**
 * @author haibin
 *
 *
 */
public class QuartzSchedulerService extends BaseSchedulerService implements ApplicationListener<ContextRefreshedEvent>,
    ApplicationContextAware {

    private static Logger log = LoggerFactory.getLogger(SchedulerJobService.class);

    @Autowired
    private SchedulerJobRepository schedulerJobRepository;

    @Autowired
    private Scheduler scheduler;

    private ApplicationContext applicationContext;

    private Set<String> registeredCache = new HashSet<>();

    @Override
    protected void internalAddSchedulerJob(SchedulerJobDO schedulerJob) throws SchedulerException {
        TriggerKey triggerKey = new TriggerKey(schedulerJob.getJobId() + "_trigger");
        boolean exist = scheduler.checkExists(triggerKey);
        if (exist) { return; }
        JobDataMap jobDataMap = new JobDataMap();
        fillJobDataMap(jobDataMap, schedulerJob);
        addSchedulerJob(loadClassFromSpring(schedulerJob), schedulerJob.getJobId(),
            applicationContext, schedulerJob.getCronExpression(), schedulerJob.getStartTime(), 10, jobDataMap);
    }

    private void fillJobDataMap(JobDataMap jobDataMap, SchedulerJobDO schedulerJobDO) {
        jobDataMap.putIfAbsent(SCHEDULER_PARAM_KET_JOB_ID, schedulerJobDO.getJobId());
    }

    @Override
    protected void internalDeleteSchedulerJob(SchedulerJobDO schedulerJob) throws SchedulerException {
        TriggerKey triggerKey = new TriggerKey(schedulerJob.getJobId() + "_trigger");
        boolean exist = scheduler.checkExists(triggerKey);
        if (exist) {
            Trigger trigger = scheduler.getTrigger(triggerKey);
            scheduler.pauseTrigger(triggerKey);
            scheduler.deleteJob(trigger.getJobKey());
        }
    }

    @Override
    protected void rescheduleCronJob(SchedulerJobDO schedulerJobDO) throws Exception {
        TriggerKey triggerKey = new TriggerKey(getTriggerName(schedulerJobDO.getJobId()));
        boolean exist = scheduler.checkExists(triggerKey);
        if (exist) {
            Trigger trigger = scheduler.getTrigger(triggerKey);
            scheduler.deleteJob(trigger.getJobKey());
            scheduler.pauseTrigger(triggerKey);
            JobDataMap jobDataMap = new JobDataMap();
            fillJobDataMap(jobDataMap, schedulerJobDO);
            JobDetail jobDetail = initJobDetail(loadClassFromSpring(schedulerJobDO), schedulerJobDO.getName(),
                applicationContext, jobDataMap);
            trigger = initTrigger(jobDetail, schedulerJobDO.getName(), schedulerJobDO.getCronExpression(),
                schedulerJobDO.getStartTime(), 2000);
            scheduler.scheduleJob(jobDetail, trigger);
        } else {
            internalAddSchedulerJob(schedulerJobDO);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        applicationContext = event.getApplicationContext();
        initRamSchedulerJob(applicationContext);
        initPersistenceSchedulerJob(applicationContext);
    }

    private Class loadClassFromSpring(SchedulerJobDO schedulerJobDO) {
        try {
            return applicationContext.getBean(
                Thread.currentThread().getContextClassLoader().loadClass(schedulerJobDO.getSchedulerBeanClass()))
                .getClass();
        } catch (Exception no) {
            return ChaosQuartzClassLoadHelper.getJobClassName(schedulerJobDO.getSchedulerBeanClass());
        }
    }

    private String getTriggerName(String jobName) {
        return jobName + "_trigger";
    }

    private void initPersistenceSchedulerJob(ApplicationContext applicationContext) {
        List<SchedulerJobDO> schedulerJobDOS = schedulerJobRepository.findEnabledSchedulerJob();
        for (SchedulerJobDO schedulerJob : schedulerJobDOS) {
            Class clazz = loadClassFromSpring(schedulerJob);
            if (clazz != null) {
                if (registeredCache.contains(schedulerJob.getJobId())) { continue; }
                registeredCache.add(schedulerJob.getJobId());
                JobDataMap jobDataMap = new JobDataMap();
                fillJobDataMap(jobDataMap, schedulerJob);
                try {
                    addSchedulerJob(clazz,
                        schedulerJob.getJobId(),
                        applicationContext, schedulerJob.getCronExpression(), schedulerJob.getStartTime(), 0,
                        jobDataMap);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private void initRamSchedulerJob(ApplicationContext applicationContext) {
        Map<String, Object> beans = applicationContext.getBeansWithAnnotation(SchedulerJob.class);
        for (Map.Entry<String, Object> beanMap : beans.entrySet()) {
            if (registeredCache.contains(beanMap.getKey())) { continue; }
            registeredCache.add(beanMap.getKey());
            Class beanClass = beanMap.getValue().getClass();
            SchedulerJob schedulerJob = AnnotationUtils.findAnnotation(beanClass,
                SchedulerJob.class);
            long startDelay = schedulerJob.randomStartDelay() ? RandomUtils.nextLong(1000, 4000) : 0L;
            addSchedulerJob(beanClass, schedulerJob.name(),
                applicationContext, schedulerJob.cronExpression(), null, startDelay, null);
        }
    }

    private void addSchedulerJob(Class beanClass, String name, ApplicationContext applicationContext,
        String cronExpression, Date startTime, long startDelay, JobDataMap jobDataMap) {
        if (Strings.isNullOrEmpty(cronExpression)) { return; }
        try {
            JobDetail jobDetail = initJobDetail(beanClass, name, applicationContext, jobDataMap);
            Trigger trigger = initTrigger(jobDetail, name, cronExpression, startTime, startDelay);
            scheduler.scheduleJob(jobDetail, trigger);
            log.info("add scheduler job success,job class:{},name:{}", beanClass.getName(),
                name);
        } catch (Exception ex) {
            if (ex instanceof ObjectAlreadyExistsException) {
                log.error(
                    "init scheduler job failed,beanClass:" + beanClass.getName() + ",name:" + name + ",exist job");
            } else {
                log.error(
                    "init scheduler job failed,beanClass:" + beanClass.getName() + ",name:" + name, ex);
            }
        }
    }

    private JobDetail initJobDetail(Class beanClass, String name, ApplicationContext applicationContext,
        JobDataMap jobDataMap) {
        JobDetailFactoryBean jobDetailFactoryBean = QuartzConfig.createJobDetail(beanClass,
            name, "DEFAULT");
        jobDetailFactoryBean.setApplicationContext(applicationContext);
        if (jobDataMap != null) {
            jobDetailFactoryBean.setJobDataMap(jobDataMap);
        }
        jobDetailFactoryBean.afterPropertiesSet();
        return jobDetailFactoryBean.getObject();
    }

    private Trigger initTrigger(JobDetail jobDetail, String name, String cronExpression, Date startTime,
        long startDelay) throws ParseException {
        Trigger trigger = null;
        if (cronExpression != null) {
            CronTriggerFactoryBean cronTriggerFactoryBean = QuartzConfig.createCronTrigger(jobDetail, startTime,
                cronExpression, name + "_trigger");
            cronTriggerFactoryBean.setStartDelay(startDelay);
            cronTriggerFactoryBean.afterPropertiesSet();
            trigger = cronTriggerFactoryBean.getObject();
        } else {
            SimpleTriggerFactoryBean simpleTriggerFactoryBean = QuartzConfig.createSimpleTrigger(jobDetail,
                startTime,
                0, name + "_trigger");
            simpleTriggerFactoryBean.afterPropertiesSet();
            trigger = simpleTriggerFactoryBean.getObject();
        }
        return trigger;
    }

}
