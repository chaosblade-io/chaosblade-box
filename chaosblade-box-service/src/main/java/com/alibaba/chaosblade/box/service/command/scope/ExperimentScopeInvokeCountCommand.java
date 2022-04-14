package com.alibaba.chaosblade.box.service.command.scope;

import com.alibaba.chaosblade.box.common.commands.SpringBeanCommand;
import com.alibaba.chaosblade.box.dao.mapper.CloudManualMapper;
import com.alibaba.chaosblade.box.service.model.scope.ExperimentScopeInvokeCount;
import com.alibaba.chaosblade.box.service.model.scope.ScopeInfoQueryRequest;
import com.google.common.base.Strings;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author haibin
 *
 *
 */
@Component
public class ExperimentScopeInvokeCountCommand
    extends SpringBeanCommand<ScopeInfoQueryRequest, List<ExperimentScopeInvokeCount>> {

    @Autowired
    private CloudManualMapper cloudManualMapper;

    @Override
    public List<ExperimentScopeInvokeCount> execute(ScopeInfoQueryRequest scopeInfoQueryRequest) {
        String configurationId = scopeInfoQueryRequest.getConfigurationId();
        if (Strings.isNullOrEmpty(configurationId)) { return new ArrayList<>(); }
        Date monthDate = DateUtils.truncate(DateUtils.addMonths(new Date(), -2),
            Calendar.MONTH);
        Map<Date, ExperimentScopeInvokeCount> result = initResult(monthDate, new Date());
        cloudManualMapper.countInvocationByHostAndDate(configurationId,
            monthDate).stream().map(
            stringObjectMap -> {
                ExperimentScopeInvokeCount experimentScopeInvokeCount = new ExperimentScopeInvokeCount();
                try {
                    experimentScopeInvokeCount.setTime(
                        DateUtils.parseDate(stringObjectMap.get("date").toString(),
                            DateFormatUtils.ISO_8601_EXTENDED_DATE_FORMAT.getPattern()));
                } catch (ParseException e) {
                }
                experimentScopeInvokeCount.setTotal(Integer.parseInt(stringObjectMap.get("total").toString()));
                return experimentScopeInvokeCount;
            }).forEach(experimentScopeInvokeCount -> {
            if (result.containsKey(experimentScopeInvokeCount.getTime())) {
                result.put(experimentScopeInvokeCount.getTime(), experimentScopeInvokeCount);
            }
        });
        return result.values().stream().sorted(Comparator.comparing(ExperimentScopeInvokeCount::getTime)).collect(
            Collectors.toList());
    }

    private Map<Date, ExperimentScopeInvokeCount> initResult(Date startTime, Date endTime) {
        Map<Date, ExperimentScopeInvokeCount> result = new HashMap<>();
        int i = 0;
        while (true) {
            Date tempDate = DateUtils.addDays(startTime, i);
            if (tempDate.compareTo(endTime) > 0) {
                break;
            }
            ExperimentScopeInvokeCount experimentScopeInvokeCount = new ExperimentScopeInvokeCount();
            experimentScopeInvokeCount.setTotal(0);
            experimentScopeInvokeCount.setTime(tempDate);
            result.put(tempDate, experimentScopeInvokeCount);
            i++;
        }
        return result;
    }

}
