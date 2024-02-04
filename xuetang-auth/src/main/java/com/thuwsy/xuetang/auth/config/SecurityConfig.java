package com.thuwsy.xuetang.auth.config;

import com.thuwsy.xuetang.auth.service.impl.UserDetailsServiceImpl;
import com.thuwsy.xuetang.auth.utils.JwtAuthenticationFilter;
import com.thuwsy.xuetang.auth.utils.JwtUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.io.PrintWriter;

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
    private UserDetailsServiceImpl userDetailsServiceImpl;
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    @Autowired
    private JwtUtil jwtUtil;

    @Bean // 密码加密器（对密码进行加密，提高安全性）
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 注册AuthenticationManager，将它与UserDetailsService、PasswordEncoder关联
     */
    @Bean
    public AuthenticationManager authenticationManager(PasswordEncoder passwordEncoder) {
        // 它是AuthenticationProvider的一个常用实现类
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        // 关联使用的UserDetailsService
        provider.setUserDetailsService(userDetailsServiceImpl);
        // 关联使用的密码加密器
        provider.setPasswordEncoder(passwordEncoder);
        // 将AuthenticationProvider放入AuthenticationManager中
        return new ProviderManager(provider);
    }

    /**
     * 配置Security过滤器链
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // 1. 关闭CSRF防护
        http.csrf(conf -> conf.disable());

        // 2. 设置需要认证的url
        http.authorizeHttpRequests(conf -> conf
                .requestMatchers("/auth/**").permitAll() // 登录相关的url无需认证
                .anyRequest().authenticated() // 其余请求都需要认证(登录)
        );

        // 3. 退出登录相关配置
        http.logout(conf -> conf
                .logoutUrl("/auth/logout") // 退出登录提交post请求的url
                .logoutSuccessHandler(this::onLogoutSuccess) // 退出登录成功后的处理方法
        );

        // 4. 认证或授权失败时的处理方式
        http.exceptionHandling(conf -> conf
                // 用户未登录时的处理方法
                .authenticationEntryPoint(this::onUnauthorized)
                // 用户权限不足访问时的处理方法
                .accessDeniedHandler(this::onAccessDeny)
        );

        // 5. 使用基于token的校验，就需要将session改为无状态
        http.sessionManagement(conf -> conf
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        // 6. 将我们自定义的JwtAuthenticationFilter加入过滤器链
        // 注意，检验JWT的过滤器一定要在登录认证的过滤器之前
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // 退出登录成功后的处理方法
    private void onLogoutSuccess(HttpServletRequest request,
                                 HttpServletResponse response,
                                 Authentication authentication)
            throws IOException, ServletException {

        response.setContentType("application/json;charset=utf-8");
        PrintWriter writer = response.getWriter();
        // 1. 获取请求头中的token内容
        String headerToken = request.getHeader("Authorization");
        // 2. 令该token失效，也就是将该token添加到Redis的黑名单中
        if (jwtUtil.invalidateJwt(headerToken)) {
            writer.write("退出登录成功");
        } else {
            writer.write("退出登录失败");
        }
    }

    // 用户未登录时的处理方法
    private void onUnauthorized(HttpServletRequest request,
                                HttpServletResponse response,
                                AuthenticationException exception)
            throws IOException, ServletException {

        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(exception.getMessage());
    }

    // 用户权限不足访问时的处理方法
    private void onAccessDeny(HttpServletRequest request,
                              HttpServletResponse response,
                              AccessDeniedException exception)
            throws IOException, ServletException {

        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(exception.getMessage());
    }
}
