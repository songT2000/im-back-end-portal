package com.im.callback.controller;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.alibaba.fastjson.JSON;
import com.im.callback.controller.url.ApiUrl;
import com.im.callback.entity.TiCallbackRequest;
import com.im.callback.service.TiCallbackCommandEnum;
import com.im.callback.service.TiCallbackRouter;
import com.im.common.cache.impl.SysConfigCache;
import com.im.common.cache.sysconfig.bo.ImConfigBO;
import com.im.common.util.api.im.tencent.entity.result.TiBaseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class CallbackController {
    private static final Log LOG = LogFactory.get();

    private TiCallbackRouter tiCallbackRouter;
    private SysConfigCache sysConfigCache;

    @Autowired
    public void setTiCallbackRouter(TiCallbackRouter tiCallbackRouter) {
        this.tiCallbackRouter = tiCallbackRouter;
    }

    @Autowired
    public void setSysConfigCache(SysConfigCache sysConfigCache) {
        this.sysConfigCache = sysConfigCache;
    }

    /**
     * 腾讯IM回掉函数
     *
     * @param requestBody     业务数据json
     * @param sdkAppId        App 在即时通信 IM 分配的应用标识
     * @param contentType     可选，通常值为 JSON
     * @param callbackCommand 回调命令关键字
     * @param clientIp        客户端 IP 地址
     * @param optPlatform     客户端平台，对应不同的平台类型，可能的取值有：RESTAPI（使用 REST API 发送请求）、Web（使用 Web SDK 发送请求）、Android、iOS、Windows、Mac、IPad、Unknown（使用未知类型的设备发送请求）
     * @return 应答json
     */
    @RequestMapping(value = ApiUrl.TIM_CALLBACK, method = RequestMethod.POST)
    public String callback(@RequestBody String requestBody,
                           @RequestParam("SdkAppid") String sdkAppId,
                           @RequestParam("contenttype") String contentType,
                           @RequestParam("CallbackCommand") String callbackCommand,
                           @RequestParam("ClientIP") String clientIp,
                           @RequestParam("OptPlatform") String optPlatform) {

        LOG.info("\n接收腾讯IM回掉请求：[SdkAppid=[{}], [contenttype=[{}], CallbackCommand=[{}], ClientIP=[{}],"
                        + " OptPlatform=[{}], requestBody=[\n{}\n] ",
                sdkAppId, contentType, callbackCommand, clientIp, optPlatform, requestBody);

        ImConfigBO imConfig = sysConfigCache.getImConfigFromLocal();
        if (!imConfig.getTecentImSdkAppid().equals(Long.parseLong(sdkAppId))) {
            //sdk appId不匹配
            return JSON.toJSONString(TiBaseResult.success());
        }
        TiCallbackCommandEnum anEnum = TiCallbackCommandEnum.getByCommand(callbackCommand);
        if (anEnum == null) {
            //没有监听该事件，直接返回成功
            return JSON.toJSONString(TiBaseResult.success());
        }
        TiCallbackRequest request = JSON.parseObject(requestBody, anEnum.getRequest());
        request.setClientIp(clientIp);
        request.setContentType(contentType);
        request.setOptPlatform(optPlatform);
        request.setSdkAppId(sdkAppId);
        TiBaseResult result = tiCallbackRouter.route(callbackCommand, request);
        return JSON.toJSONString(result);

    }
}
