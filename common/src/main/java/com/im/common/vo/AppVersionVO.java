package com.im.common.vo;

import com.im.common.entity.AppVersion;
import com.im.common.entity.Bank;
import com.im.common.entity.enums.AppTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.cglib.beans.BeanCopier;

import java.time.LocalDateTime;

/**
 * app版本管理VO
 */
@Data
@ApiModel
public class AppVersionVO {
    public static final BeanCopier BEAN_COPIER = BeanCopier.create(AppVersion.class, AppVersionVO.class, false);

    public AppVersionVO(AppVersion appVersion) {
        BEAN_COPIER.copy(appVersion, this, null);
    }

    @ApiModelProperty(value = "ID", position = 1)
    private Long id;

    @ApiModelProperty(value = "应用类型，iOS或者android", position = 2)
    private AppTypeEnum appType;

    @ApiModelProperty(value = "版本号，字符型", position = 3)
    private String versionName;

    @ApiModelProperty(value = "版本数字代号，用于对比", position = 4)
    private int versionCode;

    @ApiModelProperty(value = "是否强制升级", position = 5)
    private Boolean compulsory;

    @ApiModelProperty(value = "版本说明", position = 6)
    private String note;

    @ApiModelProperty(value = "下载地址", position = 7)
    private String downloadUrl;

    @ApiModelProperty(value = "创建时间", position = 8)
    private LocalDateTime createTime;
}
