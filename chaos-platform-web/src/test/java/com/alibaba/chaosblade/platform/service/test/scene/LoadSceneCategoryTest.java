package com.alibaba.chaosblade.platform.service.test.scene;

import com.alibaba.chaosblade.platform.cmmon.utils.JsonUtils;
import com.alibaba.chaosblade.platform.metric.ChaosPlatformApplication;
import com.alibaba.chaosblade.platform.service.SceneCategoryService;
import com.alibaba.chaosblade.platform.service.model.scene.categroy.SceneCategoryResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ChaosPlatformApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LoadSceneCategoryTest {

    @Autowired
    private SceneCategoryService sceneCategoryService;

    @Test
    public void testInput() throws Exception {
        String str = " [\n" +
                "      {\n" +
                "        \"children\": [\n" +
                "          {\n" +
                "            \"level\": 2,\n" +
                "            \"supportScopeTypes\": [\n" +
                "              \"host\",\n" +
                "              \"kubernetes\"\n" +
                "            ],\n" +
                "            \"name\": \"CPU资源\",\n" +
                "            \"categoryId\": \"1216606329818566658\",\n" +
                "            \"parentId\": \"1216606260205703169\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"level\": 2,\n" +
                "            \"supportScopeTypes\": [\n" +
                "              \"host\",\n" +
                "              \"kubernetes\"\n" +
                "            ],\n" +
                "            \"name\": \"内存资源\",\n" +
                "            \"categoryId\": \"1216606392489857026\",\n" +
                "            \"parentId\": \"1216606260205703169\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"level\": 2,\n" +
                "            \"supportScopeTypes\": [\n" +
                "              \"host\",\n" +
                "              \"kubernetes\"\n" +
                "            ],\n" +
                "            \"name\": \"磁盘资源\",\n" +
                "            \"categoryId\": \"1216606480226308098\",\n" +
                "            \"parentId\": \"1216606260205703169\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"level\": 2,\n" +
                "            \"supportScopeTypes\": [\n" +
                "              \"host\",\n" +
                "              \"kubernetes\"\n" +
                "            ],\n" +
                "            \"name\": \"网络资源\",\n" +
                "            \"categoryId\": \"1216672245176541185\",\n" +
                "            \"parentId\": \"1216606260205703169\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"level\": 2,\n" +
                "            \"supportScopeTypes\": [\n" +
                "              \"host\",\n" +
                "              \"kubernetes\"\n" +
                "            ],\n" +
                "            \"name\": \"应用进程\",\n" +
                "            \"categoryId\": \"1217020049010950145\",\n" +
                "            \"parentId\": \"1216606260205703169\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"level\": 2,\n" +
                "            \"supportScopeTypes\": [\n" +
                "              \"host\",\n" +
                "              \"kubernetes\"\n" +
                "            ],\n" +
                "            \"name\": \"容器资源\",\n" +
                "            \"categoryId\": \"1217716899703644162\",\n" +
                "            \"parentId\": \"1216606260205703169\"\n" +
                "          }\n" +
                "        ],\n" +
                "        \"level\": 1,\n" +
                "        \"supportScopeTypes\": [\n" +
                "          \"host\",\n" +
                "          \"kubernetes\"\n" +
                "        ],\n" +
                "        \"name\": \"系统资源\",\n" +
                "        \"categoryId\": \"1216606260205703169\",\n" +
                "        \"parentId\": \"\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"children\": [\n" +
                "          {\n" +
                "            \"level\": 2,\n" +
                "            \"supportScopeTypes\": [\n" +
                "              \"host\",\n" +
                "              \"kubernetes\"\n" +
                "            ],\n" +
                "            \"name\": \"延迟\",\n" +
                "            \"categoryId\": \"1216606744870113281\",\n" +
                "            \"parentId\": \"1216606670115033089\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"level\": 2,\n" +
                "            \"supportScopeTypes\": [\n" +
                "              \"host\",\n" +
                "              \"kubernetes\"\n" +
                "            ],\n" +
                "            \"name\": \"抛异常\",\n" +
                "            \"categoryId\": \"1216606820073984002\",\n" +
                "            \"parentId\": \"1216606670115033089\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"level\": 2,\n" +
                "            \"supportScopeTypes\": [\n" +
                "              \"host\",\n" +
                "              \"kubernetes\"\n" +
                "            ],\n" +
                "            \"name\": \"自定义故障\",\n" +
                "            \"categoryId\": \"1216606920988938241\",\n" +
                "            \"parentId\": \"1216606670115033089\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"level\": 2,\n" +
                "            \"supportScopeTypes\": [\n" +
                "              \"host\",\n" +
                "              \"kubernetes\"\n" +
                "            ],\n" +
                "            \"name\": \"篡改数据\",\n" +
                "            \"categoryId\": \"1216669321109118978\",\n" +
                "            \"parentId\": \"1216606670115033089\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"children\": [\n" +
                "              {\n" +
                "                \"level\": 3,\n" +
                "                \"supportScopeTypes\": [\n" +
                "                  \"host\",\n" +
                "                  \"kubernetes\"\n" +
                "                ],\n" +
                "                \"name\": \"CPU资源\",\n" +
                "                \"categoryId\": \"1217023924078092289\",\n" +
                "                \"parentId\": \"1217022989201276929\"\n" +
                "              },\n" +
                "              {\n" +
                "                \"level\": 3,\n" +
                "                \"supportScopeTypes\": [\n" +
                "                  \"host\",\n" +
                "                  \"kubernetes\"\n" +
                "                ],\n" +
                "                \"name\": \"内存资源\",\n" +
                "                \"categoryId\": \"1217023981502308353\",\n" +
                "                \"parentId\": \"1217022989201276929\"\n" +
                "              }\n" +
                "            ],\n" +
                "            \"level\": 2,\n" +
                "            \"supportScopeTypes\": [\n" +
                "              \"host\",\n" +
                "              \"kubernetes\"\n" +
                "            ],\n" +
                "            \"name\": \"资源占用\",\n" +
                "            \"categoryId\": \"1217022989201276929\",\n" +
                "            \"parentId\": \"1216606670115033089\"\n" +
                "          }\n" +
                "        ],\n" +
                "        \"level\": 1,\n" +
                "        \"supportScopeTypes\": [\n" +
                "          \"host\",\n" +
                "          \"kubernetes\"\n" +
                "        ],\n" +
                "        \"name\": \"JAVA应用\",\n" +
                "        \"categoryId\": \"1216606670115033089\",\n" +
                "        \"parentId\": \"\"\n" +
                "      }\n" +
                "    ]";

        List<SceneCategoryResponse> o = JsonUtils.reader(new TypeReference<List<SceneCategoryResponse>>() {
        }).readValue(str);
        sceneCategoryService.importSceneCategory(o);
    }
}
