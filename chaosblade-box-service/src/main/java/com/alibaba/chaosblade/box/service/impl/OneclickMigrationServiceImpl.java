package com.alibaba.chaosblade.box.service.impl;

import com.alibaba.chaosblade.box.common.commands.CommandBus;
import com.alibaba.chaosblade.box.common.common.domain.BaseRequest;
import com.alibaba.chaosblade.box.common.common.domain.user.ChaosUser;
import com.alibaba.chaosblade.box.common.common.enums.MigrationProgressEnum;
import com.alibaba.chaosblade.box.common.common.enums.MigrationStateEnum;
import com.alibaba.chaosblade.box.dao.model.MigrationConfigurationDO;
import com.alibaba.chaosblade.box.dao.repository.MigrationConfigurationRepository;
import com.alibaba.chaosblade.box.service.OneclickMigrationService;
import com.alibaba.chaosblade.box.service.command.migration.AgentMigrationCommand;
import com.alibaba.chaosblade.box.service.command.migration.ExperimentMigrationCommand;
import com.alibaba.chaosblade.box.service.model.migration.*;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class OneclickMigrationServiceImpl implements OneclickMigrationService {
    private static final String DbDefaultPort = "3306";

    private static final String MigrationFlagAll = "all";

    private static final String MigrationFlagExperiment = "experiment";

    private static final String MigrationFlagExpertise = "expertise";

    private static final String MigrationFlagAgent = "agent";


    @Autowired
    private MigrationConfigurationRepository migrationConfigurationRepository;

    @Autowired
    private CommandBus commandBus;

    @Override
    public MigrationConfigurationResponse getMigrationConfiguration(ChaosUser chaosUser) {

        Optional<MigrationConfigurationDO> migrationConfigurationDO = migrationConfigurationRepository.findByUserId(chaosUser.getUserId());
        return migrationConfigurationDO.map(OneclickMigrationServiceImpl::convert).orElse(null);
    }

    @Override
    public Boolean checkDbAccount(CheckAccountRequest checkAccountRequest) {
        String url = checkAccountRequest.getHost()+":"+checkAccountRequest.getPort();
        String jdbcUrl = "jdbc:mysql://"+url+"/chaosblade?characterEncoding=utf8&useSSL=false";
        try {
            Connection connection = DriverManager.getConnection(jdbcUrl, checkAccountRequest.getAccount(), checkAccountRequest.getPassword());
            return connection.isValid(2);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    @Override
    public Boolean saveMigrationConfiguration(ChaosUser chaosUser, SaveMigrationConfigurationRequest saveMigrationConfigurationRequest) {

        MigrationConfigurationDO migrationConfigurationDO = reverseConvert(chaosUser, saveMigrationConfigurationRequest);
        if (migrationConfigurationDO == null) {
            return false;
        }

        Optional<MigrationConfigurationDO> migrationConfigurationDoOld = migrationConfigurationRepository.findByUserId(chaosUser.getUserId());
        if (migrationConfigurationDoOld.isPresent()) {
            migrationConfigurationDO.setId(migrationConfigurationDoOld.get().getId());
            return migrationConfigurationRepository.update(migrationConfigurationDO);
        }

        return migrationConfigurationRepository.add(migrationConfigurationDO);
    }

    @Override
    public Boolean startMigration(ChaosUser chaosUser, MigrationProcessRequest migrationProcessRequest) {
        String migrationFlag = migrationProcessRequest.getMigrationFlag();
        if (migrationFlag.isEmpty()) {
            migrationFlag = MigrationFlagAll;
        }

        // get migration configuration
        Optional<MigrationConfigurationDO> migrationConfigurationDO = migrationConfigurationRepository.findByUserId(chaosUser.getUserId());
        if (!migrationConfigurationDO.isPresent()) {
            log.warn("[Start migration] need not start, have not migration configuration.");
            return true;
        }
        MigrationConfigurationDO migrationConfigurationDO1 = migrationConfigurationDO.get();


        BaseRequest baseRequest = new BaseRequest();
        baseRequest.setUser(chaosUser);
        baseRequest.setNamespace("default");
        String migrationResult;
        switch (migrationFlag) {
            case MigrationFlagExperiment:
                migrationResult = buildMigration(migrationConfigurationDO.get().getMigrationResult(), false, true, false, MigrationStateEnum.RUNNING);
                updateMigrationResult(chaosUser, migrationResult);
                commandBus.syncRun(ExperimentMigrationCommand.class, baseRequest);
                break;
            case MigrationFlagAgent:
                migrationResult = buildMigration(migrationConfigurationDO.get().getMigrationResult(), true, false, false, MigrationStateEnum.RUNNING);
                updateMigrationResult(chaosUser, migrationResult);
                commandBus.syncRun(AgentMigrationCommand.class, baseRequest);
                break;
            case MigrationFlagAll:
                migrationResult = buildMigration(migrationConfigurationDO.get().getMigrationResult(), true, true, false, MigrationStateEnum.RUNNING);
                updateMigrationResult(chaosUser, migrationResult);
                commandBus.syncRun(AgentMigrationCommand.class, baseRequest);
                commandBus.syncRun(ExperimentMigrationCommand.class, baseRequest);
            default:
                break;
        }

        return true;
    }

    public void updateMigrationResult (ChaosUser chaosUser, String migrationResult) {
        migrationConfigurationRepository.updateMigrationResultByUserId(chaosUser.getUserId(), migrationResult);
    }

    @Override
    public QueryMigrationResponse queryMigrationResult(ChaosUser chaosUser, MigrationProcessRequest migrationProcessRequest) {
        QueryMigrationResponse queryMigrationResponse = new QueryMigrationResponse();
        List<MigrationResultDetail> migrationResultDetails = new ArrayList<>();
        List<MigrationProgressDetail> migrationProgressDetails = new ArrayList<>();
//        MigrationResultDetail migrationResultDetail = MigrationResultDetail.builder().build();
        List<MigrationStateEnum> allResults = new ArrayList<>();

        String migrationFlag = migrationProcessRequest.getMigrationFlag();
        if (migrationFlag.isEmpty()) {
            migrationFlag = MigrationFlagAll;
        }

        switch (migrationFlag){
            case MigrationFlagExperiment:
                MigrationResultDetail migrationResultDetail = MigrationResultDetail.builder().
                        name(MigrationProgressEnum.EXPERIMENT_PROGRESS.getName()).
                        type(MigrationProgressEnum.EXPERIMENT_PROGRESS.getType()).
                        status(queryExperimentMigrationResult(chaosUser)).build();
                migrationResultDetails.add(migrationResultDetail);

                MigrationProgressDetail migrationProgressDetail = MigrationProgressDetail.builder().
                        name("数据迁移").type("metricMigrationResult").items(migrationResultDetails).build();
                migrationProgressDetails.add(migrationProgressDetail);

                queryMigrationResponse.setStatus(migrationResultDetail.getStatus());
                queryMigrationResponse.setProgress(migrationProgressDetails);

                return queryMigrationResponse;
            case MigrationFlagExpertise:
                MigrationResultDetail migrationResultDetail1 = MigrationResultDetail.builder().
                        name(MigrationProgressEnum.EXPERITISE_PROGRESS.getName()).
                        type(MigrationProgressEnum.EXPERITISE_PROGRESS.getType()).
                        status(queryExpertiseMigrationResult(chaosUser)).build();

                migrationResultDetails.add(migrationResultDetail1);

                MigrationProgressDetail migrationProgressDetail1 = MigrationProgressDetail.builder().
                        name("数据迁移").type("metricMigrationResult").items(migrationResultDetails).build();
                migrationProgressDetails.add(migrationProgressDetail1);

                queryMigrationResponse.setStatus(migrationResultDetail1.getStatus());
                queryMigrationResponse.setProgress(migrationProgressDetails);

                return queryMigrationResponse;
            case MigrationFlagAgent:
                MigrationResultDetail migrationResultDetail2 = MigrationResultDetail.builder().
                        name(MigrationProgressEnum.AGENT_PROGRESS.getName()).
                        type(MigrationProgressEnum.AGENT_PROGRESS.getType()).
                        status(queryAgentMigrationResult(chaosUser)).build();

                migrationResultDetails.add(migrationResultDetail2);

                MigrationProgressDetail migrationProgressDetail2 = MigrationProgressDetail.builder().
                        name("探针迁移").type("agentMigrationResult").items(migrationResultDetails).build();
                migrationProgressDetails.add(migrationProgressDetail2);

                queryMigrationResponse.setStatus(migrationResultDetail2.getStatus());
                queryMigrationResponse.setProgress(migrationProgressDetails);

                return queryMigrationResponse;
            case MigrationFlagAll:
                // build agent result
                MigrationResultDetail migrationResultDetailAll0 = MigrationResultDetail.builder().
                        name(MigrationProgressEnum.AGENT_PROGRESS.getName()).
                        type(MigrationProgressEnum.AGENT_PROGRESS.getType()).
                        status(queryAgentMigrationResult(chaosUser)).build();
                allResults.add(migrationResultDetailAll0.getStatus());

                List<MigrationResultDetail> migrationResultDetails1 = new ArrayList<>();
                migrationResultDetails1.add(migrationResultDetailAll0);
                MigrationProgressDetail migrationProgressDetail4 = MigrationProgressDetail.builder().
                        name("探针迁移").
                        type("agentMigrationResult").
                        items(migrationResultDetails1).
                        build();
                migrationProgressDetails.add(migrationProgressDetail4);

                // build metric result
                MigrationResultDetail migrationResultDetailAll1 = MigrationResultDetail.builder().
                        name(MigrationProgressEnum.EXPERIMENT_PROGRESS.getName()).
                        type(MigrationProgressEnum.EXPERIMENT_PROGRESS.getType()).
                        status(queryExperimentMigrationResult(chaosUser)).build();
                migrationResultDetails.add(migrationResultDetailAll1);
                allResults.add(migrationResultDetailAll1.getStatus());

//                MigrationResultDetail migrationResultDetailAll2 = MigrationResultDetail.builder().
//                        name(MigrationProgressEnum.EXPERITISE_PROGRESS.getName()).
//                        type(MigrationProgressEnum.EXPERITISE_PROGRESS.getType()).
//                        status(queryExpertiseMigrationResult(chaosUser)).build();
//                migrationResultDetails.add(migrationResultDetailAll2);
//                allResults.add(migrationResultDetailAll2.getStatus());

                MigrationProgressDetail migrationProgressDetail3 = MigrationProgressDetail.builder().
                        name("数据迁移").
                        type("metricMigrationResult").
                        items(migrationResultDetails).
                        build();
                migrationProgressDetails.add(migrationProgressDetail3);

                queryMigrationResponse.setProgress(migrationProgressDetails);
                break;
            default:
                throw new RuntimeException("Illegal migration flag: "+ migrationFlag);
        }
        queryMigrationResponse.setStatus(getLastMigrationResult(allResults));
        return queryMigrationResponse;
    }

    private MigrationStateEnum queryExperimentMigrationResult(ChaosUser chaosUser) {
        return queryMigrationResult(chaosUser, MigrationProgressEnum.EXPERIMENT_PROGRESS.getType());
    }

    private MigrationStateEnum queryExpertiseMigrationResult(ChaosUser chaosUser) {
        return queryMigrationResult(chaosUser, MigrationProgressEnum.EXPERITISE_PROGRESS.getType());
    }

    private MigrationStateEnum queryAgentMigrationResult(ChaosUser chaosUser) {
        return queryMigrationResult(chaosUser, MigrationProgressEnum.AGENT_PROGRESS.getType());
    }

    private MigrationStateEnum queryMigrationResult(ChaosUser chaosUser, String migrationFlag) {
        Optional<MigrationConfigurationDO> migrationConfigurationDO = migrationConfigurationRepository.findByUserId(chaosUser.getUserId());
        if (!migrationConfigurationDO.isPresent()) {
            log.info("[migration result] " + chaosUser.getUserId() + " have not migration recode");
            return MigrationStateEnum.SUCCESS;
        }

        if (migrationConfigurationDO.get().getMigrationResult().isEmpty()){
            log.info("[migration result] " + chaosUser.getUserId() + " have not migration recode");
            return MigrationStateEnum.READY;
        }
        ConcurrentHashMap migrationResult;
        migrationResult = JSON.parseObject(migrationConfigurationDO.get().getMigrationResult(), ConcurrentHashMap.class);
        if (migrationResult.containsKey(migrationFlag)) {
            return MigrationStateEnum.of(migrationResult.get(migrationFlag).toString());
        }

        return MigrationStateEnum.READY;
    }

    private MigrationStateEnum getLastMigrationResult(List<MigrationStateEnum> migrationStateEnums) {
        MigrationStateEnum migrationStateEnum = MigrationStateEnum.READY;
        Boolean isSuccess = true;
        for (MigrationStateEnum migrationState : migrationStateEnums) {
            if (migrationState == MigrationStateEnum.FAILED) {
                return MigrationStateEnum.FAILED;
            }

            if (migrationState == MigrationStateEnum.RUNNING) {
                migrationStateEnum = MigrationStateEnum.RUNNING;
                isSuccess = false;
            }

            if (migrationState == MigrationStateEnum.READY) {
                isSuccess = false;
            }
        }

        return isSuccess ? MigrationStateEnum.SUCCESS : migrationStateEnum;
    }

    private static MigrationConfigurationResponse convert(MigrationConfigurationDO migrationConfigurationDO) {
        MigrationConfigurationResponse migrationConfigurationResponse = new MigrationConfigurationResponse();
        String dbUrl = migrationConfigurationDO.getDbUrl();
        String[] dbUrlArr = dbUrl.split(":");
        if (dbUrlArr.length != 2){
            return migrationConfigurationResponse;
        }
        Integer metricMigration = migrationConfigurationDO.getMetricMigration();
        Boolean experimentFlag = ((1 & metricMigration) == 1);
        Boolean expertiseFlag = ((1 & (metricMigration>>1)) == 1);

        migrationConfigurationResponse.setUserId(migrationConfigurationDO.getUserId());
        migrationConfigurationResponse.setCloudAk(migrationConfigurationDO.getCloudAk());
        migrationConfigurationResponse.setCloudSk(migrationConfigurationDO.getCloudSk());
        migrationConfigurationResponse.setDbHost(dbUrlArr[0]);
        migrationConfigurationResponse.setDbPort(dbUrlArr[1]);
        migrationConfigurationResponse.setDbAccount(migrationConfigurationDO.getDbAccount());
        migrationConfigurationResponse.setDbPassword(migrationConfigurationDO.getDbPassword());
        migrationConfigurationResponse.setExperimentFlag(experimentFlag);
        migrationConfigurationResponse.setExpertiseFlag(expertiseFlag);

        return migrationConfigurationResponse;
    }

    private static MigrationConfigurationDO reverseConvert(ChaosUser chaosUser, SaveMigrationConfigurationRequest saveMigrationConfigurationRequest) {
        String cloudAk = saveMigrationConfigurationRequest.getCloudAccount();
        String cloudSk = saveMigrationConfigurationRequest.getCloudPassword();
        if (cloudAk.isEmpty() || cloudSk.isEmpty()) {
            return null;
        }

        Boolean experimentFlag = saveMigrationConfigurationRequest.getExperimentFlag();
        Boolean expertiseFlag = saveMigrationConfigurationRequest.getExpertiseFlag();
        String dbAccount = saveMigrationConfigurationRequest.getDbAccount();
        String dbPassword = saveMigrationConfigurationRequest.getDbPassword();
        String dbHost = saveMigrationConfigurationRequest.getDbHost();
        String dbPort = saveMigrationConfigurationRequest.getDbPort();
        if (dbPort.isEmpty()) {
            dbPort = DbDefaultPort;
        }
        if (experimentFlag || expertiseFlag) {
            if (dbAccount.isEmpty() || dbPassword.isEmpty() || dbHost.isEmpty()){
                return null;
            }
        }

        int metricMigration = experimentFlag ? 1 : 0;
        metricMigration += expertiseFlag ? 1 << 1 : 0;

        // build ready migration result
        String migrationResult = buildMigration("",true, experimentFlag, expertiseFlag, MigrationStateEnum.READY);

        MigrationConfigurationDO migrationConfigurationDO = new MigrationConfigurationDO();
        migrationConfigurationDO.setUserId(chaosUser.getUserId());
        migrationConfigurationDO.setCloudAk(cloudAk);
        migrationConfigurationDO.setCloudSk(cloudSk);
        migrationConfigurationDO.setMetricMigration(metricMigration);
        migrationConfigurationDO.setDbAccount(dbAccount);
        migrationConfigurationDO.setDbPassword(dbPassword);
        migrationConfigurationDO.setDbUrl(dbHost+":"+dbPort);
        migrationConfigurationDO.setMigrationResult(migrationResult);
        return migrationConfigurationDO;
    }

    private static String buildMigration(String migrationResultStr, Boolean agentFlag, Boolean experimentFlag, Boolean expertiseFlag, MigrationStateEnum migrationStatus) {
        Map<String, MigrationStateEnum> migrationResult = new ConcurrentHashMap<>();
        if (!migrationResultStr.isEmpty()){
            migrationResult = JSON.parseObject(migrationResultStr, ConcurrentHashMap.class);
        }

        if (agentFlag) {
            if (migrationResult.containsKey(MigrationProgressEnum.AGENT_PROGRESS.getType())){
                migrationResult.replace(MigrationProgressEnum.AGENT_PROGRESS.getType(), migrationStatus);
            }else {
                migrationResult.put(MigrationProgressEnum.AGENT_PROGRESS.getType(), migrationStatus);
            }
        }
        if (experimentFlag) {
            if (migrationResult.containsKey(MigrationProgressEnum.EXPERIMENT_PROGRESS.getType())){
                migrationResult.replace(MigrationProgressEnum.EXPERIMENT_PROGRESS.getType(), migrationStatus);
            }else {
                migrationResult.put(MigrationProgressEnum.EXPERIMENT_PROGRESS.getType(), migrationStatus);
            }
        }
        if (expertiseFlag) {
            if (migrationResult.containsKey(MigrationProgressEnum.EXPERITISE_PROGRESS.getType())){
                migrationResult.replace(MigrationProgressEnum.EXPERITISE_PROGRESS.getType(), migrationStatus);
            }else {
                migrationResult.put(MigrationProgressEnum.EXPERITISE_PROGRESS.getType(), migrationStatus);
            }
        }
        return JSON.toJSONString(migrationResult);
    }

}
