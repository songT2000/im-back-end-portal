package com.im.common.entity.enums;

/**
 * 添加好友来源
 */
public enum AddSourceTypeEnum implements BaseEnum {
    /**
     * android
     **/
    ANDROID("AddSource_Type_Android", "Android"),
    /**
     * iOS
     **/
    iOS("AddSource_Type_Android", "iOS"),
    /**
     * Web
     **/
    Web("AddSource_Type_Web", "Web"),
    /**
     * H5
     **/
    H5("AddSource_Type_Web", "H5"),
    /**
     * Windows
     **/
    Windows("AddSource_Type_Windows", "Windows"),
    /**
     * Mac
     **/
    Mac("AddSource_Type_Mac", "Mac"),
    /**
     * Other
     **/
    Other("AddSource_Type_Other", "Other"),
    /**
     * Unknown
     **/
    Unknown("AddSource_Type_Unknown", "Unknown"),
    /**
     * RestApi
     **/
    RESTAPI("AddSource_Type_RestApi", "RestApi"),

    /**
     * INVITE_CODE
     **/
    INVITE_CODE("AddSource_Type_Code", "Code"),
    ;

    private String val;
    private String str;

    AddSourceTypeEnum(String val, String str) {
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
