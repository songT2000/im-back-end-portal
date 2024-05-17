package com.im.admin.controller;

import com.im.admin.config.permission.CheckPermission;
import com.im.admin.controller.url.ApiUrl;
import com.im.common.entity.enums.UserOperationLogTypeEnum;
import com.im.common.param.IdParam;
import com.im.common.param.SensitiveWordAddParam;
import com.im.common.param.SensitiveWordPageParam;
import com.im.common.response.RestResponse;
import com.im.common.service.SensitiveWordService;
import com.im.common.util.StrUtil;
import com.im.common.util.aop.log.UserOperationLog;
import com.im.common.util.mybatis.page.PageVO;
import com.im.common.vo.SensitiveWordVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * 表情包接口
 */
@RestController
@Api(tags = "敏感词接口")
public class SensitiveWordController extends BaseController {

    private SensitiveWordService sensitiveWordService;

    @Autowired
    public void setSensitiveWordService(SensitiveWordService sensitiveWordService) {
        this.sensitiveWordService = sensitiveWordService;
    }

    @RequestMapping(value = ApiUrl.SENSITIVE_WORD_PAGE, method = RequestMethod.POST)
    @ApiOperation("敏感词分页")
    @CheckPermission
    public RestResponse<PageVO<SensitiveWordVo>> page(SensitiveWordPageParam param) {
        PageVO<SensitiveWordVo> vo = sensitiveWordService.pageVO(param, SensitiveWordVo::new);
        return RestResponse.ok(vo);
    }

    @RequestMapping(value = ApiUrl.SENSITIVE_WORD_ADD, method = RequestMethod.POST)
    @ApiOperation("新增敏感词")
    @CheckPermission
    @UserOperationLog(type = UserOperationLogTypeEnum.SENSITIVE_WORD_ADD)
    public RestResponse add(@RequestBody @Valid SensitiveWordAddParam param) {
        //全角逗号替换成半角逗号，方便后续分割词组
        String words = param.getWords().replaceAll("，", ",");
        List<String> list = StrUtil.split(words, StrUtil.COMMA);
        return sensitiveWordService.add(list);
    }

    @RequestMapping(value = ApiUrl.SENSITIVE_WORD_DELETE, method = RequestMethod.POST)
    @ApiOperation("删除敏感词")
    @CheckPermission
    @UserOperationLog(type = UserOperationLogTypeEnum.SENSITIVE_WORD_DELETE)
    public RestResponse delete(@RequestBody @Valid IdParam param) {
        return sensitiveWordService.delete(param.getId());
    }
}
