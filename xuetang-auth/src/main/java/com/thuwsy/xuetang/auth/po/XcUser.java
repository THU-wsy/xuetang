package com.thuwsy.xuetang.auth.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * 
 * @TableName xc_user
 */
@TableName(value ="xc_user")
@Data
public class XcUser implements Serializable, UserDetails {
    /**
     * 
     */
    @TableId
    private String id;

    /**
     * 
     */
    private String username;

    /**
     * 
     */
    private String password;

    /**
     * 
     */
    private String salt;

    /**
     * 微信unionid
     */
    private String wxUnionid;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 
     */
    private String name;

    /**
     * 头像
     */
    private String userpic;

    /**
     * 
     */
    private String companyId;

    /**
     * 
     */
    private String utype;

    /**
     * 
     */
    private LocalDateTime birthday;

    /**
     * 
     */
    private String sex;

    /**
     * 
     */
    private String email;

    /**
     * 
     */
    private String cellphone;

    /**
     * 
     */
    private String qq;

    /**
     * 用户状态
     */
    private String status;

    /**
     * 
     */
    private LocalDateTime createTime;

    /**
     * 
     */
    private LocalDateTime updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    // 用户权限
    @TableField(exist = false)
    List<String> permissions = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (permissions == null)
            return new ArrayList<>();
        // 通过stream将List<String>转为Collection<GrantedAuthority>
        return permissions.stream().map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}