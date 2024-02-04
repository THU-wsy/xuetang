package com.thuwsy.xuetang.base.model;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * ClassName: AuthErrorResponse
 * Package: com.thuwsy.xuetang.base.model
 * Description:
 *
 * @Author THU_wsy
 * @Create 2024/1/30 23:15
 * @Version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthErrorResponse implements Serializable {
    private String error;
    private String error_description;
    public String asJsonString() {
        // 将该对象转化为json字符串（注意null值也要写入json字符串）
        return JSONObject.toJSONString(this, JSONWriter.Feature.WriteNulls);
    }
}
