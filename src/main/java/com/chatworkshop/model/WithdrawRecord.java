package com.chatworkshop.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("withdraw_record")
public class WithdrawRecord {
    private Long id;
    private String userId;
    private String alipayAccount;
    private String realName;
    private Double amount;
    private String status;
    private Date createdAt;
}
