package com.thuwsy.xuetang.auth.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

/**
 * ClassName: DaoAuthenticationProviderCustom
 * Package: com.thuwsy.xuetang.auth.utils
 * Description:
 *
 * @Author THU_wsy
 * @Create 2024/1/29 12:41
 * @Version 1.0
 */
@Component
public class DaoAuthenticationProviderCustom extends DaoAuthenticationProvider {
    // 由于DaoAuthenticationProvider调用UserDetailsService，所以需要注入
    @Autowired
    public void setUserDetailsService(UserDetailsService userDetailsService) {
        super.setUserDetailsService(userDetailsService);
    }

    /**
     * 自定义密码校验的逻辑
     */
    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails,
                                                  UsernamePasswordAuthenticationToken authentication)
            throws AuthenticationException {

        super.additionalAuthenticationChecks(userDetails, authentication);
    }
}
