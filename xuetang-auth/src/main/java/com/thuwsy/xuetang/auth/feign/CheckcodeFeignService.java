package com.thuwsy.xuetang.auth.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * ClassName: CheckcodeFeignService
 * Package: com.thuwsy.xuetang.auth.feign
 * Description:
 *
 * @Author THU_wsy
 * @Create 2024/1/31 13:15
 * @Version 1.0
 */
@FeignClient("xuetang-checkcode")
public interface CheckcodeFeignService {
    @PostMapping(value = "/checkcode/verify")
    Boolean verify(@RequestParam("key") String key, @RequestParam("code") String code);
}
