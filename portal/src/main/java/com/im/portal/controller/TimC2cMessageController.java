package com.im.portal.controller;

import cn.hutool.core.collection.ListUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.im.common.entity.enums.PortalTypeEnum;
import com.im.common.entity.enums.UserOperationLogTypeEnum;
import com.im.common.entity.tim.TimMessageBody;
import com.im.common.entity.tim.TimMessageC2c;
import com.im.common.param.TimC2cMessagePortalExportParam;
import com.im.common.param.TimC2cMessagePortalPageParam;
import com.im.common.response.RestResponse;
import com.im.common.service.TimMessageC2cElemRelService;
import com.im.common.service.TimMessageC2cService;
import com.im.common.util.CollectionUtil;
import com.im.common.util.RandomUtil;
import com.im.common.util.StrUtil;
import com.im.common.util.TimMessageUtil;
import com.im.common.util.aop.log.UserOperationLog;
import com.im.common.util.mybatis.page.PageVO;
import com.im.common.util.user.UserUtil;
import com.im.common.vo.PortalSessionUser;
import com.im.common.vo.TimMessageC2cExportVO;
import com.im.common.vo.TimMessageC2cVO;
import com.im.portal.controller.url.ApiUrl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 单聊消息相关接口
 */
@RestController
@Api(tags = "单聊消息相关接口")
public class TimC2cMessageController extends BaseController {

    private TimMessageC2cService timMessageC2cService;
    private TimMessageC2cElemRelService timMessageC2cElemRelService;

    @Autowired
    public void setTimMessageC2cService(TimMessageC2cService timMessageC2cService) {
        this.timMessageC2cService = timMessageC2cService;
    }

    @Autowired
    public void setTimMessageC2cElemRelService(TimMessageC2cElemRelService timMessageC2cElemRelService) {
        this.timMessageC2cElemRelService = timMessageC2cElemRelService;
    }

    @RequestMapping(value = ApiUrl.TIM_C2C_MESSAGE_EXPORT, method = RequestMethod.POST)
    @UserOperationLog(type = UserOperationLogTypeEnum.TIM_C2C_MESSAGE_EXPORT)
    @ApiOperation("导出单聊消息")
    public void export(@RequestBody @Valid TimC2cMessagePortalExportParam param, HttpServletResponse response) throws IOException {
        List<TimMessageC2c> timMessageC2cList = timMessageC2cService.list(param.toQueryWrapper());
        if (CollectionUtil.isEmpty(timMessageC2cList)) {
            return;
        }
        List<TimMessageC2cVO> records = timMessageC2cList.stream().map(TimMessageC2cVO::new).collect(Collectors.toList());
        List<Long> messageIds = records.stream().map(TimMessageC2cVO::getId).collect(Collectors.toList());
        List<TimMessageBody> list = timMessageC2cElemRelService.getByIds(messageIds);
        for (TimMessageC2cVO record : records) {
            List<TimMessageBody> messageBodies = list.stream()
                    .filter(p -> p.getMessageId().equals(record.getId()))
                    .map(TimMessageUtil::customMessageConvert)
                    .collect(Collectors.toList());
            record.setMsgBody(messageBodies);
        }

        List<TimMessageC2cExportVO> vos = records.stream().map(TimMessageC2cExportVO::new).collect(Collectors.toList());

        // 导出
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        String fileName = StrUtil.format("c2c-message-{}", RandomUtil.randomToken() + ".xlsx");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName);
        response.setHeader("FileName", fileName);
        response.setHeader("Access-Control-Expose-Headers", "FileName");

        EasyExcel
                .write(response.getOutputStream(), TimMessageC2cExportVO.class)
                .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                .sheet()
                .doWrite(vos);
    }

    @RequestMapping(value = ApiUrl.TIM_C2C_MESSAGE_LIST, method = RequestMethod.POST)
    @UserOperationLog(type = UserOperationLogTypeEnum.TIM_C2C_MESSAGE_LIST)
    @ApiOperation("查询单聊消息")
    public RestResponse<PageVO<TimMessageC2cVO>> getTimFaceList(@RequestBody @Valid TimC2cMessagePortalPageParam param, HttpServletRequest request) {
        PortalSessionUser sessionUser = getSessionUser(request);
        Long friendUserId = UserUtil.getUserIdByUsernameFromLocal(param.getUsername(), PortalTypeEnum.PORTAL);
        param.setUserIds(ListUtil.of(friendUserId, sessionUser.getId()));
        return ok(timMessageC2cService.pageFormPortal(param));
    }
}
