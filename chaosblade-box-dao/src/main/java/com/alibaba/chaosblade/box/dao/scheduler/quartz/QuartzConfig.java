package com.alibaba.chaosblade.box.dao.scheduler.quartz;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.simpl.SimpleThreadPool;
import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.scheduling.quartz.*;

import javax.sql.DataSource;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * @author haibin
 *
 *
 */
@Configuration
@Slf4j
@ConditionalOnProperty(name = "scheduler.engine", havingValue = "quartz")
public class QuartzConfig {

    private ApplicationContext applicationContext;

    @Autowired
    private List<TriggerListener> triggerListenerList;

    public QuartzConfig(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Bean
    public SpringBeanJobFactory springBeanJobFactory() {
        AutowiringSpringBeanJobFactory autowiringSpringBeanJobFactory = new AutowiringSpringBeanJobFactory();
        autowiringSpringBeanJobFactory.setApplicationContext(applicationContext);
        return autowiringSpringBeanJobFactory;
    }

    @Bean
    public QuartzSchedulerService quartzSchedulerService() {
        return new QuartzSchedulerService();
    }

    @Autowired
    private DataSource dataSource;

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean() {
        SchedulerFactoryBean schedulerFactory = new ChaosSchedulerFactoryBean();
        schedulerFactory.setConfigLocation(new ClassPathResource("quartz.properties"));
        schedulerFactory.setAutoStartup(true);
        schedulerFactory.setJobFactory(springBeanJobFactory());
        schedulerFactory.setWaitForJobsToCompleteOnShutdown(true);
        schedulerFactory.setSchedulerName("ChaosClusteredScheduler");
        schedulerFactory.setDataSource(dataSource);
        schedulerFactory.setGlobalTriggerListeners(triggerListenerList.toArray(new TriggerListener[] {}));
        return schedulerFactory;
    }

    private static class ChaosSchedulerFactoryBean extends SchedulerFactoryBean {
        @SneakyThrows
        @Override
        protected Scheduler createScheduler(SchedulerFactory schedulerFactory, String schedulerName)
            throws SchedulerException {
            Properties mergedProps = new Properties();
            if (this.resourceLoader != null) {
                mergedProps.setProperty(StdSchedulerFactory.PROP_SCHED_CLASS_LOAD_HELPER_CLASS,
                    ResourceLoaderClassLoadHelper.class.getName());
            } else {
                // Set necessary default properties here, as Quartz will not apply
                // its default configuration when explicitly given properties.
                mergedProps.setProperty(StdSchedulerFactory.PROP_THREAD_POOL_CLASS, SimpleThreadPool.class.getName());
                mergedProps.setProperty(PROP_THREAD_COUNT, Integer.toString(DEFAULT_THREAD_COUNT));
            }
            if (logger.isInfoEnabled()) {
                logger.info("Loading Quartz config from [" + "quartz.properties" + "]");
            }
            PropertiesLoaderUtils.fillProperties(mergedProps, new ClassPathResource("quartz.properties"));
            mergedProps.put(StdSchedulerFactory.PROP_JOB_STORE_CLASS, ChaosJobStoreTX.class.getName());
            mergedProps.put(StdSchedulerFactory.PROP_SCHED_INSTANCE_NAME, "ChaosClusteredScheduler");
            ((StdSchedulerFactory)schedulerFactory).initialize(mergedProps);
            return super.createScheduler(schedulerFactory, schedulerName);
        }
    }

    static CronTriggerFactoryBean createCronTrigger(JobDetail jobDetail, Date startTime, String cronExpression,
                                                    String triggerName) {
        log.info("createCronTrigger(jobDetail={}, cronExpression={}, triggerName={})", jobDetail.toString(),
            cronExpression, triggerName);
        CronTriggerFactoryBean factoryBean = new CronTriggerFactoryBean();
        factoryBean.setJobDetail(jobDetail);
        factoryBean.setCronExpression(cronExpression);
        factoryBean.setStartTime(new Date());
        factoryBean.setStartDelay(2000);
        factoryBean.setName(triggerName);
        if (startTime != null) {
            factoryBean.setStartTime(startTime);
        }
        factoryBean.setMisfireInstruction(CronTrigger.MISFIRE_INSTRUCTION_DO_NOTHING);
        return factoryBean;
    }

    static SimpleTriggerFactoryBean createSimpleTrigger(JobDetail jobDetail, Date startTime, int repeatCount,
                                                        String triggerName) {
        log.info("createSimpleTrigger(jobDetail={}, startTime={},repeatCount={},triggerName={})", jobDetail.toString(),
            startTime, repeatCount, triggerName);
        SimpleTriggerFactoryBean factoryBean = new SimpleTriggerFactoryBean();
        factoryBean.setJobDetail(jobDetail);
        factoryBean.setStartTime(startTime);
        if (repeatCount > 0) {
            factoryBean.setRepeatCount(repeatCount);
        }
        factoryBean.setName(triggerName);
        factoryBean.setMisfireInstruction(SimpleTrigger.MISFIRE_INSTRUCTION_RESCHEDULE_NEXT_WITH_REMAINING_COUNT);
        return factoryBean;
    }

    static JobDetailFactoryBean createJobDetail(Class jobClass, String jobName, String group) {
        log.debug("createJobDetail(jobClass={}, jobName={})", jobClass.getName(), jobName);
        JobDetailFactoryBean factoryBean = new JobDetailFactoryBean();
        factoryBean.setName(jobName);
        factoryBean.setGroup(group);
        factoryBean.setDurability(true);
        factoryBean.setJobClass(jobClass);
        factoryBean.setDurability(true);
        return factoryBean;
    }

    public final class AutowiringSpringBeanJobFactory extends SpringBeanJobFactory implements ApplicationContextAware {
        private transient AutowireCapableBeanFactory beanFactory;

        @Override
        public void setApplicationContext(final ApplicationContext context) {
            beanFactory = context.getAutowireCapableBeanFactory();
        }

        @Override
        protected Object createJobInstance(final TriggerFiredBundle bundle) throws Exception {
            final Object job = super.createJobInstance(bundle);
            beanFactory.autowireBean(job);
            return job;
        }
    }

}
