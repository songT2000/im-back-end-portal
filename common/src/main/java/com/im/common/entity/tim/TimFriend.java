package com.im.common.entity.tim;

import com.im.common.entity.base.BaseEntity;
import com.im.common.entity.enums.AddSourceTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

/**
 * 好友关系
 * @author max.stark
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimFriend extends BaseEntity implements Serializable {
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

    public TimFriend(Long userId, Long friendUserId) {
        this.userId = userId;
        this.friendUserId = friendUserId;
    }

    public TimFriend(Long userId, Long friendUserId, AddSourceTypeEnum addSource) {
        this.userId = userId;
        this.friendUserId = friendUserId;
        this.addSource = addSource;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TimFriend)) {
            return false;
        }
        TimFriend timFriend = (TimFriend) o;
        return Objects.equals(getUserId(), timFriend.getUserId()) && Objects.equals(getFriendUserId(), timFriend.getFriendUserId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUserId(), getFriendUserId());
    }
}
