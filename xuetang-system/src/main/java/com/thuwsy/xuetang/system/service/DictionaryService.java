package com.thuwsy.xuetang.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.thuwsy.xuetang.system.po.Dictionary;

import java.util.List;

/**
 * ClassName: DictionaryService
 * Package: com.thuwsy.xuetang.system.service
 * Description: DictionaryService
 *
 * @Author THU_wsy
 * @Create 2024/1/16 21:03
 * @Version 1.0
 */
public interface DictionaryService extends IService<Dictionary> {
    /**
     * 查询所有数据字典内容
     * @return
     */
    List<Dictionary> queryAll();

    /**
     * 根据code查询数据字典
     * @param code 数据字典code
     * @return
     */
    Dictionary getByCode(String code);
}
