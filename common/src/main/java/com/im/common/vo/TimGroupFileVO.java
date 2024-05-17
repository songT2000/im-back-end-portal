package com.im.common.vo;

import com.im.common.entity.tim.TimGroupFile;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.beans.BeanCopier;

import java.time.LocalDateTime;

/**
 * 群文件信息
 */
@Data
@NoArgsConstructor
@ApiModel
public class TimGroupFileVO {
    public static final BeanCopier BEAN_COPIER = BeanCopier.create(TimGroupFile.class, TimGroupFileVO.class, false);

    public TimGroupFileVO(TimGroupFile e) {
        BEAN_COPIER.copy(e, this, null);
    }

    @ApiModelProperty(value = "群文件的ID",position = 1)
    private Long id;

    @ApiModelProperty(value = "群组的ID",position = 2)
    private String groupId;

    @ApiModelProperty(value = "群文件地址",position = 3)
    private String url;

    @ApiModelProperty(value = "群文件后缀",position = 4)
    private String postfix;

    @ApiModelProperty(value = "群文件大小，单位kb",position = 5)
    private Double size;

    @ApiModelProperty(value = "群文件名称",position = 6)
    private String fileName;

    @ApiModelProperty(value = "上传时间",position = 7)
    private LocalDateTime createTime;
}
