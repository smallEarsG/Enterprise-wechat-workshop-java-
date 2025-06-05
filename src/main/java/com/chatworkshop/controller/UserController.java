package com.chatworkshop.controller;

import com.alipay.api.internal.util.AlipaySignature;
import com.chatworkshop.model.ResponseMessage;
import com.chatworkshop.model.User;
import com.chatworkshop.model.WithdrawRecord;
import com.chatworkshop.service.AlipayAppPayService;
import com.chatworkshop.service.AlipayTransferService;
import com.chatworkshop.service.UserService;
import com.chatworkshop.service.WithdrawRecordService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    // 注册接口（支持头像上传）
    @PostMapping("/register")
    public ResponseEntity<ResponseMessage> register(
            @RequestParam String username,
            @RequestParam String passwordHash,
            @RequestParam String phone,
            @RequestParam(required = false) String invitedByCode,
            @RequestParam(defaultValue = "3") Integer tryCount,
            @RequestParam(defaultValue = "false") Boolean isMember,
            @RequestParam(defaultValue = "0") Integer points,
            @RequestParam(required = false) MultipartFile avatar
           ) {
// 手机号唯一性校验
        User existingUser = userService.lambdaQuery().eq(User::getPhone, phone).one();
        if (existingUser != null) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(new ResponseMessage(409, "该手机号已被注册", null));
        }

        User user = new User();
        user.setUsername(username);
        user.setPasswordHash(passwordHash);
        user.setPhone(phone);
        String myInviteCode = UUID.randomUUID().toString().substring(0, 6);
        user.setInviteCode(myInviteCode);
        user.setInvitedByCode(invitedByCode);
        user.setTryCount(tryCount);
        user.setIsMember(isMember);
        user.setPoints(points);
        user.setMemberExpireAt(null);

        if (avatar != null && !avatar.isEmpty()) {
            String avatarUrl = saveAvatar(avatar);
            user.setAvatar(avatarUrl);
        }

        boolean saved = userService.save(user);
        return ResponseEntity.ok(new ResponseMessage(saved ? 200 : 500, saved ? "注册成功" : "注册失败", user));
    }

    // 登录接口
    @PostMapping("/login")
    public ResponseEntity<ResponseMessage> login(@RequestParam String phone, @RequestParam String passwordHash) {
        User user = userService.lambdaQuery()
                .eq(User::getPhone, phone)
                .eq(User::getPasswordHash, passwordHash)
                .one();
        if (user == null) {
            return ResponseEntity.status(401).body(new ResponseMessage(401, "用户名或密码错误", null));
        }
        // 处理当前登录设备 token，覆盖原有 token（实现挤下线）
        boolean tokenUpdated = userService.loginAndUpdateToken(user.getId());
        if (!tokenUpdated) {
            return ResponseEntity.status(500).body(new ResponseMessage(500, "登录失败，请重试", null));
        }
        // ✅ 判断会员是否已过期
        if (user.getIsMember() != null && user.getIsMember()
                && user.getMemberExpireAt() != null
                && user.getMemberExpireAt().before(new Date())) {

            user.setIsMember(false); // 自动取消会员
            userService.updateById(user); // 更新到数据库
        }
        return ResponseEntity.ok(new ResponseMessage(200, "登录成功", user));
    }

    @PostMapping("/logout/{userId}")
    public ResponseEntity<ResponseMessage> logout(@PathVariable String userId) {
        User user = userService.getById(userId);
        if (user == null) {
            return ResponseEntity.status(404).body(new ResponseMessage(404, "用户不存在", null));
        }

        user.setCurrentToken(null); // 清空 token
        userService.updateById(user); // 更新数据库

        return ResponseEntity.ok(new ResponseMessage(200, "成功登出", null));
    }


    // 查询用户信息
    @GetMapping("/{id}")
    public ResponseEntity<ResponseMessage> getUser(@PathVariable String id) {
        User user = userService.getById(id);
        if (user == null) {
            return ResponseEntity.status(404).body(new ResponseMessage(404, "用户不存在", null));
        }
        return ResponseEntity.ok(new ResponseMessage(200, "查询成功", user));
    }

    // 编辑用户信息（昵称、密码、头像）
    @PostMapping("/update/{id}")
    public ResponseEntity<ResponseMessage> updateUser(
            @PathVariable String id,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String passwordHash,
            @RequestParam(required = false) MultipartFile avatar) {
        User user = userService.getById(id);
        if (user == null) {
            return ResponseEntity.status(404).body(new ResponseMessage(404, "用户不存在", null));
        }

        if (username != null) user.setUsername(username);
        if (passwordHash != null) user.setPasswordHash(passwordHash);
        if (avatar != null && !avatar.isEmpty()) {
            String avatarUrl = saveAvatar(avatar);
            user.setAvatar(avatarUrl);
        }

        boolean updated = userService.updateById(user);
        return ResponseEntity.ok(new ResponseMessage(updated ? 200 : 500, updated ? "更新成功" : "更新失败", user));
    }

    // 上传头像工具
    private String saveAvatar(MultipartFile file) {
        try {
            String uploadDir = "uploads/avatars/";
            Files.createDirectories(Paths.get(uploadDir));
            String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path path = Paths.get(uploadDir + filename);
            Files.write(path, file.getBytes());
            return "/avatars/" + filename;
        } catch (IOException e) {
            throw new RuntimeException("头像上传失败", e);
        }
    }
    // 开通会员接口
    @PostMapping("/activate-member/{id}/{time}")
    public ResponseEntity<ResponseMessage> activateMember(@PathVariable String id,@PathVariable String time) {
        boolean result = userService.activateMembership(id,time);
        return ResponseEntity.ok(
                new ResponseMessage(result ? 200 : 500,
                        result ? "会员开通成功，邀请人已奖励积分" : "会员开通失败",
                        null)
        );
    }
    // 跟新测试次数接口
    @GetMapping("/use-feature/{id}")
    public ResponseEntity<ResponseMessage> useFeature(@PathVariable String id) {
        boolean allowed = userService.checkAndDeductTrial(id);
        if (!allowed) {
            return ResponseEntity.status(403).body(new ResponseMessage(403, "试用次数已用尽，请开通会员", null));
        }

        // 正常返回功能结果
        return ResponseEntity.ok(new ResponseMessage(200, "功能体验成功，试用次数已扣减", null));
    }
    @Autowired
    private AlipayTransferService alipayTransferService;
    @Autowired
    private WithdrawRecordService withdrawRecordService;
    // 提现接口
    @PostMapping("/withdraw")
    public ResponseEntity<ResponseMessage> withdraw(@RequestParam String userId,
                                                    @RequestParam Integer points,
                                                    @RequestParam String alipayAccount,
                                                    @RequestParam String realName) {
        User user = userService.getById(userId);
        if (user == null || user.getPoints() < points) {
            return ResponseEntity.badRequest().body(new ResponseMessage(400, "积分不足或用户不存在", null));
        }

        boolean success = alipayTransferService.transfer(alipayAccount, points, realName);
        if (success) {
            user.setPoints(user.getPoints() - points);
            userService.updateById(user);
            // 保存提现记录
            WithdrawRecord record = new WithdrawRecord();
            record.setUserId(userId);
            record.setAlipayAccount(alipayAccount);
            record.setRealName(realName);
            record.setAmount((double) points);
            record.setStatus("SUCCESS");
            withdrawRecordService.save(record);

            return ResponseEntity.ok(new ResponseMessage(200, "提现成功", null));
        } else {
            return ResponseEntity.status(500).body(new ResponseMessage(500, "支付宝转账失败", null));
        }
    }

    @GetMapping("/withdraw-records/{userId}")
    public ResponseEntity<ResponseMessage> getWithdrawRecords(@PathVariable String userId) {
        List<WithdrawRecord> records = withdrawRecordService.lambdaQuery()
                .eq(WithdrawRecord::getUserId, userId)
                .orderByDesc(WithdrawRecord::getCreatedAt)
                .list();

        return ResponseEntity.ok(new ResponseMessage(200, "查询成功", records));
    }
    @Autowired
    private AlipayAppPayService alipayAppPayService;
    @GetMapping("/pay/member")
    public ResponseEntity<ResponseMessage> getOrderStr(@RequestParam String userId,@RequestParam double priceOne) {
        String orderNo = "vip_" + System.currentTimeMillis(); // 订单号可自定义
        double price = priceOne; // 一年会员价格 月付9.9 包年108
        String subject = "开通会员";

        String orderStr = alipayAppPayService.createOrderStr(orderNo, subject, price);
        if (orderStr == null) {
            return ResponseEntity.status(500).body(new ResponseMessage(500, "生成支付订单失败", null));
        }

        return ResponseEntity.ok(new ResponseMessage(200, "订单生成成功", orderStr));
    }
    @RequestMapping("/payment/notify")
    public String paymentNotify(HttpServletRequest request) {
        // 获取支付宝返回的参数
        Map<String, String> params = new HashMap<>();
        for (Map.Entry<String, String[]> entry : request.getParameterMap().entrySet()) {
            String value = String.join(",", entry.getValue());
            params.put(entry.getKey(), value);
        }

        try {
            // 校验支付宝回调通知
            String alipayPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAowiWmaFNNXJge6wYblD41Tm8sijn63GtTng1O4k64On4Xvm0iD15Yqxjac6dgUtaWKKT0OCrmBFNZ1yQOoLnuzUAfcviCBd5yccPyzsINUZANJoJ6e7uP6b0yZJdBqOGRvzose4jN30uQtw9w7BrbLCNXp7aBExICoAfV8u4Zo3eMW3L7UyhX/GiQMHuev/RkSZjl9R83tAYqpI6JLnPjEYyZ1dcC9ZY4mJIdCM3a/Gzpfn+pDqIxAu5t6dxJuM7PU9iqyelq75EwZht0bMoELk1EsUDmhLZ7ed/B9KaeIyQMqV0nDVA5m7OCsmd4JihQvJ7ZiZJjdnE2u5GaLN46QIDAQAB";
            boolean verifyResult = AlipaySignature.rsaCheckV2(params, alipayPublicKey, "UTF-8", "RSA2");
            if (verifyResult) {
                // 验证成功，处理支付结果
                String tradeStatus = params.get("trade_status");
                if ("TRADE_SUCCESS".equals(tradeStatus)) {
                    // 支付成功，更新订单状态
                    String outTradeNo = params.get("out_trade_no");
                    // 这里处理支付成功后的逻辑，比如更新订单状态为已支付
                }
                return "success"; // 返回给支付宝
            } else {
                return "fail"; // 验证失败，返回 fail
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "fail";
        }
    }


}
