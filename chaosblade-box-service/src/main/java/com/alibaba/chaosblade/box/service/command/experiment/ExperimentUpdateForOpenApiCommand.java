package com.alibaba.chaosblade.box.service.command.experiment;

import com.alibaba.chaosblade.box.common.commands.SpringBeanCommand;
import com.alibaba.chaosblade.box.common.common.domain.BaseRequest;
import com.alibaba.chaosblade.box.common.common.domain.Response;
import com.alibaba.chaosblade.box.common.common.enums.CommonErrorCode;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.request.ExperimentDefinitionRequest;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.request.ExperimentUpdateRequest;
import com.alibaba.chaosblade.box.common.infrastructure.exception.ChaosException;
import com.alibaba.chaosblade.box.dao.infrastructure.tunnel.TransactionUtil;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;

/**
 * @author sunpeng
 *
 *
 */
@Component
@Slf4j
public class ExperimentUpdateForOpenApiCommand extends SpringBeanCommand<ExperimentUpdateRequest, Response<Boolean>> {

    @Autowired
    private TransactionUtil transactionUtil;

    @Override
    public Response<Boolean> execute(ExperimentUpdateRequest experimentUpdateRequest) {

        return transactionUtil.execute(new TransactionCallback<Response<Boolean>>() {
            @Override
            public Response<Boolean> doInTransaction(TransactionStatus status) {
                try {
                    internalExecute(experimentUpdateRequest);
                    return Response.okWithData(true);
                } catch (Exception ex) {
                    status.setRollbackOnly();
                    log.error("update experiment by openApi failed", ex);
                    return Response.failedWith(CommonErrorCode.B_EXPERIMENT_UPDATE, ex.getMessage());
                }
            }
        });

    }

    private void internalExecute(ExperimentUpdateRequest experimentUpdateRequest) {
        //若演练定义入参不为null，则更新演练定义
        ExperimentDefinitionRequest experimentDefinitionRequest = experimentUpdateRequest.getDefinition();
        if (experimentDefinitionRequest != null) {
            BaseRequest.copy(experimentUpdateRequest, experimentDefinitionRequest);
            Response response = commandBus.syncRun(ExperimentDefinitionUpdateCommand.class,
                    experimentDefinitionRequest);
            if (!response.isSuccess()) {
                throw new ChaosException(response.getError());
            }
        }

        //若演练基本信息不为空，则更新演练基本信息
        if(!Strings.isNullOrEmpty(experimentUpdateRequest.getName())) {
            //将定义信息置为null，保证后面更够更新演练基本信息
            experimentUpdateRequest.setDefinition(null);
            Response response = commandBus.syncRun(ExperimentUpdateCommand.class, experimentUpdateRequest);
            if (!response.isSuccess()) {
                throw new ChaosException(response.getError());
            }
        }
    }

}
