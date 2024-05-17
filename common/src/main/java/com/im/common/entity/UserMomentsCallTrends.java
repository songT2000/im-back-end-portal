package com.im.common.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.im.common.entity.base.BaseEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 朋友圈动态提示
 *
 * @author max.stark
 */
@Data
@NoArgsConstructor
public class UserMomentsCallTrends extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 2484877756248864592L;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 朋友圈ID
     */
    private Long momentsId;

    /**
     * 是否查看
     */
    @TableField("`read`")
    private Boolean read;
}
