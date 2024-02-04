package com.thuwsy.xuetang.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.thuwsy.xuetang.auth.dto.FindPasswordDto;
import com.thuwsy.xuetang.auth.dto.RegisterDto;
import com.thuwsy.xuetang.auth.mapper.XcUserMapper;
import com.thuwsy.xuetang.auth.mapper.XcUserRoleMapper;
import com.thuwsy.xuetang.auth.po.XcUser;
import com.thuwsy.xuetang.auth.po.XcUserRole;
import com.thuwsy.xuetang.auth.service.VerifyService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * ClassName: VerifyServiceImpl
 * Package: com.thuwsy.xuetang.auth.service.impl
 * Description:
 *
 * @Author THU_wsy
 * @Create 2024/2/4 12:53
 * @Version 1.0
 */
@Service
public class VerifyServiceImpl implements VerifyService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private XcUserMapper xcUserMapper;

    @Autowired
    private XcUserRoleMapper xcUserRoleMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void findPassword(FindPasswordDto dto) {
        // 1. 先判断验证码是否正确
        String email = dto.getEmail();
        String checkcode = dto.getCheckcode();
        Boolean verify = verify(email, checkcode);
        if (!verify) {
            throw new RuntimeException("验证码输入错误");
        }

        // 2. 判断两次密码是否一致
        String password = dto.getPassword();
        String confirmpwd = dto.getConfirmpwd();
        if (!password.equals(confirmpwd)) {
            throw new RuntimeException("两次输入的密码不一致");
        }

        // 3. 将新密码保存到数据库
        LambdaQueryWrapper<XcUser> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(XcUser::getEmail, dto.getEmail());
        XcUser user = xcUserMapper.selectOne(lambdaQueryWrapper);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        user.setPassword(passwordEncoder.encode(password));
        xcUserMapper.updateById(user);
    }

    /**
     * 判断验证码是否正确
     */
    private Boolean verify(String email, String code) {
        String correctCode = redisTemplate.opsForValue().get(email);
        if (correctCode == null || code == null)
            return false;
        return correctCode.equals(code);
    }

    /**
     * 注册
     */
    @Override
    @Transactional
    public void register(RegisterDto registerDto) {
        // 1. 判断验证码是否正确
        String uuid = UUID.randomUUID().toString();
        String email = registerDto.getEmail();
        String checkcode = registerDto.getCheckcode();
        Boolean verify = verify(email, checkcode);
        if (!verify) {
            throw new RuntimeException("验证码输入错误");
        }

        // 2. 判断两次密码是否正确
        String password = registerDto.getPassword();
        String confirmpwd = registerDto.getConfirmpwd();
        if (!password.equals(confirmpwd)) {
            throw new RuntimeException("两次输入的密码不一致");
        }

        // 3. 判断该用户是否存在
        LambdaQueryWrapper<XcUser> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(XcUser::getEmail, registerDto.getEmail());
        XcUser user = xcUserMapper.selectOne(lambdaQueryWrapper);
        if (user != null) {
            throw new RuntimeException("用户已存在，一个邮箱只能注册一个账号");
        }

        // 4. 新增用户
        XcUser xcUser = new XcUser();
        BeanUtils.copyProperties(registerDto, xcUser);
        xcUser.setPassword(passwordEncoder.encode(password));
        xcUser.setId(uuid);
        xcUser.setUtype("101001");  // 学生类型
        xcUser.setStatus("1");
        xcUser.setName(registerDto.getNickname());
        xcUser.setCreateTime(LocalDateTime.now());
        int insert = xcUserMapper.insert(xcUser);
        if (insert <= 0) {
            throw new RuntimeException("新增用户信息失败");
        }
        XcUserRole xcUserRole = new XcUserRole();
        xcUserRole.setId(uuid);
        xcUserRole.setUserId(uuid);
        xcUserRole.setRoleId("17");
        xcUserRole.setCreateTime(LocalDateTime.now());
        int insert1 = xcUserRoleMapper.insert(xcUserRole);
        if (insert1 <= 0) {
            throw new RuntimeException("新增用户角色信息失败");
        }
    }
}
