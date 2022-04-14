package com.alibaba.chaosblade.box.common.infrastructure.error;

import com.alibaba.chaosblade.box.common.common.domain.ChaosError;
import com.alibaba.chaosblade.box.common.common.enums.CommonErrorCode;
import com.mysql.jdbc.MysqlDataTruncation;
import com.mysql.jdbc.exceptions.MySQLSyntaxErrorException;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

/**
 * @author haibin
 *
 *
 */
@Component
@Order(value = 2000)
public class DataBaseThrowableChaosErrorWrapper implements ThrowableChaosErrorWrapper {

    @Override
    public ChaosError wrapper(Throwable throwable) {
        if (throwable instanceof DataIntegrityViolationException) {
            DataIntegrityViolationException dataIntegrityViolationException
                = (DataIntegrityViolationException)throwable;
            Throwable cause = dataIntegrityViolationException.getCause();
            if (cause == null) { return new ChaosError(CommonErrorCode.S_DATA_VIOLATION); }
            if (cause.getClass().getName().equals(MysqlDataTruncation.class.getName())) {
                return new ChaosError(CommonErrorCode.S_DATA_TRUNCATION, cause.getMessage());
            }
        }
        if (throwable instanceof MySQLSyntaxErrorException) {
            return new ChaosError(CommonErrorCode.S_DATA_FIELD_MISSING);
        }
        return null;
    }
}
