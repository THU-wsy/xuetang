package com.thuwsy.xuetang.auth.utils;

import com.alibaba.fastjson2.JSONObject;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.thuwsy.xuetang.auth.po.XcUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * ClassName: JwtUtil
 * Package: com.thuwsy.xuetang.auth.utils
 * Description:
 *
 * @Author THU_wsy
 * @Create 2024/1/29 11:29
 * @Version 1.0
 */
@Component
public class JwtUtil {
    // 用于给Jwt令牌签名校验的秘钥
    @Value("${spring.security.jwt.key}")
    private String key;

    // 令牌的过期时间，以小时为单位
    @Value("${spring.security.jwt.expire}")
    private int expire;

    @Autowired
    private StringRedisTemplate template;

    /**
     * 创建JWT令牌
     * @param userJson 用户的公开信息所对应的json串
     * @return JWT令牌
     */
    public String createJwt(String userJson) {
        // 1. 加密算法
        Algorithm algorithm = Algorithm.HMAC256(key);
        // 2. 创建JWT令牌
        return JWT.create()
                .withJWTId(UUID.randomUUID().toString()) // 保存JWT的id
                .withClaim("userInfo", userJson) // 保存用户的公开信息
                .withIssuedAt(new Date()) // JWT颁发的时间
                .withExpiresAt(expireTime()) // JWT过期的时间
                .sign(algorithm); // 使用加密算法完成签名
    }

    // 计算JWT过期的时间
    private Date expireTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, expire);
        return calendar.getTime();
    }

    /**
     * 解析请求头中的JWT令牌
     */
    public DecodedJWT resolveJwt(String headerToken) {
        // 1. 将请求头中的内容，去掉"Bearer "前缀，转换为标准的JWT令牌
        String token = convertToken(headerToken);
        if (token == null) return null;

        // 2. 根据私钥和加密算法，得到JWT校验器
        Algorithm algorithm = Algorithm.HMAC256(key);
        JWTVerifier jwtVerifier = JWT.require(algorithm).build();

        // 3. 进行token校验
        try {
            // 首先，验证token的签名正确(如果token被人篡改，则会校验失败，抛出JWTVerificationException)
            DecodedJWT decodedJWT = jwtVerifier.verify(token);

            // 其次，验证token不在Redis的黑名单中
            if (isInvalidToken(decodedJWT.getId()))
                return null;

            // 最后，验证token没有过期
            Date expiresAt = decodedJWT.getExpiresAt();
            return new Date().after(expiresAt) ? null : decodedJWT;

        } catch (JWTVerificationException e) {
            return null;
        }
    }

    // 将请求头中的内容，去掉"Bearer "前缀，转换为标准的JWT令牌
    private String convertToken(String headerToken) {
        if (headerToken == null || !headerToken.startsWith("Bearer "))
            return null;
        return headerToken.substring(7);
    }

    // 判断该jwtId是否在Redis的黑名单中
    private boolean isInvalidToken(String jwtId) {
        return Boolean.TRUE.equals(template.hasKey("jwt:blacklist:" + jwtId));
    }

    /**
     * 从解析完的JWT中提取用户的公开信息并返回
     * @param decodedJWT 解析完的JWT
     * @return 用户的公开信息所对应的json串
     */
    public String toUser(DecodedJWT decodedJWT) {
        return decodedJWT.getClaims().get("userInfo").asString();
    }

    /**
     * 当用户退出登录时，删除该Token，也就是让该Token失效。返回值代表是否成功删除。
     */
    public boolean invalidateJwt(String headerToken) {
        // 1. 将请求头中的内容，去掉"Bearer "前缀，转换为标准的JWT令牌
        String token = convertToken(headerToken);
        if (token == null) return false;

        // 2. 根据私钥和加密算法，得到JWT校验器
        Algorithm algorithm = Algorithm.HMAC256(key);
        JWTVerifier jwtVerifier = JWT.require(algorithm).build();

        // 3. 删除token前，首先要确保该token是合法的
        try {
            // 首先，验证token的签名正确
            DecodedJWT decodedJWT = jwtVerifier.verify(token);

            // 然后将该token添加到Redis的黑名单中，就表示删除了该token
            return deleteToken(decodedJWT.getId(), decodedJWT.getExpiresAt());

        } catch (JWTVerificationException e) {
            return false;
        }
    }

    /**
     * 将该token添加到Redis的黑名单中
     * @param jwtId jwt的ID
     * @param expiresAt jwt过期的时间
     * @return 添加到黑名单中是否成功
     */
    private boolean deleteToken(String jwtId, Date expiresAt) {
        // 1. 如果该token已经在Redis黑名单中，则无需重复添加
        if (isInvalidToken(jwtId)) {
            return false;
        }

        // 2. 设置Redis中这个key的过期时间，等于jwt的过期日期减掉现在的日期
        Date now = new Date();
        long expire = Math.max(expiresAt.getTime() - now.getTime(), 0);

        // 3. 向Redis的黑名单中添加该key
        template.opsForValue()
                .set("jwt:blacklist:" + jwtId, "", expire, TimeUnit.MILLISECONDS);
        return true;
    }
}
