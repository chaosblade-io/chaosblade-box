package com.alibaba.chaosblade.box.service.impl;

import com.alibaba.chaosblade.box.common.common.domain.Response;
import com.alibaba.chaosblade.box.common.common.domain.user.ChaosUser;
import com.alibaba.chaosblade.box.dao.model.LoadTestDefinitionDO;
import com.alibaba.chaosblade.box.dao.model.base.PageableResponse;
import com.alibaba.chaosblade.box.dao.repository.LoadTestDefinitionRepository;
import com.alibaba.chaosblade.box.service.model.loadtest.LoadTestDefinitionCreateRequest;
import com.alibaba.chaosblade.box.service.model.loadtest.LoadTestDefinitionQueryRequest;
import com.alibaba.chaosblade.box.service.model.loadtest.LoadTestDefinitionUpdateRequest;
import com.alibaba.chaosblade.box.service.model.loadtest.LoadTestDefinitionVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * LoadTestDefinitionServiceImpl 单元测试
 *
 * @author ZhengMingZhuo
 */
@ExtendWith(MockitoExtension.class)
class LoadTestDefinitionServiceImplTest {

    @Mock
    private LoadTestDefinitionRepository loadTestDefinitionRepository;

    @InjectMocks
    private LoadTestDefinitionServiceImpl loadTestDefinitionService;

    private ChaosUser testUser;
    private LoadTestDefinitionDO testDefinitionDO;
    private LoadTestDefinitionCreateRequest createRequest;

    @BeforeEach
    void setUp() {
        // 初始化测试用户
        testUser = new ChaosUser();
        testUser.setUserId("test-user-id");
        testUser.setUserName("test-user");

        // 初始化测试数据
        testDefinitionDO = new LoadTestDefinitionDO();
        testDefinitionDO.setDefinitionId("test-definition-id");
        testDefinitionDO.setName("Test Load Definition");
        testDefinitionDO.setEngineType("JMETER");
        testDefinitionDO.setEndpoint("http://example.com");
        testDefinitionDO.setEntry("URL");
        testDefinitionDO.setCreatedBy("test-user");
        testDefinitionDO.setUserId("test-user-id");
        testDefinitionDO.setNamespace("default");
        testDefinitionDO.setIsDelete(0);
        testDefinitionDO.setGmtCreate(new Date());
        testDefinitionDO.setGmtModified(new Date());

        // 初始化创建请求
        createRequest = new LoadTestDefinitionCreateRequest();
        createRequest.setName("Test Load Definition");
        createRequest.setEngineType("JMETER");
        createRequest.setEndpoint("http://example.com");
        createRequest.setEntry("URL");
        createRequest.setNamespace("default");
        createRequest.setUser(testUser);
    }

    @Test
    void testCreateLoadTestDefinition_Success() {
        // Given
        when(loadTestDefinitionRepository.findByName(anyString(), anyString(), anyString()))
                .thenReturn(Optional.empty());
        when(loadTestDefinitionRepository.add(any(LoadTestDefinitionDO.class)))
                .thenReturn(true);

        // When
        Response<String> response = loadTestDefinitionService.createLoadTestDefinition(createRequest);

        // Then
        assertTrue(response.isSuccess());
        assertNotNull(response.getResult());
        assertTrue(response.getResult().startsWith("ldef_"));
        verify(loadTestDefinitionRepository).findByName(eq("Test Load Definition"), eq("test-user-id"), eq("default"));
        verify(loadTestDefinitionRepository).add(any(LoadTestDefinitionDO.class));
    }

    @Test
    void testCreateLoadTestDefinition_DuplicateName() {
        // Given
        when(loadTestDefinitionRepository.findByName(anyString(), anyString(), anyString()))
                .thenReturn(Optional.of(testDefinitionDO));

        // When
        Response<String> response = loadTestDefinitionService.createLoadTestDefinition(createRequest);

        // Then
        assertFalse(response.isSuccess());
        verify(loadTestDefinitionRepository).findByName(eq("Test Load Definition"), eq("test-user-id"), eq("default"));
        verify(loadTestDefinitionRepository, never()).add(any(LoadTestDefinitionDO.class));
    }

    @Test
    void testCreateLoadTestDefinition_UserNotLogin() {
        // Given
        createRequest.setUser(null);

        // When
        Response<String> response = loadTestDefinitionService.createLoadTestDefinition(createRequest);

        // Then
        assertFalse(response.isSuccess());
        verify(loadTestDefinitionRepository, never()).findByName(anyString(), anyString(), anyString());
        verify(loadTestDefinitionRepository, never()).add(any(LoadTestDefinitionDO.class));
    }

    @Test
    void testUpdateLoadTestDefinition_Success() {
        // Given
        LoadTestDefinitionUpdateRequest updateRequest = new LoadTestDefinitionUpdateRequest();
        updateRequest.setId("test-definition-id");
        updateRequest.setName("Updated Name");
        updateRequest.setUser(testUser);
        updateRequest.setNamespace("default");

        when(loadTestDefinitionRepository.findById("test-definition-id"))
                .thenReturn(Optional.of(testDefinitionDO));
        when(loadTestDefinitionRepository.findByName("Updated Name", "test-user-id", "default"))
                .thenReturn(Optional.empty());
        when(loadTestDefinitionRepository.update(any(LoadTestDefinitionDO.class)))
                .thenReturn(true);

        // When
        Response<Void> response = loadTestDefinitionService.updateLoadTestDefinition(updateRequest);

        // Then
        assertTrue(response.isSuccess());
        verify(loadTestDefinitionRepository).findById("test-definition-id");
        verify(loadTestDefinitionRepository).update(any(LoadTestDefinitionDO.class));
    }

    @Test
    void testDeleteLoadTestDefinition_Success() {
        // Given
        when(loadTestDefinitionRepository.findById("test-definition-id"))
                .thenReturn(Optional.of(testDefinitionDO));
        when(loadTestDefinitionRepository.deleteById("test-definition-id"))
                .thenReturn(true);

        // When
        Response<Void> response = loadTestDefinitionService.deleteLoadTestDefinition(
                "test-definition-id", "test-user-id", "default");

        // Then
        assertTrue(response.isSuccess());
        verify(loadTestDefinitionRepository).findById("test-definition-id");
        verify(loadTestDefinitionRepository).deleteById("test-definition-id");
    }

    @Test
    void testGetLoadTestDefinition_Success() {
        // Given
        when(loadTestDefinitionRepository.findById("test-definition-id"))
                .thenReturn(Optional.of(testDefinitionDO));

        // When
        Response<LoadTestDefinitionVO> response = loadTestDefinitionService.getLoadTestDefinition(
                "test-definition-id", "test-user-id", "default");

        // Then
        assertTrue(response.isSuccess());
        assertNotNull(response.getResult());
        assertEquals("test-definition-id", response.getResult().getId());
        assertEquals("Test Load Definition", response.getResult().getName());
        verify(loadTestDefinitionRepository).findById("test-definition-id");
    }

    @Test
    void testQueryLoadTestDefinitions_Success() {
        // Given
        LoadTestDefinitionQueryRequest queryRequest = new LoadTestDefinitionQueryRequest();
        queryRequest.setPageNum(1);
        queryRequest.setPageSize(10);
        queryRequest.setUser(testUser);
        queryRequest.setNamespace("default");

        List<LoadTestDefinitionDO> definitions = Arrays.asList(testDefinitionDO);
        PageableResponse<LoadTestDefinitionDO> pageResult = PageableResponse.of(1, 10, definitions)
                .pages(1)
                .total(1);

        when(loadTestDefinitionRepository.findByPage(1, 10, "test-user-id", "default", null, null))
                .thenReturn(pageResult);

        // When
        Response<PageableResponse<LoadTestDefinitionVO>> response = 
                loadTestDefinitionService.queryLoadTestDefinitions(queryRequest);

        // Then
        assertTrue(response.isSuccess());
        assertNotNull(response.getResult());
        assertEquals(1, response.getResult().getData().size());
        verify(loadTestDefinitionRepository).findByPage(1, 10, "test-user-id", "default", null, null);
    }

    @Test
    void testListAllLoadTestDefinitions_Success() {
        // Given
        List<LoadTestDefinitionDO> definitions = Arrays.asList(testDefinitionDO);
        when(loadTestDefinitionRepository.findAll("test-user-id", "default"))
                .thenReturn(definitions);

        // When
        Response<List<LoadTestDefinitionVO>> response = 
                loadTestDefinitionService.listAllLoadTestDefinitions("test-user-id", "default");

        // Then
        assertTrue(response.isSuccess());
        assertNotNull(response.getResult());
        assertEquals(1, response.getResult().size());
        assertEquals("test-definition-id", response.getResult().get(0).getId());
        verify(loadTestDefinitionRepository).findAll("test-user-id", "default");
    }
}
