package com.im.common.util.api.im.tencent.entity.param.message;

import cn.hutool.core.collection.ListUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.im.common.entity.enums.TiMsgTypeEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 修改取料历史消息体
 */
@Data
@NoArgsConstructor
public class TiModifyGroupMessageParam implements Serializable {

    private static final long serialVersionUID = -1904447140542257378L;
    /**
     * 必填
     * <p>
     * 操作的群 ID
     */
    @JSONField(name = "GroupId")
    private String groupId;

    /**
     * 必填
     * <p>
     * 请求修改的消息 seq
     */
    @JSONField(name = "MsgSeq")
    private Long msgSeq;

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

    /**
     * 修改发红包消息
     *
     * @param groupId 群组ID
     * @param item    红包消息内容
     * @param msgSeq  发红包消息的序列号
     */
    public TiModifyGroupMessageParam(String groupId, TiMsgCustomItem item, Long msgSeq) {
        this.groupId = groupId;
        this.msgSeq = msgSeq;

        String data = JSON.toJSONString(item);
        TIMCustomElem customElem = new TIMCustomElem();
        customElem.setData(data);
        customElem.setExt(item.getBusinessID());

        TiMsgBody tiMsgBody = new TiMsgBody();
        tiMsgBody.setMsgContent(customElem);
        tiMsgBody.setMsgType(TiMsgTypeEnum.TIMCustomElem);

        this.msgBody = ListUtil.of(tiMsgBody);

    }
}
