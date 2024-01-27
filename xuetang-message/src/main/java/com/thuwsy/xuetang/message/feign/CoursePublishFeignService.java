package com.thuwsy.xuetang.message.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * ClassName: CoursePublishFeignService
 * Package: com.thuwsy.xuetang.message.feign
 * Description:
 *
 * @Author THU_wsy
 * @Create 2024/1/24 20:08
 * @Version 1.0
 */
@FeignClient("xuetang-content")
public interface CoursePublishFeignService {
    @ResponseBody
    @PostMapping("/content/coursepublish/minio/{courseId}")
    void coursePublishToMinio(@PathVariable("courseId") Long courseId);
}
