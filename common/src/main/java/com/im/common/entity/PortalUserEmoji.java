package com.im.common.entity;

import com.im.common.entity.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * 用户自定义表情包
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PortalUserEmoji extends BaseEntity implements Serializable {

    /**
     * 用户ID
     **/
    private Long userId;

    /**
     * 表情地址
     */
    private String url;
}
