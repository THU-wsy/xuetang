package com.thuwsy.xuetang.auth.utils;

import com.alibaba.fastjson2.JSONObject;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.thuwsy.xuetang.auth.po.XcUser;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * ClassName: JwtAuthenticationFilter
 * Package: com.thuwsy.xuetang.auth.utils
 * Description:
 *
 * @Author THU_wsy
 * @Create 2024/1/29 11:37
 * @Version 1.0
 */
// 继承OncePerRequestFilter表示每次请求过滤一次
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // 1. 首先从请求头中取出JWT并解析
        String headerToken = request.getHeader("Authorization");
        DecodedJWT decodedJWT = jwtUtil.resolveJwt(headerToken);

        if (decodedJWT != null) {
            // 2. 如果jwt合法，则获取用户的公开信息，并转换为UserDetails对象
            String userJson = jwtUtil.toUser(decodedJWT);
            XcUser user = JSONObject.parseObject(userJson, XcUser.class);

            // 3. 创建Authentication（与Spring Security源码的写法类似）
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            // 4. 将配置好的Authentication保存到SecurityContext，表示已完成验证
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // 5. 最后放行，执行下一个过滤器
        filterChain.doFilter(request, response);
    }
}
