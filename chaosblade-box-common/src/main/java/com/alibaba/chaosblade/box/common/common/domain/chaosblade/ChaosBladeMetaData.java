package com.alibaba.chaosblade.box.common.common.domain.chaosblade;

import com.alibaba.chaosblade.box.common.common.constant.ChaosFunctionConstant;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * @author haibin
 *
 *
 */
public class ChaosBladeMetaData {

    public static String SUB_TARGET_JVM = "jvm";

    public static String SUB_TARGET_CONTAINER = "container";

    public static String SUB_TARGET_NETWORK = "network";

    public static String SUB_TARGET_FILE = "file";

    public static String SCOPE_HOST = "host";

    private static ChaosBladeMetaData instance = null;

    public static Map<String, String> SubTargetToCategory = new HashMap<>();

    public static Map<String, String> JvmActionToCategory = new HashMap<>();

    private static ChaosFunctionConstant chaosFunctionConstant;

    public static Map<String, Integer> getScopeToScopeType() {
        return scopeToScopeType;
    }

    public static Map<String, Integer> scopeToScopeType = new HashMap<>();

    public static Set<String> SubTargetWithsJavaAgentInstall = new HashSet<>();

    static {
        scopeToScopeType.put("container", ChaosFunctionConstant.SUPPORT_SCOPE_TYPE_K8S);
        scopeToScopeType.put("node", ChaosFunctionConstant.SUPPORT_SCOPE_TYPE_K8S);
        scopeToScopeType.put("pod", ChaosFunctionConstant.SUPPORT_SCOPE_TYPE_K8S);
        scopeToScopeType.put("docker", ChaosFunctionConstant.SUPPORT_SCOPE_TYPE_HOST);
        scopeToScopeType.put("host", ChaosFunctionConstant.SUPPORT_SCOPE_TYPE_HOST);

        SubTargetToCategory.put("container", "1217716899703644162");
        SubTargetToCategory.put("pod", "1217716899703644162");
        SubTargetToCategory.put("network", "1216672245176541185");
        SubTargetToCategory.put("cpu", "1216606329818566658");
        SubTargetToCategory.put("file", "1216606480226308098");
        SubTargetToCategory.put("mem", "1216606392489857026");
        SubTargetToCategory.put("process", "1217020049010950145");
        SubTargetToCategory.put("disk", "1216606480226308098");
        //篡改数据
        JvmActionToCategory.put("sql", "1216669321109118978");
        JvmActionToCategory.put("return", "1216669321109118978");
        //延迟
        JvmActionToCategory.put("delay", "1216606744870113281");
        //抛异常
        JvmActionToCategory.put("throwCustomException", "1216606820073984002");
        //内存
        JvmActionToCategory.put("CodeCacheFilling", "1217023981502308353");
        JvmActionToCategory.put("OutOfMemoryError", "1217023981502308353");
        //CPU
        JvmActionToCategory.put("fullload", "1217023924078092289");
        //自定义故障
        JvmActionToCategory.put("script", "1216606920988938241");
    }

    protected static Map<String, Map<String, String>> functionWordDict = new HashMap<>();

    protected static Map<String, String> scopeDict = new HashMap<>();

    protected static Map<String, String> actionDict = new HashMap<>();

    protected static Map<String, String> subTargetDict = new HashMap<>();

    public static ChaosBladeMetaData getInstance() {
        if (instance == null) {
            instance = new ChaosBladeMetaData();
        }
        return instance;
    }

    private ChaosBladeMetaData() {
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(
            "chaos-blade/function_word_segmentation_dict_up.json");
        try {
            functionWordDict = JSON.parseObject(inputStream,
                new TypeReference<Map<String, Map<String, String>>>() {}.getType());
        } catch (IOException e) {
            throw new RuntimeException("load function_word_segmentation_dict failed", e);
        }
        scopeDict = functionWordDict.get("scope");
        actionDict = functionWordDict.get("action");
        subTargetDict = functionWordDict.get("subTarget");
    }

    public static String getActionTranslation(String action) {
        return actionDict.getOrDefault(action, action);
    }

    public static String getScopeTranslation(String scope) {
        return scopeDict.getOrDefault(scope, scope);
    }

    public static String getSubTargetTranslation(String subTarget) {
        return subTargetDict.getOrDefault(subTarget, subTarget);
    }

    public static Integer AGENT_DUMPLICATE_CODE = 604;

    public Map<String, ChaosBladeAction> getAppCodeToChaosBladeAction() {
        return appCodeToChaosBladeAction;
    }

    private Map<String, ChaosBladeAction> appCodeToChaosBladeAction = new HashMap<>();

    public ChaosBladeAction getChaosBladeAction(String appCode) {

        return appCodeToChaosBladeAction.get(appCode);
    }

    public void add(String appCode, ChaosBladeAction chaosBladeAction) {
        appCodeToChaosBladeAction.put(appCode, chaosBladeAction);
    }

    public String getFunctionNameByChaosBladeAction(String originName, ChaosBladeAction chaosBladeAction) {
        if (ChaosBladeActionType.ATTACK.equals(chaosBladeAction.getActionType())) {

            return chaosBladeAction.getScope().trim().toUpperCase() + " " + chaosBladeAction.getSubTarget().trim().toUpperCase()
                    + " " + chaosBladeAction.getAction().trim().toUpperCase();
        } else {
            return originName;
        }
    }

    public String getFunctionNameParser(String originName) {
        String needParser = originName;
        if (originName.contains("recovery(")) {
            needParser = originName.substring(9, originName.length()-1);
        }

        String[] originNames = needParser.split(" ");
        if (originNames.length < 3) {
            return originName;
        }

        String parserdFunctionName = scopeDict.get(originNames[0]) + StringUtils.capitalize(
                subTargetDict.getOrDefault(originNames[1], originNames[1]) +
                        actionDict.getOrDefault(originNames[2], originNames[2])
        );
        return needParser.equals(originName) ? parserdFunctionName : "恢复(" + parserdFunctionName + ")";
    }

    public List<ChaosBladeAction> getChaosBladeActions() {
        return Lists.newArrayList(appCodeToChaosBladeAction.values());
    }
}
