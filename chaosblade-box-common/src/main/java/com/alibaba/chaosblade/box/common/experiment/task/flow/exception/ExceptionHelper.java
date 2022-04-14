package com.alibaba.chaosblade.box.common.experiment.task.flow.exception;


import com.alibaba.chaosblade.box.common.infrastructure.exception.ChaosException;

import java.lang.reflect.UndeclaredThrowableException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author haibin
 *
 *
 */
public final class ExceptionHelper {

    public static Throwable getRootCause(Throwable throwable) {
        Throwable rootCause = null;
        Throwable cause = throwable.getCause();
        while (cause != null && cause != rootCause) {
            rootCause = cause;
            cause = cause.getCause();
        }
        return rootCause;
    }

    public static Throwable getDirectCause(Throwable throwable) {
        Throwable cause = throwable.getCause();
        if (cause == null) {return throwable;}
        return cause;
    }

    public static Throwable getRealExceptionFromUndeclaredThrowableException(
        UndeclaredThrowableException undeclaredThrowableException) {
        if (undeclaredThrowableException.getCause() != null) {
            return undeclaredThrowableException.getCause();
        }
        return undeclaredThrowableException;
    }

    public static void hideChaosStack(Throwable throwable) {
        StackTraceElement[] stackTraceElements = throwable.getStackTrace();
        throwable.setStackTrace(Stream.of(stackTraceElements).filter(
                stackTraceElement -> !stackTraceElement.getClassName().startsWith("com.taobao.csp"))
            .collect(Collectors.toList())
            .toArray(new StackTraceElement[] {}));

    }

    public static String buildMessage(String message, Throwable cause) {
        if (cause != null) {
            return message + ",cause:" + detailedMessage(cause);
        } else {
            return message;
        }
    }

    public static String detailedMessage(Throwable t) {
        if (t == null) {return null;}
        if (t instanceof ChaosException) {
            ChaosException ChaosException = (ChaosException)t;
            return ChaosException.getMessage();
        }
        return detailMessage(t, false, 0);
    }

    private static String detailMessage(Throwable t, boolean newLines, int initialCounter) {
        if (t == null) {
            return "Unknown";
        }
        int counter = initialCounter + 1;
        if (t.getCause() != null) {
            StringBuilder sb = new StringBuilder();
            while (t != null) {
                sb.append(t.getClass().getSimpleName());
                if (t.getMessage() != null) {
                    sb.append("[").append(t.getMessage()).append("]");
                }
                if (!newLines) {
                    sb.append("; ");
                }
                t = t.getCause();
                if (t != null) {
                    if (newLines) {
                        sb.append("\n");
                        for (int i = 0; i < counter; i++) {
                            sb.append("\t");
                        }
                    } else {
                        sb.append("nested: ");
                    }
                }
                counter++;
            }
            return sb.toString();
        } else {
            return t.getClass().getSimpleName() + "[" + t.getMessage() + "]";
        }
    }

}
