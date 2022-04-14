package com.alibaba.chaosblade.box.common.common.util;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;


@Slf4j
public class EncryptUtil {

    private static String PRIVATE_KEY;

    private static String PUBLIC_KEY;

    private static final int MAX_ENCRYPT_BLOCK = 127;

    static {
        ClassPathResource privateKeyResource = new ClassPathResource("auth/privateKey.txt");
        ClassPathResource publicKeyResource = new ClassPathResource("auth/publicKey.txt");
        try {
            PRIVATE_KEY = CharStreams.toString(new InputStreamReader(privateKeyResource.getInputStream(), Charsets.UTF_8)).trim();
            PUBLIC_KEY = CharStreams.toString(new InputStreamReader(publicKeyResource.getInputStream(), Charsets.UTF_8)).trim();
        } catch (IOException e) {
            log.error("INIT PRIVATE_KEY, PUBLIC_KEY ERROR: ", e);
        }
    }


    private static void generateKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048, new SecureRandom());
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();

        String publicKeyString = new String(Base64.getEncoder().encode(publicKey.getEncoded()));
        String privateKeyString = new String(Base64.getEncoder().encode(privateKey.getEncoded()));

        try (PrintWriter out = new PrintWriter("./privateKey.txt")){
            out.println(privateKeyString);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try (PrintWriter out = new PrintWriter("./publicKey.txt")){
            out.println(publicKeyString);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void decrypt(String input) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, InvalidKeySpecException {
        byte[] inputByte = Base64.getDecoder().decode(input);

        byte[] decoded = Base64.getDecoder().decode(PRIVATE_KEY);
        RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, priKey);

        String out = new String(cipher.doFinal(inputByte));
        System.out.println(out);
    }

    public static String rsaEncrypt(String input) {
        String output = null;
        try {
            byte[] decoded = Base64.getDecoder().decode(PUBLIC_KEY);
            RSAPublicKey publicKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));

            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            output = Base64.getEncoder().encodeToString(cipher.doFinal(input.getBytes("UTF-8")));
        } catch (Exception e) {
            log.error("RSA encrypt Error: ", e);
        }
        return output;
    }
    public static String reEncryptPassword(String data) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, BadPaddingException, IllegalBlockSizeException, IOException, InvalidKeyException {
        byte[] decode = Base64.getDecoder().decode(PRIVATE_KEY);
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(decode);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        PrivateKey generatePaivate = kf.generatePrivate(pkcs8EncodedKeySpec);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, generatePaivate);

        byte[] bytes = data.getBytes();
        int inputLen = bytes.length;
        int offLen = 0;
        int i = 0;
        ByteArrayOutputStream bops = new ByteArrayOutputStream();
        while (inputLen - offLen > 0) {
            byte[] cache;
            if (inputLen - offLen > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(bytes, offLen, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(bytes, offLen, inputLen - offLen);
            }
            bops.write(cache);
            i++;
            offLen = MAX_ENCRYPT_BLOCK * i;
        }
        bops.close();
        byte[] encryptedData = bops.toByteArray();
//        return Base64.getEncoder().encodeToString(encryptedData);

        // md5
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.reset();
        byte[] digested = md.digest(encryptedData);
        StringBuilder sb = new StringBuilder();
        for (byte b : digested) {
            sb.append(Integer.toHexString(0xff & b));
        }
        return sb.toString();

    }

    public static String md5Encrypt(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] passBytes = input.getBytes();
            md.reset();
            byte[] digested = md.digest(passBytes);
            StringBuilder sb = new StringBuilder();
            for (byte b : digested) {
                sb.append(Integer.toHexString(0xff & b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            log.error("Not Found Algorithm MD5", e);
        }
        return null;
    }


    /**
     * dmpbLHC21A1ILY+4iFGsdZyw6Fgte8fOl5DEycJvH9I6GrqxwMbHqHpmZxcdTR2dL70k626soRvX/dgKeAC596hYcuM40yxZHM1kDlKNiLPqd2tjBhsoPguicrNZyFrn6gOtycC2iuo93Pt9whMTcNLdFUqcRII7dSfkFr27kDrva0tnKXmDVlWDYaZqe92jJrvoN8xI1b06fGWkXU8xFSIK1SeNyWRT86MlvdmGVofE4EKUzSWkoFlxENYo+yr8ziXJ/PzG7LJW7iydC971KN5r6bcu5H6aVdo1C14RLyV8+1ibpI64n9nkgWdZyeswuZ8Su9znzZbeMtMhvLFXxQ==
     * @param args
     */
    public static void main(String[] args) throws NoSuchAlgorithmException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchPaddingException, IOException, InvalidKeySpecException {
        generateKeyPair();
    }
}
