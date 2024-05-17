package com.im.common.entity.enums;

/**
 * 周期类型
 *
 * @author Barry
 * @date 2020-04-02
 */
public enum CycleTypeEnum implements BaseEnum {
    /**
     * 每天
     */
    EVERY_DAY("EVERY_DAY", "每天"),
    /**
     * 每半周，周1~3为上半周，周4~7为下半周
     */
    EVERY_HALF_WEEK("EVERY_HALF_WEEK", "每半周"),
    /**
     * 每周，周一到周天，跟月跟年都没关系，会跨月跨年
     */
    EVERY_WEEK("EVERY_WEEK", "每周"),
    /**
     * 每半月，月天数/2，多出的一天归到下半月
     */
    EVERY_HALF_MONTH("EVERY_HALF_MONTH", "每半月"),
    /**
     * 每月，本月开始至结束
     */
    EVERY_MONTH("EVERY_MONTH", "每月"),
    /**
     * 每2月，把每年分成6个2月，看当前时间处于哪个段
     */
    EVERY_2_MONTH("EVERY_2_MONTH", "每2月"),
    /**
     * 每3月每季度，把每年分成4个3月，看当前时间处于哪个段
     */
    EVERY_3_MONTH("EVERY_3_MONTH", "每季度"),
    /**
     * 每半年，把每年分成2个半年，看当前时间处于哪个段
     */
    EVERY_HALF_YEAR("EVERY_HALF_YEAR", "每半年"),
    /**
     * 每年，今年开始至结束
     */
    EVERY_YEAR("EVERY_YEAR", "每年");

    private String val;
    private String str;

    CycleTypeEnum(String val, String str) {
        this.val = val;
        this.str = str;
    }

    @Override
    public String getVal() {
        return val;
    }

    @Override
    public String getStr() {
        return str;
    }

    @Override
    public String toString() {
        return this.val;
    }
}
