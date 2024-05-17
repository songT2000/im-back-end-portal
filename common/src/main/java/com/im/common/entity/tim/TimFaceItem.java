package com.im.common.entity.tim;

import com.im.common.entity.base.BaseEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 表情包专辑元素
 */
@Data
@NoArgsConstructor
public class TimFaceItem extends BaseEntity {
    private static final long serialVersionUID = 16198109194919741L;
    /**
     * 表情包专辑的ID
     */
    private Long timFaceId;

    /**
     * 表情包序号
     */
    private Integer faceIndex;
    /**
     * 表情包地址 240*240
     */
    private String faceUrl;

}
