package com.chatworkshop.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chatworkshop.model.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    // 使用 MyBatis-Plus 提供的 CRUD 方法，无需手写
}
