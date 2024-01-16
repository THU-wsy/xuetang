package com.thuwsy.xuetang.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.thuwsy.xuetang.system.mapper.DictionaryMapper;
import com.thuwsy.xuetang.system.po.Dictionary;
import com.thuwsy.xuetang.system.service.DictionaryService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * ClassName: DictionaryServiceImpl
 * Package: com.thuwsy.xuetang.system.service.impl
 * Description: DictionaryServiceImpl
 *
 * @Author THU_wsy
 * @Create 2024/1/16 21:04
 * @Version 1.0
 */
@Service
public class DictionaryServiceImpl extends ServiceImpl<DictionaryMapper, Dictionary> implements DictionaryService {
    @Override
    public List<Dictionary> queryAll() {
        return this.list();
    }

    @Override
    public Dictionary getByCode(String code) {
        LambdaQueryWrapper<Dictionary> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Dictionary::getCode, code);
        return this.getOne(wrapper);
    }
}
