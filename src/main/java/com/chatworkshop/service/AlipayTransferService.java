package com.chatworkshop.service;

import com.alibaba.fastjson2.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayFundTransToaccountTransferRequest;
import com.alipay.api.response.AlipayFundTransToaccountTransferResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class AlipayTransferService {

    @Autowired
    private AlipayClient alipayClient;

    public boolean transfer(String alipayAccount, double amount, String realName) {
        try {
            String amountStr = new DecimalFormat("0.00").format(amount);

            Map<String, Object> bizContent = new HashMap<>();
            bizContent.put("out_biz_no", "tx_" + System.currentTimeMillis());
            bizContent.put("payee_type", "ALIPAY_LOGONID");
            bizContent.put("payee_account", alipayAccount);
            bizContent.put("amount", amountStr);
            bizContent.put("payee_real_name", realName);
            bizContent.put("remark", "积分提现");

            AlipayFundTransToaccountTransferRequest request = new AlipayFundTransToaccountTransferRequest();
            request.setBizContent(JSON.toJSONString(bizContent));

            AlipayFundTransToaccountTransferResponse response = alipayClient.execute(request);
            if (response.isSuccess()) {
                log.info("支付宝转账成功，交易号：{}", response.getOrderId());
                return true;
            } else {
                log.error("支付宝转账失败：{} - {}", response.getSubCode(), response.getSubMsg());
                return false;
            }
        } catch (AlipayApiException e) {
            log.error("支付宝转账接口调用异常", e);
            return false;
        }
    }
}
