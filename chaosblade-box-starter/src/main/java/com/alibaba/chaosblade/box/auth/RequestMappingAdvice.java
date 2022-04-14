package com.alibaba.chaosblade.box.auth;

import com.alibaba.chaosblade.box.common.commands.CommandRunTimeException;
import com.alibaba.chaosblade.box.common.common.domain.BaseRequest;
import com.alibaba.chaosblade.box.common.common.domain.ChaosError;
import com.alibaba.chaosblade.box.common.common.domain.user.ChaosUser;
import com.alibaba.chaosblade.box.common.common.enums.CommonErrorCode;
import com.alibaba.chaosblade.box.common.experiment.task.flow.exception.ExceptionHelper;
import com.alibaba.chaosblade.box.common.infrastructure.ChaosApplicationContext;
import com.alibaba.chaosblade.box.common.infrastructure.ChaosRequestContextHolder;
import com.alibaba.chaosblade.box.common.infrastructure.error.ThrowableChaosErrorWrappers;
import com.alibaba.chaosblade.box.common.infrastructure.monitor.log.ResultCodeRecord;
import com.alibaba.chaosblade.box.common.infrastructure.util.ChaosTraceUtil;
import com.alibaba.chaosblade.box.dao.infrastructure.monitor.log.RecordsRepo;
import com.alibaba.chaosblade.box.service.model.RestResponse;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;

import java.lang.reflect.Method;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.concurrent.TimeUnit;

/**
 * @author haibin
 *
 *
 */
@Aspect
@Slf4j
public class RequestMappingAdvice {

    @Autowired
    private ThrowableChaosErrorWrappers throwableChaosErrorWrappers;

    @Pointcut(
        "within(com.alibaba.chaosblade.box..*)&&@annotation(org.springframework.web.bind.annotation.PostMapping)")
//            + "&&!within(com.taobao.csp.monkeyking.web.cloud.api..*)")
    public void responsePointCut() {

    }

    @Around("responsePointCut()")
    public Object around(ProceedingJoinPoint joinPoint)
        throws Throwable {
        MethodSignature methodSignature = (MethodSignature)joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        return doAdvice(joinPoint, method);
    }

    protected Object doAdvice(ProceedingJoinPoint joinPoint, Method method) throws Throwable {
        StopWatch stopWatch = StopWatch.createStarted();
        PostMapping mapping = method.getAnnotation(PostMapping.class);
        ChaosUser chaosUser = null;
        Object baseRequest = null;
        for (Object arg : joinPoint.getArgs()) {
            if (arg instanceof ChaosUser) {
                chaosUser = (ChaosUser)arg;
            } else {
                baseRequest = arg;
            }
        }
        if (chaosUser == null) {
            chaosUser = ChaosRequestContextHolder.getApplicationContext().map(ChaosApplicationContext::getLoginUser).orElse(
                null);
        }
        preHandleParam(chaosUser, baseRequest);
        Object response = null;
        try {
            response = joinPoint.proceed();
        } catch (Exception exception) {
            Throwable throwable = exception;
            if (exception instanceof UndeclaredThrowableException) {
                throwable = ExceptionHelper.getRealExceptionFromUndeclaredThrowableException(
                    (UndeclaredThrowableException)exception);
            }
            if(exception instanceof CommandRunTimeException)
            {
                throwable = ExceptionHelper.getDirectCause(exception);
            }
            response = buildErrorResponse(throwable);
        }
        stopWatch.stop();
        recordRequest(response, chaosUser, baseRequest, stopWatch.getTime(TimeUnit.MILLISECONDS),
            mapping.value()[0]);
        return response;
    }

    private void recordRequest(Object response, ChaosUser chaosUser, Object baseRequest, long timeCost, String entrane) {
        if (response instanceof RestResponse) {
            ResultCodeRecord resultCodeRecord = new ResultCodeRecord();
            String requestId = ChaosRequestContextHolder.getApplicationContext().map(
                ChaosApplicationContext::getRequestId)
                .orElse(null);
            resultCodeRecord.setTraceId(requestId);
            RestResponse restResponse = (RestResponse)response;
            restResponse.setRequestId(requestId);
            resultCodeRecord.setSuccess(restResponse.getSuccess());
            if (!restResponse.getSuccess()) {
                resultCodeRecord.setCode(restResponse.getCode());
            }
            if (chaosUser != null) {
                resultCodeRecord.setUserId(chaosUser.getCurrentUserId());
                resultCodeRecord.addExtra("user_detail", chaosUser);
            }
            if (baseRequest != null) {
                resultCodeRecord.addExtra("request", JSON.toJSONString(baseRequest));
            }
            resultCodeRecord.addExtra("response", JSON.toJSONString(response));
            resultCodeRecord.setEntrance(entrane);
            resultCodeRecord.setCost(timeCost);
            resultCodeRecord.setSource(ResultCodeRecord.SOURCE_MAPPING);
            RecordsRepo.getResultCodeRecord().record(resultCodeRecord);
        }
    }

    private void preHandleParam(ChaosUser chaosUser, Object baseRequest) {
        if (baseRequest instanceof BaseRequest) {
            BaseRequest baseRequest1 = (BaseRequest)baseRequest;
            baseRequest1.setUser(chaosUser);
        }
    }

    protected RestResponse buildErrorResponse(Throwable exception) {
        RestResponse responseEntity = new RestResponse();
        detailResultWithThrowable(responseEntity, exception);
        responseEntity.setSuccess(false);
        responseEntity.setRequestId(ChaosTraceUtil.generateTraceId());
        return responseEntity;
    }

    private void detailResultWithThrowable(RestResponse resultSupport, Throwable throwable) {
        ChaosError ChaosError = throwableChaosErrorWrappers.wrapper(throwable, CommonErrorCode.S_SYSTEM_ERROR);
        resultSupport.setCode(ChaosError.getCode());
        resultSupport.setMessage(ChaosError.getErrorMessage());
        resultSupport.setStatusCode(ChaosError.getCodeStatus());
        if (ChaosError.getErrorCode().logWhenThrowable()) {
            log.error("System exception", throwable);
        }
    }

}
