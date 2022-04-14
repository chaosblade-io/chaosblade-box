package com.alibaba.chaosblade.box.common.infrastructure.constant;


import com.alibaba.chaosblade.box.common.common.util.MiniAppUtils;

/**
 * @author haibin.lhb
 *
 * 
 */
public enum OsTypeEnum {
	
	/**
	 * Linux系统
	 */
	Linux(0),
	/**
	 * Windows系统
	 */
	Windows(1);
	
	public int getType() {
		return type;
	}
	
	private int type;
	
	OsTypeEnum(int type) {
		this.type = type;
	}
	
	public static OsTypeEnum ofAppCode(String appCode) {
		return MiniAppUtils.isWin(appCode) ? Windows : Linux;
	}
	
}
