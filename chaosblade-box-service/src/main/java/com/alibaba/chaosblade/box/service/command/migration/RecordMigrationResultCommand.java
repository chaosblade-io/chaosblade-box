package com.alibaba.chaosblade.box.service.command.migration;


import com.alibaba.chaosblade.box.common.commands.SpringBeanCommand;
import com.alibaba.chaosblade.box.common.common.domain.BaseRequest;
import org.springframework.stereotype.Component;

@Component
public class RecordMigrationResultCommand  extends SpringBeanCommand<BaseRequest, Boolean> {
    @Override
    public Boolean execute(BaseRequest baseRequest) {

        return false;
    }
}
