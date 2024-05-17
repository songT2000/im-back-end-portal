package com.im.common.util.jwt;

import java.io.Serializable;

/**
 * Jwt保存信息
 *
 * @author Barry
 * @date 2019-10-15
 */
public class JwtInfo implements Serializable {
    private static final long serialVersionUID = -9181671617097769639L;

    /**
     * 用户名
     **/
    private String username;

    /**
     * 设备标识
     **/
    private String deviceId;

    /**
     * 设备型号
     */
    private String device;

    /**
     * 设备类型
     **/
    private String deviceType;

    /**
     * 门户类型
     */
    private String portalType;

    public JwtInfo() {
    }

    public JwtInfo(String username, String deviceId, String device, String deviceType, String portalType) {
        this.username = username;
        this.deviceId = deviceId;
        this.device = device;
        this.deviceType = deviceType;
        this.portalType = portalType;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getPortalType() {
        return portalType;
    }

    public void setPortalType(String portalType) {
        this.portalType = portalType;
    }
}
