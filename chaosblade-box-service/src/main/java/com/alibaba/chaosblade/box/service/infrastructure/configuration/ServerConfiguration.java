package com.alibaba.chaosblade.box.service.infrastructure.configuration;

import com.alibaba.chaosblade.box.common.commands.CommandExecutorPoolSpringFactory;
import com.alibaba.chaosblade.box.common.common.domain.user.ChaosUser;
import com.alibaba.chaosblade.box.common.infrastructure.util.concurrent.ThreadPool;
import com.alibaba.chaosblade.box.common.service.ExperimentSearchClient;
import com.alibaba.chaosblade.box.dao.infrastructure.app.function.UserSceneFunctionFilter;
import com.alibaba.chaosblade.box.dao.infrastructure.app.function.sync.DefaultSceneFunctionSwitch;
import com.alibaba.chaosblade.box.dao.infrastructure.app.function.sync.SceneFunctionSwitch;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.guard.DefaultExperimentGuardHostsProvider;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.guard.ExperimentGuardHostsProvider;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.ActivityTaskAsyncSupport;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.DefaultExperimentTaskHostsRecorder;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.ExperimentTaskHostRecorder;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.FlowEngineFactoryBean;
import com.alibaba.chaosblade.box.dao.infrastructure.monitor.miniapp.EmptyMetricServiceImpl;
import com.alibaba.chaosblade.box.dao.infrastructure.service.DbExperimentSearchClient;
import com.alibaba.chaosblade.box.dao.infrastructure.service.MetricService;
import com.alibaba.chaosblade.box.dao.model.ActivityTaskDO;
import com.alibaba.chaosblade.box.dao.model.SceneFunctionDO;
import com.alibaba.chaosblade.box.service.UserService;
import com.alibaba.chaosblade.box.service.auth.perimission.UserPermissionService;
import com.alibaba.chaosblade.box.service.auth.perimission.UserPermissionServiceImpl;
import com.alibaba.chaosblade.box.service.command.guard.ExperimentGuardMetricLoadCommand;
import com.alibaba.chaosblade.box.service.command.guard.ExperimentGuardRecoveryLoadCommand;
import net.javacrumbs.shedlock.core.DefaultLockingTaskExecutor;
import net.javacrumbs.shedlock.core.LockProvider;
import net.javacrumbs.shedlock.core.LockingTaskExecutor;
import net.javacrumbs.shedlock.provider.jdbctemplate.JdbcTemplateLockProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.TimeZone;

/**
 * @author haibin
 *
 *
 */
@Configuration
@ComponentScan(basePackageClasses = ServerConfiguration.class)
@EnableCaching
@EnableScheduling
@EnableAsync
public class ServerConfiguration {

    @Bean
    public CommandExecutorPoolSpringFactory commandExecutorPoolSpringFactory() {
        return new CommandExecutorPoolSpringFactory();
    }

    @Bean(destroyMethod = "shutDown")
    public ThreadPool threadPool() {
        return new ThreadPool();
    }

    @Bean
    @ConditionalOnMissingBean(value = ExperimentSearchClient.class)
    ExperimentSearchClient experimentSearchClient() {
        return new DbExperimentSearchClient();
    }

    @Bean
    @ConditionalOnMissingBean(value = SceneFunctionSwitch.class)
    SceneFunctionSwitch sceneFunctionSwitch() {
        return new DefaultSceneFunctionSwitch();
    }

    @Bean
    @ConditionalOnMissingBean(value = MetricService.class)
    MetricService metricService() {
        return new EmptyMetricServiceImpl();
    }

    @Bean
    @ConditionalOnMissingBean(value = UserPermissionService.class)
    UserPermissionService chaosUserPermissionService() {
        return new UserPermissionServiceImpl();
    }

    @Bean
    @ConditionalOnMissingBean(value = ExperimentTaskHostRecorder.class)
    ExperimentTaskHostRecorder experimentTaskHostRecorder() {
        return new DefaultExperimentTaskHostsRecorder();
    }

    @Bean
    @ConditionalOnMissingBean(value = UserSceneFunctionFilter.class)
    UserSceneFunctionFilter userSceneFunctionFilter() {
        return new UserSceneFunctionFilter() {
            @Override
            public boolean filter(ChaosUser chaosUser, SceneFunctionDO sceneFunctionDO) {
                return true;
            }

        };
    }

    @Bean
    @ConditionalOnMissingBean(value = ActivityTaskAsyncSupport.class)
    ActivityTaskAsyncSupport activityTaskAsyncSupport() {
        return new
            ActivityTaskAsyncSupport() {
                @Override
                public boolean isAsync(ActivityTaskDO activityTaskDO) {
                    return false;
                }
            };
    }

    @Bean
    @ConditionalOnMissingBean(value = UserService.class)
    UserService userService() {
        return new UserService() {
            @Override
            public ChaosUser getUserByUserId(String userId) {
                return null;
            }

            @Override
            public boolean userNameExist(String userName) {
                return false;
            }

            @Override
            public ChaosUser saveUser(String userName, String password) {
                return null;
            }

            @Override
            public ChaosUser login(String userName, String password) {
                return null;
            }

            @Override
            public void updateLastLoginTime(Long id) {

            }

        };
    }

    @ConditionalOnMissingBean
    @Bean
    FlowEngineFactoryBean flowEngineFactoryBean() {
        return new FlowEngineFactoryBean();
    }

    @Bean
    @ConditionalOnMissingBean(value = ExperimentGuardRecoveryLoadCommand.class)
    ExperimentGuardRecoveryLoadCommand experimentGuardMonitorMetricResultLoadCommand() {
        return new ExperimentGuardRecoveryLoadCommand();
    }

    @Bean
    @ConditionalOnMissingBean(value = ExperimentGuardHostsProvider.class)
    ExperimentGuardHostsProvider experimentGuardHostsProvider() {
        return new DefaultExperimentGuardHostsProvider();
    }

    @Bean
    @ConditionalOnMissingBean(value = ExperimentGuardMetricLoadCommand.class)
    ExperimentGuardMetricLoadCommand experimentGuardMonitorStrategyRecoveryLoadCommand() {
        return new ExperimentGuardMetricLoadCommand();
    }

    @Autowired
    private PlatformTransactionManager txManager;

    @Bean
    public LockProvider lockProvider(@Autowired DataSource dataSource) {
        return new JdbcTemplateLockProvider(
            JdbcTemplateLockProvider.Configuration.builder()
                .withTransactionManager(txManager)
                .withTimeZone(TimeZone.getTimeZone("UTC-08"))
                .withJdbcTemplate(new JdbcTemplate(dataSource))
                .withTableName("t_chaos_distribute_lock")
                .build()
        );
    }

    @Bean
    LockingTaskExecutor lockingTaskExecutor(@Autowired LockProvider lockProvider) {
        return new DefaultLockingTaskExecutor(lockProvider);
    }

}
