package com.alibaba.chaosblade.box.common.common.domain.response;

import lombok.Data;

/**
 * @author changjun.xcj
 */
@Data
public class Response<R> {

    private String requestId;
    private int code;
    private boolean success;
    private R result;
    private String error;

    public Response() {
    }

    public Response(String requestId, int code, boolean success, R result, String error) {
        this.requestId = requestId;
        this.code = code;
        this.success = success;
        this.result = result;
        this.error = error;
    }

    private Response(Code code, boolean success, String error, R result) {
        this.code = code.getCode();
        this.success = success;
        this.result = result;
        this.error = error;
    }

    /**
     * Construct a successful response with given object.
     *
     * @param data data object
     * @param <T>    type of the data
     * @return constructed server response
     */
    public static <T> Response<T> ofSuccess(T data) {
        return new Response<T>(Code.OK, true, null, data);
    }

    /**
     * Construct a failed response with given error message.
     *
     * @param code
     * @param error
     * @return constructed server response
     */
    public static <T> Response<T> ofFailure(Code code, String error) {
        return new Response<T>(code, false, error, null);
    }

    /**
     * Construct a failed response with given error message.
     *
     * @param code
     * @param error
     * @param data additional error of the failure
     * @return constructed server response
     */
    public static <T> Response<T> ofFailure(Code code, String error, T data) {
        return new Response<T>(code, false, error, data);
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public R getResult() {
        return result;
    }

    public void setResult(R result) {
        this.result = result;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Response{");
        sb.append("requestId='").append(requestId).append('\'');
        sb.append(", code=").append(code);
        sb.append(", success=").append(success);
        sb.append(", result=").append(result);
        sb.append(", error='").append(error).append('\'');
        sb.append('}');
        return sb.toString();
    }

    /**
     * Response code
     */
    public enum Code {
        OK(200, "success"),
        INVALID_TIMESTAMP(401, "invalid timestamp"),
        FORBIDDEN(403, "forbidden"),
        NOT_FOUND(404, "request handler not found"),
        Token_Not_Found(405, "access token not found"),
        INVALID_Parameter(406, "parameter error"),
        CHAOS_SERVICE_NOT_OPENED(410, "chaos service not opened"),
        CHAOS_SERVICE_NOT_AUTHORIZED(411, "chaos service not authorized"),
        SERVER_ERROR(500, "server error"),
        Handler_Closed(501, "handler closed"),
        PRE_HANDLE_ERROR(502, "pre handle error"),
        COMMAND_NOT_FOUND(503, "command not found"),
        TIMEOUT(510, "timeout"),
        UNINITIALIZED(511, "uninitialized"),
        ENCODE_ERROR(512, "encode error"),
        DECODE_ERROR(513, "decode error"),
        File_Not_Found(514, "file not found"),
        Download_Error(515, "download file error"),
        Deploy_Error(516, "deploy file error"),
        Service_Switch_Error(517, "service switch error"),
        Parameter_Empty(600, "parameter is empty"),
        Parameter_Type_Error(601, "parameter type error"),
        FaultInject_Cmd_Error(701, "cannot handle the faultInject cmd"),
        FaultInject_Execute_Error(702, "execute faultInject error"),
        FaultInject_Not_Support(703, "the inject type not support"),
        JavaAgent_Cmd_Error(704, "cannot handle the javaagent cmd"),
        SENTINEL_DISABLE(705, "sentinel is disabled");

        private int code;
        private String msg;

        Code(int code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public int getCode() {
            return code;
        }

        public String getMsg() {
            return msg;
        }
    }
}


