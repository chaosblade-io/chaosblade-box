package com.alibaba.chaosblade.platform.service.test.scene;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileReader;
import com.alibaba.chaosblade.platform.service.SceneService;
import com.alibaba.chaosblade.platform.metric.ChaosPlatformApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.BufferedInputStream;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ChaosPlatformApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LoadFormYamlSceneTest {

    @Autowired
    private SceneService sceneService;

    @Test
    public void testInput() {
        BufferedInputStream inputStream = FileReader.create(FileUtil.file("chaosblade-jvm-spec-0.9.0.yaml")).getInputStream();
        sceneService.inputScene(inputStream);
    }
}
