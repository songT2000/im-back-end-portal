package com.im.common.util.api.im.tencent.entity.param.message;

import com.im.common.entity.enums.CustomMessageTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 自定义消息内容
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TiMsgCustomItem implements Serializable {

    private static final long serialVersionUID = 8473590026661208962L;

    /**
     * 业务类型
     */
    private String businessID;

    /**
     * 消息内容
     */
    private Object content;

    /**
     * 操作用户的账号
     */
    private String opUser;

}
