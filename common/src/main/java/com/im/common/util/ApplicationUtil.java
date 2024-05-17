package com.im.common.util;

import cn.hutool.core.util.StrUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.im.common.constant.CommonConstant;

import java.time.Duration;
import java.time.Instant;

/**
 * 系统操作工具类
 *
 * @author Barry
 * @date 2019/1/1
 */
public final class ApplicationUtil {
    private static final Log LOG = LogFactory.get();

    public static final String THIS_CLASS_NAME = ApplicationUtil.class.getName();

    private ApplicationUtil() {
    }

    /**
     * 向控制台打印一条启动成功语句
     *
     * @param applicationName 系统名称
     * @param start           启动时间
     */
    public static void printStartSuccess(String applicationName, Instant start) {
        Duration duration = Duration.between(start, Instant.now());

        StringBuffer str = new StringBuffer();
        str.append("**************Info*************\n");
        str.append("**************Info*************\n");
        str.append("**************Info*************\n");
        str.append("【")
                .append(applicationName).append("】启动成功，耗时【")
                .append(duration.toMillis())
                .append("ms】，约【")
                .append(duration.getSeconds())
                .append("秒】\n");
        str.append("**************Info*************\n");
        str.append("**************Info*************\n");
        str.append("**************Info*************\n");
        LOG.info("\n" + str.toString());
    }

    /**
     * 向控制台打印一条错误语句
     *
     * @param message 错误消息
     */
    public static void printError(String message) {

        StringBuffer str = new StringBuffer();
        str.append("*************Error(").append(getReferenceName()).append(")**************\n");
        str.append("*************Error**************\n");
        str.append("*************Error**************\n");
        str.append(message).append("\n");
        str.append("*************Error**************\n");
        str.append("*************Error**************\n");
        str.append("*************Error**************\n");
        LOG.error("\n" + str.toString());
    }

    private static String getReferenceName() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();

        if (stackTrace == null || stackTrace.length <= 1) {
            return StrUtil.EMPTY;
        }

        StackTraceElement reference = null;

        for (int i = 1; i < stackTrace.length; i++) {
            StackTraceElement stackTraceElement = stackTrace[i];

            if (stackTraceElement.getClassName().indexOf(CommonConstant.PROJECT_BASE_PACKAGE) > -1) {
                if (!stackTraceElement.getClassName().equals(THIS_CLASS_NAME)) {
                    reference = stackTraceElement;
                    break;
                }
            }
        }

        if (reference == null) {
            return StrUtil.EMPTY;
        }

        return new StringBuilder("(")
                .append(reference.getClassName())
                .append(".").append(reference.getMethodName())
                .append(":").append(reference.getLineNumber()).append(")").toString();
    }

    /**
     * 向控制台打印一条错误语句，一般用作致命类的错误需要终止系统时使用，会强制退出系统
     *
     * @param expression 条件表达式
     * @param message    错误消息
     */
    public static void printErrorWithExitSystem(boolean expression, String message) {
        if (expression) {
            printError(message);
            System.exit(0);
        }
    }
}
