package com.im.callback.controller.url;

/**
 * 定义API地址
 *
 * @author Barry
 * @date 2020-06-01
 */
public final class ApiUrl {
    public ApiUrl() {
    }

    public static final String BASE_NO_AUTH_URL = "/api/callback";

    /**
     * 腾讯IM回调
     **/
    public static final String TIM_CALLBACK = BASE_NO_AUTH_URL + "/tim/callback";
}
