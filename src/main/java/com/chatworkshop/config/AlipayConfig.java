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
                "https://openapi-sandbox.dl.alipaydev.com/gateway.do",
                "2021000147678552", // 👈 替换
                "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCH3e89BaYdqds0qfazBl+VRhvrPY8XByMETWK/IQ5zPiT8EmH91QaHn6gIOCWo7+9ue+reh+7YDZOVgSyeF5Xub5IefdvmIMPksHOpQ/4g+EQpHmIr5nTEw1kU5kBNBXE98s1kPa+4XkmWieV42h3ioxzrZ95fp7pf1nN47254FTJHIAkjkApoWWjr8az55bA1j0wVvusqXKdyouI7IHXobo4GyBwimrpbkKAy1l48YTlfamqGlhKWwiid+5NXtv8O5PthQyttR73ySeGvXj6jZ9kLH238zfEtK5hFobI9CaujEW/aaUpN/XPEeI7YalS8G1YplwcFlAdIQjl5t69VAgMBAAECggEAMi6oxHMnreEk08jLE8iQ2UOx4Cs2hJzgNCCZDlsZSTt00Z3Aj850wPcZSx/h2Qn192rRjyvJ7gblsdyqLCIuAIk5AQYfiFolwHvj3Gr+nBLWqhFM1UYjbuFwL6Xzrf8KmoTpk9Ks6n341B9OoFp/m0v/zHrqyY3b6rX8pHha5wLy3WkUXztx5h0wP8538ctw/DVXjCHAbc0KN6b+JOBOTL9aRLG5ZlJ0mFN5bCUUPRuw9tYA2Cl81FIlo62GKlR2Ysmifemy2gttrmu0ZYZZEymPB3X/+pxAApRCa5PmdpolPafvqAewovZOLAaUvpLMhVfV9mUDhM2mv7p1wTfvaQKBgQDMUOYBkjGvMGogmQJc3O10IcD3FflTSc7sMibly1iOfDRMI4kjGmTQNMRV7yuMbDsOLJt8U5xHCal6Cong/b2kGfG7MMB6vB0w/YoIyq7LK8p4TUe7pSKYi3ioXLmCfdnnEUrbOY9AoR5cBRYCcod7N6y3PU+fqL1zxOl0W3GNKwKBgQCqPGir6fREp3pJxd2yNlu5cprUfIhPhYsLg/zt8vWfKDpv3wQFkxhACmldqh8F/hds2RfoUTK5qKWYc/qn96zSEgifJQXoYy03+ERgKsZEeSGIPghaBv53VdMenVl0MEzEwwwCC/4NXrSmee+JvedNFtHfTnnvsBRH3BY7YZp1fwKBgQDBI7hJw+kzspCgQAP7xNyy6zV+XUdTdHIm1UHv+Em+Z6sITo9Un56tMzRmB7EjDSzGOWTCIkeY34lV1KCCD1s1xVC9o1hQrYLqzMGvjemuJbdAAc3NlE0f5rgob37t3AogZDhfBApnDeelFSm5Jorr77VVgp/CTcDJr1sAo3C0AQKBgE8tE33JrUt1HYJMXeUyNQ0WiX6EgR2DPf1YIP5GvwiMPEmlzwgMtv2qGgFvS0iNOjXs7pqlkVMaZhJ29sDCiAWYT5w9Kvc/kVtzw9F6c4aLVBY6y8WbdSTprOyTQHSytjHZia82Bq4POzQmiEtnILoRAAG9TSc2nOgjK8/ogF9fAoGAJxpHFYlzWJhpsPU/GuujV+vV404XGAUmuwLDN37xJm5a9I8uWaMQUb5jLpfXsOmsy1Y9837sWeKeQmjeB80G8L/dxP3ImRHOyUDcMky+Bq7mAO3QJrl7i+4IEhpR74yjfEooKSNl+HDbsJXxdxzljX15y0U05LLfArWO0OLggRM=", // 👈 替换（可从 .pem 文件读取）
                "json",
                "UTF-8",
                "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAjwHa4UZ5QQ9HUMcHTx49Qe7oSzIGvfZmnKP05fuvFcYlmO8fVddLh/BdormiAIspfh2FFkeDWJzGsVFWA5Qg7Heh7qGUhX40Q4GDog2AsYgfI2qH0FYYH9nQhmBbD68Wc9WSBxfkkd5Hbk8smcNTfFmscLAKAObm9VRCOUnN1+eMCIDtVkWRpzI8F6eQ/L8tSsVYjFqOlvgGjiWQODdGIE6DZlcrEXfxs9RyJkycX1ZxUUBlyMT9bzChAUlrbUGpzYz/7WREItwmoT4JFIicr9rb1P58deE2Tr+IHJ+OOEkYAafech4BHGzPTCojuhkWnhIwa5fDW3qQjbmiWF5ciQIDAQAB", // 👈 替换（从支付宝开放平台获取）
                "RSA2"
        );
    }
}
