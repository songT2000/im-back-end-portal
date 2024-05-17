package com.im.common.entity.tim;

import com.im.common.entity.base.BaseEntity;
import com.im.common.entity.enums.AddSourceTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 好友申请
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimFriendAwait extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 5376948128432348790L;

    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 好友用户ID
     */
    private Long friendUserId;
    /**
     * 发起加好友请求的用户的 ID
     */
    private Long initiatorUserId;
    /**
     * 附言
     */
    private String message;
    /**
     * 备注名
     */
    private String aliasName;
    /**
     * 分组名称
     */
    private String groupName;
    /**
     * 加好友来源,如AddSource_Type_Android
     */
    private AddSourceTypeEnum addSource;


}
