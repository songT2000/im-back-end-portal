package com.im.common.entity;

import com.im.common.entity.base.BaseEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 系统默认头像，目前暂时只用于了后台批量生成用户时的头像选择
 *
 * @author Barry
 * @date 2022-04-07
 */
@Data
@NoArgsConstructor
public class SysDefaultAvatar extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1708077019929421236L;

    /**
     * URL
     **/
    private String url;
}
