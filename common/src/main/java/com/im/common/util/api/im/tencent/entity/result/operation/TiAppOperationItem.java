package com.im.common.util.api.im.tencent.entity.result.operation;

import com.alibaba.fastjson.annotation.JSONField;
import com.im.common.util.api.im.tencent.entity.result.TiBaseResult;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 运营数据结果
 */
@Data
@NoArgsConstructor
public class TiAppOperationItem extends TiBaseResult {

    /**
     * APNs 推送数
     */
    @JSONField(name = "APNSMsgNum")
    private String apnsMsgNum;
    /**
     * 活跃用户数
     */
    @JSONField(name = "ActiveUserNum")
    private String activeUserNum;
    /**
     * 应用 SDKAppID
     */
    @JSONField(name = "AppId")
    private String appId;
    /**
     * 应用名称
     */
    @JSONField(name = "AppName")
    private String appName;
    /**
     * APNs 推送数（C2C）
     */
    @JSONField(name = "C2CAPNSMsgNum")
    private String c2cApnsMsgNum;
    /**
     * 消息下发数（C2C）
     */
    @JSONField(name = "C2CDownMsgNum")
    private String c2cDownMsgNum;
    /**
     * 发消息人数（C2C）
     */
    @JSONField(name = "C2CSendMsgUserNum")
    private String c2cSendMsgUserNum;
    /**
     * 上行消息数（C2C）
     */
    @JSONField(name = "C2CUpMsgNum")
    private String c2cUpMsgNum;
    /**
     * 回调请求数
     */
    @JSONField(name = "CallBackReq")
    private String callbackReq;
    /**
     * 回调应答数
     */
    @JSONField(name = "CallBackRsp")
    private String callbackRsp;
    /**
     * 关系链对数删除量
     */
    @JSONField(name = "ChainDecrease")
    private String chainDecrease;
    /**
     * 关系链对数增加量
     */
    @JSONField(name = "ChainIncrease")
    private String chainIncrease;
    /**
     * 组织名称
     */
    @JSONField(name = "Company")
    private String company;
    /**
     * 日期
     */
    @JSONField(name = "Date")
    private String date;
    /**
     * 消息下发数
     */
    @JSONField(name = "DownMsgNum")
    private String downMsgNum;
    /**
     * APNs 推送数（群）
     */
    @JSONField(name = "GroupAPNSMsgNum")
    private String groupApnsMsgNum;
    /**
     * 累计群组数
     */
    @JSONField(name = "GroupAllGroupNum")
    private String groupAllGroupNum;
    /**
     * 解散群个数
     */
    @JSONField(name = "GroupDestroyGroupNum")
    private String groupDestroyGroupNum;
    /**
     * 消息下发数（群）
     */
    @JSONField(name = "GroupDownMsgNum")
    private String groupDownMsgNum;
    /**
     * 入群总数
     */
    @JSONField(name = "GroupJoinGroupTimes")
    private String groupJoinGroupTimes;
    /**
     * 新增群组数
     */
    @JSONField(name = "GroupNewGroupNum")
    private String groupNewGroupNum;
    /**
     * 退群总数
     */
    @JSONField(name = "GroupQuitGroupTimes")
    private String groupQuitGroupTimes;
    /**
     * 发消息群组数
     */
    @JSONField(name = "GroupSendMsgGroupNum")
    private String groupSendMsgGroupNum;
    /**
     * 发消息人数（群）
     */
    @JSONField(name = "GroupSendMsgUserNum")
    private String groupSendMsgUserNum;
    /**
     * 上行消息数（群）
     */
    @JSONField(name = "GroupUpMsgNum")
    private String groupUpMsgNum;
    /**
     * 登录次数
     */
    @JSONField(name = "LoginTimes")
    private String loginTimes;
    /**
     * 登录人数
     */
    @JSONField(name = "LoginUserNum")
    private String loginUserNum;
    /**
     * 最高在线人数
     */
    @JSONField(name = "MaxOnlineNum")
    private String maxOnlineNum;
    /**
     * 新增注册人数
     */
    @JSONField(name = "RegistUserNumOneDay")
    private String registUserNumOneDay;
    /**
     * 累计注册人数
     */
    @JSONField(name = "RegistUserNumTotal")
    private String registUserNumTotal;
    /**
     * 发消息人数
     */
    @JSONField(name = "SendMsgUserNum")
    private String sendMsgUserNum;
    /**
     * 上行消息数
     */
    @JSONField(name = "UpMsgNum")
    private String upMsgNum;
}
