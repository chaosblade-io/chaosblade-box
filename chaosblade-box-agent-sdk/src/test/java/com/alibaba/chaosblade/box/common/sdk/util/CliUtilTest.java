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

import com.alibaba.chaosblade.box.common.sdk.constant.Blade;
import com.alibaba.chaosblade.box.common.sdk.entity.ModelArgs;
import java.util.HashMap;
import java.util.Map;
import org.junit.Assert;
import org.junit.Test;

/** @author Test */
public class CliUtilTest {

  @Test
  public void testBuildExpCmd_BasicCommand() {
    // 测试基本命令构建
    ModelArgs modelArgs = new ModelArgs();
    modelArgs.setTarget("cpu");
    modelArgs.setAction("load");

    String result = CliUtil.buildExpCmd("create", modelArgs);
    Assert.assertEquals("create cpu load ", result);
  }

  @Test
  public void testBuildExpCmd_WithSubTarget() {
    // 测试包含 subTarget 的命令
    ModelArgs modelArgs = new ModelArgs();
    modelArgs.setTarget("jvm");
    modelArgs.setSubTarget("jvm");
    modelArgs.setAction("delay");

    String result = CliUtil.buildExpCmd("create", modelArgs);
    Assert.assertEquals("create jvm jvm delay ", result);
  }

  @Test
  public void testBuildExpCmd_NodeScope() {
    // 测试 node scope
    ModelArgs modelArgs = new ModelArgs();
    modelArgs.setScope("node");
    modelArgs.setTarget("cpu");
    modelArgs.setAction("load");

    String result = CliUtil.buildExpCmd("create", modelArgs);
    Assert.assertEquals("create k8s cpu load ", result);
  }

  @Test
  public void testBuildExpCmd_PodScope() {
    // 测试 pod scope
    ModelArgs modelArgs = new ModelArgs();
    modelArgs.setScope("pod");
    modelArgs.setTarget("cpu");
    modelArgs.setAction("load");

    String result = CliUtil.buildExpCmd("create", modelArgs);
    Assert.assertEquals("create k8s cpu load ", result);
  }

  @Test
  public void testBuildExpCmd_ContainerScope() {
    // 测试 container scope
    ModelArgs modelArgs = new ModelArgs();
    modelArgs.setScope("container");
    modelArgs.setTarget("cpu");
    modelArgs.setAction("load");

    String result = CliUtil.buildExpCmd("create", modelArgs);
    Assert.assertEquals("create k8s cpu load ", result);
  }

  @Test
  public void testBuildExpCmd_WindowsScope() {
    // 测试 windows scope
    ModelArgs modelArgs = new ModelArgs();
    modelArgs.setScope("windows");
    modelArgs.setTarget("cpu");
    modelArgs.setAction("load");

    String result = CliUtil.buildExpCmd("create", modelArgs);
    Assert.assertEquals("create windows cpu load ", result);
  }

  @Test
  public void testBuildExpCmd_WithFlags() {
    // 测试带 flags 的命令
    ModelArgs modelArgs = new ModelArgs();
    modelArgs.setTarget("cpu");
    modelArgs.setAction("load");

    Map<String, String> flags = new HashMap<>();
    flags.put("timeout", "60");
    flags.put("percent", "80");
    modelArgs.setFlags(flags);

    String result = CliUtil.buildExpCmd("create", modelArgs);
    Assert.assertTrue(result.contains("create cpu load "));
    Assert.assertTrue(result.contains("--timeout '60'"));
    Assert.assertTrue(result.contains("--percent '80'"));
  }

  @Test
  public void testBuildExpCmd_WithMatcherFlags() {
    // 测试带 matcherFlags 的命令
    ModelArgs modelArgs = new ModelArgs();
    modelArgs.setTarget("dubbo");
    modelArgs.setAction("delay");

    Map<String, String> matcherFlags = new HashMap<>();
    matcherFlags.put("service", "com.example.Service");
    matcherFlags.put("method", "testMethod");
    modelArgs.setMatcherFlags(matcherFlags);

    String result = CliUtil.buildExpCmd("create", modelArgs);
    Assert.assertTrue(result.contains("create dubbo delay "));
    Assert.assertTrue(result.contains("--service 'com.example.Service'"));
    Assert.assertTrue(result.contains("--method 'testMethod'"));
  }

  @Test
  public void testBuildExpCmd_WithFlagsAndMatcherFlags() {
    // 测试同时包含 flags 和 matcherFlags
    ModelArgs modelArgs = new ModelArgs();
    modelArgs.setTarget("dubbo");
    modelArgs.setAction("delay");

    Map<String, String> flags = new HashMap<>();
    flags.put("time", "1000");
    modelArgs.setFlags(flags);

    Map<String, String> matcherFlags = new HashMap<>();
    matcherFlags.put("service", "com.example.Service");
    modelArgs.setMatcherFlags(matcherFlags);

    String result = CliUtil.buildExpCmd("create", modelArgs);
    Assert.assertTrue(result.contains("create dubbo delay "));
    Assert.assertTrue(result.contains("--time '1000'"));
    Assert.assertTrue(result.contains("--service 'com.example.Service'"));
  }

  @Test
  public void testBuildExpCmd_WithScriptContent() {
    // 测试 script-content 参数（会被 base64 编码）
    ModelArgs modelArgs = new ModelArgs();
    modelArgs.setTarget("jvm");
    modelArgs.setAction("script");

    Map<String, String> flags = new HashMap<>();
    flags.put("script-content", "System.out.println(\"test\");");
    modelArgs.setFlags(flags);

    String result = CliUtil.buildExpCmd("create", modelArgs);
    Assert.assertTrue(result.contains("create jvm script "));
    Assert.assertTrue(result.contains("--script-content"));
    // script-content 应该被 base64 编码，所以不包含原始内容
    Assert.assertFalse(result.contains("System.out.println"));
  }

  @Test
  public void testBuildExpCmd_WithFilterScriptContent() {
    // 测试 filter-script-content 参数
    ModelArgs modelArgs = new ModelArgs();
    modelArgs.setTarget("jvm");
    modelArgs.setAction("script");

    Map<String, String> flags = new HashMap<>();
    flags.put("filter-script-content", "filter script");
    modelArgs.setFlags(flags);

    String result = CliUtil.buildExpCmd("create", modelArgs);
    Assert.assertTrue(result.contains("create jvm script "));
    Assert.assertTrue(result.contains("--filter-script-content"));
  }

  @Test
  public void testBuildExpCmd_IgnoreBlankFlags() {
    // 测试忽略空值和 false 值的 flags
    ModelArgs modelArgs = new ModelArgs();
    modelArgs.setTarget("cpu");
    modelArgs.setAction("load");

    Map<String, String> flags = new HashMap<>();
    flags.put("timeout", "");
    flags.put("percent", "false");
    flags.put("valid", "80");
    modelArgs.setFlags(flags);

    String result = CliUtil.buildExpCmd("create", modelArgs);
    Assert.assertFalse(result.contains("--timeout"));
    Assert.assertFalse(result.contains("--percent"));
    Assert.assertTrue(result.contains("--valid '80'"));
  }

  @Test
  public void testBuildExpCmd_IgnoreBlankMatcherFlags() {
    // 测试忽略空值和 false 值的 matcherFlags
    ModelArgs modelArgs = new ModelArgs();
    modelArgs.setTarget("dubbo");
    modelArgs.setAction("delay");

    Map<String, String> matcherFlags = new HashMap<>();
    matcherFlags.put("service", "");
    matcherFlags.put("method", "false");
    matcherFlags.put("valid", "testMethod");
    modelArgs.setMatcherFlags(matcherFlags);

    String result = CliUtil.buildExpCmd("create", modelArgs);
    Assert.assertFalse(result.contains("--service"));
    Assert.assertFalse(result.contains("--method"));
    Assert.assertTrue(result.contains("--valid 'testMethod'"));
  }

  @Test
  public void testBuildExpCmd_WindowsScopeWithFlags() {
    // 测试 windows scope 下的 flags（不使用单引号）
    ModelArgs modelArgs = new ModelArgs();
    modelArgs.setScope("windows");
    modelArgs.setTarget("cpu");
    modelArgs.setAction("load");

    Map<String, String> flags = new HashMap<>();
    flags.put("timeout", "60");
    modelArgs.setFlags(flags);

    String result = CliUtil.buildExpCmd("create", modelArgs);
    Assert.assertTrue(result.contains("create windows cpu load "));
    // Windows scope 不使用单引号
    Assert.assertTrue(result.contains("--timeout 60"));
    Assert.assertFalse(result.contains("'60'"));
  }

  @Test
  public void testBuildExpCmd_WindowsScopeWithScriptContent() {
    // 测试 windows scope 下的 script-content
    ModelArgs modelArgs = new ModelArgs();
    modelArgs.setScope("windows");
    modelArgs.setTarget("jvm");
    modelArgs.setAction("script");

    Map<String, String> flags = new HashMap<>();
    flags.put("script-content", "test script");
    modelArgs.setFlags(flags);

    String result = CliUtil.buildExpCmd("create", modelArgs);
    Assert.assertTrue(result.contains("create windows jvm script "));
    Assert.assertTrue(result.contains("--script-content"));
  }

  @Test
  public void testBuildExpCmd_CommandInjectionPrevention_Target() {
    // 测试命令注入防护 - target 参数
    ModelArgs modelArgs = new ModelArgs();
    modelArgs.setTarget("cpu ; echo rce > /tmp/rce_vul; echo");
    modelArgs.setAction("load");

    String result = CliUtil.buildExpCmd("create", modelArgs);
    // 危险字符应该被过滤掉（; 和 > 不在白名单中）
    Assert.assertFalse(result.contains(";"));
    Assert.assertFalse(result.contains(">"));
    // 命令分隔符被过滤后，无法形成有效的命令注入
    // 注意：trimCmdwithCh 只过滤字符，不会删除整个单词
    // 空格被过滤，所以 "echo rce" 变成 "echorce"，但关键的命令分隔符（; >）已被过滤
    Assert.assertFalse(result.contains("echo rce")); // 空格被过滤，无法形成命令
    // 注意："rce_vul" 中的字符都在白名单中（字母和下划线），可能被保留
    // 但关键的命令分隔符（; >）已被过滤，无法执行注入
    Assert.assertTrue(result.contains("create"));
    Assert.assertTrue(result.contains("cpu"));
    Assert.assertTrue(result.contains("load"));
  }

  @Test
  public void testBuildExpCmd_CommandInjectionPrevention_Action() {
    // 测试命令注入防护 - action 参数
    ModelArgs modelArgs = new ModelArgs();
    modelArgs.setTarget("cpu");
    modelArgs.setAction("load ; echo rce > /tmp/rce_vul; echo");

    String result = CliUtil.buildExpCmd("create", modelArgs);
    // 危险字符应该被过滤掉（; 和 > 不在白名单中）
    Assert.assertFalse(result.contains(";"));
    Assert.assertFalse(result.contains(">"));
    // 命令分隔符被过滤后，无法形成有效的命令注入
    Assert.assertFalse(result.contains("echo rce")); // 空格被过滤
    Assert.assertTrue(result.contains("create"));
    Assert.assertTrue(result.contains("cpu"));
    Assert.assertTrue(result.contains("load"));
    // 注意：纯字母 "rce" 可能保留，但无法执行命令注入（因为缺少分隔符）
  }

  @Test
  public void testBuildExpCmd_CommandInjectionPrevention_Flags() {
    // 测试命令注入防护 - flags 参数
    ModelArgs modelArgs = new ModelArgs();
    modelArgs.setTarget("cpu");
    modelArgs.setAction("load");

    Map<String, String> flags = new HashMap<>();
    flags.put("timeout", "60 ; echo rce > /tmp/rce_vul; echo");
    flags.put("percent", "80 && rm -rf /");
    modelArgs.setFlags(flags);

    String result = CliUtil.buildExpCmd("create", modelArgs);
    // 危险字符应该被过滤掉（; 和 > 不在白名单中）
    Assert.assertFalse(result.contains(";"));
    Assert.assertFalse(result.contains(">"));
    // 命令分隔符被过滤后，无法形成有效的命令注入
    // 注意：空格被过滤，所以 "echo rce" 无法形成完整命令
    Assert.assertFalse(result.contains("echo rce")); // 空格被过滤
    Assert.assertFalse(result.contains("rm -rf")); // 空格被过滤
    // 应该包含安全的数字部分
    Assert.assertTrue(result.contains("60"));
    Assert.assertTrue(result.contains("80"));
    // 注意：纯字母可能保留，但无法执行命令注入（因为缺少分隔符和空格）
  }

  @Test
  public void testBuildExpCmd_CommandInjectionPrevention_MatcherFlags() {
    // 测试命令注入防护 - matcherFlags 参数
    ModelArgs modelArgs = new ModelArgs();
    modelArgs.setTarget("dubbo");
    modelArgs.setAction("delay");

    Map<String, String> matcherFlags = new HashMap<>();
    matcherFlags.put("service", "com.example.Service ; echo rce");
    matcherFlags.put("method", "testMethod | wc -l");
    modelArgs.setMatcherFlags(matcherFlags);

    String result = CliUtil.buildExpCmd("create", modelArgs);
    // 危险字符应该被过滤掉（; 和 | 不在白名单中）
    Assert.assertFalse(result.contains(";"));
    Assert.assertFalse(result.contains("|"));
    // 命令分隔符被过滤后，无法形成有效的命令注入
    Assert.assertFalse(result.contains("echo rce")); // 空格被过滤
    Assert.assertFalse(result.contains("wc -l")); // 空格被过滤
    // 应该包含安全的部分
    Assert.assertTrue(result.contains("com.example.Service"));
    Assert.assertTrue(result.contains("testMethod"));
    // 注意：纯字母可能保留，但无法执行命令注入（因为缺少分隔符和空格）
  }

  @Test
  public void testBuildExpCmd_CommandInjectionPrevention_FlagKey() {
    // 测试命令注入防护 - flag key
    ModelArgs modelArgs = new ModelArgs();
    modelArgs.setTarget("cpu");
    modelArgs.setAction("load");

    Map<String, String> flags = new HashMap<>();
    flags.put("timeout; echo rce", "60");
    flags.put("percent && rm", "80");
    modelArgs.setFlags(flags);

    String result = CliUtil.buildExpCmd("create", modelArgs);
    // 危险字符应该被过滤掉（; 不在白名单中）
    Assert.assertFalse(result.contains(";"));
    // 命令分隔符被过滤后，无法形成有效的命令注入
    Assert.assertFalse(result.contains("echo rce")); // 空格被过滤
    // 注意："rm" 是纯字母，可能保留，但空格被过滤后 "rm -rf" 变成 "rmrf"，无法形成命令
    // 关键的命令分隔符（;）已被过滤，无法执行注入
    // 应该包含安全的 key 部分（过滤后的）
    Assert.assertTrue(result.contains("timeout"));
    Assert.assertTrue(result.contains("percent"));
    // 注意：& 是允许的字符，但 && 会被过滤为单个 &，空格被过滤，无法形成命令
  }

  @Test
  public void testBuildExpCmd_WithSpecialCharacters_Allowed() {
    // 测试允许的特殊字符（根据 trimCmdwithCh 实现）
    ModelArgs modelArgs = new ModelArgs();
    modelArgs.setTarget("test-target");
    modelArgs.setSubTarget("test_subtarget");
    modelArgs.setAction("test.action");

    Map<String, String> flags = new HashMap<>();
    flags.put("key_with_underscore", "value-with-dash");
    flags.put("key.with.dot", "value,with,comma");
    flags.put("key:with:colon", "value=with=equals");
    flags.put("key?with?question", "value+with+plus");
    flags.put("key&with&amp", "value/path");
    modelArgs.setFlags(flags);

    String result = CliUtil.buildExpCmd("create", modelArgs);
    // 允许的字符应该保留
    Assert.assertTrue(result.contains("test-target"));
    Assert.assertTrue(result.contains("test_subtarget"));
    Assert.assertTrue(result.contains("test.action"));
    Assert.assertTrue(result.contains("key_with_underscore"));
    Assert.assertTrue(result.contains("value-with-dash"));
    Assert.assertTrue(result.contains("key.with.dot"));
    Assert.assertTrue(result.contains("value,with,comma"));
  }

  @Test
  public void testBuildExpCmd_WithChineseCharacters() {
    // 测试中文字符（trimCmdwithCh 允许中文）
    ModelArgs modelArgs = new ModelArgs();
    modelArgs.setTarget("测试目标");
    modelArgs.setAction("测试动作");

    Map<String, String> flags = new HashMap<>();
    flags.put("测试键", "测试值");
    modelArgs.setFlags(flags);

    String result = CliUtil.buildExpCmd("create", modelArgs);
    // 中文字符应该保留
    Assert.assertTrue(result.contains("测试目标"));
    Assert.assertTrue(result.contains("测试动作"));
    Assert.assertTrue(result.contains("测试键"));
    Assert.assertTrue(result.contains("测试值"));
  }

  @Test
  public void testBuildExpCmd_WithNullAction() {
    // 测试 action 为 null
    ModelArgs modelArgs = new ModelArgs();
    modelArgs.setTarget("cpu");

    String result = CliUtil.buildExpCmd("create", modelArgs);
    Assert.assertEquals("create cpu ", result);
  }

  @Test
  public void testBuildExpCmd_WithNullSubTarget() {
    // 测试 subTarget 为 null
    ModelArgs modelArgs = new ModelArgs();
    modelArgs.setTarget("cpu");
    modelArgs.setAction("load");

    String result = CliUtil.buildExpCmd("create", modelArgs);
    Assert.assertEquals("create cpu load ", result);
  }

  @Test
  public void testBuildExpCmd_WithNullFlags() {
    // 测试 flags 为 null
    ModelArgs modelArgs = new ModelArgs();
    modelArgs.setTarget("cpu");
    modelArgs.setAction("load");

    String result = CliUtil.buildExpCmd("create", modelArgs);
    Assert.assertEquals("create cpu load ", result);
  }

  @Test
  public void testBuildExpCmd_WithNullMatcherFlags() {
    // 测试 matcherFlags 为 null
    ModelArgs modelArgs = new ModelArgs();
    modelArgs.setTarget("cpu");
    modelArgs.setAction("load");

    Map<String, String> flags = new HashMap<>();
    flags.put("timeout", "60");
    modelArgs.setFlags(flags);

    String result = CliUtil.buildExpCmd("create", modelArgs);
    Assert.assertTrue(result.contains("create cpu load "));
    Assert.assertTrue(result.contains("--timeout '60'"));
  }

  @Test
  public void testBuildExpCmd_WithEmptyFlags() {
    // 测试空 flags map
    ModelArgs modelArgs = new ModelArgs();
    modelArgs.setTarget("cpu");
    modelArgs.setAction("load");
    modelArgs.setFlags(new HashMap<>());

    String result = CliUtil.buildExpCmd("create", modelArgs);
    Assert.assertEquals("create cpu load ", result);
  }

  @Test
  public void testBuildExpCmd_WithEmptyMatcherFlags() {
    // 测试空 matcherFlags map
    ModelArgs modelArgs = new ModelArgs();
    modelArgs.setTarget("cpu");
    modelArgs.setAction("load");
    modelArgs.setMatcherFlags(new HashMap<>());

    String result = CliUtil.buildExpCmd("create", modelArgs);
    Assert.assertEquals("create cpu load ", result);
  }

  @Test
  public void testBuildExpCmd_ComplexScenario() {
    // 测试复杂场景：包含所有参数
    ModelArgs modelArgs = new ModelArgs();
    modelArgs.setTarget("dubbo");
    modelArgs.setSubTarget("consumer");
    modelArgs.setAction("delay");

    Map<String, String> flags = new HashMap<>();
    flags.put("time", "1000");
    flags.put("offset", "100");
    modelArgs.setFlags(flags);

    Map<String, String> matcherFlags = new HashMap<>();
    matcherFlags.put("service", "com.example.Service");
    matcherFlags.put("method", "testMethod");
    matcherFlags.put("version", "1.0.0");
    modelArgs.setMatcherFlags(matcherFlags);

    String result = CliUtil.buildExpCmd("create", modelArgs);
    Assert.assertTrue(result.contains("create dubbo consumer delay "));
    Assert.assertTrue(result.contains("--time '1000'"));
    Assert.assertTrue(result.contains("--offset '100'"));
    Assert.assertTrue(result.contains("--service 'com.example.Service'"));
    Assert.assertTrue(result.contains("--method 'testMethod'"));
    Assert.assertTrue(result.contains("--version '1.0.0'"));
  }

  @Test
  public void testBuildExpCmd_WithBladeConstant() {
    // 测试使用 Blade.CREATE 常量
    ModelArgs modelArgs = new ModelArgs();
    modelArgs.setTarget("cpu");
    modelArgs.setAction("load");

    String result = CliUtil.buildExpCmd(Blade.CREATE, modelArgs);
    Assert.assertTrue(result.startsWith("create"));
    Assert.assertTrue(result.contains("cpu"));
    Assert.assertTrue(result.contains("load"));
  }

  @Test
  public void testBuildExpCmd_TrimWhitespace() {
    // 测试参数中的空格被正确处理
    ModelArgs modelArgs = new ModelArgs();
    modelArgs.setTarget("  cpu  ");
    modelArgs.setAction("  load  ");

    Map<String, String> flags = new HashMap<>();
    flags.put("timeout", "  60  ");
    modelArgs.setFlags(flags);

    String result = CliUtil.buildExpCmd("create", modelArgs);
    // trimCmdwithCh 会过滤掉空格，但 trim() 会处理前后的空格
    Assert.assertTrue(result.contains("cpu"));
    Assert.assertTrue(result.contains("load"));
    Assert.assertTrue(result.contains("60"));
  }

  @Test
  public void testBuildExpCmd_WithUrlLikeValue() {
    // 测试类似 URL 的值（包含 : 和 /）
    ModelArgs modelArgs = new ModelArgs();
    modelArgs.setTarget("http");
    modelArgs.setAction("delay");

    Map<String, String> flags = new HashMap<>();
    flags.put("url", "http://example.com/path");
    modelArgs.setFlags(flags);

    String result = CliUtil.buildExpCmd("create", modelArgs);
    // 根据 trimCmdwithCh 的当前实现，: 和 / 应该被允许
    Assert.assertTrue(result.contains("http://example.com/path"));
  }

  @Test
  public void testBuildExpCmd_CaseInsensitiveScope() {
    // 测试 scope 大小写不敏感
    ModelArgs modelArgs1 = new ModelArgs();
    modelArgs1.setScope("NODE");
    modelArgs1.setTarget("cpu");
    modelArgs1.setAction("load");

    ModelArgs modelArgs2 = new ModelArgs();
    modelArgs2.setScope("node");
    modelArgs2.setTarget("cpu");
    modelArgs2.setAction("load");

    String result1 = CliUtil.buildExpCmd("create", modelArgs1);
    String result2 = CliUtil.buildExpCmd("create", modelArgs2);

    Assert.assertEquals(result1, result2);
    Assert.assertTrue(result1.contains("k8s"));
  }
}
