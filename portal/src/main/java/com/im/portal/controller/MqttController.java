package com.im.portal.controller;

import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import io.swagger.annotations.Api;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Barry
 * @date 2021-03-04
 */
@RestController
@Api(tags = "消息推送相关说明")
@ApiSupport(order = 99)
@ConditionalOnProperty(name = "enable", prefix = "knife4j", havingValue = "true")
public class MqttController {
    // @RequestMapping(value = ApiUrl.BASE_AUTH_URL + "/do_not_call_this_method1", method = RequestMethod.POST)
    // @ApiOperation(value = "市场详情推送", notes = "该接口不可调用，仅说明每个字段的含义，实际会是MQTT推过来<br/>" +
    //         "<span style='color: red;font-family:bold;font-size: 20px;'>订阅主题：{登录信息.mqttConfig.parentTopic}/market/detail</span><br/>" +
    //         "<span style='color: red;font-family:bold;font-size: 20px;'>messageType：MARKET_DETAIL</span><br/>" +
    //         "<span style='color: red;font-family:bold;'>接口推送的是所有币的最新详情，请自行过滤需要的详情（不包含禁用的）</span><br/>" +
    //         "<span style='color: red;font-family:bold;'>在不需要详情推送的地方，请取消订阅主题，省一点费用</span><br/>")
    // @ApiOperationSupport(order = 1)
    // public MarketDetailMqttVO marketDetailMqttVo() {
    //     return null;
    // }
    //
    // @RequestMapping(value = ApiUrl.BASE_AUTH_URL + "/do_not_call_this_method2", method = RequestMethod.POST)
    // @ApiOperation(value = "市场K线推送", notes = "该接口不可调用，仅说明每个字段的含义，实际会是MQTT推过来<br/>" +
    //         "<span style='color: red;font-family:bold;font-size: 20px;'>订阅主题：{登录信息.mqttConfig.parentTopic}/market/kline/{vcCode}/{period}</span><br/>" +
    //         "<span style='color: red;font-family:bold;font-size: 20px;'>messageType：MARKET_KLINE</span><br/>" +
    //         "<span style='color: red;font-family:bold;'>接口推送的是单个币的单个K线维度的，永远只会推最新的时间点那一条数据</span><br/>" +
    //         "<span style='color: red;font-family:bold;'>点击K线tab的时候，把上一个K线订阅取消，然后再订阅点击的这个维度</span><br/>" +
    //         "<span style='color: red;font-family:bold;'>如果推送的数据在K线图里有，则更新那个点的数据，如果没有，则新增到最新的时间点上</span><br/>" +
    //         "<span style='color: red;font-family:bold;'>在不需要详情推送的地方，请取消订阅主题，省一点费用</span><br/>")
    // @ApiOperationSupport(order = 2)
    // public MarketKlineMqttVO marketKlineMqttVo() {
    //     return null;
    // }
    //
    // @RequestMapping(value = ApiUrl.BASE_AUTH_URL + "/do_not_call_this_method3", method = RequestMethod.POST)
    // @ApiOperation(value = "极速交易订单中奖推送", notes = "该接口不可调用，仅说明每个字段的含义，实际会是MQTT推过来<br/>" +
    //         "<span style='color: red;font-family:bold;font-size: 20px;'>订阅主题：无需订阅，只要连上了MQTT就会推过来</span><br/>" +
    //         "<span style='color: red;font-family:bold;font-size: 20px;'>messageType：TIMING_TRADE_ORDER_WIN</span><br/>" +
    //         "<span style='color: red;font-family:bold;'>该推送是定向推送到当前登录用户，被推过来的数据一定是当前登录用户的</span>")
    // @ApiOperationSupport(order = 3)
    // public TimingTradeOrderMqttVO timingTradeOrderMqttVO() {
    //     return null;
    // }
    //
    // @RequestMapping(value = ApiUrl.BASE_AUTH_URL + "/do_not_call_this_method9", method = RequestMethod.POST)
    // @ApiOperation(value = "成交明细推送", notes = "每次都是全量推送(买卖各10条)，该接口不可调用，仅说明每个字段的含义，实际会是MQTT推过来<br/>" +
    //         "<span style='color: red;font-family:bold;font-size: 20px;'>订阅主题：{登录信息.mqttConfig.parentTopic}/market/trade/{vcCode}</span><br/>" +
    //         "<span style='color: red;font-family:bold;font-size: 20px;'>messageType：MARKET_TRADE</span><br/>" +
    //         "<span style='color: red;font-family:bold;'>在不需要交易明细推送的地方，请取消订阅主题，省一点费用</span>")
    // @ApiOperationSupport(order = 4)
    // public MarketTradeMqttVO marketTradeMqttVO() {
    //     return null;
    // }
}
