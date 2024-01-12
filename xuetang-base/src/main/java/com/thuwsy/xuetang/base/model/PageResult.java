package com.thuwsy.xuetang.base.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * ClassName: PageResult
 * Package: com.thuwsy.xuetang.base.model
 * Description: 封装分页查询结果
 *
 * @Author THU_wsy
 * @Create 2024/1/12 15:06
 * @Version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResult<T> implements Serializable {
    /** 数据列表 */
    private List<T> items;

    /** 总记录数 */
    private long counts;

    /** 当前页码 */
    private long page;

    /** 每页记录数 */
    private long pageSize;
}
