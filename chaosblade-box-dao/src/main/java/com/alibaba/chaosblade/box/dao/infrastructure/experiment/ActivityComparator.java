package com.alibaba.chaosblade.box.dao.infrastructure.experiment;


import com.alibaba.chaosblade.box.dao.model.ExperimentActivityDO;

import java.util.Comparator;

/**
 * @author haibin
 *
 *
 */
public class ActivityComparator implements Comparator<ExperimentActivityDO> {

    public static ActivityComparator INSTANCE = new ActivityComparator();

    @Override
    public int compare(ExperimentActivityDO o1, ExperimentActivityDO o2) {
        int phaseCompare = Integer.compare(o1.getPhase().getType(), o2.getPhase().getType());
        return phaseCompare == 0 ? Integer.compare(o1.getActivityOrder(), o2.getActivityOrder()) : phaseCompare;
    }
}
