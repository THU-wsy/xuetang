package com.thuwsy.xuetang.content.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * ClassName: EditCourseDto
 * Package: com.thuwsy.xuetang.content.dto
 * Description: 编辑课程DTO，比AddCourseDto多了一个id属性
 *
 * @Author THU_wsy
 * @Create 2024/1/17 19:26
 * @Version 1.0
 */
@Data
public class EditCourseDto extends AddCourseDto {

    @NotNull(message = "id不能为空")
    private Long id;
}
