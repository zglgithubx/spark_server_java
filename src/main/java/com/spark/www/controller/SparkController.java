package com.spark.www.controller;
import com.spark.www.pojo.request.EncryptData;
import com.spark.www.utils.AuthUtil;
import com.spark.www.utils.RSAUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import java.util.Date;
@RestController
@RequestMapping("/api")
public class SparkController {
    @Resource
    private AuthUtil authUtil;

    @Value("${chat.api.key}")
    private String apiKey;
    @Value("${chat.api.secret}")
    private String apiSecret;
    @Value("${chat.api.url}")
    private String apiUrl;

    @GetMapping("/getTicket")
    public String getToken(HttpServletRequest request) throws Exception {
        String id = request.getSession().getId();
        RSAUtil.RsaKeyPair keyPair = RSAUtil.generateKeyPair();
        RSAUtil.privateKeyMap.put(id,keyPair.getPrivateKey());
        Date date=new Date();
        long time = date.getTime();
        return RSAUtil.encryptByPublicKey(keyPair.getPublicKey(), String.valueOf(time));
    }

    @PostMapping("/getUrl")
    public Object getAuthorizationUrl(HttpServletRequest request,@RequestBody EncryptData encryptData) throws Exception {
        String data=encryptData.getData().replace("\"","");
        try {
            String id = request.getSession().getId();
            if(RSAUtil.privateKeyMap.containsKey(id)){
                return "服务异常，无法获取密钥";
            }
            String privateKey=RSAUtil.privateKeyMap.get(id);
            String text = RSAUtil.decryptByPrivateKey(privateKey, data);
            long start = Long.parseLong(text);
            long cur = new Date().getTime();
            if(cur-start>500){
                return "请求异常，请刷新页面重试. code="+(cur-start);
            }
        } catch (Exception e) {
            return "验证票据失败:"+e.getMessage();
        }
        try {
            return authUtil.getAuthorizationUrl(apiUrl, apiKey,apiSecret);
        } catch (Exception e) {
            return "获取url失败，异常信息："+e.getMessage();
        }
    }
}
