package com.im.common.entity.tim;

import cn.hutool.core.date.DatePattern;
import com.im.common.entity.base.BaseEntity;
import com.im.common.util.api.im.tencent.entity.result.operation.TiAppOperationItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.beans.BeanCopier;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * 运营数据统计
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimOperationStatistic extends BaseEntity {
    private static final long serialVersionUID = -8040805736423929738L;

    public static final BeanCopier BEAN_COPIER = BeanCopier.create(TiAppOperationItem.class, TimOperationStatistic.class, true);

    public  TimOperationStatistic(TiAppOperationItem item) {
        BEAN_COPIER.copy(item, this, (o, aClass, o1) -> {
            if (o == null) {
                return null;
            }
            if(o instanceof String){
                return Integer.valueOf((String) o);
            }
            return null;
        });
        this.statisticDate = LocalDate.parse(item.getDate(), DateTimeFormatter.ofPattern(DatePattern.PURE_DATE_PATTERN));
    }

    public TimOperationStatistic(LocalDate statisticDate) {
        this.statisticDate = statisticDate;
    }

    /**
     * APNs 推送数
     */
    private Integer apnsMsgNum;
    /**
     * 活跃用户数
     */
    private Integer activeUserNum;
    /**
     * APNs 推送数（C2C）
     */
    private Integer c2cApnsMsgNum;
    /**
     *  消息下发数（C2C）
     */
    private Integer c2cDownMsgNum;
    /**
     * 发消息人数（C2C）
     */
    private Integer c2cSendMsgUserNum;
    /**
     * 上行消息数（C2C）
     */
    private Integer c2cUpMsgNum;
    /**
     * 回调请求数
     */
    private Integer callbackReq;
    /**
     * 回调应答数
     */
    private Integer callbackRsp;
    /**
     * 关系链对数删除量
     */
    private Integer chainDecrease;
    /**
     * 关系链对数增加量
     */
    private Integer chainIncrease;
    /**
     * 统计日期
     */
    private LocalDate statisticDate;
    /**
     * 消息下发数
     */
    private Integer downMsgNum;
    /**
     * APNs 推送数（群）
     */
    private Integer groupApnsMsgNum;
    /**
     * 累计群组数
     */
    private Integer groupAllGroupNum;
    /**
     * 解散群个数
     */
    private Integer groupDestroyGroupNum;
    /**
     * 消息下发数（群）
     */
    private Integer groupDownMsgNum;
    /**
     * 入群总数
     */
    private Integer groupJoinGroupTimes;
    /**
     * 新增群组数
     */
    private Integer groupNewGroupNum;
    /**
     * 退群总数
     */
    private Integer groupQuitGroupTimes;
    /**
     * 发消息群组数
     */
    private Integer groupSendMsgGroupNum;
    /**
     * 发消息人数（群）
     */
    private Integer groupSendMsgUserNum;
    /**
     * 上行消息数（群）
     */
    private Integer groupUpMsgNum;
    /**
     * 登录次数
     */
    private Integer loginTimes;
    /**
     * 登录人数
     */
    private Integer loginUserNum;
    /**
     * 最高在线人数
     */
    private Integer maxOnlineNum;
    /**
     * 新增注册人数
     */
    private Integer registUserNumOneDay;
    /**
     * 累计注册人数
     */
    private Integer registUserNumTotal;
    /**
     * 发消息人数
     */
    private Integer sendMsgUserNum;
    /**
     * 上行消息数
     */
    private Integer upMsgNum;

}
