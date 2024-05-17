package com.im.common.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.im.common.entity.base.BaseEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 组用户
 *
 * @author Barry
 * @date 2022-03-12
 */
@Data
@NoArgsConstructor
public class UserGroupUser extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 582352057298575148L;

    public UserGroupUser(Long groupId, Long userId) {
        this.groupId = groupId;
        this.userId = userId;
    }

    public UserGroupUser(Long userId) {
        this.userId = userId;
    }

    /**
     * 分布式ID
     **/
    @TableId
    private Long id;

    /**
     * 组ID
     **/
    private Long groupId;

    /**
     * 用户ID
     **/
    private Long userId;
}
