package com.im.common.service;

import com.im.common.response.RestResponse;
import com.im.common.vo.SwitchAppResultVO;

public interface SwitchAppService {

    /**
     * 下线所有用户
     */
    void kickOutAll();

    /**
     * 导入已有的账号
     */
    void importAccount();

    /**
     * 设置账号信息
     */
    void setAccountPortrait();

    /**
     * 导入关系链数据
     */
    void importFriendData();

    /**
     * 导入单聊消息
     */
    void importC2cMessage();

    /**
     * 同步群组信息
     */
    void importGroup();

    /**
     * 导入群组成员
     */
    void importGroupMember();

    /**
     * 导入群消息
     * @param offsetDay     导入几天前的群组消息记录，注意不要超过《历史消息存储时长配置》的最长存储时长
     */
    void importGroupMessage(int offsetDay);

    /**
     * 获取导入进度
     * @param key       缓存的key
     */
    RestResponse<SwitchAppResultVO> getProcess(String key);

}
