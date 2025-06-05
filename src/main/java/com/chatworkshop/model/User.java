package com.chatworkshop.model;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("user")  // 映射数据库表名
public class User {
    private String  id;
    private String username;
    private String passwordHash;
    private String phone;
    private String inviteCode;
    private String invitedByCode;
    private Integer tryCount;
    private Boolean isMember;
    private Integer points;
    private String avatar;
    @TableField(fill = FieldFill.INSERT)
    private Date createdAt;
    private Date memberExpireAt; // ✅ 新增字段：会员到期时间
    private String currentToken; // ✅ 新增字段：创建用户时生成的token
}
