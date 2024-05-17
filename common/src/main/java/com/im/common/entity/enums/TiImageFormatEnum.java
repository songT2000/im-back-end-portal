package com.im.common.entity.enums;

/**
 * 图片格式类型
 */
public enum TiImageFormatEnum implements BaseEnum {
    JPG("1", "jpg"),
    GIF("2", "gif"),
    PNG("3", "png"),
    BMP("4", "bmp"),
    OTHER("255", "其他"),
    ;

    private String val;
    private String str;

    TiImageFormatEnum(String val, String str) {
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
