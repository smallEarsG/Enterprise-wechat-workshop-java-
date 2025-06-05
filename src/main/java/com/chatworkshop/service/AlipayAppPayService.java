package com.chatworkshop.service;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AlipayAppPayService {

    @Autowired
    private AlipayClient alipayClient;

    public String createOrderStr(String outTradeNo, String subject, double amount) {
        AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
        request.setNotifyUrl("http://106.15.137.235:8080/api/user/payment/notify"); // 支付宝支付结果回调地址（必须公网）
//        request.putOtherTextParam("app_auth_token", "202504BBc8a2ea44587a496ca1de597feea38X54");//授权token，代调用必传
        String bizContent = "{" +
                "\"out_trade_no\":\"" + outTradeNo + "\"," +
                "\"total_amount\":\"" + String.format("%.2f", amount) + "\"," +
                "\"subject\":\"" + subject + "\"," +
                "\"product_code\":\"QUICK_MSECURITY_PAY\"" +
                "}";

        request.setBizContent(bizContent);

        try {
            AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request); // ✅ 关键：sdkExecute
            return response.getBody(); // 👉 返回 orderStr，前端直接传入 uni.requestPayment
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
