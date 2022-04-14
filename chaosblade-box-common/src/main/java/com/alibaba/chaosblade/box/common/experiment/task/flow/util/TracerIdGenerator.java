package com.alibaba.chaosblade.box.common.experiment.task.flow.util;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

/**
 * 这里的实现来自于eagleeye
 *
 * @author haibin
 *
 *
 */

public class TracerIdGenerator implements IdGenerator {

    private static TracerIdGenerator
        tracerIdGenerator = new TracerIdGenerator();

    public static TracerIdGenerator instance() {
        return tracerIdGenerator;
    }

    private static String IP_16 = "ffffffff";
    private static String IP_int = "255255255255";
    private static String PID = "0000";
    private static char PID_FLAG = 'd';

    private static final String REGEX
        = "\\b((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.("
        + "(?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\b";
    private static final Pattern PATTERN = Pattern.compile(REGEX);
    private static AtomicInteger count = new AtomicInteger(1000);

    static {
        try {
            String ipAddress = SystemUtils.getLocalAddress();
            if (ipAddress != null) {
                IP_16 = getIP_16(ipAddress);
                IP_int = getIP_int(ipAddress);
            }
            PID = getHexPid(SystemUtils.getCurrentPid());
        } catch (Throwable e) {
        }
    }

    static String getHexPid(int pid) {
        // unsign short 0 to 65535
        if (pid < 0) {
            pid = 0;
        } else if (pid > 65535) {
            pid = pid % 60000;
        }
        String str = Integer.toHexString(pid);
        while (str.length() < 4) {
            str = '0' + str;
        }
        return str;
    }

    private static String getTraceId(String ip, long timestamp, int nextId) {
        StringBuilder appender = new StringBuilder(32);
        appender.append(ip).append(timestamp).append(nextId).append(PID_FLAG).append(PID);
        return appender.toString();
    }

    static String generate() {
        return getTraceId(IP_16, System.currentTimeMillis(), getNextId());
    }

    static String generate(String ip) {
        if (ip != null && !ip.isEmpty() && validate(ip)) {
            return getTraceId(getIP_16(ip), System.currentTimeMillis(), getNextId());
        } else {
            return generate();
        }
    }

    static String generateIpv4Id() {
        return IP_int;
    }

    static String generateIpv4Id(String ip) {
        if (ip != null && !ip.isEmpty() && validate(ip)) {
            return getIP_int(ip);
        } else {
            return IP_int;
        }
    }

    private static boolean validate(String ip) {
        try {
            return PATTERN.matcher(ip).matches();
        } catch (Throwable e) {
            return false;
        }
    }

    private static String getIP_16(String ip) {
        String[] ips = ip.split("\\.");
        StringBuilder sb = new StringBuilder();
        for (String column : ips) {
            String hex = Integer.toHexString(Integer.parseInt(column));
            if (hex.length() == 1) {
                sb.append('0').append(hex);
            } else {
                sb.append(hex);
            }

        }
        return sb.toString();
    }

    private static String getIP_int(String ip) {
        return ip.replace(".", "");
    }

    private static int getNextId() {
        for (; ; ) {
            int current = count.get();
            int next = (current > 9000) ? 1000 : current + 1;
            if (count.compareAndSet(current, next)) { return next; }
        }
    }

    @Override
    public String generateId() {
        return generate();
    }

}

