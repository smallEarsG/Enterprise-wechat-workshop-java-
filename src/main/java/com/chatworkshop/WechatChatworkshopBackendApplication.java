package com.chatworkshop;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.chatworkshop.mapper")  // 扫描 Mapper 接口
public class  WechatChatworkshopBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(WechatChatworkshopBackendApplication.class, args);
	}

}
