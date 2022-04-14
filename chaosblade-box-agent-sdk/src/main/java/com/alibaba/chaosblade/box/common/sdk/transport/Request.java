package com.alibaba.chaosblade.box.common.sdk.transport;

import java.util.HashMap;
import java.util.Map;

/**
 * @author changjun.xcj
 */
public class Request {
    private static final String FROM_HEADER = "FR";
    private static final String CLIENT = "C";
    private static final String SERVER = "S";
    protected final Map<String, String> headers;
    protected final Map<String, String> params;

    public Request() {
        this(false);
    }

    /**
     * @param isClient the request is created from Client, yes is true
     */

    public Request(boolean isClient) {
        this.headers = new HashMap<String, String>();
        this.params = new HashMap<String, String>();
        if (isClient) {
            headers.put(FROM_HEADER, CLIENT);
        } else {
            headers.put(FROM_HEADER, SERVER);
        }
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public String getHeader(String key) {
        return headers.get(key);
    }

    public String getParam(String key) {
        return params.get(key);
    }

    public void removeHeader(String key) {
        headers.remove(key);
    }

    public void removeParam(String key) {
        params.remove(key);
    }

    /**
     * The request is from client
     *
     * @return true if yes, otherwise return false
     */
    public boolean fromClient() {
        return CLIENT.equals(getHeader(FROM_HEADER));
    }

    /**
     * The request is from server
     *
     * @return true if yes, otherwise return false
     */
    public boolean fromServer() {
        return SERVER.equals(getHeader(FROM_HEADER));
    }

    /**
     * Add header
     *
     * @param key
     * @param value
     * @return
     */
    public Request addHeader(String key, String value) {
        if (isBlank(key)) {
            throw new IllegalArgumentException("Parameter key cannot be empty");
        }
        headers.put(key, value);
        return this;
    }

    /**
     * Add parameter
     *
     * @param key
     * @param value
     * @return
     */
    public Request addParam(String key, String value) {
        if (isBlank(key)) {
            throw new IllegalArgumentException("Parameter key cannot be empty");
        }
        params.put(key, value);
        return this;
    }

    /**
     * Is not null or not empty
     *
     * @param value
     * @return
     */
    private boolean isBlank(String value) {
        return value == null || (value.trim()).length() == 0;
    }
}
