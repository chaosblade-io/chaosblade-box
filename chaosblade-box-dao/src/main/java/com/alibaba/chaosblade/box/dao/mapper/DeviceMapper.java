package com.alibaba.chaosblade.box.dao.mapper;

import com.alibaba.chaosblade.box.common.infrastructure.util.MybatisMapper;
import com.alibaba.chaosblade.box.dao.model.DeviceDO;
import com.alibaba.chaosblade.box.dao.query.CloudDeviceQuery;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author sunju
 *
 */
@MybatisMapper
public interface DeviceMapper extends BaseMapper<DeviceDO> {


    @Select("<script>" +
            "select " +
            "td.* " +
            "from t_chaos_device td WHERE td.status = 2 " +
            "<if test='null != query.key and query.key != \"\" '>" +
            "AND td.private_ip like #{query.key} " +
            "</if>" +
            "<if test='null != query.namespace and query.namespace != \"\" '>" +
            "AND td.namespace = #{query.namespace} " +
            "</if>" +
            "<if test='null != query.userId and query.userId != \"\" '>" +
            "AND td.user_id = #{query.userId} " +
            "</if>" +
            "<if test='null != query.osType and query.osType == 1 '>" +
            "AND td.os_type = 1 " +
            "</if>" +
            "<if test='null != query.osType and query.osType == 0 '>" +
            "AND (td.os_type = 0 or td.os_type is null) " +
            "</if>" +
            "<if test='null != query.tags and query.tags.size != 0'>" +
            " AND td.configuration_id IN " +
            "(select configuration_id from t_chaos_application_device_tag adt where adt.tag_name in " +
            "<foreach collection='query.tags' item='tag' index='index' open='(' close=')' separator=',' >" +
            "#{tag}" +
            "</foreach>" +
            ")" +
            "</if>" +
            "ORDER BY td.gmt_create DESC" +
            "</script>")
    IPage<DeviceDO> selectHostPageByKeyAndTags(IPage<DeviceDO> page, @Param("query") CloudDeviceQuery query);

    @Select("<script>" +
            "select " +
            "td.* " +
            "from t_chaos_device td WHERE 1=1 " +
            "<if test='null != query.appId and query.appId != \"\" '>" +
            "AND td.app_id = #{query.appId} " +
            "</if>" +
            "<if test='null != query.status and query.status != \"\" '>" +
            "AND td.status = #{query.status} " +
            "</if>" +
            "<if test='null != query.partName and query.partName != \"\" '>" +
            "AND td.private_ip like #{query.partName} " +
            "</if>" +
            "<if test='null != query.groups and query.groups.size != 0'>" +
            "AND td.group_name in " +
            "<foreach collection='query.groups' item='groupName' index='index' open='(' close=')' separator=',' >" +
            "#{groupName}" +
            "</foreach>" +
            "</if>" +
            "<if test='null != query.dimensions and query.dimensions.size != 0'>" +
            "AND ad.dimension in " +
            "<foreach collection='query.dimensions' item='dimension' index='index' open='(' close=')' separator=',' >" +
            "#{dimension}" +
            "</foreach>" +
            "</if>" +
            "<if test='null != query.tags and query.tags.size != 0'>" +
            " AND td.private_ip IN " +
            "(select private_ip from t_chaos_application_device_tag adt where adt.tag_name in " +
            "<foreach collection='query.tags' item='tag' index='index' open='(' close=')' separator=',' >" +
            "#{tag}" +
            "</foreach>" +
            ")" +
            "</if>" +
            "ORDER BY td.gmt_create DESC" +
            "</script>")
    IPage<DeviceDO> selectK8sPageByKeyAndTags(IPage<DeviceDO> page, @Param("query") CloudDeviceQuery query);



}
