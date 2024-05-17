package com.im.common.response;

import com.im.common.util.CollectionUtil;
import com.im.common.util.StrUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author Barry
 * @date 2022-03-03
 */
public class Test {
    public static void main(String[] args) {
        String codes = "SYS_DATA_NOT_EDITABLE\n" +
                "SYS_DATA_DELETED\n" +
                "SYS_DATA_EXISTED\n" +
                "SYS_DATA_NOT_EXIST\n" +
                "SYS_DATA_NOT_EXIST_OR_EXPIRED\n" +
                "SYS_DATA_SAME_DATABASE\n" +
                "IP_NOT_ILLEGAL_IPV4_IPV4MASK_IPV6\n" +
                "IP_FORMATTING_INCORRECT\n" +
                "AREA_NOT_ALLOWED\n" +
                "SYS_ONLY_ONE_REQUEST_AT_SAME_TIME\n" +
                "SYS_DATA_ALREADY_FIRST\n" +
                "SYS_DATA_ALREADY_LAST\n" +
                "SYS_AMOUNT_INCORRECT\n" +
                "SYS_AMOUNT_NO_PRECISION\n" +
                "SYS_AMOUNT_MAX_PRECISION_INCORRECT\n" +
                "SYS_AMOUNT_NOT_IN_RANGE\n" +
                "SYS_AMOUNT_RANGE_FORMATTING_INCORRECT\n" +
                "SYS_TIME_RANGE_FORMATTING_INCORRECT\n" +
                "OSS_CONNECT_ERROR\n" +
                "SYS_UPLOAD_FILE_NOT_EMPTY\n" +
                "SYS_UPLOAD_FILE_ERROR\n" +
                "SYS_UPLOAD_FILE_ONLY_ACCEPT_JPG_OR_PNG\n" +
                "SYS_UPLOAD_FILE_SIZE_EXCEEDED\n" +
                "SYS_MAX_QUERY_PAST_DAY\n" +
                "SYS_EXPORT_DATETIME_RANGE_EXCEEDED\n" +
                "SYS_REMARK_REQUIRED\n" +
                "IM_CONNECT_EXCEPTION\n" +
                "IM_EXECUTE_EXCEPTION\n" +
                "ADMIN_ROLE_NAME_EXISTED\n" +
                "ADMIN_ROLE_NOT_SELF_OR_LOWER_ROLE\n" +
                "ADMIN_ROLE_NOT_LOWER\n" +
                "YOUR_ACCOUNT_HAS_BEEN_DISABLED\n" +
                "YOUR_ACCOUNT_LOGIN_HAS_BEEN_DISABLED\n" +
                "YOUR_ACCOUNT_RECHARGE_HAS_BEEN_DISABLED\n" +
                "YOUR_ACCOUNT_WITHDRAW_HAS_BEEN_DISABLED\n" +
                "USER_ALREADY_LOGIN\n" +
                "USER_LOGIN_USERNAME_OR_PASSWORD_ERROR\n" +
                "USER_LOGIN_USERNAME_OR_PASSWORD_ERROR_LEFT_TIME\n" +
                "USER_LOGIN_TOO_MANY_PWD_ERROR_TIMES\n" +
                "USER_ROLE_DISABLED\n" +
                "USER_ROLE_ILLEGAL\n" +
                "USER_NOT_FOUND\n" +
                "USER_INSUFFICIENT_BALANCE\n" +
                "GOOGLE_CODE_REQUIRED\n" +
                "GOOGLE_CODE_ERROR\n" +
                "GOOGLE_CODE_DUPLICATED\n" +
                "GOOGLE_ALREADY_BIND\n" +
                "GOOGLE_NOT_YET_BIND\n" +
                "USER_USERNAME_EXISTED\n" +
                "USER_NOT_ALLOW_OPERATE_SELF\n" +
                "USER_NOT_ALLOW_ADMIN\n" +
                "USER_SESSION_FORCE_LOGOUT\n" +
                "USER_SESSION_OTHER_LOGIN\n" +
                "USER_SESSION_INFO_CHANGED\n" +
                "USER_SESSION_INVALIDATE\n" +
                "USER_SESSION_EXPIRED\n" +
                "USER_SESSION_PLEASE_LOGIN_FIRST\n" +
                "USER_SESSION_TOKEN_MISSING\n" +
                "USER_SESSION_TOKEN_INVALID\n" +
                "USER_OLD_PASSWORD_INCORRECT\n" +
                "USER_PASSWORD_ALREADY_BOUND\n" +
                "USER_NEW_PWD_MUST_DIFFERENT_OLD\n" +
                "USER_FUND_PASSWORD_NOT_YET_BIND\n" +
                "USER_FUND_PASSWORD_INCORRECT\n" +
                "USER_FUND_PWD_MUST_DIFFERENT_LOGIN_PWD\n" +
                "USER_RECHARGE_DISABLED\n" +
                "USER_WITHDRAW_DISABLED\n" +
                "USER_REGISTER_INVITE_CODE_EXCEEDED\n" +
                "USER_REGISTER_DISABLED\n" +
                "USER_REGISTER_LIMIT_COUNT_BY_IP_EXCEEDED\n" +
                "USER_REGISTER_INVITE_CODE_REQUIRED\n" +
                "USER_REGISTER_INVITE_CODE_NOT_FOUND\n" +
                "USER_REGISTER_INVITE_CODE_DISABLED\n" +
                "USER_REGISTER_MOBILE_REQUIRED\n" +
                "USER_REGISTER_MOBILE_REPEAT\n" +
                "USER_REGISTER_MOBILE_VERIFICATION_CODE_REQUIRED\n" +
                "USER_REGISTER_FUND_PWD_REQUIRED\n" +
                "INVITE_CODE_NOT_FOUND\n" +
                "USER_WITHDRAW_NAME_MUST_BE_CHINESE\n" +
                "USER_WITHDRAW_NAME_ALREADY_BOUND\n" +
                "USER_WITHDRAW_NAME_NOT_YET_BIND\n" +
                "NOT_YOUR_FRIEND\n" +
                "RECHARGE_NO_CARD_FOUND\n" +
                "RECHARGE_USER_CARD_NAME_REQUIRED\n" +
                "WITHDRAW_MAX_DAILY_REQUEST_EXCEEDED\n" +
                "WITHDRAW_MAX_SAME_TIME_REQUEST_EXCEEDED\n" +
                "WITHDRAW_LIMIT_NOT_YET_FINISH\n" +
                "WITHDRAW_NOT_IN_ENABLE_TIME\n" +
                "API_RECHARGE_CONFIG_NOT_FOUND\n" +
                "API_RECHARGE_THIRD_CONFIG_FORMATTING_INCORRECT\n" +
                "API_RECHARGE_HANDLER_NOT_FOUND\n" +
                "API_RECHARGE_REQUEST_EXCEPTION\n" +
                "API_RECHARGE_REQUEST_RETURN_FAILED\n" +
                "API_RECHARGE_CALLBACK_PROCESS_FAILED\n" +
                "API_WITHDRAW_CONFIG_NOT_FOUND\n" +
                "API_WITHDRAW_THIRD_CONFIG_FORMATTING_INCORRECT\n" +
                "API_WITHDRAW_CONFIG_DISABLED\n" +
                "API_WITHDRAW_HANDLER_NOT_FOUND\n" +
                "API_WITHDRAW_REQUEST_EXCEPTION\n" +
                "API_WITHDRAW_REQUEST_RETURN_FAILED\n" +
                "API_WITHDRAW_CALLBACK_PROCESS_FAILED\n" +
                "BANK_NOT_FOUND\n" +
                "BANK_DISABLED\n" +
                "BANK_NAME_EXISTED\n" +
                "BANK_CODE_EXISTED\n" +
                "USER_BANK_CARD_NUM_IN_BLACKLIST\n" +
                "USER_BANK_CARD_CARD_NUM_EXISTED\n" +
                "USER_BANK_CARD_REQUIRED\n" +
                "BANK_WITHDRAW_DISABLED\n" +
                "USER_BANK_CARD_MAX_BIND_COUNT_EXCEEDED\n" +
                "USER_BANK_CARD_NOT_YET_BIND\n" +
                "USER_BANK_CARD_NOT_FOUND\n" +
                "USER_BANK_CARD_DISABLED\n" +
                "USER_MOMENTS_EXCEED_IMG_COUNT\n" +
                "USER_MOMENTS_NOT_READ\n" +
                "USER_MOMENTS_ALLOW\n" +
                "USER_MOMENTS_REJECT\n" +
                "REPORT_DATA_MAX_SEARCH_DAY\n" +
                "REPORT_DATE_ERROR\n" +
                "REPORT_USER_EXISTED\n" +
                "I18N_LANGUAGE_LIST_NOT_AVAILABLE\n" +
                "I18N_LANGUAGE_NOT_AVAILABLE\n" +
                "ENVELOPE_EXPIRED\n" +
                "ENVELOPE_RECEIVED\n" +
                "ENVELOPE_ALL_RECEIVED\n" +
                "ENVELOPE_NOT_TO_SELF\n" +
                "ENVELOPE_MAX_NUM_INCORRECT\n" +
                "ENVELOPE_AVERAGE_AMOUNT_INCORRECT\n" +
                "USER_IN_GROUP_CONFLICT\n" +
                "MESSAGE_CAN_NOT_WITHDRAW\n" +
                "USER_NOT_IN_GROUP\n" +
                "USER_GROUP_ALL_SHUTTING_UP\n" +
                "USER_GROUP_SHUTTING_UP\n" +
                "SMS_THIRD_CONNECT_EXCEPTION\n" +
                "SMS_THIRD_CONNECT_INCORRECT\n" +
                "SMS_VERIFICATION_CODE_EXPIRED\n" +
                "SMS_VERIFICATION_CODE_INCORRECT\n" +
                "SMS_COUNTRY_NOT_SUPPORT\n" +
                "APP_VERSION_CODE_MUST_INCREASE";

        String zhCns = "该数据目前不可编辑！\n" +
                "该数据已被删除！\n" +
                "数据已存在！\n" +
                "数据不存在！\n" +
                "数据不存在或已过期\n" +
                "您所在IP[{}]不允许操作！\n" +
                "IP[{}]不是有效IPV4/IPV4掩码/IPV6格式\n" +
                "IP格式错误\n" +
                "您所在区域[{}]不允许操作！\n" +
                "同一时间仅允许一次操作\n" +
                "已经是第一位了！\n" +
                "已经是最后一位了！\n" +
                "金额格式错误\n" +
                "金额不支持小数\n" +
                "金额格式错误，且最多{}位小数\n" +
                "金额不在{}范围内\n" +
                "金额范围格式错误\n" +
                "时间范围格式错误\n" +
                "OSS连接错误{}\n" +
                "请选择上传文件\n" +
                "上传文件出错{}\n" +
                "上传文件格式仅接受JPG或PNG\n" +
                "上传大小超出最大限制{}\n" +
                "最多只能查询过去{}天的数据\n" +
                "导出时间跨度最多{}天\n" +
                "请输入备注\n" +
                "IM连接异常\n" +
                "IM执行异常，{}\n" +
                "角色名已经存在！\n" +
                "您只能操作您已拥有的角色或下级角色！\n" +
                "您只能操作下级角色！\n" +
                "您的账号已被禁用，请联系客服！\n" +
                "您的账号登录权限已被禁用，请联系客服！\n" +
                "您的账号充值权限已被禁用，请联系客服！\n" +
                "您的账号提现权限已被禁用，请联系客服！\n" +
                "您已经登录！\n" +
                "用户名或密码错误！\n" +
                "用户名或密码错误，还剩{}机会！\n" +
                "登录密码错误次数过多，请联系管理员重置！\n" +
                "角色已被禁用！\n" +
                "非法角色！\n" +
                "用户不存在！\n" +
                "用户余额不足\n" +
                "谷歌验证码不能为空！\n" +
                "谷歌验证码错误！\n" +
                "每组验证码只允许使用1次，请稍候再试\n" +
                "您已绑定谷歌身份验证器，无需再次绑定！\n" +
                "请先绑定谷歌身份验证器！\n" +
                "用户名已存在！\n" +
                "不允许操作自己！\n" +
                "不允许操作管理员！\n" +
                "您已被强制登出！\n" +
                "您的账号在别处登录，请重新登录！\n" +
                "您的账号信息发生变化，请重新登录！\n" +
                "会话失效，请重新登录！\n" +
                "会话过期，请重新登录！\n" +
                "请先登录！\n" +
                "token参数不能为空！\n" +
                "token无效！\n" +
                "旧密码输入错误！\n" +
                "您已设置该密码，无需再次设置！\n" +
                "新旧密码不能一致！\n" +
                "请先设置资金密码！\n" +
                "资金密码错误！\n" +
                "资金密码不允许和登录密码一致！\n" +
                "用户充值权限不可用！\n" +
                "用户提现权限不可用！\n" +
                "注册链接最多生成{}个，请调整\n" +
                "注册入口已关闭，请联系客服\n" +
                "该IP[{}]注册用户数超过限制\n" +
                "请输入邀请码\n" +
                "邀请码错误，请重新输入\n" +
                "邀请码不可用，请重新输入\n" +
                "请输入手机\n" +
                "该手机号已注册\n" +
                "请输入手机验证码\n" +
                "请输入资金密码\n" +
                "邀请码不存在\n" +
                "提现姓名必须为中文\n" +
                "您已绑定提现姓名，无需再次绑定\n" +
                "请先绑定提现姓名\n" +
                "对方还不是您的好友\n" +
                "未找到可用的银行卡，请选择其它充值方式\n" +
                "请输入付款人\n" +
                "每日最多允许提交{}笔提现订单\n" +
                "同时最多允许提交{}笔提现订单\n" +
                "您当前还需消费≈{}:{}金额的虚拟货币，才可提现\n" +
                "当前已过提现时间：{}\n" +
                "未找到该三方配置，或已被删除\n" +
                "三方配置格式错误\n" +
                "未找到该三方充值处理类\n" +
                "请求三方充值异常，{}\n" +
                "请求三方充值返回错误，{}\n" +
                "回调处理失败\n" +
                "未找到该API代付配置，或已被删除\n" +
                "三方配置格式错误\n" +
                "该API代付配置已被禁用\n" +
                "未找到该API代付处理类\n" +
                "请求API代付异常，{}\n" +
                "请求API代付返回错误，{}\n" +
                "回调处理失败\n" +
                "银行不存在\n" +
                "银行已被禁用\n" +
                "银行名已存在\n" +
                "银行名编码存在\n" +
                "卡号{}已被列入黑名单，无法处理\n" +
                "卡号已存在\n" +
                "请选择银行卡\n" +
                "提现银行已被禁用\n" +
                "最多允许绑定{}张银行卡，您目前已绑定{}张\n" +
                "请先绑定银行卡\n" +
                "未找到您绑定的银行卡信息\n" +
                "该银行卡目前不可用\n" +
                "朋友圈最多发布9张图片\n" +
                "您没有权限查看\n" +
                "请选择允许查看的好友\n" +
                "请选择拒绝查看的好友\n" +
                "最多允许查询过去{}天数据，请重试\n" +
                "日期选择错误，请重试\n" +
                "用户{}不是直属下级或者不存在，无法查询，请重试\n" +
                "没有可用语言！\n" +
                "该语言不可用！\n" +
                "该红包已过期\n" +
                "该红包已被领取\n" +
                "该红包已被领取完\n" +
                "不能给自己发红包\n" +
                "群红包个数最多拆分{}个\n" +
                "平均金额必须范围必须为{}\n" +
                "用户已经在群组中，无需添加！\n" +
                "只能删除一天内的消息，请重新选择！\n" +
                "您不在群组中\n" +
                "全员禁言中，无法发言\n" +
                "您被禁言中，无法发言\n" +
                "短信服务连接异常，请选择其它方式或联系客服\n" +
                "短信服务连接失败，{}，请选择其它方式或联系客服\n" +
                "验证码过期\n" +
                "验证码错误\n" +
                "不支持该国家\n" +
                "应用数字版本号必须递增";

        String ens = "This data is currently not editable!\n" +
                "This data has been deleted!\n" +
                "Data already exists!\n" +
                "Data does not exist!\n" +
                "Data does not exist or has expired\n" +
                "Your IP [{}] does not allow operation!\n" +
                "IP[{}] is not in valid IPV4/IPV4 mask/IPV6 format\n" +
                "IP format error\n" +
                "Operation not allowed in your region [{}]!\n" +
                "Only one operation is allowed at the same time\n" +
                "Already the first!\n" +
                "Already the last one!\n" +
                "Amount formatted incorrectly\n" +
                "Amount does not support decimals\n" +
                "Amount is malformed with at most {} decimal places\n" +
                "Amount not in {} range\n" +
                "Amount range format error\n" +
                "wrong time range format\n" +
                "OSS connection error {}\n" +
                "Please select upload file\n" +
                "Error uploading file {}\n" +
                "Upload file format only accepts JPG or PNG\n" +
                "Upload size exceeds maximum limit {}\n" +
                "You can only query data for the past {} days at most\n" +
                "Export time span up to {} days\n" +
                "Please enter a note\n" +
                "IM connection is abnormal\n" +
                "IM execution exception, {}\n" +
                "Character name already exists!\n" +
                "You can only operate characters you already own or subordinate characters!\n" +
                "You can only operate subordinate characters!\n" +
                "Your account has been disabled, please contact customer service!\n" +
                "Your account login permission has been disabled, please contact customer service!\n" +
                "Your account recharge permission has been disabled, please contact customer service!\n" +
                "Your account withdrawal permission has been disabled, please contact customer service!\n" +
                "You are logged in!\n" +
                "wrong user name or password!\n" +
                "Incorrect username or password, {} chance left!\n" +
                "The login password is wrong too many times, please contact the administrator to reset!\n" +
                "Character has been disabled!\n" +
                "Illegal character!\n" +
                "User does not exist!\n" +
                "Insufficient user balance\n" +
                "Google verification code cannot be empty!\n" +
                "Google verification code error!\n" +
                "Each verification code can only be used once, please try again later\n" +
                "You are already bound to Google Authenticator, no need to bind again!\n" +
                "Please bind Google Authenticator first!\n" +
                "Username already exists!\n" +
                "Do not operate yourself!\n" +
                "Operation administrator is not allowed!\n" +
                "You have been forcibly logged out!\n" +
                "Your account is logged in elsewhere, please log in again!\n" +
                "Your account information has changed, please log in again!\n" +
                "The session is invalid, please log in again!\n" +
                "Session expired, please log in again!\n" +
                "please log in first!\n" +
                "The token parameter cannot be empty!\n" +
                "Invalid token!\n" +
                "The old password was entered incorrectly!\n" +
                "You have already set this password, no need to set it again!\n" +
                "The old and new passwords cannot be the same!\n" +
                "Please set the fund password first!\n" +
                "Funding password is wrong!\n" +
                "The fund password is not allowed to be the same as the login password!\n" +
                "User recharge permission is not available!\n" +
                "User withdrawal permission is unavailable!\n" +
                "A maximum of {} registration links can be generated, please adjust\n" +
                "The registration entrance has been closed, please contact customer service\n" +
                "The number of registered users of this IP[{}] exceeds the limit\n" +
                "Please enter the invitation code\n" +
                "Invitation code error, please re-enter\n" +
                "Invitation code is unavailable, please re-enter\n" +
                "Please enter your phone\n" +
                "The phone number is already registered\n" +
                "Please enter your mobile phone verification code\n" +
                "Please enter the fund password\n" +
                "Invitation code does not exist\n" +
                "Withdrawal name must be in Chinese\n" +
                "You have already bound the withdrawal name, no need to bind again\n" +
                "Please bind the withdrawal name first\n" +
                "The person is not your friend yet\n" +
                "No available bank card was found, please select another top-up method\n" +
                "Please enter payer\n" +
                "A maximum of {} withdrawal orders are allowed per day\n" +
                "A maximum of {} withdrawal orders are allowed at the same time\n" +
                "You still need to spend ≈{}:{} amount of virtual currency before you can withdraw\n" +
                "The current withdrawal time has passed: {}\n" +
                "The third-party configuration was not found, or has been deleted\n" +
                "3rd party configuration format error\n" +
                "The third-party recharge processing class was not found\n" +
                "The request for third-party recharge is abnormal, {}\n" +
                "Requesting third-party recharge returns an error, {}\n" +
                "Callback processing failed\n" +
                "The API payment configuration is not found, or has been deleted\n" +
                "3rd party configuration format error\n" +
                "The API payment configuration has been disabled\n" +
                "The API payment processing class was not found\n" +
                "Request API payment exception, {}\n" +
                "An error was returned when requesting API payment, {}\n" +
                "Callback processing failed\n" +
                "Bank does not exist\n" +
                "Bank is disabled\n" +
                "bank name already exists\n" +
                "Bank name code exists\n" +
                "Card number {} has been blacklisted and cannot be processed\n" +
                "Card number already exists\n" +
                "Please select a bank card\n" +
                "Withdrawal bank has been disabled\n" +
                "A maximum of {} bank cards are allowed to be bound, and you have bound {} cards at present\n" +
                "Please bind your bank card first\n" +
                "Your linked bank card information was not found\n" +
                "This card is currently unavailable\n" +
                "Publish up to 9 pictures in the circle of friends\n" +
                "You do not have permission to view\n" +
                "Please select friends who are allowed to view\n" +
                "Please select friends who refuse to view\n" +
                "Maximum allowed to query past {} days of data, please try again\n" +
                "Incorrect date selection, please try again\n" +
                "User {} is not a direct subordinate or does not exist and cannot be queried, please try again\n" +
                "No language available!\n" +
                "This language is not available!\n" +
                "This red packet has expired\n" +
                "The red envelope has been claimed\n" +
                "The red envelope has been collected\n" +
                "You can't give yourself a red envelope\n" +
                "The maximum number of group red envelopes can be divided into {} pieces\n" +
                "Average amount must range must be {}\n" +
                "User is already in the group, no need to add!\n" +
                "Only messages within one day can be deleted, please select again!\n" +
                "you are not in the group\n" +
                "All members are silenced, unable to speak\n" +
                "You are banned from speaking\n" +
                "The SMS service connection is abnormal, please choose another method or contact customer service\n" +
                "SMS service connection failed, {}, please choose another method or contact customer service\n" +
                "Verification code expired\n" +
                "Verification code error\n" +
                "This country is not supported\n" +
                "Application numeric version number must be incremented";

        String jaJps = "このデータは現在編集できません！\n" +
                "このデータは削除されました！\n" +
                "データはすでに存在します！\n" +
                "データが存在しません！\n" +
                "データが存在しないか、有効期限が切れています\n" +
                "あなたのIP [{}]は操作を許可していません！\n" +
                "IP [{}]が有効なIPV4 / IPV4マスク/ IPV6形式ではありません\n" +
                "IPフォーマットエラー\n" +
                "お住まいの地域では操作が許可されていません[{}]！\n" +
                "同時に許可される操作は1つだけです\n" +
                "すでに最初！\n" +
                "もう最後です！\n" +
                "正しくフォーマットされていない金額\n" +
                "金額は小数をサポートしていません\n" +
                "金額は、小数点以下{}桁までで不正な形式になっています\n" +
                "{}の範囲外の金額\n" +
                "金額範囲フォーマットエラー\n" +
                "間違った時間範囲形式\n" +
                "OSS接続エラー{}\n" +
                "アップロードファイルを選択してください\n" +
                "ファイルのアップロード中にエラーが発生しました{}\n" +
                "アップロードファイル形式はJPGまたはPNGのみを受け入れます\n" +
                "アップロードサイズが上限を超えています{}\n" +
                "クエリできるのは、最大で過去{}日間のデータのみです。\n" +
                "エクスポート期間は最大{}日\n" +
                "メモを入力してください\n" +
                "IM接続が異常です\n" +
                "IM実行例外、{}\n" +
                "キャラクター名はすでに存在します！\n" +
                "すでに所有しているキャラクターまたは従属キャラクターのみ操作できます！\n" +
                "従属キャラクターしか操作できません！\n" +
                "アカウントが無効になっています。カスタマーサービスにご連絡ください。\n" +
                "アカウントのログイン許可が無効になっています。カスタマーサービスにご連絡ください。\n" +
                "アカウントの再充電許可が無効になっています。カスタマーサービスにご連絡ください。\n" +
                "アカウントの引き出し許可が無効になっています。カスタマーサービスにご連絡ください。\n" +
                "ログインしています！\n" +
                "間違ったユーザー名またはパスワード！\n" +
                "ユーザー名またはパスワードが正しくありません。{}チャンスが残っています！\n" +
                "ログインパスワードが何度も間違っている場合は、管理者に連絡してリセットしてください。\n" +
                "キャラクターが無効になりました！\n" +
                "違法なキャラクター！\n" +
                "ユーザーは存在しません！\n" +
                "不十分なユーザーバランス\n" +
                "Google確認コードを空にすることはできません。\n" +
                "Google検証コードエラー！\n" +
                "各確認コードは1回のみ使用できます。後でもう一度お試しください\n" +
                "すでにGoogle認証システムにバインドされているので、再度バインドする必要はありません。\n" +
                "最初にGoogle認証システムをバインドしてください。\n" +
                "ユーザー名は既に存在します！\n" +
                "自分で操作しないでください！\n" +
                "運用管理者は許可されていません！\n" +
                "強制的にログアウトしました！\n" +
                "あなたのアカウントは他の場所でログインしています。もう一度ログインしてください！\n" +
                "アカウント情報が変更されました。もう一度ログインしてください。\n" +
                "セッションが無効です。もう一度ログインしてください。\n" +
                "セッションの有効期限が切れました。もう一度ログインしてください。\n" +
                "最初にログインしてください！\n" +
                "トークンパラメータを空にすることはできません！\n" +
                "無効なトークン！\n" +
                "古いパスワードが間違って入力されました！\n" +
                "このパスワードはすでに設定されているので、再度設定する必要はありません。\n" +
                "古いパスワードと新しいパスワードを同じにすることはできません。\n" +
                "最初にファンドのパスワードを設定してください！\n" +
                "資金調達のパスワードが間違っています！\n" +
                "ファンドのパスワードをログインパスワードと同じにすることはできません。\n" +
                "ユーザーの再充電許可は利用できません！\n" +
                "ユーザーの撤退許可は利用できません！\n" +
                "最大{}の登録リンクを生成できます。調整してください\n" +
                "登録入口は閉鎖されていますので、カスタマーサービスまでご連絡ください\n" +
                "このIP [{}]の登録ユーザー数が制限を超えています\n" +
                "招待コードを入力してください\n" +
                "招待コードエラー、再入力してください\n" +
                "招待コードは利用できません。再入力してください\n" +
                "電話を入力してください\n" +
                "電話番号はすでに登録されています\n" +
                "携帯電話の確認コードを入力してください\n" +
                "ファンドのパスワードを入力してください\n" +
                "招待コードが存在しません\n" +
                "引き出し名は中国語である必要があります\n" +
                "引き出し名はすでにバインドされているので、再度バインドする必要はありません\n" +
                "最初に引き出し名をバインドしてください\n" +
                "その人はまだあなたの友達ではありません\n" +
                "利用可能な銀行カードが見つかりませんでした。別のチャージ方法を選択してください\n" +
                "支払人を入力してください\n" +
                "1日あたり最大{}の引き出し注文が許可されます\n" +
                "同時に最大{}の引き出し注文が許可されます\n" +
                "引き出す前に、仮想通貨の≈{}：{}の金額を使う必要があります\n" +
                "現在の撤退時間が経過しました：{}\n" +
                "サードパーティの構成が見つからなかったか、削除されました\n" +
                "サードパーティの構成フォーマットエラー\n" +
                "サードパーティの再充電処理クラスが見つかりませんでした\n" +
                "サードパーティの再充電の要求は異常です、{}\n" +
                "サードパーティの再充電を要求すると、エラー{}が返されます\n" +
                "コールバック処理に失敗しました\n" +
                "API支払い構成が見つからないか、削除されました\n" +
                "サードパーティの構成フォーマットエラー\n" +
                "API支払い構成が無効になっています\n" +
                "API支払い処理クラスが見つかりませんでした\n" +
                "API支払いの例外をリクエストします。{}\n" +
                "API支払いをリクエストすると、エラーが返されました、{}\n" +
                "コールバック処理に失敗しました\n" +
                "銀行は存在しません\n" +
                "銀行が無効になっています\n" +
                "銀行名はすでに存在します\n" +
                "銀行名コードが存在します\n" +
                "カード番号{}はブラックリストに登録されており、処理できません\n" +
                "カード番号はすでに存在します\n" +
                "銀行カードを選択してください\n" +
                "引き出し銀行が無効になりました\n" +
                "最大{}枚の銀行カードをバインドできます。現在、{}枚のカードをバインドしています。\n" +
                "最初に銀行カードをバインドしてください\n" +
                "リンクされた銀行カード情報が見つかりませんでした\n" +
                "このカードは現在ご利用いただけません\n" +
                "友達の輪に最大9枚の写真を公開する\n" +
                "表示する権限がありません\n" +
                "閲覧を許可されている友達を選択してください\n" +
                "閲覧を拒否する友達を選択してください\n" +
                "過去{}日間のデータのクエリに許可されている最大数、再試行してください\n" +
                "日付の選択が正しくありません。もう一度やり直してください\n" +
                "ユーザー{}は直接の部下ではないか、存在せず、クエリできません。もう一度やり直してください\n" +
                "利用できる言語はありません！\n" +
                "この言語は利用できません！\n" +
                "この赤いパケットは期限切れです\n" +
                "赤い封筒が要求されました\n" +
                "赤い封筒が集められました\n" +
                "あなたは自分自身に赤い封筒を与えることはできません\n" +
                "グループの赤い封筒の最大数は{}個に分割できます\n" +
                "平均金額の範囲は{}である必要があります\n" +
                "ユーザーはすでにグループに含まれているため、追加する必要はありません。\n" +
                "削除できるのは1日以内のメッセージのみです。もう一度選択してください。\n" +
                "あなたはグループに属していません\n" +
                "すべてのメンバーは沈黙し、話すことができません\n" +
                "あなたは話すことを禁止されています\n" +
                "SMSサービス接続が異常です。別の方法を選択するか、カスタマーサービスに連絡してください\n" +
                "SMSサービス接続に失敗しました。{}、別の方法を選択するか、カスタマーサービスに連絡してください\n" +
                "確認コードの有効期限が切れました\n" +
                "検証コードエラー\n" +
                "この国はサポートされていません\n" +
                "アプリケーションの数値バージョン番号をインクリメントする必要があります";

        String[] codeArr = codes.split("\n");
        String[] zhCnArr = zhCns.split("\n");
        String[] enArr = ens.split("\n");
        String[] jaJpArr = jaJps.split("\n");

        String sqlTemp = "INSERT INTO `i18n_translate` (`id`, `group`, `key`, `language_code`, `value`, `create_time`, `update_time`)\n" +
                "VALUES\n" +
                "\t(NULL, 'RSP_MSG', '{}', 'zh-CN', '{}', '2020-05-27 14:54:19', '2020-05-27 14:54:19'),\n" +
                "\t(NULL, 'RSP_MSG', '{}', 'en', '{}', '2020-05-27 14:54:19', '2020-05-27 14:54:19'),\n" +
                "\t(NULL, 'RSP_MSG', '{}', 'ja-JP', '{}', '2020-05-27 14:54:19', '2020-05-27 14:54:19');";

        for (int i = 0; i < codeArr.length; i++) {
            String code = codeArr[i];
            String zhCn = zhCnArr[i];
            String en = enArr[i];
            String jaJp = jaJpArr[i];

            String sql = StrUtil.format(sqlTemp, code, zhCn, code, en, code, jaJp);
            System.out.println(sql);
            System.out.println();
        }
    }
}
