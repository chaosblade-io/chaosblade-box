package com.alibaba.chaosblade.box.dao.model.base;

import com.alibaba.chaosblade.box.dao.model.ApplicationDeviceDO;
import com.alibaba.chaosblade.box.dao.model.DeviceDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 应用设备对象表.
 * 区别于{@link ApplicationDeviceDO}
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString
public class ChaosApplicationDeviceDO extends BaseDO {

	/**
	 * 归属应用
	 */
	private String appName;

	/**
	 * namespace
	 */
	private String namespace;

	/**
	 * 归属分组
	 */
	private String groupName;

	private String deviceName;

	private String privateIp;

	private String publicIp;

	/**
	 * 进程ID,可空
	 */
	private Integer pid;

	/**
	 * 设备类型，deviceType
	 */
	private Integer deviceType;

	/**
	 * 设备连接时间
	 */
	private Long connectTime;

	/**
	 * 最后一次心跳时间
	 */
	private Long lastHealthPingTime;

	/**
	 * 设备configurationId
	 *
	 * @see DeviceDO
	 */
	private String hostConfigurationId;

	/**
	 * 进程id信息,和process或者pod绑定
	 */
	private String configurationId;

	/**
	 * 用户id
	 */
	private String userId;
	/**
	 * 集群ID
	 */
	private String clusterId;

	/**
	 * appId
	 */
	private Long appId;

	/**
	 * 状态
	 */
	private Integer status;

	/**
	 * 是否删除
	 */
	private Boolean isDeleted;

	/**
	 * 资源划分维度
	 */
	private Integer dimension;

	/**
	 * kubernetes namespace
	 */
	private String kubNamespace;

	/**
	 * 操作系统类型
	 */
	private Integer osType;

}
