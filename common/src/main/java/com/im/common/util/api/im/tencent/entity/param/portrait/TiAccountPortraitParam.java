package com.im.common.util.api.im.tencent.entity.param.portrait;

import com.im.common.entity.enums.SexEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * 设置资料参数
 */
@Data
@NoArgsConstructor
public class TiAccountPortraitParam {
    private String username;
    private String nickname;
    private String avatar;
    private SexEnum sex;
    private LocalDate birthday;
    private String selfSignature;
    private Boolean addFriendEnabled;
    private Boolean enabled;//用户是否可用，默认可用
}
