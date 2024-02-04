package com.thuwsy.xuetang.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.thuwsy.xuetang.auth.mapper.XcMenuMapper;
import com.thuwsy.xuetang.auth.mapper.XcUserMapper;
import com.thuwsy.xuetang.auth.po.XcMenu;
import com.thuwsy.xuetang.auth.po.XcUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * ClassName: UserDetailsServiceImpl
 * Package: com.thuwsy.xuetang.auth.service.impl
 * Description:
 *
 * @Author THU_wsy
 * @Create 2024/1/30 22:17
 * @Version 1.0
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private XcUserMapper xcUserMapper;
    @Autowired
    private XcMenuMapper xcMenuMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1. 根据用户名从数据库查找用户
        LambdaQueryWrapper<XcUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(XcUser::getUsername, username);
        XcUser xcUser = xcUserMapper.selectOne(wrapper);
        if (xcUser == null) {
            throw new UsernameNotFoundException("用户名或密码错误");
        }

        // 2. 从数据库查询用户的权限信息，采用RBAC模型需要多表联查
        String userId = xcUser.getId();
        List<XcMenu> xcMenus = xcMenuMapper.selectPermissionByUserId(userId);
        // 转化为List<String>
        List<String> permissions = new ArrayList<>();
        if (xcMenus != null) {
            xcMenus.forEach(menu -> permissions.add(menu.getCode()));
        }
        // 设置用户权限................
        xcUser.setPermissions(permissions);

        // 3. 返回UserDetails对象
        return xcUser;
    }
}
