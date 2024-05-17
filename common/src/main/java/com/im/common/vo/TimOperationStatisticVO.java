package com.im.common.vo;

import com.im.common.entity.tim.TimOperationStatistic;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.beans.BeanCopier;

import java.time.LocalDate;

/**
 * 每日运营数据统计
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimOperationStatisticVO {

    public static final BeanCopier BEAN_COPIER = BeanCopier.create(TimOperationStatistic.class, TimOperationStatisticVO.class, false);

    public TimOperationStatisticVO(TimOperationStatistic item) {
        BEAN_COPIER.copy(item, this, null);
    }

    /**
     * 统计日期
     */
    @ApiModelProperty("统计日期")
    private LocalDate statisticDate;
    /**
     * 活跃用户数
     */
    @ApiModelProperty("活跃用户数")
    private Integer activeUserNum;
    /**
     *  消息下发数（C2C）
     */
    @ApiModelProperty("消息下发数（C2C）")
    private Integer c2cDownMsgNum;

    /**
     * 消息下发数
     */
    @ApiModelProperty("消息下发数")
    private Integer downMsgNum;

    /**
     * 消息下发数（群）
     */
    @ApiModelProperty("消息下发数（群）")
    private Integer groupDownMsgNum;
    /**
     * 累计群组数
     */
    @ApiModelProperty("累计群组数")
    private Integer groupAllGroupNum;
    /**
     * 登录次数
     */
    @ApiModelProperty("登录次数")
    private Integer loginTimes;
    /**
     * 登录人数
     */
    @ApiModelProperty("登录人数")
    private Integer loginUserNum;
    /**
     * 最高在线人数
     */
    @ApiModelProperty("最高在线人数")
    private Integer maxOnlineNum;
    /**
     * 新增注册人数
     */
    @ApiModelProperty("新增注册人数")
    private Integer registUserNumOneDay;
    /**
     * 累计注册人数
     */
    @ApiModelProperty("累计注册人数")
    private Integer registUserNumTotal;

}
