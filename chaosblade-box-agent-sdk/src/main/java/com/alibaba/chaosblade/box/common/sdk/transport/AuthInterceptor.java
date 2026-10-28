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


package com.alibaba.chaosblade.box.common.sdk.transport;

import com.alibaba.chaosblade.box.common.sdk.constant.Header;
import com.alibaba.fastjson.JSON;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * Sign the request invoking to the agent, and check the sign of the request from the agent.
 *
 * <p>The algorithm must stay consistent with the agent side (chaos-agent pkg/tools.Sign): sn =
 * Base64(hex(sha256(signData + sk))). The exact signData is carried in the "sd" header so that the
 * agent verifies it verbatim instead of re-serializing the params with its own JSON marshaller.
 */
public class AuthInterceptor extends RequestInterceptorChain {

  @Override
  public void doHandle(Request request) throws RequestException {
    String sign = request.getHeader(Header.SN);
    if (sign == null || sign.length() == 0) {
      throw new RequestException("missing sign");
    }
    if (!sign.equals(sign(signData(request), secretKey(request)))) {
      throw new RequestException("illegal request");
    }
  }

  @Override
  public void doInvoke(Request request) throws RequestException {
    String signData = JSON.toJSONString(request.getParams());
    request.addHeader(Header.SD, signData);
    request.addHeader(Header.SN, sign(signData, secretKey(request)));
  }

  private String signData(Request request) {
    String signData = request.getHeader(Header.SD);
    if (signData == null || signData.length() == 0) {
      signData = JSON.toJSONString(request.getParams());
    }
    return signData;
  }

  private String secretKey(Request request) {
    String secretKey = request.getHeader(Header.SK);
    return secretKey == null ? "" : secretKey;
  }

  public static String sign(String signData, String secretKey) throws RequestException {
    try {
      MessageDigest digest = MessageDigest.getInstance("SHA-256");
      byte[] hash = digest.digest((signData + secretKey).getBytes(StandardCharsets.UTF_8));
      StringBuilder hex = new StringBuilder(hash.length * 2);
      for (byte b : hash) {
        hex.append(Character.forDigit((b >> 4) & 0xF, 16));
        hex.append(Character.forDigit(b & 0xF, 16));
      }
      return Base64.getEncoder().encodeToString(hex.toString().getBytes(StandardCharsets.UTF_8));
    } catch (NoSuchAlgorithmException e) {
      throw new RequestException("sign the request failed", e);
    }
  }
}
