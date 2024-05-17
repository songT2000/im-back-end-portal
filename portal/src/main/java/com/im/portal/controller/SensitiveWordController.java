package com.im.portal.controller;

import com.im.common.cache.impl.SensitiveWordCache;
import com.im.common.entity.SensitiveWord;
import com.im.common.response.RestResponse;
import com.im.common.util.CollectionUtil;
import com.im.common.vo.SensitiveWordVo;
import com.im.portal.controller.url.ApiUrl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 敏感词接口
 */
@RestController
@Api(tags = "敏感词接口")
public class SensitiveWordController extends BaseController {

    private SensitiveWordCache sensitiveWordCache;

    @Autowired
    public void setSensitiveWordCache(SensitiveWordCache sensitiveWordCache) {
        this.sensitiveWordCache = sensitiveWordCache;
    }

    @RequestMapping(value = ApiUrl.SENSITIVE_WORD_LIST, method = RequestMethod.POST)
    @ApiOperation(value = "获取所有敏感词信息[支持缓存]", notes = "缓存Key=SENSITIVE_WORD")
    public RestResponse<List<SensitiveWordVo>> getTimFaceList() {
        List<SensitiveWord> list = sensitiveWordCache.listFromRedis();
        if (CollectionUtil.isNotEmpty(list)) {
            List<SensitiveWordVo> result = list.stream().map(SensitiveWordVo::new).collect(Collectors.toList());
            return RestResponse.ok(result);
        }
        return RestResponse.ok(new ArrayList<>());
    }

}
