/*
 * Copyright 1999-2021 Alibaba Group Holding Ltd.
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

package com.alibaba.chaosblade.box.web.controller;

import com.alibaba.chaosblade.box.scenario.api.init.SceneParamComponentLoader;
import com.alibaba.chaosblade.box.service.ClusterService;
import com.alibaba.chaosblade.box.service.K8SToolsService;
import com.alibaba.chaosblade.box.web.ChaosbladeBoxApplication;
import com.alibaba.chaosblade.box.web.model.Response;
import com.alibaba.chaosblade.box.web.model.SystemInfoRequest;
import com.alibaba.chaosblade.box.web.model.SystemInfoResponse;
import com.alibaba.chaosblade.box.common.utils.JsonUtils;
import com.alibaba.chaosblade.box.metric.init.MetricCateGoryLoader;
import com.alibaba.chaosblade.box.scenario.api.init.SceneCategoryLoader;
import com.alibaba.chaosblade.box.scenario.api.model.ToolsOverview;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.representer.Representer;

import java.io.ByteArrayInputStream;

/**
 * @author yefei
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ChaosbladeBoxApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MainControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;

    @MockBean
    private SceneCategoryLoader sceneCategoryLoader;

    @MockBean
    private SceneParamComponentLoader sceneParamComponentLoader;

    @MockBean
    private MetricCateGoryLoader metricCateGoryLoader;

    @MockBean
    private ClusterService clusterService;

    @MockBean
    private K8SToolsService k8SToolsService;

    @Before
    public void init() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void testSystemInfo() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/SystemInfo")
                .contentType(MediaType.APPLICATION_JSON)
        ).andReturn();
        MockHttpServletResponse httpServletResponse = mvcResult.getResponse();
        Assert.assertEquals(httpServletResponse.getStatus(), 200);

        Response<SystemInfoResponse> response = JsonUtils.readValue(
                new TypeReference<Response<SystemInfoResponse>>() {
                },
                httpServletResponse.getContentAsByteArray());

        Assert.assertEquals(response.getData().getVersion(), "0.4.2");
    }

    @Test
    public void testI18n() throws Exception {
        SystemInfoRequest systemInfoRequest = new SystemInfoRequest();
        systemInfoRequest.setLocale("en");
        mockMvc.perform(MockMvcRequestBuilders
                .post("/api/I18n")
                .content(JsonUtils.writeValueAsBytes(systemInfoRequest))
                .contentType(MediaType.APPLICATION_JSON)
        ).andReturn();


        Assert.assertEquals(LocaleContextHolder.getLocale().toLanguageTag(), "en");
    }

    @Test
    public void testFetchPublicChaostools() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/FetchPublicChaostools")
                .contentType(MediaType.APPLICATION_JSON)
        ).andReturn();
        MockHttpServletResponse httpServletResponse = mvcResult.getResponse();
        Assert.assertEquals(httpServletResponse.getStatus(), 200);

        String contentAsString = httpServletResponse.getContentAsString();
        Assert.assertTrue(contentAsString.contains("chaosblade"));
    }

    @Test
    public void testFetchChaostoolsOverview() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/FetchChaostoolsOverview/chaosblade/overview.yaml")
                .contentType(MediaType.APPLICATION_JSON)
        ).andReturn();
        MockHttpServletResponse httpServletResponse = mvcResult.getResponse();
        Assert.assertEquals(httpServletResponse.getStatus(), 200);
        Representer representer = new Representer();
        representer.getPropertyUtils().setSkipMissingProperties(true);
        Yaml yaml = new Yaml(representer);
        ToolsOverview toolsOverview = yaml.loadAs(new ByteArrayInputStream(httpServletResponse.getContentAsByteArray()), ToolsOverview.class);

        Assert.assertEquals(toolsOverview.getName(), "chaosblade");
    }
}
