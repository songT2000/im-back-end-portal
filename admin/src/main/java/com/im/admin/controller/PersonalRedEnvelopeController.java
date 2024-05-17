package com.im.admin.controller;

import com.im.admin.config.permission.CheckPermission;
import com.im.admin.controller.url.ApiUrl;
import com.im.common.param.PersonalRedEnvelopePageAdminParam;
import com.im.common.response.RestResponse;
import com.im.common.service.PersonalRedEnvelopeService;
import com.im.common.util.mybatis.page.PageVO;
import com.im.common.vo.PersonalRedEnvelopeAdminVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 个人红包Controller
 *
 * @author Barry
 * @date 2021-06-24
 */
@RestController
@Api(tags = "个人红包相关接口")
public class PersonalRedEnvelopeController extends BaseController {
    private PersonalRedEnvelopeService personalRedEnvelopeService;

    @Autowired
    public void setPersonalRedEnvelopeService(PersonalRedEnvelopeService personalRedEnvelopeService) {
        this.personalRedEnvelopeService = personalRedEnvelopeService;
    }

    @RequestMapping(value = ApiUrl.PERSONAL_RED_ENVELOPE_PAGE, method = RequestMethod.POST)
    @CheckPermission
    @ApiOperation("分页")
    public RestResponse<PageVO<PersonalRedEnvelopeAdminVO>> personalRedEnvelopePage(@RequestBody @Valid PersonalRedEnvelopePageAdminParam param) {
        PageVO<PersonalRedEnvelopeAdminVO> pageVO = personalRedEnvelopeService.pageVO(param, PersonalRedEnvelopeAdminVO::new);
        return ok(pageVO);
    }
}
