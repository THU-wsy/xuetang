package com.thuwsy.xuetang.content.controller;

import com.thuwsy.xuetang.content.dto.BindTeachplanMediaDto;
import com.thuwsy.xuetang.content.dto.SaveTeachplanDto;
import com.thuwsy.xuetang.content.dto.TeachplanDto;
import com.thuwsy.xuetang.content.service.TeachplanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ClassName: TeachplanController
 * Package: com.thuwsy.xuetang.content.controller
 * Description: 教学计划controller
 *
 * @Author THU_wsy
 * @Create 2024/1/17 21:39
 * @Version 1.0
 */
@RestController
@RequestMapping("/content/teachplan")
public class TeachplanController {

    @Autowired
    private TeachplanService teachplanService;

    /**
     * 根据课程id查询教学计划（只有两级：大章节和小章节）
     */
    @GetMapping("/{courseId}/tree-nodes")
    public List<TeachplanDto> getTreeNodes(@PathVariable Long courseId) {
        return teachplanService.findTeachplanTree(courseId);
    }

    /**
     * 新增/修改教学计划
     */
    @PostMapping
    public void saveTeachplan(@RequestBody @Validated SaveTeachplanDto dto) {
        teachplanService.saveTeachplan(dto);
    }

    /**
     * 删除教学计划
     */
    @DeleteMapping("/{id}")
    public void deleteTeachplan(@PathVariable Long id) {
        teachplanService.deleteTeachplan(id);
    }

    /**
     * 下移课程章节
     */
    @PostMapping("/movedown/{id}")
    public void moveDownTeachplan(@PathVariable Long id) {
        teachplanService.moveDownTeachplan(id);
    }

    /**
     * 上移课程章节
     */
    @PostMapping("/moveup/{id}")
    public void moveUpTeachplan(@PathVariable Long id) {
        teachplanService.moveUpTeachplan(id);
    }


}
