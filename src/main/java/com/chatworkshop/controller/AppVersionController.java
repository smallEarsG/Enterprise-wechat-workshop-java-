package com.chatworkshop.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chatworkshop.entity.AppVersion;
import com.chatworkshop.mapper.AppVersionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/app")
public class AppVersionController {

    @Autowired
    private AppVersionMapper appVersionMapper;

    @GetMapping("/version")
    public ResponseEntity<Map<String, Object>> getLatestAndroidVersion() {
        AppVersion android = appVersionMapper.selectOne(
                new QueryWrapper<AppVersion>()
                        .eq("platform", "android")
                        .orderByDesc("id")
                        .last("limit 1")
        );

        if (android == null) {
            return ResponseEntity.status(404).body(Map.of("error", "未找到 Android 版本信息"));
        }

        Map<String, Object> result = new HashMap<>();
        result.put("latestVersion", android.getLatestVersion());
        result.put("apkUrl", android.getPackageUrl());
        result.put("updateLog", android.getUpdateLog());
        result.put("forceUpdate", android.getForceUpdate());

        return ResponseEntity.ok(result);
    }
}
