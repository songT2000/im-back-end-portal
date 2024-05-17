package com.im.common.vo;

import com.im.common.entity.base.BaseEntity;
import com.im.common.entity.tim.TimOperationStatistic;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.beans.BeanCopier;

import java.time.LocalDate;
import java.util.List;

/**
 * 运营数据统计
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimStatisticVO {

    @ApiModelProperty("累计注册人数")
    private Integer registUserNumTotal;

    @ApiModelProperty("累计群组数")
    private Integer groupAllGroupNum;

    @ApiModelProperty("累计消息数")
    private Integer messageNumTotal;

    @ApiModelProperty("当前在线用户数")
    private Integer currentLoginUserNum;

    @ApiModelProperty("每日统计数据")
    private List<TimOperationStatisticVO> list;

}
