package com.im.common.entity;

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
public class UserMoments extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 5260217774094407718L;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 文字内容
     */
    private String content;

    /**
     * 图片链接地址，英文逗号分割，最多9个
     */
    private String imgUrls;

    /**
     * 视频链接地址，只允许一个
     */
    private String videoUrls;

    /**
     * 发布的IP地址
     */
    private String ip;

    /**
     * 发布的位置
     */
    private String address;

    /**
     * 朋友圈类型
     */
    private TrendsTypeEnum trendsType;


}
