package com.smart.commons.core.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author shizhongming
 * 2020/1/12 7:13 下午
 */
public final class ExceptionUtils {

    private ExceptionUtils() {
        throw new IllegalStateException("Utility class");
    }

    @SuppressWarnings("unchecked")
    public static <E extends Exception> void doThrow(Exception e) throws E {
        throw (E)e;
    }

    /**
     * 异常对战信息转为string
     * @param e 异常信息
     * @return 转为String的对战信息
     */
    public static String stackTraceToString(Exception e) {
        StackTraceElement[] stackTraceElements = e.getStackTrace();
        List<String> stackTraceList = new ArrayList<>(stackTraceElements.length + 1);
        stackTraceList.add(e.toString());
        for (StackTraceElement element : stackTraceElements) {
            stackTraceList.add("    at " + element.toString());
        }
        return String.join("\n", stackTraceList);
    }
}
