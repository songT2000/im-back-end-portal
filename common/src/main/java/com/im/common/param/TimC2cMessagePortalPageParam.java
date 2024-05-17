package com.im.common.param;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.im.common.entity.tim.TimMessageC2c;
import com.im.common.util.mybatis.page.AbstractPageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * 查询单聊消息分页参数
 */
@Data
@NoArgsConstructor
@ApiModel
public class TimC2cMessagePortalPageParam extends AbstractPageParam<TimMessageC2c> {

    @NotBlank
    @ApiModelProperty(value = "好友的账号", required = true)
    private String username;

    @ApiModelProperty(value = "搜索关键字")
    private String keyword;

    @ApiModelProperty(value = "消息类型,TIMImageElem=图像消息，TIMTextElem=文本消息，TIMFileElem=文件消息，TIMVideoFileElem=视频消息，为空是全部")
    private String msgType;

    private List<Long> userIds;


    @Override
    public Wrapper<TimMessageC2c> toQueryWrapper(Object wrapperParam) {
        //需要自己构建SQL
        return null;
    }
}
