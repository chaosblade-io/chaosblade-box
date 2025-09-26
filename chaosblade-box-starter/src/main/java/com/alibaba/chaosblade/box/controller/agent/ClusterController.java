package com.alibaba.chaosblade.box.controller.agent;

import com.alibaba.chaosblade.box.annotation.LoginUser;
import com.alibaba.chaosblade.box.common.commands.CommandBus;
import com.alibaba.chaosblade.box.common.common.domain.user.ChaosUser;
import com.alibaba.chaosblade.box.controller.BaseController;
import com.alibaba.chaosblade.box.dao.model.base.PageableResponse;
import com.alibaba.chaosblade.box.model.RestResponseUtil;
import com.alibaba.chaosblade.box.service.command.cluster.ExperimentClustersSearchCommand;
import com.alibaba.chaosblade.box.service.command.cluster.ExperimentNodesQueryCommand;
import com.alibaba.chaosblade.box.service.command.cluster.ExperimentPodsSearchCommand;
import com.alibaba.chaosblade.box.service.model.RestResponse;
import com.alibaba.chaosblade.box.service.model.agent.ExperimentClusterQueryRequest;
import com.alibaba.chaosblade.box.service.model.cluster.ExperimentClusterVO;
import com.alibaba.chaosblade.box.service.model.cluster.ExperimentPodVO;
import com.alibaba.chaosblade.box.service.model.experiment.ExperimentScope;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClusterController extends BaseController {

  @Autowired private CommandBus commandBus;

  @PostMapping("ListExperimentClusters")
  public RestResponse<List<ExperimentClusterVO>> listExperimentClusters(
      @LoginUser ChaosUser chaosUser,
      @RequestBody ExperimentClusterQueryRequest experimentClusterQueryRequest) {
    experimentClusterQueryRequest.setUser(chaosUser);
    return RestResponseUtil.okWithData(
        commandBus.syncRun(ExperimentClustersSearchCommand.class, experimentClusterQueryRequest));
  }

  @PostMapping("SearchExperimentPodsByNode")
  public RestResponse<PageableResponse<ExperimentPodVO>> searchExperimentPodsByNode(
      @LoginUser ChaosUser chaosUser,
      @RequestBody ExperimentClusterQueryRequest experimentClusterQueryRequest) {
    experimentClusterQueryRequest.setUser(chaosUser);
    return RestResponseUtil.okWithData(
        commandBus.syncRun(ExperimentPodsSearchCommand.class, experimentClusterQueryRequest));
  }

  @PostMapping("ListExperimentNodesByCluster")
  public RestResponse<PageableResponse<ExperimentScope>> listExperimentNodesByCluster(
      @LoginUser ChaosUser chaosUser,
      @RequestBody ExperimentClusterQueryRequest experimentClusterQueryRequest) {
    experimentClusterQueryRequest.setUser(chaosUser);
    return RestResponseUtil.okWithData(
        commandBus.syncRun(ExperimentNodesQueryCommand.class, experimentClusterQueryRequest));
  }

  @PostMapping("SearchExperimentPodsByCluster")
  public RestResponse<PageableResponse<ExperimentPodVO>> searchExperimentPodsByCluster(
      @LoginUser ChaosUser chaosUser,
      @RequestBody ExperimentClusterQueryRequest experimentClusterQueryRequest) {
    experimentClusterQueryRequest.setUser(chaosUser);
    return RestResponseUtil.okWithData(
        commandBus.syncRun(ExperimentPodsSearchCommand.class, experimentClusterQueryRequest));
  }
}
