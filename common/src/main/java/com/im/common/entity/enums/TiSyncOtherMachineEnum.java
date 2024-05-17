package com.im.common.entity.enums;

/**
 * 同步到发送方策略
 */
public enum TiSyncOtherMachineEnum implements BaseEnum {
    SYNC("1", "把消息同步到 From_Account 在线终端和漫游上"),
    NO_SYNC("2", "消息不同步至 From_Account");

    private String val;
    private String str;

    TiSyncOtherMachineEnum(String val, String str) {
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
