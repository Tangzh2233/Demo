package com.edu.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author Tangzh
 * @version 1.0.0
 * @date 2020/9/20 16:31
 * @description
 **/
public class Md5Util {
    private static final Logger log = LoggerFactory.getLogger(Md5Util.class);
    private static char[] Digit = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    public Md5Util() {
    }

    public static String getMd5Sum(String inputStr) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("MD5");
        byte[] inputStrByte = inputStr.getBytes();
        digest.update(inputStrByte, 0, inputStrByte.length);
        byte[] md5sum = digest.digest();
        StringBuffer sb = new StringBuffer();

        for(int i = 0; i < 16; ++i) {
            char[] ob = new char[]{Digit[md5sum[i] >> 4 & 15], Digit[md5sum[i] & 15]};
            String s = new String(ob);
            sb.append(s);
        }

        return sb.toString();
    }

    public static String md5(String str) {
        MessageDigest md5 = null;

        try {
            md5 = MessageDigest.getInstance("MD5");
            md5.update(str.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException var8) {
            log.error("", var8);
            throw new RuntimeException(var8.getMessage());
        } catch (UnsupportedEncodingException var9) {
            var9.printStackTrace();
            throw new RuntimeException(var9.getMessage());
        }

        byte[] encodedValue = md5.digest();
        int j = encodedValue.length;
        char[] finalValue = new char[j * 2];
        int k = 0;

        for(int i = 0; i < j; ++i) {
            byte encoded = encodedValue[i];
            finalValue[k++] = Digit[encoded >> 4 & 15];
            finalValue[k++] = Digit[encoded & 15];
        }

        return new String(finalValue);
    }

    public static boolean verify(String text, String sign) {
        String mysign = md5(text);
        return mysign.equals(sign);
    }

    public static String md5(File file) throws IOException {
        FileInputStream is = new FileInputStream(file);
        MessageDigest md5 = null;

        try {
            md5 = MessageDigest.getInstance("MD5");
            int n = 0;
            byte[] buffer = new byte[1024];

            while(true) {
                n = is.read(buffer);
                if (n > 0) {
                    md5.update(buffer, 0, n);
                }

                if (n == -1) {
                    is.skip(0L);
                    break;
                }
            }
        } catch (NoSuchAlgorithmException var11) {
            var11.printStackTrace();
            throw new RuntimeException(var11.getMessage());
        } finally {
            is.close();
        }

        byte[] encodedValue = md5.digest();
        int j = encodedValue.length;
        char[] finalValue = new char[j * 2];
        int var6 = 0;

        for(int i = 0; i < j; ++i) {
            byte encoded = encodedValue[i];
            finalValue[var6++] = Digit[encoded >> 4 & 15];
            finalValue[var6++] = Digit[encoded & 15];
        }

        return new String(finalValue);
    }

    public static String md5(TreeMap<String, String> params, String key) {
        String md5Str = "";

        Map.Entry entry;
        for(Iterator i$ = params.entrySet().iterator(); i$.hasNext(); md5Str = md5Str + (StringUtils.isEmpty((CharSequence)entry.getValue()) ? "" : (String)entry.getValue()) + "|") {
            entry = (Map.Entry)i$.next();
        }

        return md5(md5Str + key);
    }

    public static boolean verifySign(TreeMap<String, String> params, String signKey, String md5) {
        String encryptedStr = md5(params, signKey);
        log.debug("encryptedStr:{}", encryptedStr);
        return encryptedStr.equals(md5);
    }

    public static void main(String[] args) {
        TreeMap<String, String> params = new TreeMap();
        params.put("a", "234");
        System.out.println(md5(params, "123456"));
    }
}
