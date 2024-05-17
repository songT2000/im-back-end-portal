package com.im.common.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.im.common.cache.base.CacheProxy;
import com.im.common.cache.impl.SysConfigCache;
import com.im.common.cache.sysconfig.bo.RegisterConfigBO;
import com.im.common.constant.CommonConstant;
import com.im.common.entity.PortalUser;
import com.im.common.entity.PortalUserProfile;
import com.im.common.entity.UserLoginLog;
import com.im.common.entity.enums.PortalTypeEnum;
import com.im.common.entity.enums.SysCacheRefreshTypeEnum;
import com.im.common.entity.tim.TimUserLoginLog;
import com.im.common.mapper.PortalUserProfileMapper;
import com.im.common.response.RestResponse;
import com.im.common.service.PortalUserProfileService;
import com.im.common.service.PortalUserService;
import com.im.common.service.TimUserLoginLogService;
import com.im.common.util.CollectionUtil;
import com.im.common.util.NumberUtil;
import com.im.common.util.RandomUtil;
import com.im.common.util.StrUtil;
import com.im.common.util.api.im.tencent.entity.param.portrait.TiAccountPortraitParam;
import com.im.common.util.api.im.tencent.service.rest.TiAccountPortraitService;
import com.im.common.util.mybatis.service.MyBatisPlusServiceImpl;
import com.im.common.util.user.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * 用户资料表 服务实现类
 *
 * @author Barry
 * @date 2021-12-12
 */
@Service
public class PortalUserProfileServiceImpl
        extends MyBatisPlusServiceImpl<PortalUserProfileMapper, PortalUserProfile>
        implements PortalUserProfileService {
    private static final Log LOG = LogFactory.get();

    private SysConfigCache sysConfigCache;
    private TimUserLoginLogService timUserLoginLogService;
    private TiAccountPortraitService tiAccountPortraitService;
    private PortalUserService portalUserService;
    private CacheProxy cacheProxy;

    @Autowired
    public void setSysConfigCache(SysConfigCache sysConfigCache) {
        this.sysConfigCache = sysConfigCache;
    }

    @Autowired
    public void setTimUserLoginLogService(TimUserLoginLogService timUserLoginLogService) {
        this.timUserLoginLogService = timUserLoginLogService;
    }

    @Autowired
    public void setTiAccountPortraitService(TiAccountPortraitService tiAccountPortraitService) {
        this.tiAccountPortraitService = tiAccountPortraitService;
    }

    @Autowired
    public void setPortalUserService(PortalUserService portalUserService) {
        this.portalUserService = portalUserService;
    }

    @Autowired
    public void setCacheProxy(CacheProxy cacheProxy) {
        this.cacheProxy = cacheProxy;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createUserProfile(PortalUserProfile profile) {
        // 生成默认邀请码
        RegisterConfigBO registerConfig = sysConfigCache.getRegisterConfigFromRedis();

        // 生成长度大于0才去生成
        if (NumberUtil.isGreatThenZero(registerConfig.getAutoGenerateInviteCodeLength())) {
            String myInviteCode = generateInviteCode(registerConfig.getAutoGenerateInviteCodeLength(), 5);
            profile.setMyInviteCode(myInviteCode);
        }

        save(profile);
    }

    private String generateInviteCode(int codeLength, int tryTimes) {
        if (tryTimes <= 0) {
            return generateInviteCode(++codeLength, 10);
        }

        String randomCode = RandomUtil.randomNumberStr(codeLength);

        Integer count = lambdaQuery().eq(PortalUserProfile::getMyInviteCode, randomCode).count();

        if (NumberUtil.isGreatThenZero(count)) {
            return generateInviteCode(codeLength, --tryTimes);
        }

        return randomCode;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateLastLogin(UserLoginLog loginLog) {
        lambdaUpdate()
                .eq(PortalUserProfile::getId, loginLog.getUserId())
                .set(PortalUserProfile::getLastLoginTime, loginLog.getCreateTime())
                .set(PortalUserProfile::getLastLoginIp, loginLog.getIp())
                .set(PortalUserProfile::getLastLoginArea, loginLog.getArea())
                .update();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void syncFromSdk() {
        // 当天活跃的所有人
        long current = 1;
        final long size = 100;
        int updatedCount = 0;

        LambdaQueryWrapper<TimUserLoginLog> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TimUserLoginLog::getLoginDate, DateUtil.parseDate(DateUtil.today()));
        while (true) {
            Page<TimUserLoginLog> resultPage = timUserLoginLogService.page(new Page<>(current, size), wrapper);
            List<TimUserLoginLog> list = resultPage.getRecords();
            if (CollectionUtil.isEmpty(list)) {
                break;
            }
            Set<Long> userIds = CollectionUtil.toSet(list, e -> e.getUserId());

            updatedCount = updatedCount + syncFromSdk(userIds);

            if (!resultPage.hasNext()) {
                break;
            }
            current++;
        }

        // 还需要更新缓存
        if (updatedCount > 0) {
            cacheProxy.signalRefreshCache(SysCacheRefreshTypeEnum.PORTAL_USER);
        }
    }

    private int syncFromSdk(Set<Long> userIds) {
        if (CollectionUtil.isEmpty(userIds)) {
            return CommonConstant.INT_0;
        }

        List<String> usernames = CollectionUtil.toList(userIds, e -> UserUtil.getUsernameByIdFromLocal(e, PortalTypeEnum.PORTAL));
        RestResponse<List<TiAccountPortraitParam>> rsp = tiAccountPortraitService.getPortrait(usernames);
        if (!rsp.isOkRsp()) {
            LOG.warn("向SDK同步用户资料时发生错误，{}", rsp.getMessage());
            return CommonConstant.INT_0;
        }
        if (CollectionUtil.isEmpty(rsp.getData())) {
            return CommonConstant.INT_0;
        }

        int updatedCount = 0;

        // 对比更新portal_user
        updatedCount = updatedCount + syncUpdatePortalUser(userIds, rsp.getData());

        // 对比更新portal_user_profile
        updatedCount = updatedCount + syncUpdatePortalUserProfile(userIds, rsp.getData());

        return updatedCount;
    }

    private int syncUpdatePortalUser(Set<Long> userIds, List<TiAccountPortraitParam> list) {
        // 先获取这些人需要更新的字段信息
        List<PortalUser> userList = portalUserService
                .lambdaQuery()
                .select(PortalUser::getId, PortalUser::getNickname, PortalUser::getAvatar, PortalUser::getAddFriendEnabled)
                .in(PortalUser::getId, userIds)
                .list();
        if (CollectionUtil.isEmpty(userList)) {
            return CommonConstant.INT_0;
        }

        // 如果对比有不同，则修改数据库
        List<PortalUser> updateUserList = new ArrayList<>();
        for (TiAccountPortraitParam portrait : list) {
            Long userId = UserUtil.getUserIdByUsernameFromLocal(portrait.getUsername(), PortalTypeEnum.PORTAL);
            if (userId == null) {
                continue;
            }
            PortalUser user = CollectionUtil.findFirst(userList, e -> e.getId().equals(userId));
            if (user == null) {
                continue;
            }

            // 比较这些值是否不同
            boolean same = StrUtil.equals(user.getNickname(), portrait.getNickname())
                    && StrUtil.equals(user.getAvatar(), portrait.getAvatar())
                    && Objects.equals(user.getAddFriendEnabled(), portrait.getAddFriendEnabled());
            if (!same) {
                // 不同就修改
                user.setNickname(portrait.getNickname());
                user.setAvatar(portrait.getAvatar());
                user.setAddFriendEnabled(portrait.getAddFriendEnabled());
                updateUserList.add(user);
            }
        }

        // 批量修改
        if (CollectionUtil.isNotEmpty(updateUserList)) {
            // 这里其实是会有问题的，只会更新不为null的字段，但如果实际值就是null，那会更新不到，后面再看情况来优化
            portalUserService.updateBatchById(updateUserList);
            LOG.info("成功向SDK同步{}条portal_user", updateUserList.size());
        }

        return updateUserList.size();
    }

    private int syncUpdatePortalUserProfile(Set<Long> userIds, List<TiAccountPortraitParam> list) {
        // 先获取这些人需要更新的字段信息
        List<PortalUserProfile> profileList = lambdaQuery()
                .select(PortalUserProfile::getId, PortalUserProfile::getSex, PortalUserProfile::getBirthday, PortalUserProfile::getSelfSignature)
                .in(PortalUserProfile::getId, userIds)
                .list();
        if (CollectionUtil.isEmpty(profileList)) {
            return CommonConstant.INT_0;
        }

        // 如果对比有不同，则修改数据库
        List<PortalUserProfile> updateProfileList = new ArrayList<>();
        for (TiAccountPortraitParam portrait : list) {
            Long userId = UserUtil.getUserIdByUsernameFromLocal(portrait.getUsername(), PortalTypeEnum.PORTAL);
            if (userId == null) {
                continue;
            }
            PortalUserProfile profile = CollectionUtil.findFirst(profileList, e -> e.getId().equals(userId));
            if (profile == null) {
                continue;
            }

            // 比较这些值是否不同
            boolean same = Objects.equals(profile.getSex(), portrait.getSex())
                    && Objects.equals(profile.getBirthday(), portrait.getBirthday())
                    && StrUtil.equals(profile.getSelfSignature(), portrait.getSelfSignature());
            if (!same) {
                // 不同就修改
                profile.setSex(portrait.getSex());
                profile.setBirthday(portrait.getBirthday());
                profile.setSelfSignature(portrait.getSelfSignature());
                updateProfileList.add(profile);
            }
        }

        // 批量修改
        if (CollectionUtil.isNotEmpty(updateProfileList)) {
            // 这里其实是会有问题的，只会更新不为null的字段，但如果实际值就是null，那会更新不到，后面再看情况来优化
            updateBatchById(updateProfileList);
            LOG.info("成功向SDK同步{}条portal_user_profile", updateProfileList.size());
        }

        return updateProfileList.size();
    }
}