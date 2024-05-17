package com.im.common.constant;

import cn.hutool.core.collection.ListUtil;
import org.springframework.http.MediaType;

import java.io.File;
import java.math.BigDecimal;
import java.util.List;

/**
 * 定义一些经常使用比较通用的常量
 *
 * @author Barry
 * @date 2018/8/31
 */
public final class CommonConstant {
    /**
     * 开发环境profile变量
     **/
    public static final String PROFILE_DEV = "dev";
    /**
     * 测试环境profile变量
     **/
    public static final String PROFILE_TEST = "test";
    /**
     * 生产环境profile变量
     **/
    public static final String PROFILE_PRO = "pro";
    /**
     * 项目基础包名
     **/
    public static final String PROJECT_BASE_PACKAGE = "com.im";
    /**
     * 自定义Spring参数注解包位置
     **/
    public static final String ARGUMENT_RESOLVER_PACKAGE = PROJECT_BASE_PACKAGE + ".common.util";
    /**
     * 外部接口-请求时间戳超时时间秒数
     **/
    public static final int DEFAULT_REQUEST_TIMESTAMP_TIMEOUT_SECONDS = 180;
    /**
     * 0
     **/
    public static final int INT_0 = 0;
    /**
     * 0包装类型
     **/
    public static final Integer INT_0_PACK = Integer.valueOf(INT_0);
    /**
     * 1
     **/
    public static final int INT_1 = 1;
    /**
     * 1包装类型
     **/
    public static final Integer INT_1_PACK = Integer.valueOf(INT_1);
    /**
     * 2
     **/
    public static final int INT_2 = 2;
    /**
     * 2包装类型
     **/
    public static final Integer INT_2_PACK = Integer.valueOf(INT_2);
    /**
     * 4
     **/
    public static final int INT_4 = 4;
    /**
     * 4包装类型
     **/
    public static final Integer INT_4_PACK = Integer.valueOf(INT_4);
    /**
     * 6
     **/
    public static final int INT_6 = 6;
    /**
     * 6包装类型
     **/
    public static final Integer INT_6_PACK = Integer.valueOf(INT_6);
    /**
     * 8
     **/
    public static final int INT_8 = 8;
    /**
     * 8包装类型
     **/
    public static final Integer INT_8_PACK = Integer.valueOf(INT_8);
    /**
     * 10
     **/
    public static final int INT_10 = 10;
    /**
     * 10包装类型
     **/
    public static final Integer INT_10_PACK = Integer.valueOf(INT_10);
    /**
     * 金额分的除数
     **/
    public static final int FENG_DIV = 100;
    /**
     * 金额分的除数
     **/
    public static final Integer FENG_DIV_PACK = Integer.valueOf(FENG_DIV);
    /**
     * long 0
     **/
    public static final long LONG_0 = 0L;
    /**
     * Long 0包装类型
     **/
    public static final Long LONG_0_PACK = Long.valueOf(LONG_0);
    /**
     * long 1
     **/
    public static final long LONG_1 = 1L;
    /**
     * Long 1包装类型
     **/
    public static final Long LONG_1_PACK = Long.valueOf(LONG_1);
    /**
     * double 0
     **/
    public static final double DOUBLE_0 = 0D;
    /**
     * Double 0包装类型
     **/
    public static final Double DOUBLE_0_PACK = Double.valueOf(DOUBLE_0);
    /**
     * BigDecimal 0
     **/
    public static final BigDecimal BIG_DECIMAL_0 = BigDecimal.ZERO;
    /**
     * 英文逗号
     **/
    public static final String DOT_EN = ",";
    /**
     * 英文逗号,char
     **/
    public static final char DOT_EN_CHAR = ',';
    /**
     * 英文句号
     **/
    public static final String POINT_EN = ".";
    /**
     * 英文冒号
     **/
    public static final String COLON_EN = ":";
    /**
     * 英文分号
     **/
    public static final String SEMICOLON_EN = ";";
    /**
     * 英文问号
     **/
    public static final String QUESTION_EN = "?";
    /**
     * UTF-8编码
     **/
    public static final String CHARACTER_ENCODING_UTF8 = "UTF-8";
    /**
     * 英文减号
     **/
    public static final String MINUS_EN = "-";
    /**
     * 一个空格
     */
    public static final String SPACE = " ";
    /**
     * 星号
     */
    public static final String ASTERISK = "*";
    /**
     * 等于号
     */
    public static final String EQUAL = "=";
    /**
     * 范围
     */
    public static final String RANGE_EN = "~";
    /**
     * application/json
     **/
    public static final String APPLICATION_JSON_UTF8_VALUE = MediaType.APPLICATION_JSON_UTF8_VALUE;
    /**
     * 国际化header key
     */
    public static final String LANGUAGE_HEADER = "language";
    /**
     * 每天开始时间后缀
     */
    public static final String START_DATE_TIME_SUFFIX = " 00:00:00";
    /**
     * 每天结束时间后缀
     */
    public static final String END_DATE_TIME_SUFFIX = " 23:59:59";
    /**
     * 邮箱的@
     */
    public static final String AT = "@";
    /**
     * 不限制数字
     */
    public static final double UNLIMITED_DOUBLE = 0d;
    /**
     * 不限制数字
     */
    public static final int UNLIMITED_INT = 0;
    /**
     * 不限制数字
     */
    public static final int UNLIMITED_LONG = 0;
    /**
     * 禁止消息回调
     */
    public static final List<String> forbidCallbackControl = ListUtil.of("ForbidBeforeSendMsgCallback", "ForbidAfterSendMsgCallback");
    /**
     * 指定 SendMsgControl，设置 NoLastMsg 的情况下，表示不更新最近联系人会话；NoUnread 不计未读
     */
    public static final List<String> sendMsgControl = ListUtil.of("NoLastMsg", "NoUnread");
    /**
     * 管理员账号
     */
    public static final String ADMINISTRATOR = "administrator";
    /**
     * 传递过来的msgKey的后缀是#I18N的才进行翻译
     **/
    public static String NEED_I18N_SUFFIX = "#I18N";

    private CommonConstant() {
    }

    /**
     * 返回本地目录路径
     *
     * @param dirName 目录名
     */
    public static String getLocalFileDir(String dirName) {
        String localFileDir = System.getProperty("user.home") + File.separator + dirName + File.separator;
        File dir = new File(localFileDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return localFileDir;
    }
}
