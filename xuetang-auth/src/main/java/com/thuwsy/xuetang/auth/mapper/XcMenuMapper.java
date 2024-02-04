package com.thuwsy.xuetang.auth.mapper;

import com.thuwsy.xuetang.auth.po.XcMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author 吴晟宇
* @description 针对表【xc_menu】的数据库操作Mapper
* @createDate 2024-01-29 10:56:04
* @Entity com.thuwsy.xuetang.auth.po.XcMenu
*/
public interface XcMenuMapper extends BaseMapper<XcMenu> {
    List<XcMenu> selectPermissionByUserId(@Param("userId") String userId);
}




