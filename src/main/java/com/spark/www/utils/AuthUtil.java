package com.spark.www.utils;

import okhttp3.HttpUrl;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

@Component
public class AuthUtil {
    public String getAuthorizationUrl(String hostUrl , String apikey ,String apisecret) throws Exception {
        //获取host
        URL url = new URL(hostUrl);
        //获取鉴权时间 date
        SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);

        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        String date = format.format(new Date());
        //获取signature_origin字段
        StringBuilder builder = new StringBuilder("host: ").append(url.getHost()).append("\n").
                append("date: ").append(date).append("\n").
                append("GET ").append(url.getPath()).append(" HTTP/1.1");
        //获得signatue
        Charset charset = Charset.forName("UTF-8");


        Mac mac = Mac.getInstance("hmacsha256");
        SecretKeySpec sp = new SecretKeySpec(apisecret.getBytes(charset),"hmacsha256");
        mac.init(sp);
        byte[] basebefore = mac.doFinal(builder.toString().getBytes(charset));
        String signature = Base64.getEncoder().encodeToString(basebefore);
        //获得 authorization_origin
        String authorization_origin = String.format("api_key=\"%s\",algorithm=\"%s\",headers=\"%s\",signature=\"%s\"",apikey,"hmac-sha256","host date request-line",signature);
        //获得authorization
        String authorization = Base64.getEncoder().encodeToString(authorization_origin.getBytes(charset));
        //获取httpurl
        HttpUrl httpUrl = HttpUrl.parse("https://" + url.getHost() + url.getPath()).newBuilder().
                addQueryParameter("authorization", authorization).
                addQueryParameter("date", date).
                addQueryParameter("host", url.getHost()).
                build();
        return httpUrl.toString();
    }
}
