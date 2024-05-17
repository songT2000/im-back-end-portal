package com.im.common.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.im.common.entity.base.BaseEntity;
import com.im.common.entity.enums.TrendsTypeEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 朋友圈记录
 *
 * @author max.stark
 */
@Data
@NoArgsConstructor
public class UserMomentsTrends extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 3464596996418711554L;
    /**
     * 用户ID，0表示所有好友可以看
     */
    private Long userId;

    /**
     * 朋友圈ID
     */
    private Long momentsId;

    /**
     * 是否允许查看
     */
    @TableField("`allow`")
    private Boolean allow;

}
