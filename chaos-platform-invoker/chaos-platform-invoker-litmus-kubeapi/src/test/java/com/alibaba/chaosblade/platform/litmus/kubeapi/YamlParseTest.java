package com.alibaba.chaosblade.platform.litmus.kubeapi;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.alibaba.chaosblade.platform.litmus.kubeapi.crd.experiment.ChaosExperiment;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.representer.Representer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yefei
 */
public class YamlParseTest {

    public static void main(String[] args) {

        HttpRequest request = HttpUtil.createGet("https://hub.litmuschaos.io/api/chaos/1.13.0?file=charts/generic/experiments.yaml");
        HttpResponse execute = request.execute();
        String s = new String(execute.bodyBytes());

        List<ChaosExperiment> list = new ArrayList<>();
        for (String s1 : StrUtil.split(s, "---")) {
            if (StrUtil.isBlank(s1)) {
                continue;
            }
            s1 = s1.trim();
            Representer representer = new Representer();
            representer.getPropertyUtils().setSkipMissingProperties(true);
            Yaml yaml = new Yaml(representer);
            ChaosExperiment chaosExperiment = yaml.loadAs(s1, ChaosExperiment.class);
            list.add(chaosExperiment);
            System.out.println(chaosExperiment.getApiVersion());
        }
        System.out.println(list.size());
    }
}
