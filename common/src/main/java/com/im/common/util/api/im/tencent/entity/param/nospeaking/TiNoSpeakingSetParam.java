package com.im.common.util.api.im.tencent.entity.param.nospeaking;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.LongCodec;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 设置禁言
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TiNoSpeakingSetParam implements Serializable {

    /**
     * 永久禁言
     */
    public static final long forever = 4294967295L;


    /**
     * 设置禁言配置的帐号
     */
    @JSONField(name = "Set_Account")
    private String setAccount;
    /**
     * 单聊消息禁言时间，单位为秒，非负整数，最大值为4294967295
     * <br>0表示取消该帐号的群组消息禁言
     * <br>4294967295表示该帐号被设置永久禁言
     * <br>其它值表示该帐号的具体禁言时间
     */
    @JSONField(name = "C2CmsgNospeakingTime",serializeUsing = LongCodec.class)
    private Long c2CMsgNospeakingTime;
    /**
     * 群组消息禁言时间，单位为秒，非负整数，最大值为4294967295
     * <br>0表示取消该帐号的群组消息禁言
     * <br>4294967295表示该帐号被设置永久禁言
     * <br>其它值表示该帐号的具体禁言时间
     */
    @JSONField(name = "GroupmsgNospeakingTime",serializeUsing = LongCodec.class)
    private Long groupMsgNospeakingTime;
}
