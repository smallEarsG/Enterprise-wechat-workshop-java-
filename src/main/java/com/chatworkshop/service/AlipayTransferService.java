package com.chatworkshop.service;

import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayFundTransToaccountTransferRequest;
import com.alipay.api.response.AlipayFundTransToaccountTransferResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;

@Service
public class AlipayTransferService {

    @Autowired
    private AlipayClient alipayClient;

    public boolean transfer(String alipayAccount, double amount, String realName) {
        try {
            DecimalFormat df = new DecimalFormat("0.00");
            String amountStr = df.format(amount);

            AlipayFundTransToaccountTransferRequest request = new AlipayFundTransToaccountTransferRequest();
            request.setBizContent("{" +
                    "\"out_biz_no\":\"" + System.currentTimeMillis() + "\"," +
                    "\"payee_type\":\"ALIPAY_LOGONID\"," +
                    "\"payee_account\":\"" + alipayAccount + "\"," +
                    "\"amount\":\"" + amountStr + "\"," +
                    "\"payee_real_name\":\"" + realName + "\"," +
                    "\"remark\":\"积分提现\"" +
                    "}");

            AlipayFundTransToaccountTransferResponse response = alipayClient.execute(request);
            return response.isSuccess();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
