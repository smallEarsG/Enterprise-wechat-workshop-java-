package com.chatworkshop.config;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AlipayConfig {

    @Bean
    public AlipayClient alipayClient() {

        return new DefaultAlipayClient(
                "https://openapi.alipay.com/gateway.do",
                "2021005137695616", // 👈 替换 v2
                "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCfB4nXF/WoHrtnBggexOYAakW1CJo2G2TXTT4wRnePvc29F+OOhcrn9/3l1HwlNVG4BHoo3eVXZxtrUGzgwgfziE8FQ4WPNHesekLeoJNX/Hsm/Fp/MMMReX+u6m5oP1AsqO8IkAZ7STlrcra1BbXL3gsqq/WexkzERrZKFwPu2e55nV3TG9wN95VOC2HDosyHJcqi8Bhx7+T4z3coahPB3ximQaAnMrt4HmJzYnXqAI1ZNCXKpk8G+IKS87xhgBaK0bDp2i90L4efmOPtEJBXWMcd2y4c2+aQuYgX9Ut0iWLELwlVSKoa97ol1KLLFpN1ZYyKfYBf+OveIS6TQo7vAgMBAAECggEACTiOrYIeJFlj1F1tKxuPmoaO9ChDiM+d0T4RVQtA83y16TjxYNoxIbInbqQbbqzgyCK6UpRLL/4ygM5xAbc+bsiAjHWV0O/h4IZWSkJVSMzC90e6l5VkOicpWCMPs8u4Al7pPT3collkku5b2bMzD1cYnqjJ3SLyYI8ZEsKy3657dgdkxgle+ob/IUExrQIhRAGrhFvBH5Y/cSIn8FoSi4lcDLrjQlQ+ku51Z4BZXFOH8H7t1WkmKvwrdOZwQiG7u9xdM9pTz8dQY9zLNiilnQb913yN/ECagI+b/zX0dAzFZPWArvf1isYvX/Eo72LLHKLX0BwJz3mSxJGYZ8NSgQKBgQDxjuESQMprDtX5D/jjz8zsGO3FozN9CHFO1DD5iXULu3m0Va8BXBktwR5FJx8xqp4Spk5608zSqxgQsmLGkjkLu3BHlJ70O7ZHQwsSyBMV8s/VBPliUI5z4htoiexNKcNpPLY0GBJDrjAYLwnb+XpO254vSZCkEd20GAr7JpvW6QKBgQCoiYheEaJ8DUfDkZZWMQThokyIGJPRFOwO6LA0J7bbdBQJ5njAZUWvuVsshp7UXtpgjt0oPyMUmjy1tmaTJNYBz1+5FSDYc4waa9WPMao54C6/ieZHKaKvgTQuuY7xwuBJLUXKsqjkWr5TZ2VlO4gonSZ1Tq2spBwlCSlElEhAFwKBgGa2hXe4exnYvh1gsElWQ+Gkm/4s7ijdXSjSy1xgGZf6iL6orHici8xL0K5A+1+oF/0AIGDUDMULoXkAEUY0SPN3KUAWrxxVA6X7mQrMbmknzT3cFpq6fEhEPblO5F7PQltOSH+QOLv0pliyL7QMufgsJHAhDNLnEemRS6INgbh5AoGAPzfvrhGBWHUG0UYgFU6n3RcrC/XkJI5riaAg0D+BaMoe5iPMcabLQAIQQCBzHYhC8+tC6+DbANRmJ2c3DMTJfb5S0rMpMegNyq9eWSsxBiqgfnp0pYbpnlmhwFEcXHVT+j8AKetgTN/4+oITV6jABzQDhAcyQEsCv6WzFCVnAJsCgYEA0TVgGR41aCCrN075TEQZdeRKchprLtgeF2KmHW7ZnOTGYrVexIKwiZq2D87GJDfGsaRlR7bf8zrMfm4Jlw6nSeS1vGth9k1MU/dnucs1geNuWcYuQvkIwQ1minGHo4qv4NBJpX+to4VPWYZ+rm+JC4EavX9KKqRoKJmHpUaHJn0=", // 👈 替换（可从 .pem 文件读取）
                "json",
                "UTF-8",
                "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAowiWmaFNNXJge6wYblD41Tm8sijn63GtTng1O4k64On4Xvm0iD15Yqxjac6dgUtaWKKT0OCrmBFNZ1yQOoLnuzUAfcviCBd5yccPyzsINUZANJoJ6e7uP6b0yZJdBqOGRvzose4jN30uQtw9w7BrbLCNXp7aBExICoAfV8u4Zo3eMW3L7UyhX/GiQMHuev/RkSZjl9R83tAYqpI6JLnPjEYyZ1dcC9ZY4mJIdCM3a/Gzpfn+pDqIxAu5t6dxJuM7PU9iqyelq75EwZht0bMoELk1EsUDmhLZ7ed/B9KaeIyQMqV0nDVA5m7OCsmd4JihQvJ7ZiZJjdnE2u5GaLN46QIDAQAB", // 👈 替换（从支付宝开放平台获取）
                "RSA2"
        );

    }
}
