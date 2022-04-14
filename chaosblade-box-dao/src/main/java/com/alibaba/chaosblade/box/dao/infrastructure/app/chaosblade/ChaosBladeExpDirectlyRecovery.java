package com.alibaba.chaosblade.box.dao.infrastructure.app.chaosblade;

import com.alibaba.chaosblade.box.common.common.domain.response.Response;
import com.alibaba.chaosblade.box.dao.model.ChaosBladeExpUidDO;
import com.alibaba.chaosblade.box.dao.repository.ChaosBladeExpUidRepository;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.RejectedExecutionException;

/**
 * @author haibin.lhb
 *
 * 
 */
@Component
@Slf4j
public class ChaosBladeExpDirectlyRecovery {

    @Autowired
    private ChaosBladeInvoker chaosBladeInvoker;

    @Autowired
    private ChaosBladeExpUidRepository chaosBladeExpUidRepository;

    public void recoveryByExpRecord(ChaosBladeExpUidDO chaosBladeExpUidDO) {
        if (chaosBladeExpUidDO == null || chaosBladeExpUidDO.getExpired()) {
            return;
        }
        boolean done = true;
        if (!Strings.isNullOrEmpty(chaosBladeExpUidDO.getConfigurationId())) {
            try {
                Response<String> stringResponse = chaosBladeInvoker
                        .destroyByChaosBladeExpDO(
                                chaosBladeExpUidDO);
                chaosBladeExpUidDO.addAttribute("cb_destroy_cover",
                        Boolean.valueOf(stringResponse.isSuccess()).toString());
                if (!stringResponse.isSuccess()) {
                    chaosBladeExpUidDO.addAttribute("cb_destroy_cover_error",
                            stringResponse.getError());
                    log.info("destroy cover failed,expId:{},error:{}",
                            chaosBladeExpUidDO.getExpUid(), stringResponse.getError());
                } else {
                    log.info("destroy cover success,expId:{},result:{}",
                            chaosBladeExpUidDO.getExpUid(), stringResponse.getResult());
                }
            } catch (RejectedExecutionException rejectedExecutionException) {
                done = false;
            } catch (Exception exception) {
                log.error("destroy cover failed,expId:{}", chaosBladeExpUidDO.getExpUid(), exception);
                chaosBladeExpUidDO.addAttribute("cb_destroy_cover", "false");
                chaosBladeExpUidDO.addAttribute("cb_destroy_cover_error",
                        exception.getMessage());
            }
        }
        if (done) {
            chaosBladeExpUidDO.setExpired(true);
            chaosBladeExpUidDO.setExpiredTime(new Date());
            chaosBladeExpUidRepository.update(chaosBladeExpUidDO);
        }
    }
}
