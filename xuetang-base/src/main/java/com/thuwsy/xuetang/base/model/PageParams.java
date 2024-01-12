package com.thuwsy.xuetang.base.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ClassName: PageParams
 * Package: com.thuwsy.xuetang.base.model
 * Description: 封装分页查询请求的通用参数
 *
 * @Author THU_wsy
 * @Create 2024/1/12 14:56
 * @Version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageParams {
    /** 当前页码 */
    private Long pageNo = 1L;

    /** 每页记录数 */
    private Long pageSize = 10L;
}
