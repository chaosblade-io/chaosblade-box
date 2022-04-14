package com.alibaba.chaosblade.box.common.sdk.util;

import com.alibaba.chaosblade.box.common.sdk.constant.Blade;
import com.alibaba.chaosblade.box.common.sdk.entity.*;
import com.alibaba.chaosblade.box.common.sdk.parser.JarYamlService;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author Changjun Xiao
 */
public class CliUtil {

    public static final String CREATE = "create";
    public static final String DESTROY = "destroy";

    public static final String NODE_KIND = "node";
    public static final String POD_KIND = "pod";
    public static final String CONTAINER_KIND = "container";

    public static final String ENVIRONMENT_OS = "os";
    public static final String ENVIRONMENT_JAVA = "java";
    /**
     * 转换模型为 chaosblade cli 参数
     *
     * @param command
     * @param modelArgs
     * @return
     */
    public static String buildExpCmd(String command, ModelArgs modelArgs) {
        StringBuilder sb = new StringBuilder(command);
        if ("node".equalsIgnoreCase(modelArgs.getScope()) ||
            "pod".equalsIgnoreCase(modelArgs.getScope()) ||
            "container".equalsIgnoreCase(modelArgs.getScope())) {
            sb.append(" ").append("k8s ").append(modelArgs.getTarget()).append(" ");
        }else if ("windows".equalsIgnoreCase(modelArgs.getScope())){
            sb.append(" ").append("windows").append(" ").append(modelArgs.getTarget()).append(" ");
        } else {
            sb.append(" ").append(modelArgs.getTarget().trim()).append(" ");
            if (modelArgs.getSubTarget() != null) {
                sb.append(modelArgs.getSubTarget().trim()).append(" ");
            }
        }
        if (modelArgs.getAction() != null) {
            sb.append(modelArgs.getAction().trim()).append(" ");
        }
        // add action flags
        Map<String, String> flags = modelArgs.getFlags();
        if (flags != null) {
            for (Entry<String, String> entry : flags.entrySet()) {
                String value = entry.getValue();
                if (StringUtil.isBlank(value) || "false".equalsIgnoreCase(value)) {
                    continue;
                }
                if ("windows".equalsIgnoreCase(modelArgs.getScope())){
                    sb.append("--").append(entry.getKey().trim()).append(" ")
                            .append(base64encodeScriptContent(entry.getKey(), value).trim()).append(" ");
                }else {
                    sb.append("--").append(entry.getKey().trim()).append(" ").append("'")
                            .append(base64encodeScriptContent(entry.getKey(), value).trim()).append("'").append(" ");
                }

            }
        }
        // add matcher flags
        Map<String, String> matcherFlags = modelArgs.getMatcherFlags();
        if (matcherFlags != null) {
            for (Entry<String, String> entry : matcherFlags.entrySet()) {
                String value = entry.getValue();
                if (StringUtil.isBlank(value) || value.equalsIgnoreCase("false")) {
                    continue;
                }
                if ("windows".equalsIgnoreCase(modelArgs.getScope())){
                    sb.append("--").append(entry.getKey().trim()).append(" ")
                            .append(base64encodeScriptContent(entry.getKey(), entry.getValue()).trim())
                            .append(" ");
                }else {
                    sb.append("--").append(entry.getKey().trim()).append(" ").append("'")
                            .append(base64encodeScriptContent(entry.getKey(), entry.getValue()).trim()).append("'")
                            .append(" ");
                }

            }
        }
        return sb.toString();
    }

    private static String base64encodeScriptContent(String key, String value) {
        if ("filter-script-content".equals(key) || "script-content".equals(key)) {
            return Base64Util.encode(value.getBytes(Charset.forName("UTF-8")), false);
        }
        return value;
    }

    /**
     * 构建状态查询的命令
     *
     * @param uid
     * @param type
     * @return
     */
    public static String buildStatusCmd(String uid, String type) {
        StringBuilder sb = new StringBuilder(Blade.STATUS);
        sb.append(" ").append("--type").append(" ").append(type.trim());
        if (!StringUtil.isBlank(uid)) {
            sb.append(" ").append("--uid").append(" ").append(uid);
        }
        return sb.toString();
    }

    /**
     * 构建销毁命令
     *
     * @param uid
     * @return
     */
    public static String buildDestroyCmd(String uid) {
        StringBuilder sb = new StringBuilder(Blade.DESTROY);
        sb.append(" ").append(uid);
        return sb.toString();
    }

    /**
     * @param modelArgs
     * @return
     * @throws UnsupportedOperationException
     */
    public static String buildDestroyCmdWithoutUid(ModelArgs modelArgs) throws UnsupportedOperationException {
        StringBuilder sb = new StringBuilder(Blade.DESTROY);
        if ("node".equalsIgnoreCase(modelArgs.getScope()) ||
            "pod".equalsIgnoreCase(modelArgs.getScope()) ||
            "container".equalsIgnoreCase(modelArgs.getScope()) ||
            "cplus".equalsIgnoreCase(modelArgs.getTarget()) ||
            "tengine".equalsIgnoreCase(modelArgs.getTarget()) ||
            modelArgs.getTarget().startsWith("node-")) {
            throw new UnsupportedOperationException("not support the scope kind");
        } else {
            if ("windows".equalsIgnoreCase(modelArgs.getScope())){
                sb.append(" ").append("windows");
            }
            sb.append(" ").append(modelArgs.getTarget().trim()).append(" ");
            if (modelArgs.getSubTarget() != null) {
                sb.append(modelArgs.getSubTarget().trim()).append(" ");
            }
        }
        if (modelArgs.getAction() != null) {
            sb.append(modelArgs.getAction().trim()).append(" ");
        }
        // add action flags
        Map<String, String> flags = modelArgs.getFlags();
        if (flags != null) {
            for (Entry<String, String> entry : flags.entrySet()) {
                String value = entry.getValue();
                if (StringUtil.isBlank(value) || "false".equalsIgnoreCase(value)) {
                    continue;
                }
                // 单独处理 jvm script --script-content 参数，需要 base64 编码
                if ("script".equalsIgnoreCase(modelArgs.getAction()) &&
                    "script-content".equalsIgnoreCase(entry.getKey())) {
                    value = Base64Util.encode(value.getBytes(Charset.forName("UTF-8")), false);
                }
                if ("windows".equalsIgnoreCase(modelArgs.getScope())){
                    sb.append("--").append(entry.getKey().trim()).append(" ")
                            .append(base64encodeScriptContent(entry.getKey(), value).trim()).append(" ");
                }else {
                    sb.append("--").append(entry.getKey().trim()).append(" ").append("'")
                            .append(base64encodeScriptContent(entry.getKey(), value).trim()).append("'").append(" ");
                }

            }
        }
        // add matcher flags
        Map<String, String> matcherFlags = modelArgs.getMatcherFlags();
        if (matcherFlags != null) {
            for (Entry<String, String> entry : matcherFlags.entrySet()) {
                String value = entry.getValue();
                if (StringUtil.isBlank(value) || value.equalsIgnoreCase("false")) {
                    continue;
                }
                if ("windows".equalsIgnoreCase(modelArgs.getScope())){
                    sb.append("--").append(entry.getKey().trim()).append(" ")
                            .append(base64encodeScriptContent(entry.getKey(), entry.getValue()).trim())
                            .append(" ");
                }else {
                    sb.append("--").append(entry.getKey().trim()).append(" ")
                            .append("'").append(base64encodeScriptContent(entry.getKey(), entry.getValue()).trim())
                            .append("'").append(" ");
                }

            }
        }
        return sb.toString();
    }

    /**
     * 构建撤销命令
     *
     * @param uid
     * @return
     */
    public static String buildRevokeCmd(String uid) {
        StringBuilder sb = new StringBuilder(Blade.REVOKE);
        sb.append(" ").append(uid);
        return sb.toString();
    }

    /**
     * 构建 prepare 命令
     *
     * @param prepareArgs
     * @return
     */
    public static String buildPrepareCmd(PrepareArgs prepareArgs) {
        StringBuilder sb = new StringBuilder(Blade.PREPARE);
        sb.append(" ").append(prepareArgs.getType()).append(" ");
        Map<String, String> flags = prepareArgs.getFlags();
        if (flags != null) {
            for (Entry<String, String> entry : flags.entrySet()) {
                String value = entry.getValue();
                if (StringUtil.isBlank(value) || value.equalsIgnoreCase("false")) {
                    continue;
                }
                if ("windows".equalsIgnoreCase(prepareArgs.getScope())){
                    sb.append("--").append(entry.getKey().trim()).append(" ")
                            .append(entry.getValue().trim()).append(" ");
                }else{
                    sb.append("--").append(entry.getKey().trim()).append(" '")
                            .append(entry.getValue().trim()).append("' ");
                }

            }
        }
        return sb.toString();
    }

    /**
     * 构建 query 命令, query TARGET FILED
     *
     * @param target
     * @param field
     * @return
     */
    public static String buildQueryCmd(String target, String field) {
        StringBuilder sb = new StringBuilder(Blade.QUERY);
        sb.append(" ").append(target).append(" ").append(field);
        return sb.toString();
    }

    public static String buildCheckCmd(String command, ModelArgs modelArgs, String environment) {
        StringBuilder sb = new StringBuilder(Blade.CHECK);
        // 1. check only for os environment
        if ("node".equalsIgnoreCase(modelArgs.getScope()) ||
                "pod".equalsIgnoreCase(modelArgs.getScope()) ||
                "container".equalsIgnoreCase(modelArgs.getScope()) ||
                "docker".equalsIgnoreCase(modelArgs.getScope()) ||
                "k8s".equalsIgnoreCase(modelArgs.getScope()) ||
                "cplus".equalsIgnoreCase(modelArgs.getTarget()) ||
                "tengine".equalsIgnoreCase(modelArgs.getTarget()) ||
                modelArgs.getTarget().startsWith("node-") ||
                modelArgs.getTarget().startsWith("k8s-") ||
                "windows".equalsIgnoreCase(modelArgs.getScope())) {
            return "";
        }

        // 2. if environment is java, return java check command
        if (environment.equals(ENVIRONMENT_JAVA)){
            return "check java";
        }else if (environment.equals(ENVIRONMENT_OS)){
            sb.append(" ").append(environment);
        }else{
            throw new UnsupportedOperationException("not support the environment check");
        }

        // 3. judge is need check or not, add target and action
        if (!isNeedCheckCommand(modelArgs.getTarget().trim(), modelArgs.getAction().trim())) {
            return "";
        }
        sb.append(" ").append(modelArgs.getTarget().trim()).append(" ");
        if (modelArgs.getSubTarget() != null) {
            sb.append(modelArgs.getSubTarget().trim()).append(" ");
        }
        if (modelArgs.getAction() != null) {
            sb.append(modelArgs.getAction().trim()).append(" ");
        }

        // 4. add action flags
        Map<String, String> flags = modelArgs.getFlags();
        if (flags != null) {
            for (Entry<String, String> entry : flags.entrySet()) {
                String value = entry.getValue();
                if (StringUtil.isBlank(value) || "false".equalsIgnoreCase(value)) {
                    continue;
                }
                sb.append("--").append(entry.getKey().trim()).append(" ").append("'")
                        .append(base64encodeScriptContent(entry.getKey(), value).trim()).append("'").append(" ");
            }
        }
        // 5. add matcher flags
        Map<String, String> matcherFlags = modelArgs.getMatcherFlags();
        if (matcherFlags != null) {
            for (Entry<String, String> entry : matcherFlags.entrySet()) {
                String value = entry.getValue();
                if (StringUtil.isBlank(value) || value.equalsIgnoreCase("false")) {
                    continue;
                }
                sb.append("--").append(entry.getKey().trim()).append(" ").append("'")
                        .append(base64encodeScriptContent(entry.getKey(), entry.getValue()).trim()).append("'").append(" ");
            }
        }

        return sb.toString();
    }
    private static boolean isNeedCheckCommand(String target, String act){
        ChaosModels chaosModels = new JarYamlService().parse("chaosblade.spec-check.yaml");
        List<ModelSpecBean> items = chaosModels.getItems();
        boolean flag = false;
        for (ModelSpecBean item:items){
            if (!target.equals(item.getTarget())){
                continue;
            }

            List<ActionSpecBean> actions = item.getActions();
            for (ActionSpecBean action:actions){
                if (!act.equals(action.getAction())){
                    continue;
                }

                flag = true;
            }
        }
        return flag;
    }

}
