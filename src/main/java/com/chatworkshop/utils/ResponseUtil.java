package com.chatworkshop.utils;

import com.chatworkshop.model.ResponseMessage;
import org.springframework.http.ResponseEntity;

public class ResponseUtil {

    // 成功响应
    public static ResponseEntity<ResponseMessage> success(Object data, String message) {
        return ResponseEntity.ok(new ResponseMessage(200, message, data));
    }

    // 错误响应
    public static ResponseEntity<ResponseMessage> error(int code, String message) {
        return ResponseEntity.status(code).body(new ResponseMessage(code, message, null));
    }
}
