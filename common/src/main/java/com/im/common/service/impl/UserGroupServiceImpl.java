package com.im.common.service.impl;

import com.im.common.cache.base.CacheProxy;
import com.im.common.cache.impl.ApiRechargeConfigCache;
import com.im.common.cache.impl.BankCardRechargeConfigCache;
import com.im.common.constant.CommonConstant;
import com.im.common.entity.UserGroup;
import com.im.common.entity.UserGroupApiRechargeConfig;
import com.im.common.entity.UserGroupBankCardRechargeConfig;
import com.im.common.entity.UserGroupUser;
import com.im.common.entity.enums.PortalTypeEnum;
import com.im.common.entity.enums.SysCacheRefreshTypeEnum;
import com.im.common.mapper.UserGroupApiRechargeConfigMapper;
import com.im.common.mapper.UserGroupBankCardRechargeConfigMapper;
import com.im.common.mapper.UserGroupMapper;
import com.im.common.mapper.UserGroupUserMapper;
import com.im.common.param.IdParam;
import com.im.common.param.UserGroupAddAdminParam;
import com.im.common.param.UserGroupEditAdminParam;
import com.im.common.param.UserGroupPageAdminParam;
import com.im.common.response.ResponseCode;
import com.im.common.response.RestResponse;
import com.im.common.service.UserGroupApiRechargeConfigService;
import com.im.common.service.UserGroupBankCardRechargeConfigService;
import com.im.common.service.UserGroupService;
import com.im.common.service.UserGroupUserService;
import com.im.common.util.CollectionUtil;
import com.im.common.util.NumberUtil;
import com.im.common.util.StrUtil;
import com.im.common.util.mybatis.page.PageVO;
import com.im.common.util.mybatis.service.MyBatisPlusServiceImpl;
import com.im.common.util.user.UserUtil;
import com.im.common.vo.UserGroupAdminVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 用户组 服务实现类
 *
 * @author Barry
 * @date 2021-04-11
 */
@Service
public class UserGroupServiceImpl
        extends MyBatisPlusServiceImpl<UserGroupMapper, UserGroup>
        implements UserGroupService {
    private UserGroupUserService userGroupUserService;
    private UserGroupUserMapper userGroupUserMapper;
    private UserGroupBankCardRechargeConfigService userGroupBankCardRechargeConfigService;
    private UserGroupBankCardRechargeConfigMapper userGroupBankCardRechargeConfigMapper;
    private UserGroupApiRechargeConfigService userGroupApiRechargeConfigService;
    private UserGroupApiRechargeConfigMapper userGroupApiRechargeConfigMapper;
    private BankCardRechargeConfigCache bankCardRechargeConfigCache;
    private ApiRechargeConfigCache apiRechargeConfigCache;
    private CacheProxy cacheProxy;

    @Autowired
    public void setUserGroupUserService(UserGroupUserService userGroupUserService) {
        this.userGroupUserService = userGroupUserService;
    }

    @Autowired
    public void setUserGroupUserMapper(UserGroupUserMapper userGroupUserMapper) {
        this.userGroupUserMapper = userGroupUserMapper;
    }

    @Autowired
    public void setUserGroupBankCardRechargeConfigService(UserGroupBankCardRechargeConfigService userGroupBankCardRechargeConfigService) {
        this.userGroupBankCardRechargeConfigService = userGroupBankCardRechargeConfigService;
    }

    @Autowired
    public void setUserGroupBankCardRechargeConfigMapper(UserGroupBankCardRechargeConfigMapper userGroupBankCardRechargeConfigMapper) {
        this.userGroupBankCardRechargeConfigMapper = userGroupBankCardRechargeConfigMapper;
    }

    @Autowired
    public void setUserGroupApiRechargeConfigService(UserGroupApiRechargeConfigService userGroupApiRechargeConfigService) {
        this.userGroupApiRechargeConfigService = userGroupApiRechargeConfigService;
    }

    @Autowired
    public void setUserGroupApiRechargeConfigMapper(UserGroupApiRechargeConfigMapper userGroupApiRechargeConfigMapper) {
        this.userGroupApiRechargeConfigMapper = userGroupApiRechargeConfigMapper;
    }

    @Autowired
    public void setBankCardRechargeConfigCache(BankCardRechargeConfigCache bankCardRechargeConfigCache) {
        this.bankCardRechargeConfigCache = bankCardRechargeConfigCache;
    }

    @Autowired
    public void setApiRechargeConfigCache(ApiRechargeConfigCache apiRechargeConfigCache) {
        this.apiRechargeConfigCache = apiRechargeConfigCache;
    }

    @Autowired
    public void setCacheProxy(CacheProxy cacheProxy) {
        this.cacheProxy = cacheProxy;
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public PageVO<UserGroupAdminVO> pageVOForAdmin(UserGroupPageAdminParam param) {
        PageVO<UserGroupAdminVO> pageVO = pageVO(param, UserGroupAdminVO::new);

        // 组装额外数据
        List<UserGroupAdminVO> list = pageVO.getRecords();
        if (CollectionUtil.isNotEmpty(list)) {
            combineExtraDataForAdmin(list);
        }

        return pageVO;
    }

    private void combineExtraDataForAdmin(List<UserGroupAdminVO> list) {
        Set<Long> groupIds = CollectionUtil.toSet(list, e -> e.getId());

        // 用户列表
        List<UserGroupAdminVO> userGroupUserList = userGroupUserMapper.sumCount(groupIds);

        // 银行卡充值配置列表
        List<UserGroupAdminVO> userGroupBankCardRechargeConfigList = userGroupBankCardRechargeConfigMapper.sumCount(groupIds);

        // 三方充值配置列表
        List<UserGroupAdminVO> userGroupApiRechargeConfigList = userGroupApiRechargeConfigMapper.sumCount(groupIds);

        // 填充数据
        for (UserGroupAdminVO vo : list) {
            UserGroupAdminVO userCount = CollectionUtil.findFirst(userGroupUserList, e -> e.getId().equals(vo.getId()));
            vo.setUserCount(userCount == null ? CommonConstant.INT_0 : NumberUtil.nullOrZero(userCount.getUserCount()));

            UserGroupAdminVO bankCardRechargeConfigCount = CollectionUtil.findFirst(userGroupBankCardRechargeConfigList, e -> e.getId().equals(vo.getId()));
            vo.setBankCardRechargeConfigCount(bankCardRechargeConfigCount == null ? CommonConstant.INT_0 : NumberUtil.nullOrZero(bankCardRechargeConfigCount.getBankCardRechargeConfigCount()));

            UserGroupAdminVO apiRechargeConfigCount = CollectionUtil.findFirst(userGroupApiRechargeConfigList, e -> e.getId().equals(vo.getId()));
            vo.setApiRechargeConfigCount(apiRechargeConfigCount == null ? CommonConstant.INT_0 : NumberUtil.nullOrZero(apiRechargeConfigCount.getApiRechargeConfigCount()));
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse addForAdmin(UserGroupAddAdminParam param) {
        String realName = StrUtil.trim(param.getName());

        // 组名不能重复
        {
            Integer count = lambdaQuery().eq(UserGroup::getName, realName).count();
            if (NumberUtil.isGreatThenZero(count)) {
                return RestResponse.failed(ResponseCode.USER_GROUP_NAME_EXISTED);
            }
        }

        // 检查用户列表，禁止操作试玩用户
        List<UserGroupUser> userList = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(param.getUserList())) {
            for (String username : param.getUserList()) {
                Long userId = UserUtil.getUserIdByUsernameFromLocal(username, PortalTypeEnum.PORTAL);
                if (userId == null) {
                    return RestResponse.failed(ResponseCode.USER_NOT_FOUND, username);
                }
                userList.add(new UserGroupUser(userId));
            }
        }

        // 检查银行卡充值配置列表
        List<UserGroupBankCardRechargeConfig> bankCardRechargeConfigList = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(param.getBankCardRechargeConfigIdList())) {
            for (Long bankCardRechargeConfigId : param.getBankCardRechargeConfigIdList()) {
                String name = bankCardRechargeConfigCache.getNameByIdFromLocal(bankCardRechargeConfigId);
                if (StrUtil.isBlank(name)) {
                    return RestResponse.failed(ResponseCode.BANK_CARD_RECHARGE_CONFIG_NOT_FOUND, bankCardRechargeConfigId);
                }
                bankCardRechargeConfigList.add(new UserGroupBankCardRechargeConfig(bankCardRechargeConfigId));
            }
        }

        // 检查三方充值配置列表
        List<UserGroupApiRechargeConfig> apiRechargeConfigList = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(param.getApiRechargeConfigIdList())) {
            for (Long apiRechargeConfigId : param.getApiRechargeConfigIdList()) {
                String adminName = apiRechargeConfigCache.getAdminNameByIdFromLocal(apiRechargeConfigId);
                if (StrUtil.isBlank(adminName)) {
                    return RestResponse.failed(ResponseCode.API_RECHARGE_CONFIG_NOT_FOUND, apiRechargeConfigId);
                }
                apiRechargeConfigList.add(new UserGroupApiRechargeConfig(apiRechargeConfigId));
            }
        }

        // 保存用户组
        UserGroup userGroup = new UserGroup();
        userGroup.setName(realName);
        userGroup.setRemark(StrUtil.trim(param.getRemark()));
        boolean saved = save(userGroup);
        if (!saved) {
            return RestResponse.SYS_DATA_STATUS_ERROR;
        }

        // 保存用户列表
        if (CollectionUtil.isNotEmpty(userList)) {
            userList.forEach(e -> e.setGroupId(userGroup.getId()));
            userGroupUserService.saveBatch(userList);
        }

        // 保存银行卡充值配置列表
        if (CollectionUtil.isNotEmpty(bankCardRechargeConfigList)) {
            bankCardRechargeConfigList.forEach(e -> e.setGroupId(userGroup.getId()));
            userGroupBankCardRechargeConfigService.saveBatch(bankCardRechargeConfigList);
        }

        // 保存三方充值配置列表
        if (CollectionUtil.isNotEmpty(apiRechargeConfigList)) {
            apiRechargeConfigList.forEach(e -> e.setGroupId(userGroup.getId()));
            userGroupApiRechargeConfigService.saveBatch(apiRechargeConfigList);
        }

        // 刷新缓存
        cacheProxy.signalRefreshCache(SysCacheRefreshTypeEnum.USER_GROUP);
        cacheProxy.signalRefreshCache(SysCacheRefreshTypeEnum.USER_GROUP_USER);
        cacheProxy.signalRefreshCache(SysCacheRefreshTypeEnum.USER_GROUP_BANK_CARD_RECHARGE_CONFIG);
        cacheProxy.signalRefreshCache(SysCacheRefreshTypeEnum.USER_GROUP_API_RECHARGE_CONFIG);

        return RestResponse.OK;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse editForAdmin(UserGroupEditAdminParam param) {
        {
            Integer count = lambdaQuery().eq(UserGroup::getId, param.getId()).count();
            if (!NumberUtil.isGreatThenZero(count)) {
                return RestResponse.failed(ResponseCode.SYS_DATA_NOT_EXIST);
            }
        }

        String realName = StrUtil.trim(param.getName());

        // 组名不能重复
        {
            Integer count = lambdaQuery().eq(UserGroup::getName, realName).ne(UserGroup::getId, param.getId()).count();
            if (NumberUtil.isGreatThenZero(count)) {
                return RestResponse.failed(ResponseCode.USER_GROUP_NAME_EXISTED);
            }
        }

        // 修改用户组
        boolean updated = lambdaUpdate()
                .eq(UserGroup::getId, param.getId())
                .set(UserGroup::getName, realName)
                .set(UserGroup::getRemark, StrUtil.trim(param.getRemark()))
                .update();
        if (!updated) {
            return RestResponse.SYS_DATA_STATUS_ERROR;
        }

        // 刷新缓存
        cacheProxy.signalRefreshCache(SysCacheRefreshTypeEnum.USER_GROUP);

        return RestResponse.OK;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse deleteForAdmin(IdParam param) {
        // 删除用户组
        lambdaUpdate().eq(UserGroup::getId, param.getId()).remove();

        // 删除用户组用户
        userGroupUserService.lambdaUpdate().eq(UserGroupUser::getGroupId, param.getId()).remove();

        // 删除银行卡充值配置
        userGroupBankCardRechargeConfigService.lambdaUpdate().eq(UserGroupBankCardRechargeConfig::getGroupId, param.getId()).remove();

        // 删除三方充值配置
        userGroupApiRechargeConfigService.lambdaUpdate().eq(UserGroupApiRechargeConfig::getGroupId, param.getId()).remove();

        // 刷新缓存
        cacheProxy.signalRefreshCache(SysCacheRefreshTypeEnum.USER_GROUP);
        cacheProxy.signalRefreshCache(SysCacheRefreshTypeEnum.USER_GROUP_USER);
        cacheProxy.signalRefreshCache(SysCacheRefreshTypeEnum.USER_GROUP_BANK_CARD_RECHARGE_CONFIG);
        cacheProxy.signalRefreshCache(SysCacheRefreshTypeEnum.USER_GROUP_API_RECHARGE_CONFIG);

        return RestResponse.OK;
    }
}