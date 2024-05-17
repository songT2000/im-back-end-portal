package com.im.common.util.api.im.tencent.entity.param.portrait;

import com.im.common.entity.enums.BaseEnum;

/**
 * 资料
 */
public enum TiAccountPortraitTagEnum implements BaseEnum {
    /**
     * 昵称，长度不得超过500个字节
     **/
    NICK("Tag_Profile_IM_Nick", "昵称"),
    /**
     * Gender_Type_Unknown：没设置性别
     * Gender_Type_Female：女性
     * Gender_Type_Male：男性
     */
    GENDER("Tag_Profile_IM_Gender", "性别"),
    /**
     * 推荐用法：20190419
     */
    BIRTHDAY("Tag_Profile_IM_BirthDay", "生日"),
    /**
     * 长度不得超过16个字节，推荐用法如下：
     * App 本地定义一套数字到地名的映射关系
     * 后台实际保存的是4个 uint32_t 类型的数字
     * 其中第一个 uint32_t 表示国家
     * 第二个 uint32_t 用于表示省份
     * 第三个 uint32_t 用于表示城市
     * 第四个 uint32_t 用于表示区县
     */
    LOCATION("Tag_Profile_IM_Location", "所在地"),
    /**
     * 长度不得超过500个字节
     */
    SELF_SIGNATURE("Tag_Profile_IM_SelfSignature", "个性签名"),
    /**
     * 加好友验证方式
     * AllowType_Type_NeedConfirm：需要经过自己确认对方才能添加自己为好友
     * AllowType_Type_AllowAny：允许任何人添加自己为好友
     * AllowType_Type_DenyAny：不允许任何人添加自己为好友
     */
    ALLOW_TYPE("Tag_Profile_IM_AllowType", "加好友验证方式"),
    /**
     * 语言
     */
    LANGUAGE("Tag_Profile_IM_Language", "语言"),
    /**
     * 头像URL，长度不得超过500个字节
     */
    IMAGE("Tag_Profile_IM_Image", "头像URL"),
    /**
     * 消息设置，标志位：Bit0：置0表示接收消息，置1则不接收消息
     */
    MSG_SETTINGS("Tag_Profile_IM_MsgSettings", "消息设置"),
    /**
     * 管理员禁止加好友标识，AdminForbid_Type_None：默认值，允许加好友，AdminForbid_Type_SendOut：禁止该用户发起加好友请求
     */
    ADMIN_FORBID_TYPE("Tag_Profile_IM_AdminForbidType", "管理员禁止加好友标识"),
    /**
     * 等级，通常一个 UINT-8 数据即可保存一个等级信息，您可以考虑拆分保存，从而实现多种角色的等级信息
     */
    LEVEL("Tag_Profile_IM_Level", "等级"),
    /**
     * 角色，通常一个 UINT-8 数据即可保存一个角色信息，您可以考虑拆分保存，从而保存多种角色信息(暂定用于用户是否可用，0：禁用，1：可用，null：可用)
     */
    ROLE("Tag_Profile_IM_Role", "角色");

    private String val;
    private String str;

    TiAccountPortraitTagEnum(String val, String str) {
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
