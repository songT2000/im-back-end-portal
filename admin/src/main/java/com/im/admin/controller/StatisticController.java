package com.im.admin.controller;

import com.im.admin.controller.url.ApiUrl;
import com.im.common.entity.enums.ActionEnum;
import com.im.common.entity.tim.TimOperationStatistic;
import com.im.common.entity.tim.TimUserDeviceState;
import com.im.common.response.RestResponse;
import com.im.common.service.*;
import com.im.common.util.DateTimeUtil;
import com.im.common.util.LocalDateTimeUtil;
import com.im.common.vo.TimOperationStatisticVO;
import com.im.common.vo.TimStatisticVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 数据统计
 */
@RestController
@Api(tags = "数据统计接口")
public class StatisticController extends BaseController {

    private TimOperationStatisticService timOperationStatisticService;
    private TimMessageC2cService timMessageC2cService;
    private TimMessageGroupService timMessageGroupService;
    private TimUserDeviceStateService timUserDeviceStateService;
    private PortalUserService portalUserService;
    private TimGroupService timGroupService;

    @Autowired
    public void setTimOperationStatisticService(TimOperationStatisticService timOperationStatisticService) {
        this.timOperationStatisticService = timOperationStatisticService;
    }

    @Autowired
    public void setTimMessageC2cService(TimMessageC2cService timMessageC2cService) {
        this.timMessageC2cService = timMessageC2cService;
    }

    @Autowired
    public void setTimMessageGroupService(TimMessageGroupService timMessageGroupService) {
        this.timMessageGroupService = timMessageGroupService;
    }

    @Autowired
    public void setTimUserDeviceStateService(TimUserDeviceStateService timUserDeviceStateService) {
        this.timUserDeviceStateService = timUserDeviceStateService;
    }

    @Autowired
    public void setPortalUserService(PortalUserService portalUserService) {
        this.portalUserService = portalUserService;
    }

    @Autowired
    public void setTimGroupService(TimGroupService timGroupService) {
        this.timGroupService = timGroupService;
    }

    @RequestMapping(value = ApiUrl.TIM_STATISTIC, method = RequestMethod.POST)
    @ApiOperation("IM数据统计")
    public RestResponse<List<TimOperationStatisticVO>> query() {
        LocalDateTime lastMonth = LocalDateTimeUtil.offset(LocalDateTime.now(), -1, ChronoUnit.MONTHS);
        // 查询最近一个月的数据统计
        List<TimOperationStatistic> list = timOperationStatisticService.lambdaQuery()
                .ge(TimOperationStatistic::getStatisticDate, lastMonth)
                .orderByDesc(TimOperationStatistic::getStatisticDate)
                .list();
        if (list == null) {
            list = new ArrayList<>();
        }
        // 补齐数据(不要当天数据)
        LocalDateTime start = LocalDateTimeUtil.offset(LocalDateTime.now(), -1, ChronoUnit.DAYS);
        do {
            LocalDateTime finalStart = start;
            Optional<LocalDate> any = list.stream().map(TimOperationStatistic::getStatisticDate).filter(p ->
                    DateTimeUtil.toDateStr(p).equals(DateTimeUtil.toDateStr(finalStart))).findAny();
            if (!any.isPresent()) {
                list.add(new TimOperationStatistic(DateTimeUtil.fromDateStrToLocalDate(DateTimeUtil.toDateStr(finalStart))));
            }
            start = LocalDateTimeUtil.offset(start, -1, ChronoUnit.DAYS);
        } while (!DateTimeUtil.toDateStr(start).equals(DateTimeUtil.toDateStr(lastMonth)));

        // 消息总数
        int messageCount = timMessageC2cService.count() + timMessageGroupService.count();
        // 当前在线用户数
        Integer currentLoginCount = timUserDeviceStateService.lambdaQuery().eq(TimUserDeviceState::getAction, ActionEnum.Login).count();
        // 群组总数
        int groupCount = timGroupService.count();
        // 用户总数
        int userCount = portalUserService.count();
        // 格式化排序
        List<TimOperationStatisticVO> voList = list.stream().map(TimOperationStatisticVO::new)
                .sorted(Comparator.comparing(TimOperationStatisticVO::getStatisticDate)).collect(Collectors.toList());
        TimStatisticVO result = new TimStatisticVO();
        result.setMessageNumTotal(messageCount);
        result.setCurrentLoginUserNum(currentLoginCount);
        result.setGroupAllGroupNum(groupCount);
        result.setRegistUserNumTotal(userCount);
        result.setList(voList);

        return ok(result);
    }
}
