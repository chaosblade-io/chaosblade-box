package com.alibaba.chaosblade.box.dao.repository;

import com.alibaba.chaosblade.box.dao.mapper.UserMapper;
import com.alibaba.chaosblade.box.dao.model.UserDo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.base.Strings;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * @author sunju
 *
 */
@Repository
@Component
public class LicenseRepository {

    private static final Integer NOT_DELETE_FLAG = 0;

    @Resource
    private UserMapper userMapper;

    public Optional<UserDo> getUserLicense(String userId) {
        if (!Strings.isNullOrEmpty(userId)) {
            QueryWrapper<UserDo> queryWrapper = new QueryWrapper<UserDo>()
                .eq("user_id", userId)
                .eq("is_deleted", NOT_DELETE_FLAG);
            return Optional.ofNullable(userMapper.selectOne(queryWrapper));
        }
        return Optional.empty();
    }

//    public Optional<LicenseDO> getUserLicense(String userId, String regionId) {
//        if (!Strings.isNullOrEmpty(userId) && !Strings.isNullOrEmpty(regionId)) {
//            QueryWrapper<LicenseDO> queryWrapper = new QueryWrapper<LicenseDO>()
//                    .eq("user_id", userId)
//                    .eq("region_id", regionId)
//                    .eq("is_delete", NOT_DELETE_FLAG);
//            return Optional.ofNullable(licenseMapper.selectOne(queryWrapper));
//        }
//        return Optional.empty();
//    }

}
