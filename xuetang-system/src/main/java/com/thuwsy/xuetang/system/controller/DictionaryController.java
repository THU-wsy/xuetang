package com.thuwsy.xuetang.system.controller;

import com.thuwsy.xuetang.system.po.Dictionary;
import com.thuwsy.xuetang.system.service.DictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * ClassName: DictionaryController
 * Package: com.thuwsy.xuetang.system.controller
 * Description: 数据字典controller
 *
 * @Author THU_wsy
 * @Create 2024/1/16 20:53
 * @Version 1.0
 */
@RequestMapping("/system")
@RestController
public class DictionaryController {
    @Autowired
    private DictionaryService dictionaryService;

    @GetMapping("/dictionary/all")
    public List<Dictionary> queryAll() {
        return dictionaryService.queryAll();
    }

    @GetMapping("/dictionary/code/{code}")
    public Dictionary getByCode(@PathVariable("code") String code) {
        return dictionaryService.getByCode(code);
    }
}
