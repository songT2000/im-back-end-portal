package com.im.common.util.api.im.tencent.entity.param.message;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.im.common.entity.enums.TiMsgTypeEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 修改单聊消息体
 */
@Data
@NoArgsConstructor
public class TiModifyC2cMessageParam implements Serializable {

    private static final long serialVersionUID = 4104331082297530040L;
    /**
     * 必填
     * <p>
     * 消息发送方 UserID（不填就是管理员）
     */
    @JSONField(name = "From_Account")
    private String fromAccount;

    /**
     * 必填
     * <p>
     * 消息接收方 UserID
     */
    @JSONField(name = "To_Account")
    private String toAccount;

    /**
     * 必填
     * <p>
     * 待修改消息的唯一标识。
     */
    @JSONField(name = "MsgKey")
    private String msgKey;


    /**
     * 必填
     * <p>
     * 消息内容，具体格式请参考
     *
     * @see TiMsgBody
     * （注意，一条消息可包括多种消息元素，MsgBody 为 集合类型）
     */
    @JSONField(name = "MsgBody")
    private List<TiMsgBody> msgBody;


    public TiModifyC2cMessageParam(String fromAccount, String toAccount, String msgKey, TiMsgCustomItem item) {
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;

        String data = JSON.toJSONString(item);
        TIMCustomElem customElem = new TIMCustomElem();
        customElem.setData(data);
        customElem.setExt(item.getBusinessID());

        TiMsgBody tiMsgBody = new TiMsgBody();
        tiMsgBody.setMsgContent(customElem);
        tiMsgBody.setMsgType(TiMsgTypeEnum.TIMCustomElem);

        this.msgBody = new ArrayList<>();
        this.msgBody.add(tiMsgBody);

        this.msgKey = msgKey;
    }
}
