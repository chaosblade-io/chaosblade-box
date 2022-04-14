package com.alibaba.chaosblade.box.common.commands.decorator;

import com.alibaba.chaosblade.box.common.commands.Command;
import com.alibaba.chaosblade.box.common.commands.CommandDecorator;
import com.alibaba.chaosblade.box.common.commands.SpringBeanCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * @author haibin
 *
 *
 */
@Component
public class SpringAutowiredSetterCommandDecorator implements CommandDecorator {

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public <Response> Command<Response> decorate(Command<Response> sourceCommand) {
        if (sourceCommand == null) {
            return null;
        }
        if (!(sourceCommand instanceof SpringBeanCommand)) {
            this.applicationContext.getAutowireCapableBeanFactory().autowireBean(sourceCommand);
        }
        return sourceCommand;
    }
}
