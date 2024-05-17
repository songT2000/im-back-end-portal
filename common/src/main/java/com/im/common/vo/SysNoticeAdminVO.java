package com.im.common.vo;

import com.im.common.cache.impl.I18nLanguageCache;
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
public class SysNoticeAdminVO {
    private static final BeanCopier BEAN_COPIER = BeanCopier.create(SysNotice.class, SysNoticeAdminVO.class, false);

    public SysNoticeAdminVO(SysNotice e, I18nLanguageCache i18nLanguageCache) {
        BEAN_COPIER.copy(e, this, null);

        this.languageName = i18nLanguageCache.getNameByCodeFromLocal(e.getLanguageCode());
    }

    @ApiModelProperty(value = "ID", position = 1)
    private Long id;

    @ApiModelProperty(value = "标题", position = 2)
    private String title;

    @ApiModelProperty(value = "内容", position = 3)
    private String content;

    @ApiModelProperty(value = "简介", position = 4)
    private String simpleContent;

    @ApiModelProperty(value = "排序", position = 5)
    private Integer sort;

    @ApiModelProperty(value = "是否显示", position = 6)
    private Boolean showing;

    @ApiModelProperty(value = "是否置顶", position = 7)
    private Boolean top;

    @ApiModelProperty(value = "语言编码", position = 8)
    private String languageCode;

    @ApiModelProperty(value = "语言名称", position = 9)
    private String languageName;

    @ApiModelProperty(value = "创建时间", position = 10)
    private LocalDateTime createTime;

    @ApiModelProperty(value = "最后修改时间", position = 11)
    private LocalDateTime updateTime;
}
