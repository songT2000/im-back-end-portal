package com.im.admin.controller;

import com.im.admin.config.permission.CheckPermission;
import com.im.admin.controller.url.ApiUrl;
import com.im.common.entity.enums.UserOperationLogTypeEnum;
import com.im.common.entity.tim.TimFace;
import com.im.common.param.IdParam;
import com.im.common.param.TimFaceAddParam;
import com.im.common.param.TimFaceItemAddParam;
import com.im.common.param.TimFaceItemDeleteParam;
import com.im.common.response.RestResponse;
import com.im.common.service.TimFaceService;
import com.im.common.util.CollectionUtil;
import com.im.common.util.aop.log.UserOperationLog;
import com.im.common.vo.TimFaceVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 表情包接口
 */
@RestController
@Api(tags = "表情包接口")
public class TimFaceController extends BaseController{

    private TimFaceService timFaceService;

    @Autowired
    public void setTimFaceService(TimFaceService timFaceService) {
        this.timFaceService = timFaceService;
    }

    @RequestMapping(value = ApiUrl.TIM_FACE_LIST, method = RequestMethod.POST)
    @ApiOperation("获取所有表情包信息")
    @CheckPermission
    public RestResponse<List<TimFaceVo>> getTimFaceList() {
        List<TimFace> timFaces = timFaceService.getAll();
        if(CollectionUtil.isNotEmpty(timFaces)){
            List<TimFaceVo> result = timFaces.stream().map(timFace-> new TimFaceVo(timFace,timFace.getItems())).collect(Collectors.toList());
            return RestResponse.ok(result);
        }
        return RestResponse.OK;
    }

    @RequestMapping(value = ApiUrl.TIM_FACE_ADD, method = RequestMethod.POST)
    @ApiOperation("新增表情包专辑")
    @CheckPermission
    @UserOperationLog(type= UserOperationLogTypeEnum.TIM_FACE_ADD)
    public RestResponse add(@RequestBody @Valid TimFaceAddParam param) {
        return timFaceService.add(param);
    }

    @RequestMapping(value = ApiUrl.TIM_FACE_DELETE, method = RequestMethod.POST)
    @ApiOperation("删除表情包专辑")
    @CheckPermission
    @UserOperationLog(type= UserOperationLogTypeEnum.TIM_FACE_DELETE)
    public RestResponse delete(@RequestBody @Valid IdParam param) {
        return timFaceService.delete(param.getId());
    }

    @RequestMapping(value = ApiUrl.TIM_FACE_ITEM_ADD, method = RequestMethod.POST)
    @ApiOperation("新增表情包元素")
    @CheckPermission
    @UserOperationLog(type= UserOperationLogTypeEnum.TIM_FACE_ITEM_ADD)
    public RestResponse addItem(@RequestBody @Valid TimFaceItemAddParam param) {
        return timFaceService.addItem(param);
    }

    @RequestMapping(value = ApiUrl.TIM_FACE_ITEM_DELETE, method = RequestMethod.POST)
    @ApiOperation("删除表情包元素")
    @CheckPermission
    @UserOperationLog(type= UserOperationLogTypeEnum.TIM_FACE_ITEM_DELETE)
    public RestResponse deleteItem(@RequestBody @Valid TimFaceItemDeleteParam param) {
        return timFaceService.deleteItem(param);
    }
}
