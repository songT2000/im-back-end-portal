package com.im.common.entity.tim;

import com.baomidou.mybatisplus.annotation.TableField;
import com.im.common.entity.base.BaseEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 表情包专辑
 */
@Data
@NoArgsConstructor
public class TimFace extends BaseEntity {
    private static final long serialVersionUID = 1807364873963538197L;
    /**
     * 专辑名称
     */
    private String faceName;
    /**
     * 聊天面板图标，50*50
     */
    private String chatPanelIcon;

    /**
     * 表情专辑内含的表情
     */
    @TableField(exist = false)
    private List<TimFaceItem> items;

}
