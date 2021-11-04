package com.kjxls.payment.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author: abel.tang.
 * @Date 2021/11/4 下午1:45.
 * @Version 1.0
 */
@Component
@ConfigurationProperties(prefix = PaymentProperties.PREFIX)
@Data
public class PaymentProperties {

    public static final String PREFIX = "spring.payment";
    /**
     * 当前服务域名
     */
    public String domain;
    /**
     * 微信商户配置
     */
    public WeChatPay weChatPay = new WeChatPay();

    // https://pay.weixin.qq.com/index.php/core/cert/api_cert#/
    @Data
    public class WeChatPay{
        /**
         * 商户号
         */
        private String merchantId;
        /**
         * 商户API证书的证书序列号
         */
        private String merchantSerialNumber;
        /**
         * API密钥
         */
        private String apiKey;
        /**
         * API密钥
         */
        private String apiV3Key;
    }
}
