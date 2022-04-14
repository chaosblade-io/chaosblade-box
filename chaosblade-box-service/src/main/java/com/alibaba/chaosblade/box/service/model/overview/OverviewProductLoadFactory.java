package com.alibaba.chaosblade.box.service.model.overview;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author sunpeng
 *
 *
 */
@Slf4j
@Component
public class OverviewProductLoadFactory implements InitializingBean {

    private static final String LOAD_PATH_MESSAGE = "product/message.json";

    private static final String LOAD_PATH_PRACTICE = "product/practice.json";

    private final static ObjectMapper mapper = new ObjectMapper();

    /**
     * 消息
     */
    private List<OverviewProductMessage> messageList = new ArrayList<>();

    /**
     * 最佳实践
     */
    private List<OverviewProductMessage> practiceList = new ArrayList<>();

    private static List<String> getList(String path) throws IOException {
//        List<String> result = new ArrayList<>();
//        Resource[] resources =
//                new PathMatchingResourcePatternResolver()
//                        .getResources(ResourceUtils.CLASSPATH_URL_PREFIX + path);
//        for(Resource resource : resources) {
//            result.add(readJsonFile(resource.getFile()));
//        }

//        InputStream stream = ResourceUtil.getStream(path);
//
//        return mapper.readerFor(new TypeReference<List<String>>() {
//        }).readValue(IoUtil.readBytes(stream));

//        return readProperties(path, (new TypeReference<List<String>>() {}));
        return null;
    }


    public static <T> T readProperties(String resourceLocation, TypeReference<T> javaType) throws IOException {
        try (InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(
                resourceLocation)) {
            assert inputStream != null;
            return mapper.readValue(inputStream, javaType);
        }
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

    public List<OverviewProductMessage> getMessage() {
        return messageList;
    }

    public List<OverviewProductMessage> getPractice() {
        return practiceList;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        try {
            List<OverviewProductMessage> productMessage = readProperties(LOAD_PATH_MESSAGE, (new TypeReference<List<OverviewProductMessage>>() {}));
            messageList.addAll(productMessage);
        } catch (Exception e) {
            log.error("[OverviewProductMessage] parseObject fail ");
        }

//        List<String> messageLsonList = getList(LOAD_PATH_MESSAGE);
//        for(String jsonStr :messageLsonList) {
//            List<OverviewProductMessage> productMessage = null;
//            try {
//                productMessage = JSONObject.parseObject(jsonStr, List.class);
//                messageList.addAll(productMessage);
//            } catch (Exception e) {
//                log.error("[OverviewProductMessage] parseObject fail ");
//            }
//        }


        try {
            List<OverviewProductMessage> productMessage = readProperties(LOAD_PATH_PRACTICE, (new TypeReference<List<OverviewProductMessage>>() {}));
            messageList.addAll(productMessage);
        } catch (Exception e) {
            log.error("[OverviewProductPractice] parseObject fail ");
        }

//        List<String> practiceLsonList = getList(LOAD_PATH_PRACTICE);
//        for(String jsonStr :practiceLsonList) {
//            List<OverviewProductMessage> productMessage = null;
//            try {
//                productMessage = JSONObject.parseObject(jsonStr, List.class);
//                practiceList.addAll(productMessage);
//            } catch (Exception e) {
//                log.error("[OverviewProductPractice] parseObject fail ");
//            }
//        }
    }
}
