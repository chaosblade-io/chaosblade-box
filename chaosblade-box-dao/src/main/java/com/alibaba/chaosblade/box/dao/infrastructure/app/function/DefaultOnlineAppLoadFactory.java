package com.alibaba.chaosblade.box.dao.infrastructure.app.function;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;


/**
 * 默认上架的小程序
 *
 * @author sunpeng
 * 
 *
 */
@Slf4j
@Component
public class DefaultOnlineAppLoadFactory implements InitializingBean {

    private static final String LOAD_PATH = "scene/default_online_functions.json";

    private final static ObjectMapper mapper = new ObjectMapper();

    private List<String> functionMap = new ArrayList<>();

    private static List<String> getList(String path) throws IOException {
        List<String> result = new ArrayList<>();
        Resource[] resources =
                new PathMatchingResourcePatternResolver()
                        .getResources(ResourceUtils.CLASSPATH_URL_PREFIX + "scene/default_online_functions.json");
        log.info("[PayPackLoadFactory] res count : {}", resources.length);
        for(Resource resource : resources) {
            log.info("[PayPackLoadFactory] res name : {}", resource.getFilename());
            result.add(readJsonFile(resource.getFile()));
        }
        return result;
    }

    private static String readJsonFile(File file) {
        FileReader fileReader = null;
        Reader reader = null;
        try {
            fileReader = new FileReader(file);
            reader = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8);
            int ch;
            StringBuffer sb = new StringBuffer();
            while ((ch = reader.read()) != -1) {
                sb.append((char) ch);
            }
            fileReader.close();
            reader.close();
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
            log.error("[order] read json file error:", e);
        } finally {
            if(fileReader != null){
                try {
                    fileReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(reader != null){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public List<String> getDefaultFcuntions() {
        return functionMap;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        try {
            List<String> productMessage = readProperties(LOAD_PATH, (new TypeReference<List<String>>() {}));
            functionMap.addAll(productMessage);
        } catch (Exception e) {
            log.error("[DefaultOnlineFunctions] parseObject fail ");
        }

//        List<String> jsonList = getList(LOAD_PATH);
//        for(String jsonStr :jsonList) {
//            try {
//                functionMap.addAll(JSONObject.parseObject(jsonStr, List.class));
//            } catch (Exception e) {
//                log.error("[DefaultOnlineFunctions] parseObject fail ");
//            }
//        }
    }

    public static <T> T readProperties(String resourceLocation, TypeReference<T> javaType) throws IOException {
        try (InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(
                resourceLocation)) {
            assert inputStream != null;
            return mapper.readValue(inputStream, javaType);
        }
    }
}
