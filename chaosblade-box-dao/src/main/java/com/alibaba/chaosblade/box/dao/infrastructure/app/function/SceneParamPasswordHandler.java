/*
 * Copyright 2025 The ChaosBlade Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.alibaba.chaosblade.box.dao.infrastructure.app.function;

import java.nio.charset.StandardCharsets;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;

/** @author haibin */
public class SceneParamPasswordHandler {

  private static String key = "ahaschaoahaschao";

  private static String AES = "AES/ECB/PKCS5Padding";

  // 加密
  public static String encrypt(String sSrc) throws Exception {
    byte[] raw = key.getBytes(StandardCharsets.UTF_8);
    SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
    // "算法/模式/补码方式"
    Cipher cipher = Cipher.getInstance(AES);
    cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
    byte[] encrypted = cipher.doFinal(sSrc.getBytes(StandardCharsets.UTF_8));
    // 此处使用BASE64做转码功能，同时能起到2次加密的作用。
    return new Base64().encodeToString(encrypted);
  }

  // 解密
  public static String decrypt(String sSrc) throws Exception {
    try {
      byte[] raw = key.getBytes(StandardCharsets.UTF_8);
      SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
      Cipher cipher = Cipher.getInstance(AES);
      cipher.init(Cipher.DECRYPT_MODE, skeySpec);
      // 先用base64解密
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
