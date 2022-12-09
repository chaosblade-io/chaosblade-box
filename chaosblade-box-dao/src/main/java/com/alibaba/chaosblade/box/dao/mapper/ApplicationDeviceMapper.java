package com.alibaba.chaosblade.box.dao.mapper;

import com.alibaba.chaosblade.box.common.infrastructure.util.MybatisMapper;
import com.alibaba.chaosblade.box.dao.model.ApplicationDeviceDO;
import com.alibaba.chaosblade.box.dao.query.ApplicationDeviceQuery;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author haibin
 *
 *
 */
@MybatisMapper
public interface ApplicationDeviceMapper extends BaseMapper<ApplicationDeviceDO> {


    @Select("<script>" +
            "select tmp.* from (" +
            "SELECT t.* FROM t_chaos_application_device t where t.id in ( " +
            "select " +
            "max(ad.id) " +
            "from t_chaos_application_device ad WHERE 1=1 " +
            "<if test='null != query.appId and query.appId != \"\" '>" +
            "AND ad.app_id = #{query.appId} " +
            "</if>" +
            "<if test='null != query.status and query.status != \"\" '>" +
            "AND ad.status = #{query.status} " +
            "</if>" +
            "<if test='null != query.partName and query.partName != \"\" '>" +
            "AND ad.private_ip like #{query.partName} " +
            "</if>" +
            "<if test='null != query.osType' >" +
            "AND ad.os_type = #{query.osType} " +
            "</if>" +
            "<if test='null != query.groups and query.groups.size != 0'>" +
            "AND ad.group_name in " +
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
            " AND ad.configuration_id IN " +
            "(select configuration_id from t_chaos_application_device_tag adt where adt.tag_name in " +
            "<foreach collection='query.tags' item='tag' index='index' open='(' close=')' separator=',' >" +
            "#{tag}" +
            "</foreach>" +
            ")" +
            "</if>" +
            "group by ad.private_ip " +
            ")) tmp " +
            "ORDER BY tmp.gmt_create DESC" +
            "</script>")
    IPage<ApplicationDeviceDO> selectPageByTagsForHost(IPage<ApplicationDeviceDO> page, @Param("query") ApplicationDeviceQuery query);


    @Select("<script>" +
            "select " +
            "ad.* " +
            "from t_chaos_application_device ad WHERE 1=1 " +
            "<if test='null != query.appId and query.appId != \"\" '>" +
            "AND ad.app_id = #{query.appId} " +
            "</if>" +
            "<if test='null != query.status and query.status != \"\" '>" +
            "AND ad.status = #{query.status} " +
            "</if>" +
            "<if test='null != query.partName and query.partName != \"\" '>" +
            "AND ad.private_ip like #{query.partName} " +
            "</if>" +
            "<if test='null != query.groups and query.groups.size != 0'>" +
            "AND ad.group_name in " +
            "<foreach collection='query.groups' item='groupName' index='index' open='(' close=')' separator=',' >" +
            "#{groupName}" +
            "</foreach>" +
            "</if>" +
            "<if test='null != query.kubNamespaces and query.kubNamespaces.size != 0'>" +
            "AND ad.kub_namespace in " +
            "<foreach collection='query.kubNamespaces' item='kubNamespace' index='index' open='(' close=')' separator=',' >" +
            "#{kubNamespace}" +
            "</foreach>" +
            "</if>" +
            "<if test='null != query.dimensions and query.dimensions.size != 0'>" +
            "AND ad.dimension in " +
            "<foreach collection='query.dimensions' item='dimension' index='index' open='(' close=')' separator=',' >" +
            "#{dimension}" +
            "</foreach>" +
            "</if>" +
            "<if test='null != query.tags and query.tags.size != 0'>" +
            " AND ad.configuration_id IN " +
            "(select configuration_id from t_chaos_application_device_tag adt where adt.tag_name in " +
            "<foreach collection='query.tags' item='tag' index='index' open='(' close=')' separator=',' >" +
            "#{tag}" +
            "</foreach>" +
            ")" +
            "</if>" +
            "ORDER BY ad.gmt_create DESC" +
            "</script>")
    IPage<ApplicationDeviceDO> selectPageByTagsForK8s(IPage<ApplicationDeviceDO> page, @Param("query") ApplicationDeviceQuery query);

}
