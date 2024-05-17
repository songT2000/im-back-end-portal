package com.im.common.util.api.im.tencent.entity.result.nospeaking;

import com.alibaba.fastjson.annotation.JSONField;
import com.im.common.util.api.im.tencent.entity.result.TiBaseResult;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 设置禁言
 */
@Data
@NoArgsConstructor
public class TiNoSpeakingResult extends TiBaseResult {

    /**
     * 单聊消息禁言时间，单位为秒，非负整数，最大值为4294967295
     * <br>0表示取消该帐号的群组消息禁言
     * <br>4294967295表示该帐号被设置永久禁言
     * <br>其它值表示该帐号的具体禁言时间
     */
    @JSONField(name = "C2CmsgNospeakingTime")
    private Long c2CMsgNospeakingTime;
    /**
     * 群组消息禁言时间，单位为秒，非负整数，最大值为4294967295
     * <br>0表示取消该帐号的群组消息禁言
     * <br>4294967295表示该帐号被设置永久禁言
     * <br>其它值表示该帐号的具体禁言时间
     */
    @JSONField(name = "GroupmsgNospeakingTime")
    private Long groupMsgNospeakingTime;
}
