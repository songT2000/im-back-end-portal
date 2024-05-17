package com.im.common.util.api.im.tencent.service.rest;

import com.im.common.response.RestResponse;
import com.im.common.util.api.im.tencent.entity.param.portrait.TiAccountPortraitParam;

import java.util.Collection;
import java.util.List;

/**
 * 资料管理接口
 */
public interface TiAccountPortraitService {
    /**
     * 设置资料
     *
     * @param portrait 资料信息
     */
    RestResponse setPortrait(TiAccountPortraitParam portrait);

    /**
     * 设置资料
     *
     * @param username
     * @param enabled
     * @return
     */
    RestResponse enableDisableAddFriend(String username, Boolean enabled);

    /**
     * 设置资料
     *
     * @param username
     * @param enabled
     * @return
     */
    RestResponse enableDisable(String username, Boolean enabled);

    /**
     * 拉取资料
     *
     * @param username 用户名
     * @return
     */
    RestResponse<TiAccountPortraitParam> getPortrait(String username);

    /**
     * 拉取资料
     * 建议每次拉取的用户数不超过100，避免因回包数据量太大导致回包失败。
     * 请确保请求中的所有帐号都已导入即时通信 IM，如果请求中含有未导入即时通信 IM 的帐号，即时通信 IM 后台将会提示错误。
     *
     * @param usernames 用户名列表
     * @return
     */
    RestResponse<List<TiAccountPortraitParam>> getPortrait(Collection<String> usernames);
}
