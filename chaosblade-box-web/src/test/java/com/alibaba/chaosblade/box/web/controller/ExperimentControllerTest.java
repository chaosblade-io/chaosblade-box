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

import com.alibaba.chaosblade.box.service.ClusterService;
import com.alibaba.chaosblade.box.service.K8SToolsService;
import com.alibaba.chaosblade.box.web.ChaosbladeBoxApplication;
import com.alibaba.chaosblade.box.common.utils.JsonUtils;
import com.alibaba.chaosblade.box.metric.init.MetricCateGoryLoader;
import com.alibaba.chaosblade.box.scenario.api.init.SceneCategoryLoader;
import com.alibaba.chaosblade.box.service.ExperimentService;
import com.alibaba.chaosblade.box.service.model.experiment.CreateExperimentRequest;
import com.alibaba.chaosblade.box.service.model.experiment.ExperimentRequest;
import com.alibaba.chaosblade.box.service.model.experiment.ExperimentResponse;
import com.alibaba.chaosblade.box.web.model.Response;
import com.alibaba.testable.core.annotation.MockDiagnose;
import com.alibaba.testable.core.annotation.MockMethod;
import com.alibaba.testable.core.model.LogLevel;
import com.alibaba.testable.core.model.MockScope;
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

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ChaosbladeBoxApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ExperimentControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;

    @MockBean
    private SceneCategoryLoader sceneCategoryLoader;

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

    @MockDiagnose(LogLevel.ENABLE)
    public static class Mock {

        @MockMethod(scope = MockScope.ASSOCIATED)
        private ExperimentResponse createExperiment(ExperimentService self, CreateExperimentRequest createExperimentRequest) {
            log.info("mock create experiment");
            return ExperimentResponse.builder().build();
        }

        @MockMethod(scope = MockScope.ASSOCIATED)
        private void deleteExperiment(ExperimentService self, ExperimentRequest experimentRequest) throws Exception {
            log.info("mock delete experiment");
        }
    }

    @Test
    public void testCreateExperiment() throws Exception {

        CreateExperimentRequest request = new CreateExperimentRequest();
        request.setExperimentName("cpu-load");
        request.setScenarioId(0L);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .post("/api/CreateExperiment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtils.writeValueAsBytes(request))
        ).andReturn();

        MockHttpServletResponse httpServletResponse = mvcResult.getResponse();
        Response<?> response = JsonUtils.readValue(Response.class, httpServletResponse.getContentAsByteArray());
        Assert.assertEquals(httpServletResponse.getStatus(), 200);
        Assert.assertTrue(response.isSuccess());
    }

    @Test
    public void testDeleteExperiment() throws Exception {

        ExperimentRequest request = new ExperimentRequest();
        request.setExperimentId(0L);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .post("/api/DeleteExperiment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtils.writeValueAsBytes(request))
        ).andReturn();

        MockHttpServletResponse httpServletResponse = mvcResult.getResponse();
        Response<?> response = JsonUtils.readValue(Response.class, httpServletResponse.getContentAsByteArray());
        Assert.assertEquals(httpServletResponse.getStatus(), 200);
        Assert.assertTrue(response.isSuccess());
    }

    @Test
    public void testDeleteExperimentError() throws Exception {

        ExperimentRequest request = new ExperimentRequest();

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .post("/api/DeleteExperiment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtils.writeValueAsBytes(request))
        ).andReturn();

        MockHttpServletResponse httpServletResponse = mvcResult.getResponse();
        Response<?> response = JsonUtils.readValue(Response.class, httpServletResponse.getContentAsByteArray());
        Assert.assertEquals(String.valueOf(response.getCode()), "6001");
        Assert.assertTrue(!response.isSuccess());
    }

}

