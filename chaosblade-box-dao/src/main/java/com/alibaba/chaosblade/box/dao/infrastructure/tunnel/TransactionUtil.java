package com.alibaba.chaosblade.box.dao.infrastructure.tunnel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * @author haibin
 *
 *
 */
@Component
public class TransactionUtil {

    @Autowired
    private PlatformTransactionManager txManager;

    public <T> T execute(TransactionCallback<T> action) {
        TransactionTemplate transactionTemplate = new TransactionTemplate(txManager);
        return transactionTemplate.execute(action);
    }

    public <T> T execute(TransactionCallback<T> action, TransactionDefinition transactionDefinition) {
        TransactionTemplate transactionTemplate = new TransactionTemplate(txManager, transactionDefinition);
        return transactionTemplate.execute(action);
    }

    public static void setRollback(TransactionStatus status) {
        if (status.isNewTransaction()) {
            status.setRollbackOnly();
        }
    }
}
