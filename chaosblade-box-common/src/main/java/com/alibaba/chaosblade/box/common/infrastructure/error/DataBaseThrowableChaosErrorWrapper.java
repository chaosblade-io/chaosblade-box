package com.alibaba.chaosblade.box.common.infrastructure.error;

import com.alibaba.chaosblade.box.common.common.domain.ChaosError;
import com.alibaba.chaosblade.box.common.common.enums.CommonErrorCode;
import com.mysql.cj.jdbc.exceptions.MysqlDataTruncation;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import java.sql.SQLException;

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
        // 使用 SQLException 来捕获 MySQL 语法错误,兼容 MySQL 5 和 MySQL 8
        if (throwable instanceof SQLException) {
            SQLException sqlException = (SQLException) throwable;
            // MySQL 语法错误的 SQL State 通常是 42000
            if (sqlException.getSQLState() != null && sqlException.getSQLState().startsWith("42")) {
                return new ChaosError(CommonErrorCode.S_DATA_FIELD_MISSING);
            }
        }
        return null;
    }
}
