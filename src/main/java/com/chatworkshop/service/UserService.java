package com.chatworkshop.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chatworkshop.mapper.UserMapper;
import com.chatworkshop.model.User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;

@Service
public class UserService extends ServiceImpl<UserMapper, User> {
    /**
     * 开通会员：设置会员状态和到期时间，并触发邀请人积分奖励
     */
    public boolean activateMembership(Long userId) {
        User user = this.getById(userId);
        if (user == null) return false;

        user.setIsMember(true);

        // 设置到期时间为当前时间 + 1 年
        user.setMemberExpireAt(Date.from(
                LocalDateTime.now().plusYears(1)
                        .atZone(ZoneId.systemDefault()).toInstant()
        ));

        boolean updated = this.updateById(user);

        if (updated) {
            rewardInviterPoints(user); // 邀请人奖励逻辑
        }

        return updated;
    }
    public boolean rewardInviterPoints(User newMember) {
        String invitedByCode = newMember.getInvitedByCode();
        if (invitedByCode == null || invitedByCode.isEmpty()) return false;

        User inviter = this.lambdaQuery()
                .eq(User::getInviteCode, invitedByCode)
                .one();

        if (inviter == null) return false;

        inviter.setPoints(inviter.getPoints() + 10); // 邀请人奖励 10 积分
        return this.updateById(inviter);
    }
    // 试用期限内，用户可以免费试用
    public boolean checkAndDeductTrial(Long userId) {
        User user = this.getById(userId);
        if (user == null) return false;

        if (Boolean.TRUE.equals(user.getIsMember())) {
            return true; // 是会员，无限制
        }

        Integer tryCount = user.getTryCount();
        if (tryCount == null || tryCount <= 0) {
            return false; // 无试用次数
        }

        user.setTryCount(tryCount - 1);
        this.updateById(user); // 更新试用次数
        return true;
    }
    // 登录成功后，更新 token


    public boolean loginAndUpdateToken(Long userId) {
        User user = this.getById(userId);
        if (user == null) return false;

        // 生成新的 token（UUID 随机生成）
        String newToken = UUID.randomUUID().toString();

        // 更新当前用户的 token，将旧 token 覆盖
        user.setCurrentToken(newToken);
        return this.updateById(user); // 更新数据库
    }


}
