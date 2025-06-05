package com.chatworkshop.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("app_version")
public class AppVersion {
    private Long id;
    private String platform;
    private String latestVersion;
    private String packageUrl;
    private String updateLog;
    private Boolean forceUpdate;
}
