package com.thuwsy.xuetang.search.dto;

import com.thuwsy.xuetang.base.model.PageResult;
import lombok.Data;

import java.util.List;

/**
 * ClassName: SearchPageResultDto
 * Package: com.thuwsy.xuetang.search.dto
 * Description:
 *
 * @Author THU_wsy
 * @Create 2024/1/24 22:29
 * @Version 1.0
 */
@Data
public class SearchPageResultDto<T> extends PageResult<T> {
    // 大分类列表
    private List<String> mtList;

    // 小分类列表
    private List<String> stList;

    public SearchPageResultDto(List<T> items, long counts, long page, long pageSize) {
        super(items, counts, page, pageSize);
    }
}
