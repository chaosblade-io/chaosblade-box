package com.alibaba.chaosblade.box.dao.scheduler.quartz;

import com.alibaba.chaosblade.box.dao.model.SchedulerJobDO;
import org.quartz.simpl.CascadingClassLoadHelper;

import java.util.HashMap;
import java.util.Map;

/**
 * @author haibin.lhb
 *
 * 
 */
public class ChaosQuartzClassLoadHelper extends CascadingClassLoadHelper {

    private static Map<String, Class<?>> namesConvertor = new HashMap<>();

    static {
        addJobClass(SchedulerJobDO.class);
    }

    public static void addJobClass(Class<?> clazz) {
        namesConvertor.put(clazz.getSimpleName(), clazz);
    }

    public static void addJobClass(String name, Class<?> clazz) {
        namesConvertor.put(name, clazz);
    }

    public static Class<?> getJobClassName(String beanClass) {
        for (Map.Entry<String, Class<?>> classEntry : namesConvertor.entrySet()) {
            if (beanClass.endsWith(classEntry.getKey())) {
                return classEntry.getValue();
            }
        }
        return null;
    }

    @Override
    public void initialize() {
        super.initialize();
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        for (Map.Entry<String, Class<?>> classEntry : namesConvertor.entrySet()) {
            if (name.endsWith(classEntry.getKey())) {
                return classEntry.getValue();
            }
        }
        return super.loadClass(name);
    }

}
