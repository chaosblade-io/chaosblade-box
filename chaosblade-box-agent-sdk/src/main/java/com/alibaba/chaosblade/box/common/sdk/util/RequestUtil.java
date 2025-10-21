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

package com.alibaba.chaosblade.box.common.sdk.util;

import com.alibaba.chaosblade.box.common.sdk.constant.Header;
import com.alibaba.chaosblade.box.common.sdk.entity.ModelArgs;
import com.alibaba.chaosblade.box.common.sdk.transport.Request;
import com.alibaba.fastjson.JSON;

/** @author Changjun Xiao */
public class RequestUtil {

  public static Request createRequest(
      String machine, String port, String machineType, String ak, String sk, String command) {
    Request request = new Request();
    request
        .addHeader(Header.HOST, machine)
        .addHeader(Header.PORT, port)
        .addHeader(Header.TYPE, machineType)
        .addHeader(Header.AK, ak)
        .addHeader(Header.SK, sk);
    request.addParam(Header.CMD, command);
    return request;
  }

  public static Request createRequest(
      String machine, String port, String handler, String machineType) {
    Request request = new Request();
    request
        .addHeader(Header.HOST, machine)
        .addHeader(Header.PORT, port)
        .addHeader("handler", handler)
        .addHeader(Header.TYPE, machineType);
    return request;
  }

  /**
   * 创建 request 请求
   *
   * @param scope
   * @param port
   * @param command
   * @return
   */
  public static Request createRequest(String scope, String port, String command) {
    Request request = new Request();
    request.addHeader(Header.HOST, scope).addHeader(Header.PORT, port);
    request
        .addParam(Header.CMD, command)
        .addParam(Header.TIMESTAMP_KEY, System.currentTimeMillis() + "");
    return request;
  }

  /**
   * @param scope
   * @param command
   * @return
   */
  public static Request createRequest(String scope, String command) {
    Request request = new Request();
    request.addHeader(Header.HOST, scope);
    request.addParam(Header.CMD, command);
    return request;
  }

  /**
   * 创建 request 请求
   *
   * @param machine
   * @param machineType
   * @param ak
   * @param sk
   * @return
   */
  public static Request createLitmusRequest(
      String machine, String machineType, String ak, String sk, String version) {
    Request request = new Request();
    request
        .addHeader(Header.HOST, machine)
        .addHeader(Header.TYPE, machineType)
        .addHeader(Header.AK, ak)
        .addHeader(Header.SK, sk);
    request.addParam("version", version);
    return request;
  }

  public static Request createLitmusRequest(
      String chaosAction,
      String machine,
      String machineType,
      String namespace,
      String name,
      String ak,
      String sk) {
    Request request = new Request();
    request
        .addHeader(Header.HOST, machine)
        .addHeader(Header.TYPE, machineType)
        .addHeader(Header.AK, ak)
        .addHeader(Header.SK, sk);
    request
        .addParam("name", name)
        .addParam("namespace", namespace)
        .addParam("chaosAction", chaosAction);
    return request;
  }

  public static Request createLitmusRequest(
      String machine, String machineType, String ak, String sk) {
    Request request = new Request();
    request
        .addHeader(Header.HOST, machine)
        .addHeader(Header.TYPE, machineType)
        .addHeader(Header.AK, ak)
        .addHeader(Header.SK, sk);
    return request;
  }

  public static Request createLitmusRequest(
      String chaosAction,
      ModelArgs modelArgs,
      String namespace,
      String experimentType,
      String ak,
      String sk) {
    Request request = new Request();
    request
        .addHeader(Header.HOST, modelArgs.getMachine())
        .addHeader(Header.TYPE, modelArgs.getMachineType())
        .addHeader(Header.AK, ak)
        .addHeader(Header.SK, sk);
    String scope = modelArgs.getScope();
    String target = modelArgs.getTarget();
    String action = modelArgs.getAction();
    String experimentName;
    if (target.equals(scope)) {
      experimentName = target + "-" + action;
    } else {
      experimentName = scope + "-" + target + "-" + action;
    }

    request
        .addParam("chaosAction", chaosAction)
        .addParam("namespace", namespace)
        .addParam("experimentType", experimentType)
        .addParam("experimentName", experimentName)
        .addParam("appInfo", JSON.toJSONString(modelArgs.getMatcherFlags()))
        .addParam("components", JSON.toJSONString(modelArgs.getFlags()));

    return request;
  }
}
