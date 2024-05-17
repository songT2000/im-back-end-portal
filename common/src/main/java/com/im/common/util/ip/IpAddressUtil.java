package com.im.common.util.ip;

import cn.hutool.core.lang.Validator;
import cn.hutool.core.net.Ipv4Util;
import cn.hutool.core.net.NetUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.im.common.constant.CommonConstant;
import com.im.common.util.CollectionUtil;
import com.im.common.util.FileUtil;
import com.im.common.util.NumberUtil;
import com.im.common.util.StrUtil;
import net.ipip.ipdb.City;
import net.ipip.ipdb.CityInfo;
import net.ipip.ipdb.IPFormatException;
import net.ipip.ipdb.InvalidDatabaseException;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * IP地址工具类(ipip免费ipv4库、ipv6wry库),需手动调用init方法以加载IP库
 * 支持IPV4，IPV6
 *
 * @author Barry
 * @date 2018/10/4
 */
public final class IpAddressUtil {
    private static final Log LOG = LogFactory.get();
    /**
     * 本机IPV4地址
     **/
    private static Set<String> LOCAL_IPV4S = NetUtil.localIpv4s();
    /**
     * 本机IPV6地址
     **/
    private static Set<String> LOCAL_IPV6S = CollectionUtil.toSet("::1", "0:0:0:0:0:0:0:1");

    private IpAddressUtil() {
    }

    /**
     * IPIP库路径
     */
    private static final String IPIP_FILE = "ip/ipipfree.ipdb";

    /**
     * ipip城市实例
     */
    private static City IPIP_CITY_INSTANCE = null;

    /**
     * IPIP语言
     */
    private static final String IPIP_LANGUAGE = "CN";

    /**
     * IPV6库路径
     */
    private static final String IPV6_FILE = "ip/ipv6wry.db";

    /**
     * 未知区域
     */
    private static final String UNDEFINED_AREA = "未知区域";

    public static void main(String[] args) {
        try {
            System.out.println(isInIpv4Mask("101.32.0.0/16", "101.32.111.111"));
            System.out.println(isInIpv4Mask("101.32.0.0/16", "102.32.111.111"));

            // ipip
            // String ip = "3.112.36.76";
            //
            // ClassPathResource resource = new ClassPathResource(IPIP_FILE);
            // City city = new City(resource.getInputStream());
            // // District city = new District(resource.getInputStream());
            //
            // String[] locations = city.find(ip, IPIP_LANGUAGE);
            // if (CollectionUtil.isEmpty(locations)) {
            //     System.out.println("null");
            // } else {
            //     System.out.println(CollectionUtil.join(",", true, locations));
            // }

            // ip6
            // String ip = "2409:8a74:cf:8f0:9459:4ba4:a172:1f5c";
            //
            // ClassPathResource resource = new ClassPathResource(IPV6_FILE);
            // IPV6Util.load(resource.getFile());
            //
            // String[] locations = IPV6Util.find(ip);
            // if (CollectionUtil.isEmpty(locations)) {
            //     System.out.println("null");
            // } else {
            //     System.out.println(CollectionUtil.join(",", true, locations));
            // }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 调用此方法以加载IP数据库
     */
    public static void init(ApplicationContext applicationContext) throws Exception {
        // 加载ipip数据库
        initIpip();
        // 加载ipv6数据库
        initIpv6(applicationContext);
    }

    /**
     * 调用此方法加载ipip数据库
     */
    private static void initIpip() throws Exception {
        ClassPathResource resource = new ClassPathResource(IPIP_FILE);
        IPIP_CITY_INSTANCE = new City(resource.getInputStream());
    }

    /**
     * 调用此方法加载ipv6数据库
     */
    private static void initIpv6(ApplicationContext applicationContext) throws Exception {
        File tempFile = FileUtil.copyResourceFileIntoTemp(IPV6_FILE, applicationContext);

        IPV6Util.load(tempFile);
    }

    /**
     * 根据IP返回地理位置
     *
     * @param ip IPV4/IPV6
     * @return 地理位置，如  广东省深圳市,鹏博士长城宽带
     */
    public static String findLocation(String ip) {
        if (isIpv6(ip)) {
            return findLocationByIpv6(ip);
        } else if (isIpv4(ip)) {
            return findLocationByIpip(ip);
        }
        return UNDEFINED_AREA;
    }

    /**
     * ipv4查找地址
     */
    private static String findLocationByIpip(String ip) {
        if (IPIP_CITY_INSTANCE == null) {
            throw new IllegalStateException("请先调用init初始化IPIP库文件");
        }

        try {
            String[] locations = IPIP_CITY_INSTANCE.find(ip, IPIP_LANGUAGE);
            if (CollectionUtil.isEmpty(locations)) {
                return UNDEFINED_AREA;
            }

            return CollectionUtil.joinWithoutBlank(locations);
        } catch (IPFormatException | InvalidDatabaseException e) {
            LOG.error(e);
        }

        return UNDEFINED_AREA;
    }

    /**
     * ipv6查找地址
     */
    private static String findLocationByIpv6(String ip) {
        try {
            String[] locations = IPV6Util.find(ip);
            if (CollectionUtil.isEmpty(locations)) {
                return UNDEFINED_AREA;
            }

            return CollectionUtil.joinWithoutBlank(locations);
        } catch (Exception e) {
            LOG.error(e);
        }

        return UNDEFINED_AREA;
    }

    /**
     * 根据IP返回地理位置
     *
     * @param ip IPV4
     * @return 地理位置，如  广东省深圳市,鹏博士长城宽带
     */
    public static CityInfo findCity(String ip) {
        if (IPIP_CITY_INSTANCE == null) {
            throw new IllegalStateException("请先调用init初始化IP库文件");
        }

        try {
            CityInfo info = IPIP_CITY_INSTANCE.findInfo(ip, IPIP_LANGUAGE);
            return info;
        } catch (IPFormatException e) {
            LOG.error(e);
        } catch (InvalidDatabaseException e) {
            LOG.error(e);
        }
        return null;
    }

    /**
     * 获取IP列表中不合法的IPv4
     *
     * @param ips IP列表，多个以英文逗号分割
     * @return 如果返回null则表示没有，否则有非法IP
     */
    public static String getIllegalIPV4(String ips) {
        if (StrUtil.isBlank(ips)) {
            return null;
        }

        String[] checkIpArr = StrUtil.splitToArray(ips, StrUtil.COMMA);
        for (String checkIp : checkIpArr) {
            if (!Validator.isIpv4(checkIp)) {
                return checkIp;
            }
        }

        return null;
    }

    /**
     * 判断IP地址是否是IPV4
     *
     * @param ip IP
     * @return
     */
    public static boolean isIpv4(String ip) {
        return Validator.isIpv4(ip);
    }

    /**
     * 判断IP地址是否是IPV4掩码
     *
     * @param ip IP
     * @return
     */
    public static boolean isIpv4Mask(String ip) {
        if (StrUtil.isNotBlank(ip) && ip.contains(Ipv4Util.IP_MASK_SPLIT_MARK)) {
            final String[] ipParam = StrUtil.splitToArray(ip, Ipv4Util.IP_MASK_SPLIT_MARK);
            String ipStr = ipParam[0];
            return Validator.isIpv4(ipStr) && NumberUtil.isInteger(ipParam[1]);
        }

        return false;
    }

    /**
     * 判断IP地址是否是IPV4段，格式127.0.0.1~127.0.0.255
     *
     * @param ip IP
     * @return
     */
    public static boolean isIpv4Range(String ip) {
        if (StrUtil.isNotBlank(ip) && ip.contains(CommonConstant.RANGE_EN)) {
            final String[] ipParam = StrUtil.splitToArray(ip, CommonConstant.RANGE_EN);
            String ip1 = ipParam[0];
            String ip2 = ipParam[1];
            return Validator.isIpv4(ip1) && Validator.isIpv4(ip2);
        }

        return false;
    }

    /**
     * 判断IP地址是否是IPV6
     *
     * @param ip IP
     * @return
     */
    public static boolean isIpv6(String ip) {
        return Validator.isIpv6(ip);
    }

    /**
     * 判断IP是否是本机IP
     * IPV4：127.0.0.1 | 本机内网地址
     * IPV6：0:0:0:0:0:0:0:1 | ::1
     *
     * @param ip IP地址
     * @return
     */
    public static boolean isLocalIp(String ip) {
        return LOCAL_IPV4S.contains(ip) || LOCAL_IPV6S.contains(ip);
    }

    /**
     * IP是否在IP段中
     *
     * @param ipRange
     * @param ip
     * @return
     */
    public static boolean isInIPv4Range(String ipRange, String ip) {
        if (StrUtil.isBlank(ipRange) || StrUtil.isBlank(ip) || !ipRange.contains(CommonConstant.RANGE_EN)) {
            return false;
        }

        String[] ipRangeArr = StrUtil.splitToArray(ipRange, CommonConstant.RANGE_EN);
        String ipStart = ipRangeArr[0];
        String ipEnd = ipRangeArr[1];
        if (!isIpv4(ipStart) || !isIpv4(ipEnd)) {
            return false;
        }

        List<String> ipRangeList = Ipv4Util.list(ipStart, ipEnd);
        if (CollectionUtil.isEmpty(ipRangeList)) {
            return false;
        }
        HashSet<String> ipRangeSet = new HashSet<>(ipRangeList);
        return ipRangeSet.contains(ip);
    }

    /**
     * IP是否在掩码中
     *
     * @param ipMask
     * @param ip
     * @return
     */
    public static boolean isInIpv4Mask(String ipMask, String ip) {
        if (StrUtil.isBlank(ipMask) || StrUtil.isBlank(ip) || !ipMask.contains(Ipv4Util.IP_MASK_SPLIT_MARK)) {
            return false;
        }

        long ipLong = Ipv4Util.ipv4ToLong(ip);

        final String[] ipParam = StrUtil.splitToArray(ipMask, Ipv4Util.IP_MASK_SPLIT_MARK);
        String ipStr = ipParam[0];

        if (!isIpv4(ipStr)) {
            return false;
        }

        int maskBit = Integer.valueOf(ipParam[1]);
        String beginIpStr = Ipv4Util.getBeginIpStr(ipStr, maskBit);
        String endIpStr = Ipv4Util.getEndIpStr(ipStr, maskBit);

        long beginIpLong = Ipv4Util.ipv4ToLong(beginIpStr);
        long endIpLong = Ipv4Util.ipv4ToLong(endIpStr);

        if (ipLong >= beginIpLong && ipLong <= endIpLong) {
            return true;
        }

        return false;
    }

    /**
     * IP是否在IPV4/IPV4掩码/IPV4段/IPV6中
     *
     * @param ipRange 支持IPV4/IPV4掩码/IPV4段/IPV6格式，多个用英文逗号
     * @param ip      要查询的IP
     * @return
     */
    public static boolean isInMultipleRangeOrMask(String ipRange, String ip) {
        if (StrUtil.isBlank(ipRange) || StrUtil.isBlank(ip)) {
            return false;
        }
        Set<String> ipRangeSet = StrUtil.strArrToSetTrim(ipRange);

        // 直接匹配
        if (ipRangeSet.contains(ip)) {
            return true;
        }

        // 掩码/IP段匹配
        String whiteIp = CollectionUtil.findFirst(ipRangeSet, e -> IpAddressUtil.isInIpv4Mask(e, ip) || IpAddressUtil.isInIPv4Range(e, ip));
        return StrUtil.isNotBlank(whiteIp);
    }

    /**
     * inputStream转File
     *
     * @param ins
     * @param file
     * @return
     */
    public static File inputStreamToFile(InputStream ins, File file) {
        OutputStream os = null;
        try {
            os = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            ins.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }
}
