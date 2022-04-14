package com.alibaba.chaosblade.box.common.sdk;

import com.alibaba.chaosblade.box.common.sdk.entity.ChaosModels;
import com.alibaba.chaosblade.box.common.sdk.parser.JarYamlService;
import com.alibaba.chaosblade.box.common.sdk.parser.LocalYamlService;
import com.alibaba.chaosblade.box.common.sdk.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Changjun Xiao
 */
public abstract class ChaosBase {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChaosBase.class);

    /**
     * chaosblade 所支持的所有操作定义
     */
    private volatile ChaosModels models;

    /**
     * 私有，只能通过 {@link ChaosFactory} 获取
     */
    protected ChaosBase() {
    }

    /**
     * 获取混沌实验操作定义，如果存在，则返回，否则解析场景文件 读取默认 oss 上的场景文件 如果默认的 oss 场景文件读取失败，则读取 SDK 中的场景文件
     *
     * @return
     */
    public ChaosModels getChaosModels() throws Exception {
        if (models != null) {
            return models;
        }
        models = loadChaosModels(null);
        return models;
    }

    /**
     * 从指定文件中读取场景定义 如果读取失败，则读取默认 oss 上的场景文件 如果默认的 oss 场景文件读取失败，则读取 SDK 中的场景文件
     *
     * @param expFilePath 文件地址
     * @param cache       是否缓存读取结果
     * @return
     */
    public ChaosModels getChaosModels(String expFilePath, boolean cache) throws Exception {
        if (models != null && cache) {
            return models;
        }
        models = loadChaosModels(expFilePath);
        return models;
    }

    /**
     * 刷新混沌实验场景
     *
     * @param expFilePath
     * @return
     * @throws Exception
     */
    public synchronized ChaosModels refreshChaosModels(String expFilePath) throws Exception {
        models = loadChaosModels(expFilePath);
        return models;
    }

    /**
     * 加载
     *
     * @param expFilePath
     * @return
     * @throws Exception
     */
    private ChaosModels loadChaosModels(String expFilePath) throws Exception {
        if (!StringUtil.isBlank(expFilePath)) {
            return new LocalYamlService().parse(expFilePath);
        }
        String yamlFileName = getYamlFileName();
        LOGGER.info("load {} yaml file", yamlFileName);
        return new JarYamlService().parse(yamlFileName);
    }

    /**
     * 返回对应的 yaml file
     *
     * @return
     */
    public abstract String getYamlFileName();
}
