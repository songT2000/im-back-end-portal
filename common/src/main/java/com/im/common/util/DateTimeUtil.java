package com.im.common.util;

import cn.hutool.core.util.StrUtil;
import com.im.common.constant.CommonConstant;
import com.im.common.entity.enums.CycleTypeEnum;
import com.im.common.exception.ImException;
import com.im.common.response.ResponseCode;

import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 时间工具类,如果要做时间增减操作,在返回的{@link LocalDateTime#plusDays(long)}上调用
 *
 * @author Barry
 * @date 2018/5/22
 * @since JDK1.8
 */
public final class DateTimeUtil {

    /**
     * 时间格式：yyyy-MM-dd HH:mm:ss
     */
    private static final DateTimeFormatter DATETIME_FORMATTER_LONG = TimeFormat.LONG_DATE_PATTERN_LINE.formatter;
    /**
     * 时间格式：yyyy-MM-dd
     */
    private static final DateTimeFormatter DATETIME_FORMATTER_SHORT = TimeFormat.SHORT_DATE_PATTERN_LINE.formatter;

    private DateTimeUtil() {
    }

    /**
     * String转时间，格式yyyy-MM-dd HH:mm:ss
     *
     * @param time 时间str
     * @return LocalDateTime
     */
    public static LocalDateTime fromDateTimeStr(String time) {
        return LocalDateTime.parse(time, DATETIME_FORMATTER_LONG);
    }

    /**
     * String转时间，格式HH:mm:ss
     *
     * @param time 时间str
     * @return LocalTime
     */
    public static LocalTime fromTimeStr(String time) {
        return LocalTime.parse(time, TimeFormat.TIME_PATTERN_LINE.formatter);
    }

    /**
     * String 转日期时间，格式自行指定
     *
     * @param time   指定时间字符串
     * @param format 时间格式
     * @return LocalDateTime
     */
    public static LocalDateTime fromDateTimeStr(String time, String format) {
        return LocalDateTime.parse(time, DateTimeFormatter.ofPattern(format));
    }

    /**
     * String转日期，格式yyyy-MM-dd
     *
     * @param date 日期str
     * @return LocalDateTime
     */
    public static LocalDateTime fromDateStr(String date) {
        return LocalDate.parse(date, DATETIME_FORMATTER_SHORT).atStartOfDay();
    }

    /**
     * String转日期，格式yyyy-MM-dd
     *
     * @param date 日期str
     * @return LocalDateTime
     */
    public static LocalDate fromDateStrToLocalDate(String date) {
        return LocalDate.parse(date, DATETIME_FORMATTER_SHORT);
    }

    /**
     * String转日期，格式yyyy-MM-dd
     *
     * @param date 日期str
     * @return LocalDateTime
     */
    public static LocalDate fromDateStrToLocalDate(String date, String format) {
        return LocalDate.parse(date, DateTimeFormatter.ofPattern(format));
    }

    /**
     * String转日期，格式自行指定
     *
     * @param date   指定日期字符串
     * @param format 的时间格式
     * @return LocalDateTime
     */
    public static LocalDateTime fromDateStr(String date, TimeFormat format) {
        return LocalDate.parse(date, format.formatter).atStartOfDay();
    }

    /**
     * 从秒转成时间
     *
     * @param seconds
     * @return
     */
    public static LocalDateTime fromSeconds(long seconds) {
        return LocalDateTime.ofInstant(Instant.ofEpochSecond(seconds), ZoneId.systemDefault());
    }

    /**
     * 从毫秒转成时间
     *
     * @param milliSeconds
     * @return
     */
    public static LocalDateTime fromMilliSeconds(long milliSeconds) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(milliSeconds), ZoneId.systemDefault());
    }

    /**
     * 时间转String，格式yyyy-MM-dd HH:mm:ss
     *
     * @param time 时间
     * @return String
     */
    public static String toDateTimeStr(LocalDateTime time) {
        return DATETIME_FORMATTER_LONG.format(time);
    }

    /**
     * 时间转String，格式yyyy-MM-dd
     *
     * @param date 日期
     * @return String
     */
    public static String toDateStr(LocalDateTime date) {
        return DATETIME_FORMATTER_SHORT.format(date);
    }

    /**
     * 时间转String，格式yyyy-MM-dd
     *
     * @param date 日期
     * @return String
     */
    public static String toDateStr(LocalDate date) {
        return DATETIME_FORMATTER_SHORT.format(date);
    }

    /**
     * 时间转String，格式yyyy-MM-dd
     *
     * @param date 日期
     * @return String
     */
    public static String toDateStr(LocalDate date, String format) {
        return DateTimeFormatter.ofPattern(format).format(date);
    }

    /**
     * 时间转String，格式HH:mm:ss
     *
     * @param time 时间
     * @return String
     */
    public static String toTimeStr(LocalTime time) {
        return TimeFormat.TIME_PATTERN_LINE.formatter.format(time);
    }

    /**
     * 时间转String，格式自行指定
     *
     * @param dateTime 时间
     * @param format   要格式化的时间格式
     * @return String
     */
    public static String toStr(LocalDateTime dateTime, TimeFormat format) {
        return format.formatter.format(dateTime);
    }

    /**
     * 时间转String，格式自行指定
     *
     * @param dateTime 时间
     * @param format   要格式化的时间格式
     * @return String
     */
    public static String toStr(LocalDateTime dateTime, String format) {
        return DateTimeFormatter.ofPattern(format).format(dateTime);
    }

    /**
     * 时间字符串转String，格式自行指定
     *
     * @param dateTimeStr 时间，格式yyyy-MM-dd HH:mm:ss
     * @param toFormat    要格式化的时间格式
     * @return String
     */
    public static String toStrFromDateTimeStr(String dateTimeStr, String toFormat) {
        LocalDateTime localDateTime = fromDateTimeStr(dateTimeStr);
        return DateTimeUtil.toStr(localDateTime, toFormat);
    }

    /**
     * 时间字符串转String，格式自行指定
     *
     * @param dateTimeStr 时间
     * @param toFormat    要格式化的时间格式
     * @return String
     */
    public static String toStrFromDateTimeStr(String dateTimeStr, TimeFormat toFormat) {
        LocalDateTime localDateTime = fromDateTimeStr(dateTimeStr);
        return DateTimeUtil.toStr(localDateTime, toFormat);
    }

    /**
     * 从时间转成秒
     *
     * @param time
     * @return
     */
    public static long toSeconds(LocalDateTime time) {
        return time.atZone(ZoneId.systemDefault()).toEpochSecond();
    }

    /**
     * 从时间转成毫秒
     *
     * @param time
     * @return
     */
    public static long toMilliSeconds(LocalDateTime time) {
        return time.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    /**
     * 获取当前时间,格式yyyy-MM-dd HH:mm:ss
     *
     * @return String
     */
    public static String getNowDateTimeStr() {
        return toDateTimeStr(LocalDateTime.now());
    }

    /**
     * 获取当前时间,格式yyyy-MM-dd
     *
     * @return String
     */
    public static String getNowDateStr() {
        return toDateStr(LocalDateTime.now());
    }

    /**
     * 获取当前时间，并按照指定的时间格式进行格式化
     *
     * @param format 时间格式 DateTimeUtil.TimeFormat
     * @return String
     */
    public static String getNowStr(TimeFormat format) {
        return toStr(LocalDateTime.now(), format);
    }

    /**
     * 获取当前时间，并按照指定的时间格式进行格式化
     *
     * @param format 时间格式
     * @return String
     */
    public static String getNowStr(String format) {
        return toStr(LocalDateTime.now(), format);
    }

    /**
     * 获取两个时间相差多少天, after - before
     *
     * @param before 之前的时间
     * @param after  之后的时间
     * @return long
     */
    public static long betweenDays(LocalDateTime before, LocalDateTime after) {
        Duration duration = Duration.between(before, after);
        return duration.toDays();
    }

    /**
     * 获取两个时间相差多少天, after - before
     *
     * @param before 之前的时间
     * @param after  之后的时间
     * @return long
     */
    public static long betweenDays(LocalDate before, LocalDate after) {
        return after.toEpochDay() - before.toEpochDay();
    }

    /**
     * 获取两个时间相差多少小时, after - before
     *
     * @param before 之前的时间
     * @param after  之后的时间
     * @return long
     */
    public static long betweenHours(LocalDateTime before, LocalDateTime after) {
        Duration duration = Duration.between(before, after);
        return duration.toHours();
    }

    /**
     * 获取两个时间相差多少分钟, after - before
     *
     * @param before 之前的时间
     * @param after  之后的时间
     * @return long
     */
    public static long betweenMinutes(LocalDateTime before, LocalDateTime after) {
        Duration duration = Duration.between(before, after);
        return duration.toMinutes();
    }

    /**
     * 获取两个时间相差多少秒, after - before
     *
     * @param before 之前的时间
     * @param after  之后的时间
     * @return long
     */
    public static long betweenSeconds(LocalDateTime before, LocalDateTime after) {
        Duration duration = Duration.between(before, after);
        return duration.toMillis() / 1000;
    }

    /**
     * 获取两个时间相差多少毫秒, after - before
     *
     * @param before 之前的时间
     * @param after  之后的时间
     * @return long
     */
    public static long betweenMills(LocalDateTime before, LocalDateTime after) {
        Duration duration = Duration.between(before, after);
        return duration.toMillis();
    }

    /**
     * 获取之前的时间距离现在相差多少天, now - before
     *
     * @param before 比现在大的之前的时间
     * @return long
     */
    public static long betweenNowDays(LocalDateTime before) {
        return betweenDays(before, LocalDateTime.now());
    }

    /**
     * 获取之前的时间距离相差多少小时, now - before
     *
     * @param before 比现在大的之前的时间
     * @return long
     */
    public static long betweenNowHours(LocalDateTime before) {
        return betweenHours(before, LocalDateTime.now());
    }

    /**
     * 获取之前的时间距离现在相差多少分钟, now - before
     *
     * @param before 比现在大的之前的时间
     * @return long
     */
    public static long betweenNowMinutes(LocalDateTime before) {
        return betweenMinutes(before, LocalDateTime.now());
    }

    /**
     * <p>获取之前的时间距离现在相差多少秒</p>
     *
     * <ul>
     *     <li>比如：before=2019-10-18 08:55:00距离now=2019-10-18 09:00:00为300秒</li>
     *     <li>比如：before=2019-10-18 09:05:00距离now=2019-10-18 09:00:00为-300秒</li>
     * </ul>
     *
     * @param before 比现在大的之前的时间
     * @return long
     */
    public static long betweenNowSeconds(LocalDateTime before) {
        return betweenSeconds(before, LocalDateTime.now());
    }

    /**
     * 获取之前的时间距离现在相差多少豪秒
     *
     * @param before 比现在大的之前的时间
     * @return long
     */
    public static long betweenNowMills(LocalDateTime before) {
        return betweenMills(before, LocalDateTime.now());
    }

    /**
     * 判断dateTime1是否在dateTime2之前端
     *
     * @param dateTime1 时间1，yyyy-MM-dd HH:mm:ss
     * @param dateTime2 时间2，yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static boolean isBefore(String dateTime1, String dateTime2) {
        LocalDateTime localDateTime1 = fromDateTimeStr(dateTime1);
        LocalDateTime localDateTime2 = fromDateTimeStr(dateTime2);
        return localDateTime1.isBefore(localDateTime2);
    }

    /**
     * 判断dateTime1是否在dateTime2之前端
     *
     * @param dateTime1 时间1，yyyy-MM-dd HH:mm:ss
     * @param dateTime2 时间2，yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static boolean isBefore(LocalDateTime dateTime1, LocalDateTime dateTime2) {
        return dateTime1.isBefore(dateTime2);
    }

    /**
     * 指定时间是否处于两个时间之中，left <= middle >= right
     *
     * @param left   左边较小的时间
     * @param middle 中间比较的时间
     * @param right  右边较大的时间
     * @return boolean
     */
    public static boolean isBetween(LocalDateTime left, LocalDateTime middle, LocalDateTime right) {
        return (middle.isAfter(left) || middle.isEqual(left)) && (middle.isBefore(right) || middle.isEqual(right));
    }

    /**
     * 指定时间是否处于两个时间之中，left <= middle >= right
     *
     * @param left   左边较小的时间
     * @param middle 中间比较的时间
     * @param right  右边较大的时间
     * @return boolean
     */
    public static boolean isBetween(LocalTime left, LocalTime middle, LocalTime right) {
        return (middle.isAfter(left) || middle.equals(left)) && (middle.isBefore(right) || middle.equals(right));
    }

    /**
     * 指定时间是否处于两个时间之中，left <= middle >= right
     *
     * @param left   左边较小的时间
     * @param middle 中间比较的时间
     * @param right  右边较大的时间
     * @return boolean
     */
    public static boolean isBetween(LocalDate left, LocalDate middle, LocalDate right) {
        return (middle.isAfter(left) || middle.isEqual(left)) && (middle.isBefore(right) || middle.isEqual(right));
    }

    /**
     * date1 <= date2
     *
     * @param date1 date1
     * @param date2 date2
     * @return boolean
     */
    public static boolean isBeforeOrEqual(LocalDateTime date1, LocalDateTime date2) {
        return date1.isBefore(date2) || date1.isEqual(date2);
    }

    /**
     * date1是否在date2之后，date1 >= date2，
     *
     * @param date1 date1
     * @param date2 date2
     * @return boolean
     */
    public static boolean isAfterOrEqual(LocalDateTime date1, LocalDateTime date2) {
        return date1.isAfter(date2) || date1.isEqual(date2);
    }

    /**
     * date1 == date2
     *
     * @param date1 date1
     * @param date2 date2
     * @return boolean
     */
    public static boolean isEqual(LocalDateTime date1, LocalDateTime date2) {
        return date1.isEqual(date2);
    }

    /**
     * 判断是否是Date，格式yyyy-MM-dd HH:mm:ss
     *
     * @param dateTime 日期时间
     * @return boolean
     */
    public static boolean isDatetime(String dateTime) {
        try {
            LocalDateTime localDateTime = fromDateTimeStr(dateTime);

            return localDateTime != null;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 判断是否是Date，格式yyyy-MM-dd
     *
     * @param date 日期
     * @return boolean
     */
    public static boolean isDate(String date) {
        try {
            LocalDateTime localDateTime = fromDateStr(date);

            return localDateTime != null;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 判断是否是HH:mm:ss格式
     *
     * @param time 时分秒
     * @return boolean
     */
    public static boolean isTime(String time) {
        return tryFormatTime(time, TimeFormat.TIME_PATTERN_LINE);
    }

    /**
     * 判断是否是time，HH:mm:ss
     *
     * @param time   日期
     * @param format 格式
     * @return boolean
     */
    public static boolean tryFormatTime(String time, TimeFormat format) {
        try {
            LocalTime localDateTime = LocalTime.parse(time, format.formatter);

            return localDateTime != null;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 在当前时间基础上加N秒
     *
     * @param seconds 加多少秒
     * @return 处理后的时间，yyyy-MM-dd HH:mm:ss
     */
    public static String plusSecondsFromNowToDateTimeStr(int seconds) {
        LocalDateTime localDateTime = LocalDateTime.now();
        localDateTime.plusSeconds(seconds);
        return toDateTimeStr(localDateTime);
    }

    /**
     * 在当前时间基础上加N分钟
     *
     * @param minutes 加多少分钟
     * @return 处理后的时间，yyyy-MM-dd HH:mm:ss
     */
    public static String plusMinutesFromNowToDateTimeStr(int minutes) {
        LocalDateTime localDateTime = LocalDateTime.now();
        localDateTime = localDateTime.plusMinutes(minutes);
        return toDateTimeStr(localDateTime);
    }

    /**
     * 在当前时间基础上加N小时
     *
     * @param hours 加多少小时
     * @return 处理后的时间，yyyy-MM-dd HH:mm:ss
     */
    public static String plusHoursFromNowToDateTimeStr(int hours) {
        LocalDateTime localDateTime = LocalDateTime.now();
        localDateTime = localDateTime.plusHours(hours);
        return toDateTimeStr(localDateTime);
    }

    /**
     * 在当前时间基础上加N天
     *
     * @param days 加多少天
     * @return 处理后的时间，yyyy-MM-dd
     */
    public static String plusDaysFromNowToDateStr(int days) {
        LocalDateTime localDateTime = LocalDateTime.now();
        localDateTime.plusDays(days);
        return toDateStr(localDateTime);
    }

    /**
     * 在当前时间基础上加N天
     *
     * @param days 加多少天
     * @return 处理后的时间，yyyy-MM-dd HH:mm:ss
     */
    public static String plusDaysFromNowToDateTimeStr(int days) {
        LocalDateTime localDateTime = LocalDateTime.now();
        localDateTime.plusDays(days);
        return toDateTimeStr(localDateTime);
    }

    /**
     * 在当前时间基础上减N秒
     *
     * @param seconds 多少秒
     * @return 处理后的时间，yyyy-MM-dd HH:mm:ss
     */
    public static String minusSecondsFromNowToDateTimeStr(int seconds) {
        LocalDateTime localDateTime = LocalDateTime.now();
        localDateTime = localDateTime.minusSeconds(seconds);
        return toDateTimeStr(localDateTime);
    }

    /**
     * 在当前时间基础上减N分钟
     *
     * @param minutes 多少分钟
     * @return 处理后的时间，yyyy-MM-dd HH:mm:ss
     */
    public static String minusMinutesFromNowToDateTimeStr(int minutes) {
        LocalDateTime localDateTime = LocalDateTime.now();
        localDateTime = localDateTime.minusMinutes(minutes);
        return toDateTimeStr(localDateTime);
    }

    /**
     * 在当前时间基础上减N分钟
     *
     * @param minutes 减多少分钟
     * @return 处理后的时间，yyyy-MM-dd
     */
    public static String minusMinutesFromNowToDateStr(int minutes) {
        LocalDateTime localDateTime = LocalDateTime.now();
        localDateTime = localDateTime.minusMinutes(minutes);
        return toDateStr(localDateTime);
    }

    /**
     * 在当前时间基础上减N小时
     *
     * @param hours 减多少小时
     * @return 处理后的时间，yyyy-MM-dd HH:mm:ss
     */
    public static String minusHoursFromNowToDateTimeStr(int hours) {
        LocalDateTime localDateTime = LocalDateTime.now();
        localDateTime = localDateTime.minusHours(hours);
        return toDateTimeStr(localDateTime);
    }

    /**
     * 在当前时间基础上减N天
     *
     * @param days 多少天
     * @return 处理后的时间，yyyy-MM-dd
     */
    public static String minusDaysFromNowToDateStr(int days) {
        LocalDateTime localDateTime = LocalDateTime.now();
        localDateTime = localDateTime.minusDays(days);
        return toDateStr(localDateTime);
    }

    /**
     * 在当前时间基础上减N天
     *
     * @param days 多少天
     * @return 处理后的时间，yyyy-MM-dd HH:mm:ss
     */
    public static String minusDaysFromNowToDateTimeStr(int days) {
        LocalDateTime localDateTime = LocalDateTime.now();
        localDateTime = localDateTime.minusDays(days);
        return toDateTimeStr(localDateTime);
    }

    /**
     * 将一个字符串的日期转换为当天开始时间,给字符串加00:00:00
     *
     * @param date 日期,格式yyyy-MM-dd
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static String patchHHMMSSForStartDate(String date) {
        if (date != null && !date.contains(CommonConstant.COLON_EN)) {
            return StrUtil.trim(date) + CommonConstant.START_DATE_TIME_SUFFIX;
        }
        return date;
    }

    /**
     * 将一个字符串的日期转换为当天开始时间,给字符串加23:59:59
     *
     * @param date 日期,格式yyyy-MM-dd
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static String patchHHMMSSForEndDate(String date) {
        if (date != null && !date.contains(CommonConstant.COLON_EN)) {
            return StrUtil.trim(date) + CommonConstant.END_DATE_TIME_SUFFIX;
        }
        return date;
    }

    /**
     * 时间格式
     */
    public enum TimeFormat {

        /**
         * 短时间格式
         */
        SHORT_DATE_PATTERN_LINE("yyyy-MM-dd"),
        SHORT_DATE_PATTERN_SLASH("yyyy/MM/dd"),
        SHORT_DATE_PATTERN_DOUBLE_SLASH("yyyy\\MM\\dd"),
        SHORT_DATE_PATTERN_NONE("yyyyMMdd"),

        /**
         * 长时间格式
         */
        LONG_DATE_PATTERN_LINE("yyyy-MM-dd HH:mm:ss"),
        LONG_DATE_PATTERN_SLASH("yyyy/MM/dd HH:mm:ss"),
        LONG_DATE_PATTERN_DOUBLE_SLASH("yyyy\\MM\\dd HH:mm:ss"),
        LONG_DATE_PATTERN_NONE("yyyyMMdd HH:mm:ss"),

        /**
         * 长时间格式 带毫秒
         */
        LONG_DATE_PATTERN_WITH_MILSEC_LINE("yyyy-MM-dd HH:mm:ss.SSS"),
        LONG_DATE_PATTERN_WITH_MILSEC_SLASH("yyyy/MM/dd HH:mm:ss.SSS"),
        LONG_DATE_PATTERN_WITH_MILSEC_DOUBLE_SLASH("yyyy\\MM\\dd HH:mm:ss.SSS"),
        LONG_DATE_PATTERN_WITH_MILSEC_NONE("yyyyMMdd HH:mm:ss.SSS"),

        LONG_DATE_PATTERN_WITH_MILL_NONE("yyyyMMddHHmmssSSS"),

        /**
         * 长时间格式不带分割符
         */
        LONG_DATE_NO_SEPARATOR_2Y("yyMMddHHmmss"),
        LONG_DATE_NO_SEPARATOR_4Y("yyyyMMddHHmmss"),

        TIME_PATTERN_LINE("HH:mm:ss");

        private transient DateTimeFormatter formatter;

        TimeFormat(String pattern) {
            formatter = DateTimeFormatter.ofPattern(pattern);
        }
    }

    /**
     * 根据年/月获取一个月的天数
     *
     * @param year  年
     * @param month 月
     * @return List<String>
     */
    public static List<String> getDaysByYearAndMonth(int year, int month) {
        List<String> list = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, 0);

        int day = calendar.get(Calendar.DAY_OF_MONTH);
        for (int i = 1; i <= day; i++) {
            list.add(year + "-" + String.format("%02d", month) + "-" + String.format("%02d", i));
        }

        return list;
    }

    /**
     * 获取某年某月的第一天
     *
     * @return String
     */
    public static String getFirstDayOfMonth(int year, int month) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        return sdf.format(calendar.getTime());
    }

    /**
     * 获取某年某月的最后一天
     *
     * @return String
     */
    public static String getLastDayOfMonth(int year, int month) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        return sdf.format(calendar.getTime());
    }

    /**
     * 判断是否是服务时间格式，为空时返回true
     *
     * @param serviceTime 09:00:00~02:00:00
     * @return boolean
     */
    public static boolean isServiceTimeFormat(String serviceTime) {
        if (StrUtil.isBlank(serviceTime)) {
            return true;
        }

        String[] serviceTimeArr = serviceTime.split(CommonConstant.RANGE_EN);

        if (serviceTimeArr == null || serviceTimeArr.length != 2) {
            return false;
        }

        String startStr = serviceTimeArr[0];
        String endStr = serviceTimeArr[1];

        if (DateTimeUtil.isTime(startStr) && DateTimeUtil.isTime(endStr)) {
            return true;
        }

        return false;
    }

    /**
     * 判断当前时间是否在服务时间内
     * <p>
     * 开始时间 <= 当前时间 >= 结束时间
     *
     * @param serviceTime 09:00:00~02:00:00，(00:00:00~00:00:00，像这种开始结束一样的，24小时可用)
     * @return boolean
     */
    public static boolean isDuringServiceTime(String serviceTime) {
        if (StrUtil.isBlank(serviceTime)) {
            return true;
        }

        String[] serviceTimeArr = serviceTime.split(CommonConstant.RANGE_EN);

        if (serviceTimeArr == null || serviceTimeArr.length != 2) {
            return false;
        }

        String startStr = serviceTimeArr[0];
        String endStr = serviceTimeArr[1];
        // 两个一样，24小时可用
        if (startStr.equalsIgnoreCase(endStr)) {
            return true;
        }
        LocalTime start = null;
        LocalTime end = null;
        if (DateTimeUtil.isTime(startStr)) {
            start = DateTimeUtil.fromTimeStr(startStr);
        }
        if (DateTimeUtil.isTime(endStr)) {
            end = DateTimeUtil.fromTimeStr(endStr);
        }

        // 配置错误
        if (start == null || end == null) {
            return false;
        }

        LocalTime now = LocalTime.now();
        if (end.isBefore(start)) {
            // 跨天
            return (now.isBefore(end) || now.equals(end)) || (now.isAfter(start) || now.equals(start));
        } else if (end.isAfter(start)) {
            // 没跨天
            return isBetween(start, now, end);
        } else {
            return true;
        }
    }

    /**
     * 获取今天开始的时间，有偏移以偏移进行计算，否则00:00:00
     *
     * @param reportOffsetTime HH:mm:ss
     * @return LocalDateTime
     */
    public static LocalDateTime getOffsetTodayStart(String reportOffsetTime) {
        return getOffsetStartDateTime(0, reportOffsetTime);
    }

    /**
     * 获取今天结束的时间，有偏移以偏移进行计算，否则23:59:59
     *
     * @param reportOffsetTime HH:mm:ss
     * @return LocalDateTime
     */
    public static LocalDateTime getOffsetTodayEnd(String reportOffsetTime) {
        return getOffsetEndDateTime(0, reportOffsetTime);
    }

    /**
     * 获取某天的开始时间，有偏移就按偏移算，没偏移就按正常的00:00:00-23:59:59这样来算
     *
     * @param dateStr          某天
     * @param reportOffsetTime 不为空时按照报表时间进行偏移, HH:mm:ss
     * @return
     */
    public static LocalDateTime getOffsetStartDateTime(String dateStr, String reportOffsetTime) {
        LocalDate date = fromDateStrToLocalDate(dateStr);
        LocalDateTime dateTime;

        if (!shouldOffsetReportTime(reportOffsetTime)) {
            dateTime = date.atStartOfDay();
        } else {
            dateTime = date.atTime(fromTimeStr(reportOffsetTime));
        }

        return dateTime;
    }

    /**
     * 获取某天的开始时间，有偏移就按偏移算，没偏移就按正常的00:00:00-23:59:59这样来算
     *
     * @param dateStr          某天
     * @param reportOffsetTime 不为空时按照报表时间进行偏移, HH:mm:ss
     * @return
     */
    public static LocalDateTime getOffsetEndDateTime(String dateStr, String reportOffsetTime) {
        LocalDate date = fromDateStrToLocalDate(dateStr);
        LocalDateTime dateTime;

        if (!shouldOffsetReportTime(reportOffsetTime)) {
            dateTime = date.atTime(23, 59, 59);
        } else {
            dateTime = date.atTime(fromTimeStr(reportOffsetTime));
            dateTime = dateTime.minusSeconds(1);
        }

        return dateTime;
    }

    /**
     * 获取一天的开始时间，有偏移就按偏移算，没偏移就按正常的00:00:00-23:59:59这样来算
     * <p>
     * 看当前时间是否小于报表偏移时间
     * <p>
     * 案例1
     * 当前时间：15:00:00
     * 偏移时间：03:00:00
     * 当前时间大于等于偏移时间，结果：今天的03:00:00
     * <p>
     * 案例2
     * 当前时间：02:00:00
     * 偏移时间：03:00:00
     * 当前时间小于偏移时间，结果：昨天的03:00:00
     *
     * @param addDay           增减天数，0就是今天
     * @param reportOffsetTime 不为空时按照报表时间进行偏移, HH:mm:ss
     * @return
     */
    public static LocalDateTime getOffsetStartDateTime(long addDay, String reportOffsetTime) {
        LocalDateTime dateTime = LocalDateTime.now();
        if (addDay != 0) {
            dateTime = dateTime.plusDays(addDay);
        }

        if (!shouldOffsetReportTime(reportOffsetTime)) {
            dateTime = dateTime.toLocalDate().atStartOfDay();
        } else {
            dateTime = dateTime.toLocalDate().atTime(fromTimeStr(reportOffsetTime));
            if (isLowerThanTimeHMS(LocalTime.now(), reportOffsetTime)) {
                dateTime = dateTime.minusDays(1);
            }
        }

        return dateTime;
    }

    /**
     * 获取一天的结束时间，有偏移就按偏移算，没偏移就按正常的00:00:00-23:59:59这样来算
     * <p>
     * 看当前时间是否小于报表偏移时间
     * <p>
     * 案例1
     * 当前时间：15:00:00
     * 偏移时间：03:00:00
     * 当前时间大于等于偏移时间，结果：明天的02:59:59
     * <p>
     * 案例2
     * 当前时间：02:00:00
     * 偏移时间：03:00:00
     * 当前时间小于偏移时间，结果：今天的02:59:59
     *
     * @param addDay           增减天数，0就是今天
     * @param reportOffsetTime 不为空时按照报表时间进行偏移, HH:mm:ss
     * @return
     */
    public static LocalDateTime getOffsetEndDateTime(long addDay, String reportOffsetTime) {
        LocalDateTime dateTime = LocalDateTime.now();
        if (addDay != 0) {
            dateTime = dateTime.plusDays(addDay);
        }

        if (!shouldOffsetReportTime(reportOffsetTime)) {
            dateTime = dateTime.toLocalDate().atTime(23, 59, 59);
        } else {
            dateTime = dateTime.toLocalDate().atTime(fromTimeStr(reportOffsetTime));
            dateTime = dateTime.minusSeconds(1);
            if (!isLowerThanTimeHMS(LocalTime.now(), reportOffsetTime)) {
                dateTime = dateTime.plusDays(1);
            }
        }

        return dateTime;
    }

    /**
     * 看这个时间是属于哪一天
     *
     * @param dateTime         时间
     * @param reportOffsetTime 报表偏移配置，HH:mm:ss
     * @return
     */
    public static String getOffsetDateStrByDateTime(LocalDateTime dateTime, String reportOffsetTime) {
        if (!shouldOffsetReportTime(reportOffsetTime)) {
            return toDateStr(dateTime);
        }

        LocalTime localTime = dateTime.toLocalTime();
        if (isLowerThanTimeHMS(localTime, reportOffsetTime)) {
            // 小于则直接返回昨天
            LocalDateTime yesterday = dateTime.minusDays(1);
            return toDateStr(yesterday);
        }
        return toDateStr(dateTime);
    }

    /**
     * 获取日期来获取偏移的日期加时间
     * <p>
     * 案例1
     * 偏移时间：03:00:00
     * 开始日期：2020-02-28
     * 结束时间：2020-02-28
     * 结果：2020-02-28 03:00:00 ~ 2020-02-29 02:59:59
     * <p>
     * 案例2
     * 偏移时间：03:00:00
     * 开始日期：2020-02-26
     * 结束时间：2020-02-28
     * 当前时间小于偏移时间，结果：2020-02-26 03:00:00 ~ 2020-02-29 02:59:59
     *
     * @param startDate        开始日期
     * @param endDate          结束日期
     * @param reportOffsetTime 偏移时间
     * @return 偏移后的日期时间范围
     */
    public static LocalDateTime[] getOffsetDateTimeRange(String startDate, String endDate, String reportOffsetTime) {
        if (!shouldOffsetReportTime(reportOffsetTime)) {
            LocalDateTime startDateTime = fromDateTimeStr(patchHHMMSSForStartDate(startDate));

            LocalDateTime endDateTime = fromDateTimeStr(patchHHMMSSForEndDate(endDate));

            return new LocalDateTime[]{startDateTime, endDateTime};
        }

        LocalTime offsetTime = fromTimeStr(reportOffsetTime);

        LocalDateTime startDateTime = fromDateStr(startDate)
                .toLocalDate()
                .atTime(offsetTime);

        LocalDateTime endDateTime = fromDateStr(endDate)
                .plusDays(1)
                .toLocalDate()
                .atTime(offsetTime)
                .minusSeconds(1);

        return new LocalDateTime[]{startDateTime, endDateTime};
    }

    /**
     * 获取日期来获取偏移的日期加时间
     * <p>
     * 案例1
     * 偏移时间：03:00:00
     * 开始日期：2020-02-28
     * 结束时间：2020-02-28
     * 结果：2020-02-28 03:00:00 ~ 2020-02-29 02:59:59
     * <p>
     * 案例2
     * 偏移时间：03:00:00
     * 开始日期：2020-02-26
     * 结束时间：2020-02-28
     * 当前时间小于偏移时间，结果：2020-02-26 03:00:00 ~ 2020-02-29 02:59:59
     *
     * @param startDate        开始日期
     * @param endDate          结束日期
     * @param reportOffsetTime 偏移时间
     * @return 偏移后的日期时间范围
     */
    public static LocalDateTime[] getOffsetDateTimeRange(LocalDate startDate, LocalDate endDate, String reportOffsetTime) {
        if (!shouldOffsetReportTime(reportOffsetTime)) {
            LocalDateTime startDateTime = startDate.atStartOfDay();

            LocalDateTime endDateTime = endDate.atTime(23, 59, 59);

            return new LocalDateTime[]{startDateTime, endDateTime};
        }

        LocalTime offsetTime = fromTimeStr(reportOffsetTime);

        LocalDateTime startDateTime = startDate
                .atTime(offsetTime);

        LocalDateTime endDateTime = endDate
                .plusDays(1)
                .atTime(offsetTime)
                .minusSeconds(1);

        return new LocalDateTime[]{startDateTime, endDateTime};
    }

    /**
     * 获取从开始日期到结束日期和所有日期列表（包含开始和结束）
     *
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return 偏移后的日期时间范围
     */
    public static List<String> listDateRange(String startDate, String endDate) {
        LocalDateTime startDateTime = fromDateStr(startDate);
        LocalDateTime endDateTime = fromDateStr(endDate);

        long betweenDays = betweenDays(startDateTime, endDateTime);
        if (betweenDays < 0) {
            return new ArrayList<>();
        }

        List<String> dates = new ArrayList<>();

        for (int i = 0; i <= betweenDays; i++) {
            LocalDateTime dateTime = startDateTime.plusDays(i);
            dates.add(toDateStr(dateTime));
        }

        return dates;
    }

    /**
     * 判断时间是否小于指定时间
     *
     * @param now     指定时间
     * @param timeHMS 配置时间
     * @return
     */
    public static boolean isLowerThanTimeHMS(LocalTime now, String timeHMS) {
        if (!isTime(timeHMS)) {
            return false;
        }
        LocalTime offsetTime = fromTimeStr(timeHMS);
        return now.isBefore(offsetTime);
    }

    /**
     * 是否应该处理报表偏移时间
     *
     * @param reportOffsetTime 报表偏移配置
     * @return
     */
    public static boolean shouldOffsetReportTime(String reportOffsetTime) {
        if (!isTime(reportOffsetTime)) {
            return false;
        }
        // 转LocalTime
        LocalTime localTime = fromTimeStr(reportOffsetTime);

        int offsetHour = localTime.getHour();
        int offsetMinute = localTime.getMinute();
        int offsetSecond = localTime.getSecond();

        if (offsetHour == 0 && offsetMinute == 0 && offsetSecond == 0) {
            return false;
        }
        return true;
    }

    /**
     * 根据周期类型和当前时间，来计算周期的起始时间，已处理偏移
     *
     * @param cycleType        周期类型
     * @param offsetCycle      偏移周期，比如下一天/下一周/下一年等就是1，上一个周期就是-1，0就是不偏移
     * @param reportOffsetTime 报表偏移时间
     * @return
     */
    public static LocalDateTime[] getCycleDateTimeRange(CycleTypeEnum cycleType, int offsetCycle, String reportOffsetTime) {
        // 起始日期
        LocalDate startDate = null;
        LocalDate endDate = null;

        // 当前时间
        LocalDate nowDate = LocalDate.now();

        // 这里正常做日期即可，偏移在最后处理
        switch (cycleType) {
            case EVERY_DAY: {
                // 每天
                if (offsetCycle != 0) {
                    nowDate = nowDate.plusDays(offsetCycle);
                }
                startDate = nowDate;
                endDate = startDate;
                break;
            }
            case EVERY_HALF_WEEK: {
                // 每半周，周1~3为上半周，周4~7为下半周
                DayOfWeek halfWeek = DayOfWeek.WEDNESDAY;
                int halfWeekDays = halfWeek.getValue();

                // 当前星期几
                int nowDayOfWeek = nowDate.getDayOfWeek().getValue();

                // 偏移周期
                if (offsetCycle != 0) {
                    for (int i = 0; i < offsetCycle; i++) {
                        if (offsetCycle > 0) {
                            nowDate = nowDayOfWeek <= halfWeekDays ? nowDate.with(halfWeek).plusDays(1) : nowDate.with(DayOfWeek.SUNDAY).plusDays(1);
                        } else {
                            nowDate = nowDayOfWeek <= halfWeekDays ? nowDate.with(DayOfWeek.MONDAY).minusDays(1) : nowDate.with(halfWeek);
                        }
                        nowDayOfWeek = nowDate.getDayOfWeek().getValue();
                    }
                }

                if (nowDayOfWeek <= halfWeekDays) {
                    // 上半周
                    startDate = nowDate.with(DayOfWeek.MONDAY);
                    endDate = nowDate.with(halfWeek);
                } else {
                    // 下半周
                    startDate = nowDate.with(halfWeek.plus(1));
                    endDate = nowDate.with(DayOfWeek.SUNDAY);
                }
                break;
            }
            case EVERY_WEEK: {
                // 每周，周一到周天，跟月跟年都没关系，会跨月跨年
                nowDate = nowDate.plusWeeks(offsetCycle);
                startDate = nowDate.with(DayOfWeek.MONDAY);
                endDate = nowDate.with(DayOfWeek.SUNDAY);
                break;
            }
            case EVERY_HALF_MONTH: {
                // 每半月，月天数/2，多出的一天归到下半月

                // 半月天数
                int halfMonthDays = nowDate.lengthOfMonth() / 2;
                // 当前月第几天
                int nowDayOfMonth = nowDate.getDayOfMonth();

                // 偏移周期
                if (offsetCycle != 0) {
                    for (int i = 0; i < offsetCycle; i++) {
                        if (offsetCycle > 0) {
                            nowDate = nowDayOfMonth <= halfMonthDays ? nowDate.plusDays(halfMonthDays + 1) : nowDate.with(TemporalAdjusters.lastDayOfMonth()).plusDays(1);
                        } else {
                            nowDate = nowDayOfMonth <= halfMonthDays ? nowDate.with(TemporalAdjusters.firstDayOfMonth()).minusDays(1) : nowDate.withDayOfMonth(halfMonthDays);
                        }
                        halfMonthDays = nowDate.lengthOfMonth() / 2;
                        nowDayOfMonth = nowDate.getDayOfMonth();
                    }
                }

                if (nowDayOfMonth <= halfMonthDays) {
                    // 上半月
                    startDate = nowDate.with(TemporalAdjusters.firstDayOfMonth());
                    endDate = nowDate.withDayOfMonth(halfMonthDays);
                } else {
                    // 下半月
                    startDate = nowDate.withDayOfMonth(halfMonthDays + 1);
                    endDate = nowDate.with(TemporalAdjusters.lastDayOfMonth());
                }
                break;
            }
            case EVERY_MONTH: {
                // 每月，本月开始至结束
                nowDate = nowDate.plusMonths(offsetCycle);
                startDate = nowDate.with(TemporalAdjusters.firstDayOfMonth());
                endDate = nowDate.with(TemporalAdjusters.lastDayOfMonth());
                break;
            }
            case EVERY_2_MONTH: {
                // 每2月，把每年分成6个2月，看当前时间处于哪个段
                int[][] monthArr = new int[][]{{1, 2}, {3, 4}, {5, 6}, {7, 8}, {9, 10}, {11, 12}};

                // 周期偏移
                if (offsetCycle != 0) {
                    nowDate = nowDate.plusMonths(offsetCycle * 2);
                }

                // 当前月
                int nowMonth = nowDate.getMonthValue();
                int startMonth = -1;
                int endMonth = -1;

                for (int[] month : monthArr) {
                    for (int m : month) {
                        if (nowMonth == m) {
                            startMonth = month[0];
                            endMonth = month[1];
                            break;
                        }
                    }
                }
                startDate = nowDate.withMonth(startMonth).with(TemporalAdjusters.firstDayOfMonth());
                endDate = nowDate.withMonth(endMonth).with(TemporalAdjusters.lastDayOfMonth());
                break;
            }
            case EVERY_3_MONTH: {
                // 每3月，把每年分成4个3月，看当前时间处于哪个段
                int[][] monthArr = new int[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}, {10, 11, 12}};

                // 周期偏移
                if (offsetCycle != 0) {
                    nowDate = nowDate.plusMonths(offsetCycle * 3);
                }

                // 当前月
                int nowMonth = nowDate.getMonthValue();
                int startMonth = -1;
                int endMonth = -1;

                for (int[] month : monthArr) {
                    for (int m : month) {
                        if (nowMonth == m) {
                            startMonth = month[0];
                            endMonth = month[2];
                            break;
                        }
                    }
                }
                startDate = nowDate.withMonth(startMonth).with(TemporalAdjusters.firstDayOfMonth());
                endDate = nowDate.withMonth(endMonth).with(TemporalAdjusters.lastDayOfMonth());
                break;
            }
            case EVERY_HALF_YEAR: {
                // 每半年，把每年分成2个半年，看当前时间处于哪个段
                int[][] monthArr = new int[][]{{1, 2, 3, 4, 5, 6}, {7, 8, 9, 10, 11, 12}};

                // 周期偏移
                if (offsetCycle != 0) {
                    nowDate = nowDate.plusMonths(offsetCycle * 6);
                }

                // 当前月
                int nowMonth = nowDate.getMonthValue();
                int startMonth = -1;
                int endMonth = -1;

                for (int[] month : monthArr) {
                    for (int m : month) {
                        if (nowMonth == m) {
                            startMonth = month[0];
                            endMonth = month[5];
                            break;
                        }
                    }
                }
                startDate = nowDate.withMonth(startMonth).with(TemporalAdjusters.firstDayOfMonth());
                endDate = nowDate.withMonth(endMonth).with(TemporalAdjusters.lastDayOfMonth());
                break;
            }
            case EVERY_YEAR: {
                // 每年，今年开始至结束
                nowDate = nowDate.plusYears(offsetCycle);
                startDate = nowDate.with(TemporalAdjusters.firstDayOfYear());
                endDate = nowDate.with(TemporalAdjusters.lastDayOfYear());
                break;
            }
        }

        if (startDate == null || endDate == null) {
            return null;
        }

        // 处理偏移后返回
        return getOffsetDateTimeRange(startDate, endDate, reportOffsetTime);
    }

    /**
     * 获取某月的开始日期
     *
     * @param offset 0本月，1下月，-1上月，依次类推
     * @return
     */
    public static LocalDate monthStart(int offset) {
        return LocalDate.now().plusMonths(offset).with(TemporalAdjusters.firstDayOfMonth());
    }

    /**
     * 获取某月的结束日期
     *
     * @param offset 0本月，1下月，-1上月，依次类推
     * @return
     */
    public static LocalDate monthEnd(int offset) {
        return LocalDate.now().plusMonths(offset).with(TemporalAdjusters.lastDayOfMonth());
    }

    /**
     * 获取某周的开始日期
     *
     * @param offset 0本周，1下周，-1上周，依次类推
     * @return
     */
    public static LocalDate weekStart(int offset) {
        return LocalDate.now().plusWeeks(offset).with(DayOfWeek.MONDAY);
    }

    /**
     * 获取某周的结束日期
     *
     * @param offset 0本周，1下周，-1上周，依次类推
     * @return
     */
    public static LocalDate weekEnd(int offset) {
        return LocalDate.now().plusWeeks(offset).with(DayOfWeek.SUNDAY);
    }

    /**
     * 获取某季度的开始日期
     * 季度一年四季， 第一季度：2月-4月， 第二季度：5月-7月， 第三季度：8月-10月， 第四季度：11月-1月
     *
     * @param offset 0本季度，1下个季度，-1上个季度，依次类推
     * @return
     */
    public static LocalDate quarterStart(int offset) {
        final LocalDate date = LocalDate.now().plusMonths(offset * 3);
        int month = date.getMonth().getValue();// 当月
        int start = 0;
        if (month >= 2 && month <= 4) {// 第一季度
            start = 2;
        } else if (month >= 5 && month <= 7) {//第二季度
            start = 5;
        } else if (month >= 8 && month <= 10) {//第三季度
            start = 8;
        } else if ((month >= 11 && month <= 12)) {//第四季度
            start = 11;
        } else if (month == 1) {//第四季度
            start = 11;
            month = 13;
        }
        return date.plusMonths(start - month).with(TemporalAdjusters.firstDayOfMonth());
    }


    /**
     * 获取某年的开始日期
     *
     * @param offset 0今年，1明年，-1去年，依次类推
     * @return
     */
    public static LocalDate yearStart(int offset) {
        return LocalDate.now().plusYears(offset).with(TemporalAdjusters.firstDayOfYear());
    }

    /**
     * 获取某年的结束日期
     *
     * @param offset 0今年，1明年，-1去年，依次类推
     * @return
     */
    public static LocalDate yearEnd(int offset) {
        return LocalDate.now().plusYears(offset).with(TemporalAdjusters.lastDayOfYear());
    }

    /**
     * 判断是否超出最大过去查询天数
     *
     * @param date
     * @param maxQueryPastDay
     */
    public static void assertMaxQueryPastDay(LocalDate date, Long maxQueryPastDay) {
        if (date == null || !NumberUtil.isGreatThenZero(maxQueryPastDay)) {
            return;
        }

        LocalDate now = LocalDate.now();
        if (date.isAfter(now) || date.isEqual(now)) {
            return;
        }

        long betweenDays = betweenDays(date, now);
        if (betweenDays > maxQueryPastDay) {
            throw new ImException(ResponseCode.SYS_MAX_QUERY_PAST_DAY, new Object[]{maxQueryPastDay});
        }
    }
}
