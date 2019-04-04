package com.edu.JavaLearning.Learning.EnDeCrypt;


import org.springframework.util.DigestUtils;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @Author: tangzh
 * @Date: 2019/3/19$ 4:33 PM$
 * 常用加解密工具类
 **/
public class EnDeCryptUtil {

    private static final char[] HEX_CHARS =
            {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f','g','h','i','j','k'};

    private static String KEY_MD5 = "MD5";
    private static String KEY_SHA = "SHA";
    /**
     * MAC算法可选以下多种算法
     * HmacMD5
     * HmacSHA1
     * HmacSHA256
     * HmacSHA384
     * HmacSHA512
     */
    public static String KEY_MAC = "HmacMD5";
    public static String KEY_MAC1 = "HmacSHA1";
    public static String KEY_MAC2 = "HmacSHA256";


    public static void main(String[] args) throws Exception {
        String data = "1234qwer";
        String enBase64Str = Base64.enCode(data);
        String md5Str = MD5.enCode(data);
        String shaStr = SHA.enCode(data);
        String key = HMAC.initMacKey();
        String enData = HMAC.enCode(data,key);
        System.out.println("Base64:\n"+enBase64Str+"\nMD5:\n"+md5Str+"\nSHA:\n"+shaStr+"\nkey:\n"+key+"\nHMAC:\n"+enData);
    }





    /**
      * @description:BASE64 严格地说，属于编码格式，而非加密算法
      * 主要就是BASE64Encoder、BASE64Decoder两个类，我们只需要知道使用对应的方法即可。
      * 另，BASE加密后产生的字节位数是8的倍数，如果不够位数以=符号填充。
    **/
    static class Base64{
        /**
          * @description: 加密
        **/
        public static String enCode(String str){
            return new BASE64Encoder().encodeBuffer(str.getBytes());
        }

        /**
          * @description: 解密
        **/
        public static String deCode(String str) throws IOException {
            return new String(new BASE64Decoder().decodeBuffer(str));
        }
    }

    /**
      * @description:MD5(信息-摘要算法）缩写
      * 通常我们不直接使用上述MD5加密。通常将MD5产生的字节数组交给BASE64再加密一把，得到相应的字符串。
      * or 直接使用的是Spring的Util类。查看源码可知是在原生md5的基础上又做了一层加工
    **/
    static class MD5{
        public static String enCode(String str) throws NoSuchAlgorithmException {
            MessageDigest md5 = getDigest(KEY_MD5);
            return encodeHex(md5.digest(str.getBytes()));
        }
    }

    /**
      * @description: SHA(Secure Hash Algorithm，安全散列算法）数字签名等密码学应用中重要的工具,
      * 被广泛地应用于电子商务等信息安全领域.虽然，SHA与MD5通过碰撞法都被破解了，
      * 但是SHA仍然是公认的安全加密算法，较之MD5更为安全。
     **/
    static class SHA{
        public static String enCode(String str) {
            MessageDigest sha = getDigest(KEY_SHA);
            return encodeHex(sha.digest(str.getBytes()));
        }
    }

    /**
      * @description:HMAC(Hash Message Authentication Code, 密钥+data->密文.equals(密钥+data)
      * 散列消息鉴别码,基于密钥的Hash算法的认证协议.
      * 消息鉴别码实现鉴别的原理是:用公开函数和密钥产生一个固定长度的值作为认证标识,
      * 用这个标识鉴别消息的完整性.使用一个密钥生成一个固定大小的小数据块,即MAC,
      * 并将其加入到消息中,然后传输.接收方利用与发送方共享的密钥进行鉴别认证等.
     **/
    static class HMAC{
        /**
         * 初始化密钥
         */
        public static String initMacKey() throws NoSuchAlgorithmException {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(KEY_MAC1);
            return encodeHex(keyGenerator.generateKey().getEncoded());
        }

        public static String enCode(String data,String key) throws Exception {
            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), KEY_MAC);
            Mac mac = Mac.getInstance(keySpec.getAlgorithm());
            mac.init(keySpec);
            byte[] bytes = mac.doFinal(data.getBytes());
            return encodeHex(bytes);
        }
    }

    private static MessageDigest getDigest(String algorithm){
        try {
            return MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException(algorithm+"非法!");
        }
    }

    /**
      * @description:盗取org.springframework.util.DigestUtils
      * 生成HEX_CHARS中随机的 32位字符串
    **/
    private static String encodeHex(byte[] bytes) {
        char chars[] = new char[32];
        for (int i = 0; i < chars.length; i = i + 2) {
            byte b = bytes[i / 2];
            chars[i] = HEX_CHARS[(b >>> 0x4) & 0xf];
            chars[i + 1] = HEX_CHARS[b & 0xf];
        }
        return new String(chars);
    }

}
