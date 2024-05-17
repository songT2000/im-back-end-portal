package com.im.common.util.jwt;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.im.common.util.RandomUtil;
import com.im.common.util.RequestUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.time.Duration;
import java.util.Date;
import java.util.Optional;

/**
 * JWT工具类,依赖java-jwt 3.3.0
 *
 * @author Barry
 * @date 2018/5/24
 */
public final class JwtUtil {
    /**
     * jwt头
     **/
    public static final String AUTH_HEADER = "Authorization";

    /**
     * 用户名
     **/
    private static final String USERNAME = "USERNAME";

    /**
     * JWT信息
     **/
    private static final String JWT_INFO = "JWT_INFO";

    /**
     * JWT信息
     **/
    private static final String RANDOM_UNIQUE_ID = "_RANDOM_UNIQUE_ID";


    /**
     * 从header中获取token
     *
     * @param request HttpServletRequest
     * @return String
     */
    public static String getTokenFromHeader(HttpServletRequest request) {
        // 从header中获取
        String authHeader = RequestUtil.getHeader(request, AUTH_HEADER);

        if (StrUtil.isBlank(authHeader)) {
            // // 从param中获取
            // if (fromParam) {
            //     // 从param中获取
            //     authHeader = RequestUtil.getStringParamTrim(request, AUTH_HEADER);
            // }
            //
            // // 从cookie中获取
            // if (fromCookie) {
            //     Cookie[] cookies = request.getCookies();
            //     if (cookies == null || cookies.length <= 0) {
            //         return null;
            //     }
            //     for (Cookie cookie : cookies) {
            //         if (cookie.getName().equals(AUTH_HEADER)) {
            //             authHeader = cookie.getValue();
            //             break;
            //         }
            //     }
            // }
            return authHeader;
        }

        return authHeader;
    }

    /**
     * 根据用户名校验jwt token是否正确及过期
     *
     * @param username  用户名
     * @param jwtToken  JWT Token
     * @param jwtSecret JWT密钥
     * @return true: 正确; false: 不正确;
     */
    public static boolean verify(String username, String jwtToken, String jwtSecret) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(jwtSecret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withClaim(USERNAME, username)
                    .build();
            verifier.verify(jwtToken);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    /**
     * 根据jwt token获取存储在里面的用户名
     *
     * @param jwtToken JWT Token
     * @return 用户名
     */
    public static String getUsername(String jwtToken) {
        try {
            DecodedJWT jwt = JWT.decode(jwtToken);

            return Optional.ofNullable(jwt)
                    .map(decodedJWT -> jwt.getClaim(USERNAME))
                    .map(claim -> claim.asString())
                    .orElse(null);
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    /**
     * 根据jwt token获取存储在里面的信息
     *
     * @param jwtToken JWT Token
     * @return 用户名
     */
    public static JwtInfo getJwtInfo(String jwtToken) {
        try {
            DecodedJWT jwt = JWT.decode(jwtToken);

            String infoStr = Optional.ofNullable(jwt)
                    .map(decodedJWT -> jwt.getClaim(JWT_INFO))
                    .map(claim -> claim.asString())
                    .orElse(null);

            if (StrUtil.isBlank(infoStr)) {
                return null;
            }

            return JSON.parseObject(infoStr, JwtInfo.class);
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    /**
     * 生成jwt token
     *
     * @param jwtInfo       jwt信息
     * @param jwtSecret     JWT密钥
     * @param expireSeconds 使用时限秒数
     * @return jwt token
     */
    public static String sign(JwtInfo jwtInfo, String jwtSecret, long expireSeconds) {
        try {
            long expireMills = Duration.ofSeconds(expireSeconds).toMillis();

            Date date = new Date(System.currentTimeMillis() + expireMills);

            Algorithm algorithm = Algorithm.HMAC256(jwtSecret);
            // 附带username信息，这里使用一个不会重复的随机值，确保不会因为各端的用户名重复而产生重复token
            return JWT.create()
                    .withClaim(USERNAME, jwtInfo.getUsername())
                    .withClaim(JWT_INFO, JSON.toJSONString(jwtInfo))
                    .withClaim(RANDOM_UNIQUE_ID, RandomUtil.randomToken())
                    .withExpiresAt(date)
                    .sign(algorithm);
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    public static void main(String[] args) {
        // 测试生成jwt token
        long expireSeconds = 3;
        String username = "aaa";
        String deviceId = "123456789012345678901234567890";
        String deviceType = "1";
        String jwtSecret = "d8$kd8m(8jdj19s&k1ap";
        String portalType = "1";

        JwtInfo info = new JwtInfo(username, deviceId, "iPhoneX", deviceType, portalType);

        String token = sign(info, jwtSecret, expireSeconds);

        System.out.println(token);

        System.out.println(JwtUtil.getUsername(token));
        System.out.println(JwtUtil.getJwtInfo(token).getDeviceId());
        System.out.println(JwtUtil.getJwtInfo(token).getDevice());
        System.out.println(JwtUtil.getJwtInfo(token).getDeviceType());
        System.out.println(JwtUtil.getJwtInfo(token).getUsername());
        System.out.println(JwtUtil.verify(username, token, jwtSecret));


        // System.out.println(JwtUtil.verify(username, "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJVU0VSTkFNRSI6ImFhYSIsImV4cCI6MTU3MTEzMDExNCwiSldUX0lORk8iOiJ7XCJkZXZpY2VJZFwiOlwiMTIzNDU2Nzg5MDEyMzQ1Njc4OTAxMjM0NTY3ODkwXCIsXCJkZXZpY2VUeXBlXCI6XCIxXCIsXCJ1c2VybmFtZVwiOlwiYWFhXCIsXCJ2ZXJzaW9uXCI6XCIxLjBcIn0ifQ.Og5aPVmHRbcnOEIkHrLMSDA6qfRyZrAR3e3Q6V78pNo", jwtSecret));
    }
}
