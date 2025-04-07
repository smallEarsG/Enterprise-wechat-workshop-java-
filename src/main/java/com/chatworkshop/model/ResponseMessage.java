package com.chatworkshop.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseMessage {
    private int code;      // 状态码
    private String message;  // 服务器返回的消息
    private Object data;   // 返回的数据内容（可以是任意类型）
}
