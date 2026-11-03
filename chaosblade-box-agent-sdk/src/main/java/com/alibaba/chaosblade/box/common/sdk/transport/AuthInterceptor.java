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
 *
 * <p>The secret key is the credential the server issued to that agent when it registered, i.e. the
 * device userId. It travels in the "sk" header only as far as this interceptor, which consumes it
 * and drops it together with "ak" before the request is serialized.
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
    String secretKey = request.getHeader(Header.SK);
    if (secretKey == null || secretKey.isEmpty()) {
      // Fail here rather than sign with an empty key. The agent verifies with the
      // credential it was issued at registration, so an empty-key signature is always
      // rejected there as a bare "illegal request" that says nothing about the real
      // cause, which is a missing device credential on this side.
      throw new RequestException(
          "missing agent secret key, cannot sign the request to "
              + request.getHeader(Header.HOST)
              + ":"
              + request.getHeader(Header.PORT));
    }
    String signData = JSON.toJSONString(request.getParams());
    request.addHeader(Header.SD, signData);
    request.addHeader(Header.SN, sign(signData, secretKey));
    // The credentials only feed the signature. The agent never reads them (it verifies
    // with its own local copy) and this transport is plain HTTP, so neither may leave
    // this process.
    request.removeHeader(Header.SK);
    request.removeHeader(Header.AK);
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
