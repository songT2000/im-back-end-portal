package com.im.common.vo;

import com.im.common.entity.SysNotice;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.beans.BeanCopier;

import java.time.LocalDateTime;

/**
 * 系统公告
 *
 * @author max.stark
 */
@Data
@NoArgsConstructor
@ApiModel
public class SysNoticePortalVO {
    public static final BeanCopier BEAN_COPIER = BeanCopier.create(SysNotice.class, SysNoticePortalVO.class, false);

    public SysNoticePortalVO(SysNotice e) {
        BEAN_COPIER.copy(e, this, null);
    }

    @ApiModelProperty("ID")
    private Long id;

    @ApiModelProperty("公告标题")
    private String title;

    @ApiModelProperty("公告内容")
    private String content;

    @ApiModelProperty("公告简介")
    private String simpleContent;

    @ApiModelProperty("排序")
    private Integer sort;

    @ApiModelProperty("是否置顶")
    private Boolean top;

    @ApiModelProperty("语言编码")
    private String languageCode;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("最后修改时间")
    private LocalDateTime updateTime;
}
