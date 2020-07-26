package com.zhangjie.springbootstudy.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggerUtils {
    public static final boolean isDebug = LoggerFactory.getLogger(LoggerUtils.class).isDebugEnabled();

    public LoggerUtils() {
    }

    public static void debug(Class<?> clazz, String message) {
        if (isDebug) {
            Logger logger = LoggerFactory.getLogger(clazz);
            logger.debug(message);
        }
    }

    public static void info(Class<?> clazz, String message) {
        Logger logger = LoggerFactory.getLogger(clazz);
        logger.info(message);
    }

    public static void fmtDebug(Class<?> clazz, String fmtString, Object... value) {
        if (isDebug) {
            if (!StringUtils.isBlank(fmtString)) {
                if (null != value && value.length != 0) {
                    fmtString = String.format(fmtString, value);
                }

                debug(clazz, fmtString);
            }
        }
    }

    public static void error(Class<?> clazz, String message, Exception e) {
        Logger logger = LoggerFactory.getLogger(clazz);
        if (null == e) {
            logger.error(message);
        } else {
            logger.error(message, e);
        }
    }

    public static void error(Class<?> clazz, Exception e) {
        error(clazz, e.getMessage(), e);
    }

    public static void error(Class<?> clazz, String message) {
        error(clazz, message, (Exception)null);
    }

    public static void fmtError(Class<?> clazz, Exception e, String fmtString, Object... value) {
        if (!StringUtils.isBlank(fmtString)) {
            if (null != value && value.length != 0) {
                fmtString = String.format(fmtString, value);
            }

            error(clazz, fmtString, e);
        }
    }

    public static void fmtError(Class<? extends Object> clazz, String fmtString, Object... value) {
        if (!StringUtils.isBlank(fmtString)) {
            if (null != value && value.length != 0) {
                fmtString = String.format(fmtString, value);
            }

            error(clazz, fmtString);
        }
    }

}
