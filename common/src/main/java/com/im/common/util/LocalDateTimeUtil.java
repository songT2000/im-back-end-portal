package com.im.common.util;

import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeUtil extends cn.hutool.core.date.LocalDateTimeUtil{

    /**
     * 将long类型的timestamp转为LocalDateTime
     */
    public static LocalDateTime getDateTimeOfTimestamp(long timestamp) {
        Instant instant = Instant.ofEpochMilli(timestamp);
        ZoneId zone = ZoneId.systemDefault();
        return LocalDateTime.ofInstant(instant, zone);
    }

    /**
     * 将LocalDateTime转为自定义的时间格式的字符串
     */
    public static String getDateTimeAsString(LocalDateTime localDateTime, String format) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return localDateTime.format(formatter);
    }

    /**
     * 将LocalDateTime转为long类型的timestamp
     */
    public static long getTimestampOfDateTime(LocalDateTime localDateTime) {
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDateTime.atZone(zone).toInstant();
        return instant.toEpochMilli();
    }

    /**
     * 将某时间字符串转为自定义时间格式的LocalDateTime
     */
    public static LocalDateTime parseStringToDateTime(String time, String format) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern(format);
        return LocalDateTime.parse(time, df);
    }

    /**
     * 将某时间字符串转为自定义时间格式的LocalDateTime
     */
    public static LocalDateTime getLocalDateEndOfDay(LocalDate localDate) {
        return localDate.atTime(23,59,59);
    }

    public static void main(String[] args) {
        System.out.println(System.currentTimeMillis());
        System.out.println(getTimestampOfDateTime(LocalDateTime.now()));
    }
}
