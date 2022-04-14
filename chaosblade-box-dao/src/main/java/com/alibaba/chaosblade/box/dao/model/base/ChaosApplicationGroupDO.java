package com.alibaba.chaosblade.box.dao.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 应用分组表
 *
 * @author haibin
 *
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString
public class ChaosApplicationGroupDO extends BaseDO {

	private static final long serialVersionUID = -4260038555263063763L;
	/**
	 * 应用分组名
	 */
	private String name;

	/**
	 * 分组名对应的展示名称
	 */
	private String display;

	/**
	 * 应用id
	 */
	private String appName;

	/**
	 * appId
	 */
	private Long appId;

}
