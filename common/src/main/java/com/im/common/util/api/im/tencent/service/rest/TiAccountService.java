package com.im.common.util.api.im.tencent.service.rest;

import com.im.common.response.RestResponse;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 对接腾讯im账号管理api
 */
public interface TiAccountService {
    /**
     * 导入单个账号
     *
     * @param username
     * @return
     */
    RestResponse accountImport(String username);

    /**
     * 导入单个账号
     * <br>本接口用于将 App 自有账号导入即时通信 IM 账号系统，为该账号创建一个对应的内部 ID，使该账号能够使用即时通信 IM 服务。
     * <br>参考地址：<a href="https://cloud.tencent.com/document/product/269/1608">https://cloud.tencent.com/document/product/269/1608</a>
     *
     * @param username
     * @param nickname
     * @param avatar
     * @return
     */
    RestResponse accountImport(String username, String nickname, String avatar);

    /**
     * 批量导入账号
     * <br>本接口单次最多支持导入100个账号，同一个账号重复导入仅会创建1个内部 ID。
     * 本接口不支持导入帐号的昵称和头像信息，您可以调用 设置资料 接口设置昵称和头像等信息。
     *
     * @param usernames 账号集合，单次最多导入100个账号
     * @return true是成功，false是失败
     */
    RestResponse<Map<String, Boolean>> multiAccountImport(Collection<String> usernames);

    /**
     * 查询账号
     *
     * @param usernames 用户名，单次请求最多支持100个帐号
     * @return true就是有，false就是没有
     */
    RestResponse<Map<String, Boolean>> accountCheck(Collection<String> usernames);

    /**
     * 踢下线
     *
     * @param username 用户名
     */
    RestResponse kick(String username);

    /**
     * 批量查询账号登陆状态
     *
     * @param usernames 用户名，单次最多不超过500个
     * @return true在线，false离线
     */
    RestResponse<Map<String, Boolean>> queryOnlineStatus(List<String> usernames);
}
