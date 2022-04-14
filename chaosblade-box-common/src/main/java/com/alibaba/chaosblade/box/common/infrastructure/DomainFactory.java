package com.alibaba.chaosblade.box.common.infrastructure;

import com.alibaba.chaosblade.box.common.common.enums.CommonErrorCode;
import com.alibaba.chaosblade.box.common.infrastructure.exception.ChaosException;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author haibin.lhb
 *
 *
 */
@Component
public class DomainFactory implements ApplicationContextAware {
    private ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

    /**
     * 通过无参数构造器实例化，并给Autowired注解的属性赋值
     *
     * @param tClass 待实例化对象类型
     * @param <T>
     * @return
     */
    public <T extends IChaosDomain> T getBean(Class<T> tClass) {
        try {
            T t = tClass.newInstance();
            return getBean(t);
        } catch (Exception e) {
            throw new ChaosException(CommonErrorCode.S_SYSTEM_ERROR,
                "Class new instance error. ClassName=" + tClass.getSimpleName(), e);
        }
    }

    /**
     * 给已经实例化对象的Autowired注解的属性赋值
     *
     * @param t
     * @return
     */
    public <T extends IChaosDomain> T getBean(T t) {
        try {
            context.getAutowireCapableBeanFactory().autowireBean(t);
            return t;
        } catch (Exception e) {
            throw new ChaosException(CommonErrorCode.S_SYSTEM_ERROR,
                "Field Autowired Error. ClassName=" + t.getClass().getSimpleName(), e);
        }
    }
}
