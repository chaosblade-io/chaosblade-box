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

import cn.hutool.core.collection.CollUtil;
import com.alibaba.chaosblade.box.service.model.device.*;
import com.alibaba.chaosblade.box.web.ChaosbladeBoxApplication;
import com.alibaba.chaosblade.box.common.utils.JsonUtils;
import com.alibaba.chaosblade.box.dao.page.PageQuery;
import com.alibaba.chaosblade.box.dao.page.PageUtils;
import com.alibaba.chaosblade.box.metric.init.MetricCateGoryLoader;
import com.alibaba.chaosblade.box.scenario.api.init.SceneCategoryLoader;
import com.alibaba.chaosblade.box.service.DeviceService;
import com.alibaba.chaosblade.box.web.model.PageResponse;
import com.alibaba.chaosblade.box.web.model.Response;
import com.alibaba.testable.core.annotation.MockDiagnose;
import com.alibaba.testable.core.annotation.MockMethod;
import com.alibaba.testable.core.model.LogLevel;
import com.alibaba.testable.core.model.MockScope;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

/**
 * @author yefei
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ChaosbladeBoxApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MachineControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;

    @MockBean
    private SceneCategoryLoader sceneCategoryLoader;

    @MockBean
    private MetricCateGoryLoader metricCateGoryLoader;

    @Before
    public void init() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @MockDiagnose(LogLevel.ENABLE)
    public static class Mock {

        @MockMethod(scope = MockScope.ASSOCIATED)
        private List<DeviceResponse> getMachinesForHost(
                DeviceService self,
                DeviceRequest deviceRequest) {
            log.info("mock getMachinesForHost");
            return CollUtil.newArrayList(DeviceResponse.builder().build());
        }

        @MockMethod(scope = MockScope.ASSOCIATED)
        private List<DeviceNodeResponse> getMachinesForNode(
                DeviceService self,
                DeviceNodeRequest deviceNodeRequest) {
            log.info("mock getMachinesForHost");
            return CollUtil.newArrayList(DeviceNodeResponse.builder().build());
        }

        @MockMethod(scope = MockScope.ASSOCIATED)
        private List<DevicePodResponse> getMachinesForPod(DeviceService self,
                                                          DevicePodRequest devicePodRequest) {
            log.info("mock getMachinesForPod");
            return CollUtil.newArrayList(new DevicePodResponse());
        }
    }

    @Test
    public void testGetMachinesForHost() throws Exception {

        DeviceRequest request = DeviceRequest.builder()
                .original("host")
                .build();

        PageUtils.startPage(new PageQuery());
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .post("/api/GetMachinesForHost")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtils.writeValueAsBytes(request))
        ).andReturn();

        MockHttpServletResponse httpServletResponse = mvcResult.getResponse();
        Response<PageResponse<List<DeviceResponse>>> response = JsonUtils.readValue(
                new TypeReference<Response<PageResponse<List<DeviceResponse>>>>() {
                },
                httpServletResponse.getContentAsByteArray());
        Assert.assertEquals(httpServletResponse.getStatus(), 200);
        Assert.assertEquals(response.getData().getData().size(), 1);
        Assert.assertEquals(response.getData().getOriginal(), "host");
    }

    @Test
    public void testGetMachinesForNodePageable() throws Exception {

        DeviceNodeRequest request = DeviceNodeRequest.builder()
                .original("node")
                .build();

        PageUtils.startPage(new PageQuery());
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .post("/api/GetMachinesForNodePageable")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtils.writeValueAsBytes(request))
        ).andReturn();

        MockHttpServletResponse httpServletResponse = mvcResult.getResponse();
        Response<PageResponse<List<DeviceNodeResponse>>> response = JsonUtils.readValue(
                new TypeReference<Response<PageResponse<List<DeviceNodeResponse>>>>() {
                },
                httpServletResponse.getContentAsByteArray());
        Assert.assertEquals(httpServletResponse.getStatus(), 200);
        Assert.assertEquals(response.getData().getData().size(), 1);
        Assert.assertEquals(response.getData().getOriginal(), "node");
    }

    @Test
    public void testGetMachinesForPodPageable() throws Exception {

        DevicePodRequest request = DevicePodRequest.builder()
                .original("pod")
                .build();

        PageUtils.startPage(new PageQuery());
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .post("/api/GetMachinesForPodPageable")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtils.writeValueAsBytes(request))
        ).andReturn();

        MockHttpServletResponse httpServletResponse = mvcResult.getResponse();
        Response<PageResponse<List<DevicePodResponse>>> response = JsonUtils.readValue(
                new TypeReference<Response<PageResponse<List<DevicePodResponse>>>>() {
                },
                httpServletResponse.getContentAsByteArray());
        Assert.assertEquals(httpServletResponse.getStatus(), 200);
        Assert.assertEquals(response.getData().getData().size(), 1);
        Assert.assertEquals(response.getData().getOriginal(), "pod");
    }
}
