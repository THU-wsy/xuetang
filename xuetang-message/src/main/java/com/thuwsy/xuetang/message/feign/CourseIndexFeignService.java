package com.thuwsy.xuetang.message.feign;

import com.thuwsy.xuetang.message.po.CourseIndex;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * ClassName: CourseIndexFeignService
 * Package: com.thuwsy.xuetang.message.feign
 * Description:
 *
 * @Author THU_wsy
 * @Create 2024/1/25 17:46
 * @Version 1.0
 */
@FeignClient("xuetang-search")
public interface CourseIndexFeignService {
    @PostMapping("/search/index/course")
    Boolean add(@RequestBody CourseIndex courseIndex);
}
