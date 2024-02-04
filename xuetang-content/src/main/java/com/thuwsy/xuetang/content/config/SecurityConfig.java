package com.thuwsy.xuetang.content.config;

import com.thuwsy.xuetang.base.model.AuthErrorResponse;
import com.thuwsy.xuetang.content.utils.JwtAuthenticationFilter;
import com.thuwsy.xuetang.content.utils.JwtUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

/**
 * ClassName: SecurityConfig
 * Package: com.thuwsy.xuetang.auth.config
 * Description:
 *
 * @Author THU_wsy
 * @Create 2024/1/29 11:38
 * @Version 1.0
 */
@EnableWebSecurity
@EnableMethodSecurity
@Configuration
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 配置Security过滤器链
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // 1. 关闭CSRF防护
        http.csrf(conf -> conf.disable());

        // 2. 设置需要认证的url
        http.authorizeHttpRequests(conf -> conf
                .requestMatchers("/content/course/**").authenticated()
                .anyRequest().permitAll()
        );

        // 3. 认证或授权失败时的处理方式
        http.exceptionHandling(conf -> conf
                // 用户未登录时的处理方法
                .authenticationEntryPoint(this::onUnauthorized)
                // 用户权限不足访问时的处理方法
//                .accessDeniedHandler(this::onAccessDeny)
        );

        // 4. 使用基于token的校验，就需要将session改为无状态
        http.sessionManagement(conf -> conf
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        // 5. 将我们自定义的JwtAuthenticationFilter加入过滤器链
        // 注意，检验JWT的过滤器一定要在登录认证的过滤器之前
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // 用户未登录时的处理方法
    private void onUnauthorized(HttpServletRequest request,
                                HttpServletResponse response,
                                AuthenticationException exception)
            throws IOException, ServletException {

        response.setContentType("application/json;charset=utf-8");
        String json = new AuthErrorResponse("unauthorized", exception.getMessage()).asJsonString();
        response.getWriter().write(json);
    }

//    // 用户权限不足访问时的处理方法
//    private void onAccessDeny(HttpServletRequest request,
//                              HttpServletResponse response,
//                              AccessDeniedException exception)
//            throws IOException, ServletException {
//
//        response.setContentType("application/json;charset=utf-8");
//        String json = new AuthErrorResponse("权限不足", exception.getMessage()).asJsonString();
//        response.getWriter().write(json);
//    }
}
