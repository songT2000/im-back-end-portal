package com.im.common.response;

import com.im.common.util.StrUtil;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Barry
 * @date 2022-03-03
 */
public class TestAdminMenu {
    public static void main(String[] args) {
        String groups = "RSP_MSG\n" +
                "RSP_MSG\n" +
                "RSP_MSG\n" +
                "RSP_MSG\n" +
                "RSP_MSG\n" +
                "RSP_MSG\n" +
                "ADMIN_MENU";

        String codes = "USER_GROUP_NOT_FOUND\n" +
                "USER_GROUP_NAME_EXISTED\n" +
                "USER_GROUP_USER_EXISTED\n" +
                "USER_GROUP_BANK_CARD_RECHARGE_CONFIG_EXISTED\n" +
                "USER_GROUP_API_RECHARGE_CONFIG_EXISTED\n" +
                "BANK_CARD_RECHARGE_CONFIG_NOT_FOUND\n" +
                "USER_GROUP";

        String zhCns = "用户组不存在\n" +
                "该名称已存在\n" +
                "用户[{}]已存在该用户组中\n" +
                "银行卡充值配置[{}]已存在该用户组中\n" +
                "三方充值配置[{}]已存在该用户组中\n" +
                "未找到该银行卡充值配置，或已被删除\n" +
                "用户组管理";

        String ens = "User group not existed\n" +
                "This name already existed\n" +
                "User {} already in this group\n" +
                "Bank card recharge config {} already in this group\n" +
                "Api recharge config {} already in this group\n" +
                "The bank card recharge configuration was not found, or has been deleted\n" +
                "User group MGT";

        String jaJps = "ユーザーグループが存在しません\n" +
                "この名前はすでに存在します\n" +
                "ユーザー[{}]はすでにこのユーザーグループに存在します\n" +
                "銀行カードのリチャージ構成[{}]はすでにこのユーザーグループに存在します\n" +
                "サードパーティの再充電構成[{}]は、このユーザーグループにすでに存在します\n" +
                "銀行カードのリチャージ設定が見つからないか、削除されました\n" +
                "ユーザーグループ管理";

        String[] groupArr = groups.split("\n");
        String[] codeArr = codes.split("\n");
        String[] zhCnArr = zhCns.split("\n");
        String[] enArr = ens.split("\n");
        String[] jaJpArr = jaJps.split("\n");

        String sqlTemp = "INSERT INTO `i18n_translate` (`id`, `group`, `key`, `language_code`, `value`, `create_time`, `update_time`)\n" +
                "VALUES\n" +
                "\t(NULL, '{}', '{}', 'zh-CN', '{}', '2020-05-27 14:54:19', '2020-05-27 14:54:19'),\n" +
                "\t(NULL, '{}', '{}', 'en', '{}', '2020-05-27 14:54:19', '2020-05-27 14:54:19'),\n" +
                "\t(NULL, '{}', '{}', 'ja-JP', '{}', '2020-05-27 14:54:19', '2020-05-27 14:54:19');";

        Set<String> repeatCodes = new HashSet<>();

        for (int i = 0; i < codeArr.length; i++) {
            String group = groupArr[i];
            String code = codeArr[i];
            String zhCn = zhCnArr[i];
            String en = enArr[i];
            String jaJp = jaJpArr[i];

            if (repeatCodes.contains(code)) {
                continue;
            }
            repeatCodes.add(code);

            String sql = StrUtil.format(sqlTemp, group, code, zhCn, group, code, en, group, code, jaJp);
            System.out.println(sql);
            System.out.println();
        }
    }
}
