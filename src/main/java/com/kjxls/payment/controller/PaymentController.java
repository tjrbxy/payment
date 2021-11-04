package com.kjxls.payment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.kjxls.payment.properties.PaymentProperties;
import com.wechat.pay.contrib.apache.httpclient.WechatPayHttpClientBuilder;
import com.wechat.pay.contrib.apache.httpclient.auth.AutoUpdateCertificatesVerifier;
import com.wechat.pay.contrib.apache.httpclient.auth.PrivateKeySigner;
import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Credentials;
import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Validator;
import com.wechat.pay.contrib.apache.httpclient.util.PemUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.security.PrivateKey;

/**
 * @Author: abel.tang.
 * @Date 2021/11/4 下午1:26.
 * @Version 1.0
 */
@RestController
@Slf4j
public class PaymentController {

    @Resource
    PaymentProperties paymentProperties;

    @RequestMapping(value = "payment",method = RequestMethod.POST)
    public Object payment(HttpServletRequest request) throws IOException {

        String merchantId = paymentProperties.getWeChatPay().getMerchantId();
        String merchantSerialNumber = paymentProperties.getWeChatPay().getMerchantSerialNumber();
        String apiV3Key = paymentProperties.getWeChatPay().getApiV3Key();
        // https://github.com/wechatpay-apiv3/wechatpay-apache-httpclient#%E5%A6%82%E4%BD%95%E5%8A%A0%E8%BD%BD%E5%95%86%E6%88%B7%E7%A7%81%E9%92%A5;
        PrivateKey merchantPrivateKey = PemUtil.loadPrivateKey(
                new FileInputStream("/app/apiclient_key.pem"));

        //不需要传入微信支付证书了
        AutoUpdateCertificatesVerifier verifier = new AutoUpdateCertificatesVerifier(
                new WechatPay2Credentials(merchantId, new PrivateKeySigner(merchantSerialNumber, merchantPrivateKey)),
                apiV3Key.getBytes("utf-8"));
//...
        WechatPayHttpClientBuilder builder = WechatPayHttpClientBuilder.create()
                .withMerchant(merchantId , merchantSerialNumber, merchantPrivateKey)
                .withValidator(new WechatPay2Validator(verifier));
// ... 接下来，你仍然可以通过builder设置各种参数，来配置你的HttpClient

// 通过WechatPayHttpClientBuilder构造的HttpClient，会自动的处理签名和验签
        HttpClient httpClient = builder.build();

// 后面跟使用Apache HttpClient一样
        HttpPost httpPost = new HttpPost("https://api.mch.weixin.qq.com/v3/pay/transactions/jsapi");
        httpPost.addHeader("Accept", "application/json");
        httpPost.addHeader("Content-type","application/json; charset=utf-8");

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectMapper objectMapper = new ObjectMapper();

        ObjectNode rootNode = objectMapper.createObjectNode();
        rootNode.put("mchid",paymentProperties.getWeChatPay().getMerchantId())
                .put("appid", "wx8a52b3939956e080")
                .put("description", "Image形象店-深圳腾大-QQ公仔")
                .put("notify_url", "https://www.weixin.qq.com/wxpay/pay.php")
                .put("out_trade_no", "1217752501201407033233368018");
        rootNode.putObject("amount")
                .put("total", 1);
        rootNode.putObject("payer")
                .put("openid", "ozZK458S9LLNni3Q0PfYDm_JcjZQ");

        objectMapper.writeValue(bos, rootNode);

        httpPost.setEntity(new StringEntity(bos.toString("UTF-8"), "UTF-8"));
        HttpResponse response = httpClient.execute(httpPost);

        String bodyAsString = EntityUtils.toString(response.getEntity());
        System.out.println(bodyAsString);

        // HttpResponse response = httpClient.execute(...);
        return null;
    }
}
