package com.im.common.entity.enums;

/**
 * 朋友圈动态类型
 *
 * @author Max
 */
public enum TrendsReviewTypeEnum implements BaseEnum {
    /**
     * 点赞
     **/
    LIKE("1", "点赞"),

    /**
     * 评论
     **/
    REVIEW("2", "评论");

    private String val;
    private String str;

    TrendsReviewTypeEnum(String val, String str) {
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
