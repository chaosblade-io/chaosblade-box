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

package com.alibaba.chaosblade.platform.web.controller;

import com.alibaba.chaosblade.platform.cmmon.utils.JsonUtils;
import com.alibaba.chaosblade.platform.service.DeviceService;
import com.alibaba.chaosblade.platform.service.model.device.DeviceRegisterRequest;
import com.alibaba.chaosblade.platform.web.ChaosPlatformApplication;
import com.alibaba.chaosblade.platform.web.model.Response;
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
@SpringBootTest(classes = ChaosPlatformApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AgentReportControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;

    @Before
    public void init() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @MockDiagnose(LogLevel.ENABLE)
    public static class Mock {

        @MockMethod(scope = MockScope.ASSOCIATED)
        private void deviceRegister(DeviceService self, DeviceRegisterRequest deviceRegisterRequest) {
            log.info("mock device register");
        }
    }

    @Test
    public void testAgentReport() throws Exception {

        DeviceRegisterRequest request = DeviceRegisterRequest.builder().build();

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .post("/chaos/AgentRegister")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtils.writeValueAsBytes(request))
        ).andReturn();

        MockHttpServletResponse httpServletResponse = mvcResult.getResponse();
        Response<?> response = JsonUtils.readValue(Response.class, httpServletResponse.getContentAsByteArray());
        Assert.assertEquals(httpServletResponse.getStatus(), 200);
        Assert.assertTrue(response.isSuccess());
    }

}

