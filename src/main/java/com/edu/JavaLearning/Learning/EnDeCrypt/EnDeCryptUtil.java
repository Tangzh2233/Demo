package com.edu.JavaLearning.Learning.EnDeCrypt;


import com.edu.dao.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @Author: tangzh
 * @Date: 2019/3/19$ 4:33 PM$
 * 常用加解密工具类
 **/
public class EnDeCryptUtil {

    private final static Logger logger = LoggerFactory.getLogger(EnDeCryptUtil.class);

    private static final char[] HEX_CHARS = {'1','q','7','z','2','w','8','x','3','e','0','c','4','r','f','5'};

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
        String data = "用户信息有误";
        String enBase64Str = Base64.enCode(data);
        String s = Base64.deCode(enBase64Str);
        System.out.println(initKey(KEY_MAC));
        String md5Str = MD5.enCode(data);
        String shaStr = SHA.enCode(data);
        String enData = HMAC.enCode(data,HMAC.macKey);
        String desEnCode = DES.enCode(data);
        String aesEncode = AES.enCode(data,AES.AES_KEY);
        System.out.println("Base64:\n"+enBase64Str+s+"\nMD5:\n"+md5Str+"\nSHA:\n"+shaStr+"\nHMAC:\n"+enData+"\nDES\n"+desEnCode+"\nAES\n"+aesEncode);
    }

    /**
      * @description:BASE64 严格地说，属于编码格式，而非加密算法
      * 主要就是BASE64Encoder、BASE64Decoder两个类，我们只需要知道使用对应的方法即可。
      * 另，BASE加密后产生的字节位数是8的倍数，如果不够位数以=符号填充。
    **/
    static class Base64{
        /**
          * 加密
        **/
        public static String enCode(String data){
            return new BASE64Encoder().encodeBuffer(data.getBytes(Charset.forName("UTF-8")));
        }

        /**
          * 解密
        **/
        public static String deCode(String str) {
            try {
                return new String(new BASE64Decoder().decodeBuffer(str),Charset.forName("UTF-8"));
            }catch (Exception e){
                logger.error("Base64 解密失败",e);
                return null;
            }
        }
    }

    /**
      * MD5(信息-摘要算法）缩写
      * 通常我们不直接使用上述MD5加密。通常将MD5产生的字节数组交给BASE64再加密一把，得到相应的字符串。
      * or 直接使用的是Spring的Util类。查看源码可知是在原生md5的基础上又做了一层加工
    **/
    static class MD5{
        public static String enCode(String str) throws NoSuchAlgorithmException {
            String KEY_MD5 = "MD5";
            MessageDigest md5 = getDigest(KEY_MD5);
            return encodeHex(md5.digest(str.getBytes()));
        }
    }

    /**
      * SHA(Secure Hash Algorithm，安全散列算法）数字签名等密码学应用中重要的工具,
      * 被广泛地应用于电子商务等信息安全领域.虽然，SHA与MD5通过碰撞法都被破解了，
      * 但是SHA仍然是公认的安全加密算法，较之MD5更为安全。
     **/
    static class SHA{
        public static String enCode(String str) {
            String KEY_SHA = "SHA";
            MessageDigest sha = getDigest(KEY_SHA);
            return encodeHex(sha.digest(str.getBytes()));
        }
    }

    /**
      * HMAC(Hash Message Authentication Code, 密钥+data->密文.equals(密钥+data)
      * 散列消息鉴别码,基于密钥的Hash算法的认证协议.
      * 消息鉴别码实现鉴别的原理是:用公开函数和密钥产生一个固定长度的值作为认证标识,
      * 用这个标识鉴别消息的完整性.使用一个密钥生成一个固定大小的小数据块,即MAC,
      * 并将其加入到消息中,然后传输.接收方利用与发送方共享的密钥进行鉴别认证等.
     **/
    static class HMAC{
        /**
         * 初始化密钥
         */
        private static String macKey = "4c74r57x103q13r0qc2508rz53ex87f7";

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

    /**DES加解密*/
    static class DES{
        private static String DES_KEY = "t#5y7^u8i@0knv*x";
        private static String DES = "DES";
        private static String defaultEncoding = "UTF-8";

        public static String enCode(String content){
            byte[] bytes = desCode(content.getBytes(), DES_KEY, Cipher.ENCRYPT_MODE);
            return parseByte2HexStr(Objects.requireNonNull(bytes));
        }

        public static String deCode(String content){
            byte[] bytes = desCode(parseHexStr2Byte(content), DES_KEY, Cipher.DECRYPT_MODE);
            return new String(Objects.requireNonNull(bytes));
        }

        private static byte[] desCode(byte[] data,String key,int flag){
            try {
                byte[] keyBytes = key.getBytes(Charset.forName(defaultEncoding));
                DESKeySpec keySpec = new DESKeySpec(keyBytes);
                SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
                SecretKey secretKey = keyFactory.generateSecret(keySpec);
                Cipher cipher = Cipher.getInstance(DES);
                cipher.init(flag,secretKey);
                return cipher.doFinal(data);
            }catch (Exception e){
                logger.error("DES Exception:",e);
            }
            return null;
        }
    }

    /**AES 加解密*/
    static class AES{
        /**密钥*/
        private static String AES_KEY = "S%?3*`7v6005U2s%h!By5H=O4M6J^tp7";
        private static String AES = "AES";
        private static String defaultEncoding = "UTF-8";

        public static String enCode(String content,String key){
            byte[] bytes = aesCode(content.getBytes(), key, Cipher.ENCRYPT_MODE);
            return parseByte2HexStr(Objects.requireNonNull(bytes));
        }

        public static String deCode(String content,String key){
            byte[] bytes = aesCode(parseHexStr2Byte(content), key, Cipher.DECRYPT_MODE);
            return new String(Objects.requireNonNull(bytes));
        }

        private static SecureRandom getRandom(){
            try {
                return SecureRandom.getInstance("SHA1PRNG");
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
                return new SecureRandom();
            }
        }
        /**flag:加解密标识*/
        private static byte[] aesCode(byte[] content,String key,int flag){
            try{
                SecureRandom random = getRandom();
                random.setSeed(key.getBytes(Charset.forName(defaultEncoding)));
                KeyGenerator keyGenerator = KeyGenerator.getInstance(AES);
                keyGenerator.init(128,random);
                SecretKey secretKey = keyGenerator.generateKey();
                byte[] encoded = secretKey.getEncoded();
                SecretKeySpec keySpec = new SecretKeySpec(encoded, AES);
                Cipher cipher = Cipher.getInstance(AES);
                cipher.init(flag,keySpec);
                return cipher.doFinal(content);
            }catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
                logger.error("aesCode Exception :",e);
            }
            return null;
        }

    }

    /**
     * 非对称加密算法RSA算法组件
     * 非对称算法一般是用来传送对称加密算法的密钥来使用的，相对于DH算法，RSA算法只需要一方构造密钥，不需要
     * 大费周章的构造各自本地的密钥对了。DH算法只能算法非对称算法的底层实现。而RSA算法算法实现起来较为简单
     */
    static class RSA{
        /**非对称算法*/
        private static String RSA = "RSA";
        /**
         *密钥长度，DH算法的默认密钥长度是1024
         *密钥长度必须是64的倍数，在512到65536位之间
         * */
        private static int KEY_SIZE = 1024;
        /**公钥*/
        private static String PUB_KEY = "RSAPublicKey";
        /**私钥*/
        private static String PRI_KEY = "RSAPrivateKey";

        public static Map<String,byte[]> initKey(){
            try {
                HashMap<String, byte[]> map = new HashMap<>(2);
                /**实例密钥生成器*/
                KeyPairGenerator generator = KeyPairGenerator.getInstance(RSA);
                /**初始化密钥生成器*/
                generator.initialize(KEY_SIZE);
                /**生成密钥对*/
                KeyPair keyPair = generator.generateKeyPair();
                map.put(PUB_KEY,keyPair.getPublic().getEncoded());
                map.put(PRI_KEY,keyPair.getPrivate().getEncoded());
                return map;
            } catch (NoSuchAlgorithmException e) {
                logger.error("RSA initKey Exception:",e);
                return null;
            }
        }

        /**
         * RSA 私钥加密
         * @param data 待加密数据
         * @param key  私钥
         */
        public static String enCodeByPriKey(String data,byte[] key){
            try {
                byte[] bytes = rsaCodeByPriKey(data.getBytes(), key, Cipher.ENCRYPT_MODE);
                return parseByte2HexStr(bytes);
            } catch (Exception e) {
                logger.error("RSA 私钥加密异常:",e);
                return null;
            }
        }

        /**
         * RSA 私钥解密
         * @param data 待解密数据
         * @param key  私钥
         */
        public static String deCodeByPriKey(String data,byte[] key){
            try {
                byte[] bytes = rsaCodeByPriKey(parseHexStr2Byte(data), key, Cipher.DECRYPT_MODE);
                return new String(bytes);
            } catch (Exception e) {
                logger.error("RSA 私钥解密异常:",e);
                return null;
            }
        }

        /**
         * RSA 公钥加密
         * @param data 待加密数据
         * @param key  公钥
         */
        public static String enCodeByPubKey(String data,byte[] key){
            try{
                byte[] bytes = rsaCodeByPubKey(data.getBytes(), key, Cipher.ENCRYPT_MODE);
                return parseByte2HexStr(bytes);
            }catch (Exception e){
                logger.error("RSA 公钥加密异常:",e);
                return null;
            }
        }

        /**
         * RSA 公钥解密
         * @param data 待解密数据
         * @param key  公钥
         */
        public static String deCodeByPubKey(String data,byte[] key){
            try{
                byte[] bytes = rsaCodeByPubKey(parseHexStr2Byte(data), key, Cipher.DECRYPT_MODE);
                return new String(bytes);
            }catch (Exception e){
                logger.error("RSA 公钥解密异常:",e);
                return null;
            }
        }


        /**
         * 私钥加解密
         * flag:Cipher.ENCRYPT_MODE -> 加密
         * flag:Cipher.DECRYPT_MODE -> 解密
         */
        private static byte[] rsaCodeByPriKey(byte[] data,byte[] key,int flag) throws Exception{
            //初始化私钥
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(key);
            //实例化密钥工厂
            KeyFactory keyFactory = KeyFactory.getInstance(RSA);
            //产生私钥
            PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
            cipher.init(flag,privateKey);
            return cipher.doFinal(data);
        }

        /**
         * 公钥加解密
         * flag:Cipher.ENCRYPT_MODE -> 加密
         * flag:Cipher.DECRYPT_MODE -> 解密
         */
        private static byte[] rsaCodeByPubKey(byte[] data,byte[] key,int flag) throws Exception{
            //初始化公钥
            X509EncodedKeySpec x509keySpec = new X509EncodedKeySpec(key);
            //实例化密钥工厂
            KeyFactory keyFactory = KeyFactory.getInstance(RSA);
            //产生公钥
            PublicKey publicKey = keyFactory.generatePublic(x509keySpec);
            //数据加密
            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
            cipher.init(flag,publicKey);
            return cipher.doFinal(data);
        }

        /**
         * RSA 测试
         * 网络传输的数据 1:公钥 2:数据的AES密文 3:密钥加密的AES key值
         */
        public static void main(String[] args) {
            Map<String, byte[]> keyMap = initKey();
            assert keyMap != null;
            byte[] pubKey = keyMap.get(PUB_KEY);
            byte[] priKey = keyMap.get(PRI_KEY);
            //AES key
            String aesKey = AES.AES_KEY;
            String data = "AES加密数据,RSA加密aesKey";
            //实际数据传输密文
            String aesEncodeData = AES.enCode(data,aesKey);
            //公钥
            String aesPubKey = Base64.enCode(new String(pubKey));
            //RSA 对AES key 进行私钥非对称加密
            String aesEncodeByPriKey = enCodeByPriKey(aesKey, priKey);
            System.out.println("传输的公钥:\n"+aesPubKey);
            System.out.println("传输私钥加密的aesKey\n"+aesEncodeByPriKey+"\n传输数据密文:\n"+aesEncodeData);
            System.out.println("================数据传输完成、开始解密===============");
            System.out.println("目前数据 1:公钥 2:数据的AES密文 3:密钥加密的Aes Key值");
            //RSA 对AES key 进行公钥非对称解密
            String rasAesKey = deCodeByPubKey(aesEncodeByPriKey, pubKey);
            System.out.println("公钥解密的Aes Key:\n"+rasAesKey);
            //AES 解密
            String aesDecode = AES.deCode(aesEncodeData,rasAesKey);
            System.out.println("AES 解密结果:\n"+aesDecode);
        }

    }

    /**
      * @see org.springframework.util.DigestUtils
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
    /**byte -> String  字符数组中的ASCLL码,10进制->16进制*/
    private static String parseByte2HexStr(byte buf[]) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }
    /**String -> byte  对应parseByte2HexStr将str两位分割,16进制->10进制*/
    private static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1) {
            return null;
        }
        byte[] result = new byte[hexStr.length()/2];
        for (int i = 0;i< hexStr.length()/2; i++) {
            int high = Integer.parseInt(hexStr.substring(i*2, i*2+1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }

    /**
     * 获取随机密钥
     */
    private static String initKey(String algorithm){
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(algorithm);
            return encodeHex(keyGenerator.generateKey().getEncoded());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }


}
