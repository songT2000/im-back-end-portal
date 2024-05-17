package com.im.portal.controller;

import com.im.common.cache.impl.TimFaceCache;
import com.im.common.entity.tim.TimFace;
import com.im.common.response.RestResponse;
import com.im.common.util.CollectionUtil;
import com.im.common.vo.TimFaceVo;
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
 * 表情包接口
 */
@RestController
@Api(tags = "表情包接口")
public class TimFaceController extends BaseController{

    private TimFaceCache timFaceCache;

    @Autowired
    public void setTimFaceCache(TimFaceCache timFaceCache) {
        this.timFaceCache = timFaceCache;
    }

    @RequestMapping(value = ApiUrl.TIM_FACE_LIST, method = RequestMethod.POST)
    @ApiOperation(value = "获取所有表情包信息[支持缓存]", notes = "缓存Key=TIM_FACE")
    public RestResponse<List<TimFaceVo>> getTimFaceList() {
        List<TimFace> timFaces = timFaceCache.listFromRedis();
        if(CollectionUtil.isNotEmpty(timFaces)){
            List<TimFaceVo> result = timFaces.stream().map(timFace-> new TimFaceVo(timFace,timFace.getItems())).collect(Collectors.toList());
            return RestResponse.ok(result);
        }
        return RestResponse.ok(new ArrayList<>());
    }

}
