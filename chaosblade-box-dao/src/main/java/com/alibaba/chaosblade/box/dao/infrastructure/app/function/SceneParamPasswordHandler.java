package com.alibaba.chaosblade.box.dao.infrastructure.app.function;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

/**
 * @author haibin
 *
 *
 */
public class SceneParamPasswordHandler {

    private static String key = "ahaschaoahaschao";

    private static String AES = "AES/ECB/PKCS5Padding";

    // 加密
    public static String encrypt(String sSrc) throws Exception {
        byte[] raw = key.getBytes(StandardCharsets.UTF_8);
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        //"算法/模式/补码方式"
        Cipher cipher = Cipher.getInstance(AES);
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(sSrc.getBytes(StandardCharsets.UTF_8));
        //此处使用BASE64做转码功能，同时能起到2次加密的作用。
        return new Base64().encodeToString(encrypted);
    }

    // 解密
    public static String decrypt(String sSrc) throws Exception {
        try {
            byte[] raw = key.getBytes(StandardCharsets.UTF_8);
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance(AES);
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            //先用base64解密
            byte[] encrypted1 = new Base64().decode(sSrc);
            try {
                byte[] original = cipher.doFinal(encrypted1);
                return new String(original, StandardCharsets.UTF_8);
            } catch (Exception e) {
                return null;
            }
        } catch (Exception ex) {
            return null;
        }
    }

}
