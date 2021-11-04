package com.kjxls.payment.controller;

import com.kjxls.payment.properties.PaymentProperties;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.codec.binary.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;

@RestController
@Slf4j
public class OauthController {

    @Autowired
    PaymentProperties paymentProperties;

    @RequestMapping(value = "api/v1/oauth",method = RequestMethod.GET)
    public Object oauth(
            HttpServletResponse response,
            @RequestParam(value = "state",defaultValue = "") String state,
            @RequestParam(value = "code",defaultValue = "") String code) throws IOException {
        String appId = "wx8a52b3939956e080";
        if (StringUtils.equals("",code)){
            String api = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + appId
                    +"&redirect_uri=" + URLEncoder.encode(paymentProperties.domain  + "/api/v1/oauth","utf-8")
                    +"&response_type=code&scope=snsapi_base&state=123#wechat_redirect";

            response.sendRedirect(api);
        }else{
            String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + appId
                    +"&secret=46641adbc471769bc541f77f207def1c&code="+code
                    +"&grant_type=authorization_code";

            OkHttpClient client = new OkHttpClient().newBuilder().build();
            Request request = new Request.Builder()
                    .url(url)
                    .method("GET", null)
                    .build();
            Response responses = client.newCall(request).execute();

            return responses.body().string();

        }
        return null;
    }
}
