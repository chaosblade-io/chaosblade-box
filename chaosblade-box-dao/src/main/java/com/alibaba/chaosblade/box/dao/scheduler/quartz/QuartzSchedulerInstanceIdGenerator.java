package com.alibaba.chaosblade.box.dao.scheduler.quartz;

import com.alibaba.chaosblade.box.common.experiment.task.flow.util.SystemUtils;
import org.apache.commons.lang3.StringUtils;
import org.quartz.SchedulerException;
import org.quartz.spi.InstanceIdGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;

/**
 * @author: xinyuan.ymm
 * @create: 2018-03-06 下午3:39
 */
public class QuartzSchedulerInstanceIdGenerator implements InstanceIdGenerator {

    private static final Logger log = LoggerFactory.getLogger(QuartzSchedulerInstanceIdGenerator.class);

    private static final String OS_NAME = "os.name";

    private static final String WINDOWS = "Windows";

    private static final String MAC = "Mac OS";

    @Override
    public String generateInstanceId() throws SchedulerException {
        String id;
        try {
            if (isLocalDev()) {
                id = getHostName();
            } else {
                id = SystemUtils.getLocalAddress();
            }

            if (StringUtils.isBlank(id)) {
                id = InetAddress.getLocalHost().getHostAddress() + "_" + System.currentTimeMillis();
            }
        } catch (Exception e) {
            throw new SchedulerException("Couldn't generate instance id!", e);
        }

        return id;
    }

    private String getHostName() throws SchedulerException {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (Exception e) {
            throw new SchedulerException("Couldn't get host name!", e);
        }
    }

    private boolean isLocalDev() {
        if (StringUtils.indexOfIgnoreCase(System.getProperty(OS_NAME), WINDOWS) >= 0) {
            return true;
        }

        if (StringUtils.indexOfIgnoreCase(System.getProperty(OS_NAME), MAC) >= 0) {
            return true;
        }

        return false;
    }
}
