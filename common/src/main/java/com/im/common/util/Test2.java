package com.im.common.util;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.URLUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.digest.HMac;
import cn.hutool.crypto.digest.HmacAlgorithm;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.RpcAcsRequest;
import com.aliyuncs.profile.DefaultProfile;
import com.im.common.entity.PortalUser;
import com.im.common.util.api.im.tencent.entity.param.portrait.TiAccountPortraitParam;
import com.im.common.util.url.UrlParamUtil;
import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.STSourceType;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

/**
 * @author Barry
 * @date 2022-01-27
 */
public class Test2 {
    public static void main(String[] args) {
        PortalUser user = new PortalUser();
        // user.setNickname("test");
        // user.setAvatar("test");
        user.setAddFriendEnabled(true);
        TiAccountPortraitParam portrait = new TiAccountPortraitParam();
        portrait.setAddFriendEnabled(true);


        boolean same = StrUtil.equals(user.getNickname(), portrait.getNickname())
                && StrUtil.equals(user.getAvatar(), portrait.getAvatar())
                && Objects.equals(user.getAddFriendEnabled(), portrait.getAddFriendEnabled());
        System.out.println(same);

        // String prefix = "+86";
        // System.out.println(StrUtil.replace(prefix, "+", ""));

        // try {
        //     String accessKeyId = "LTAI5tPGY4U7PJDKkUtUt28N";
        //     String accessKeySecret = "eP9BJuPBpOYXWveuvvTErbKHXDTP4w";
        //
        //     Map<String, String> param = new TreeMap<>();
        //
        //     // 公共参数
        //     param.put("Format", "JSON");
        //     param.put("Version", "2018-05-01");
        //     param.put("AccessKeyId", accessKeyId);
        //
        //     // 使用UTC时间，比如北京时间2013年1月10日20点0分0秒，表示为2013-01-10T12:00:00Z，注意20变成12
        //     // 先获取北京时间
        //     ZonedDateTime beijingNow = ZonedDateTime.now(ZoneId.of("Asia/Shanghai"));
        //     // 再获取UTC时间
        //     ZonedDateTime utfNow = beijingNow.withZoneSameInstant(ZoneOffset.UTC);
        //     String timestamp = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'").format(utfNow);
        //     param.put("Timestamp", URLEncoder.encode(timestamp, "UTF-8"));
        //     // param.put("Timestamp", URLEncoder.encode("2022-01-27T20:56:00Z", "UTF-8"));
        //     // param.put("Timestamp", "2022-01-27T21%3A58%3A10Z");
        //     param.put("SignatureNonce", RandomUtil.randomNumbers(15));
        //     param.put("SignatureMethod", "HMAC-SHA1");
        //     param.put("SignatureVersion", "1.0");
        //
        //     // 请求参数
        //     param.put("Action", "SendMessageToGlobe");
        //     param.put("To", "639451670168");
        //     // param.put("From", "IMTECH");
        //     param.put("Message", URLEncoder.encode("【IMTECH】您好，验证码123456，一分钟内有效。", "UTF-8"));
        //     // param.put("Message", URLEncoder.encode("hello", "UTF-8"));
        //
        //     String signStr = UrlParamUtil.join(param);
        //     signStr = "GET&"+ URLEncoder.encode("/", "UTF-8") +"&" + URLEncoder.encode(signStr, "UTF-8");
        //
        //     System.out.println(signStr);
        //
        //     HMac hMac = SecureUtil.hmacSha1(accessKeySecret + "&");
        //     String sign = hMac.digestBase64(signStr, false);
        //     // System.out.println(hMac.digestBase64(curl, true));
        //
        //     param.put("Signature", URLEncoder.encode(sign, "UTF-8") + "11");
        //     // param.put("Signature", sign);
        //
        //     String rsp = HttpClientUtil.get("https://dysmsapi.ap-southeast-1.aliyuncs.com", param, 30);
        //     System.out.println(rsp);
        // } catch (Exception e) {
        //     e.printStackTrace();
        // }


        // // 加密算法
        // String key = "testsecret&";
        // byte[] keyByte = key.getBytes(StandardCharsets.UTF_8);
        // String data = "GET&%2F&AccessKeyId%3Dtestid%26Action%3DGetDeviceInfos%26AppKey%3D23267207%26Devices%3De2ba19de97604f55b165576736477b74%252C92a1da34bdfd4c9692714917ce22d53d%26Format%3DXML%26RegionId%3Dcn-hangzhou%26SignatureMethod%3DHMAC-SHA1%26SignatureNonce%3Dc4f5f0de-b3ff-4528-8a89-fa478bda8d80%26SignatureVersion%3D1.0%26Timestamp%3D2016-03-29T03%253A59%253A24Z%26Version%3D2015-08-27";
        // byte[] dataByte = data.getBytes(StandardCharsets.UTF_8);
        //
        // // apache
        // {
        //     Mac mac = HmacUtils.getInitializedMac(HmacAlgorithms.HMAC_SHA_1, keyByte);
        //     byte[] bytes = mac.doFinal(dataByte);
        //     System.out.println(Base64.encode(bytes));
        // }
        //
        // // hutool
        // {
        //     HMac hMac = SecureUtil.hmacSha1(key);
        //     System.out.println(hMac.digestBase64(data, false));
        // }
        //
        // // 原生
        // {
        //     try {
        //         SecretKey secretKey = new SecretKeySpec(keyByte, "HmacSHA1");
        //         // 生成一个指定 Mac 算法 的 Mac 对象
        //         Mac mac = Mac.getInstance("HmacSHA1");
        //         // 用给定密钥初始化 Mac 对象
        //         mac.init(secretKey);
        //
        //         byte[] bytes = mac.doFinal(dataByte);
        //         System.out.println(Base64.encode(bytes));
        //     } catch (NoSuchAlgorithmException e) {
        //         e.printStackTrace();
        //     } catch (InvalidKeyException e) {
        //         e.printStackTrace();
        //     }
        // }

        // String accessKeyId = "LTAI5tPGY4U7PJDKkUtUt28N";
        // String accessKeySecret = "eP9BJuPBpOYXWveuvvTErbKHXDTP4w";
        //
        // Map<String, String> param = new HashMap<>();
        // param.put("Format", "JSON");
        // param.put("Version", "2018-05-01");
        // param.put("AccessKeyId", accessKeyId);
        // param.put("Timestamp", DateTimeUtil.toStr(LocalDateTime.now(), "yyyy-MM-ddTHH:mm:ssZ"));
        // param.put("SignatureNonce", RandomUtil.randomNumbers(15));
        //
        // param.put("SignatureMethod", "HMAC-SHA1");
        // param.put("SignatureVersion", "1.0");
        // param.put("Action", "SendMessageToGlobe");
        // param.put("To", "SendMessageToGlobe");
        // param.put("From", "IMTECH");
        // param.put("Message", "您好，验证码123456，一分钟内有效。");
        //
        // String signStr = StrUtil.format("{}{}{}", "2026749374", "2026749374", time);
        // String sign = Md5Util.getMd5Code(signStr);
        // param.put("Signature", );
        //
        //
        //
        // param.put("sign", sign);
        //
        // String url = "http://zzsms365.com/v2sms.aspx";
        //
        // try {
        //     String rsp = HttpClientUtil.postWithForm(url, param, 30);
        //     // String rsp = HttpClientUtil.postWithForm("http://zzsms365.com/v2smsGBK.aspx", param, 30);
        //
        //     // String curl = UrlParamUtil.join(param);
        //     // String rsp = HttpClientUtil.postWithAny(url, curl, null,MediaType.APPLICATION_FORM_URLENCODED_VALUE + ";charset=UTF-8", 30);
        //     System.out.println(rsp);
        // } catch (IOException e) {
        //     e.printStackTrace();
        // }
    }
}
