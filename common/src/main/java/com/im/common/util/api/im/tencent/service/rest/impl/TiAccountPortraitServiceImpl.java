package com.im.common.util.api.im.tencent.service.rest.impl;

import com.im.common.constant.CommonConstant;
import com.im.common.entity.enums.SexEnum;
import com.im.common.response.RestResponse;
import com.im.common.util.CollectionUtil;
import com.im.common.util.DateTimeUtil;
import com.im.common.util.StrUtil;
import com.im.common.util.api.im.tencent.entity.param.portrait.TiAccountPortraitParam;
import com.im.common.util.api.im.tencent.entity.param.portrait.TiAccountPortraitTagEnum;
import com.im.common.util.api.im.tencent.entity.param.portrait.TiAccountPortraitTagParam;
import com.im.common.util.api.im.tencent.entity.result.portrait.TiPortraitQueryResult;
import com.im.common.util.api.im.tencent.request.RequestExecutor;
import com.im.common.util.api.im.tencent.request.api.PortraitApiEnum;
import com.im.common.util.api.im.tencent.service.rest.TiAccountPortraitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class TiAccountPortraitServiceImpl implements TiAccountPortraitService {
    private RequestExecutor requestExecutor;

    @Autowired
    public void setRequestExecutor(RequestExecutor requestExecutor) {
        this.requestExecutor = requestExecutor;
    }

    @Override
    public RestResponse setPortrait(TiAccountPortraitParam portrait) {
        Map<String, Object> param = new HashMap<>();
        param.put("From_Account", portrait.getUsername());

        List<TiAccountPortraitTagParam> tagList = new ArrayList<>();
        // 昵称，null会被修改为字符串空
        {
            tagList.add(new TiAccountPortraitTagParam(TiAccountPortraitTagEnum.NICK, Optional.ofNullable(portrait.getNickname()).orElse(StrUtil.EMPTY)));
        }

        // 性别，null会被修改为Unknown
        {
            tagList.add(new TiAccountPortraitTagParam(TiAccountPortraitTagEnum.GENDER, convertSexFromSystem(portrait.getSex())));
        }

        // 生日，不能为null，只能是integer类型，20190419格式
        {
            if (portrait.getBirthday() != null) {
                tagList.add(new TiAccountPortraitTagParam(TiAccountPortraitTagEnum.BIRTHDAY, convertBirthdayFromSystem(portrait.getBirthday())));
            }
        }

        // 个性签名，null会被修改为字符串空
        {
            tagList.add(new TiAccountPortraitTagParam(TiAccountPortraitTagEnum.SELF_SIGNATURE, Optional.ofNullable(portrait.getSelfSignature()).orElse(StrUtil.EMPTY)));
        }

        // 头像，null会被修改为一个空格
        {
            tagList.add(new TiAccountPortraitTagParam(TiAccountPortraitTagEnum.IMAGE, StrUtil.blankToDefault(portrait.getAvatar(), StrUtil.SPACE)));
        }

        // 管理员禁止加好友标识，null则默认值，允许加好友
        {
            tagList.add(new TiAccountPortraitTagParam(TiAccountPortraitTagEnum.ADMIN_FORBID_TYPE, convertAddFriendEnabledFromSystem(portrait.getAddFriendEnabled())));
        }

        // 用户角色，null则默认值（可用），0为禁用，1为可用
        if (portrait.getEnabled() != null) {
            tagList.add(new TiAccountPortraitTagParam(TiAccountPortraitTagEnum.ROLE, portrait.getEnabled() ? CommonConstant.INT_1 : CommonConstant.INT_0));
        }

        param.put("ProfileItem", tagList);

        return requestExecutor.execute(PortraitApiEnum.portrait_set.getUrl(), param);
    }

    @Override
    public RestResponse enableDisableAddFriend(String username, Boolean enabled) {
        Map<String, Object> param = new HashMap<>();
        param.put("From_Account", username);

        List<TiAccountPortraitTagParam> tagList = new ArrayList<>();

        // 管理员禁止加好友标识，null则默认值，允许加好友
        tagList.add(new TiAccountPortraitTagParam(TiAccountPortraitTagEnum.ADMIN_FORBID_TYPE, convertAddFriendEnabledFromSystem(enabled)));

        param.put("ProfileItem", tagList);

        return requestExecutor.execute(PortraitApiEnum.portrait_set.getUrl(), param);
    }

    @Override
    public RestResponse enableDisable(String username, Boolean enabled) {
        Map<String, Object> param = new HashMap<>();
        param.put("From_Account", username);

        List<TiAccountPortraitTagParam> tagList = new ArrayList<>();

        // 用户角色，null则默认值（可用），0为禁用，1为可用
        tagList.add(new TiAccountPortraitTagParam(TiAccountPortraitTagEnum.ROLE, enabled ? CommonConstant.INT_1 : CommonConstant.INT_0));

        param.put("ProfileItem", tagList);

        return requestExecutor.execute(PortraitApiEnum.portrait_set.getUrl(), param);
    }

    @Override
    public RestResponse<TiAccountPortraitParam> getPortrait(String username) {
        RestResponse<List<TiAccountPortraitParam>> rsp = getPortrait(CollectionUtil.toList(username));
        if (!rsp.isOkRsp() || CollectionUtil.isEmpty(rsp.getData())) {
            return RestResponse.buildClearData(rsp);
        }
        return RestResponse.ok(rsp.getData().get(0));
    }

    @Override
    public RestResponse<List<TiAccountPortraitParam>> getPortrait(Collection<String> usernames) {
        Map<String, Object> param = new HashMap<>();
        param.put("To_Account", usernames);

        List<TiAccountPortraitTagEnum> tagList = new ArrayList<>();
        tagList.add(TiAccountPortraitTagEnum.NICK);
        tagList.add(TiAccountPortraitTagEnum.GENDER);
        tagList.add(TiAccountPortraitTagEnum.BIRTHDAY);
        tagList.add(TiAccountPortraitTagEnum.SELF_SIGNATURE);
        tagList.add(TiAccountPortraitTagEnum.IMAGE);
        tagList.add(TiAccountPortraitTagEnum.ADMIN_FORBID_TYPE);

        param.put("TagList", tagList);

        RestResponse<TiPortraitQueryResult> rsp = requestExecutor.execute(PortraitApiEnum.portrait_get.getUrl(), param, TiPortraitQueryResult.class);
        if (!rsp.isOkRsp()) {
            return RestResponse.buildClearData(rsp);
        }
        // 从接口转换成系统认识的数据
        if (rsp.getData() == null || CollectionUtil.isEmpty(rsp.getData().getUserProfileItem())) {
            return RestResponse.OK;
        }

        List<TiPortraitQueryResult.UserProfileItemDTO> items = rsp.getData().getUserProfileItem();
        List<TiAccountPortraitParam> list = new ArrayList<>();
        for (TiPortraitQueryResult.UserProfileItemDTO item : items) {
            if (item.getResultCode() != 0) {
                continue;
            }
            TiAccountPortraitParam portrait = new TiAccountPortraitParam();
            portrait.setUsername(item.getToAccount());

            List<TiAccountPortraitTagParam> profileList = item.getProfileItem();
            for (TiAccountPortraitTagParam profile : profileList) {
                switch (profile.getTag()) {
                    case NICK: {
                        String value = StrUtil.toString(profile.getValue());
                        portrait.setNickname(StrUtil.isEmpty(value) ? null : value);
                        break;
                    }
                    case GENDER:
                        portrait.setSex(convertSexFromIm(StrUtil.toString(profile.getValue())));
                        break;
                    case BIRTHDAY:
                        portrait.setBirthday(convertBirthdayFromIm(StrUtil.toString(profile.getValue())));
                        break;
                    case SELF_SIGNATURE: {
                        String value = StrUtil.toString(profile.getValue());
                        portrait.setSelfSignature(StrUtil.isEmpty(value) ? null : value);
                        break;
                    }
                    case IMAGE: {
                        String value = StrUtil.toString(profile.getValue());
                        portrait.setAvatar(StrUtil.isBlank(value) ? null : value);
                        break;
                    }
                    case ADMIN_FORBID_TYPE:
                        portrait.setAddFriendEnabled(convertAddFriendEnabledFromIm(StrUtil.toString(profile.getValue())));
                        break;
                    default:
                        break;
                }
            }

            list.add(portrait);
        }

        return RestResponse.ok(list);
    }

    private String convertSexFromSystem(SexEnum sex) {
        if (sex == null) {
            return "Gender_Type_Unknown";
        }
        if (sex == SexEnum.MALE) {
            return "Gender_Type_Male";
        }
        if (sex == SexEnum.FEMALE) {
            return "Gender_Type_Female";
        }
        return "Gender_Type_Unknown";
    }

    private SexEnum convertSexFromIm(String gender) {
        if (StrUtil.isBlank(gender)) {
            return null;
        }
        if ("Gender_Type_Male".equalsIgnoreCase(gender)) {
            return SexEnum.MALE;
        }
        if ("Gender_Type_Female".equalsIgnoreCase(gender)) {
            return SexEnum.FEMALE;
        }
        return null;
    }

    private Integer convertBirthdayFromSystem(LocalDate birthday) {
        if (birthday == null) {
            return null;
        }
        return Integer.valueOf(DateTimeUtil.toDateStr(birthday, "yyyyMMdd"));
    }

    private LocalDate convertBirthdayFromIm(String birthday) {
        if (StrUtil.isBlank(birthday)) {
            return null;
        }
        return DateTimeUtil.fromDateStrToLocalDate(birthday, "yyyyMMdd");
    }

    private String convertAddFriendEnabledFromSystem(Boolean addFriendEnabled) {
        if (addFriendEnabled == null || Boolean.TRUE.equals(addFriendEnabled)) {
            return "AdminForbid_Type_None";
        }
        return "AdminForbid_Type_SendOut";
    }

    private Boolean convertAddFriendEnabledFromIm(String addFriendEnabled) {
        if (StrUtil.isBlank(addFriendEnabled) || "AdminForbid_Type_None".equals(addFriendEnabled)) {
            return true;
        }
        return false;
    }
}
