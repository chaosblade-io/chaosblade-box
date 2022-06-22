package com.alibaba.chaosblade.box.common.common.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.chaosblade.box.common.common.domain.user.ChaosUser;
import lombok.Data;

import java.io.Serializable;

/**
 * @author haibin
 * 
 *
 */
@Data
public class BaseRequest implements Serializable {
	
	private String namespace;
	
	private String requestId;

	private String lang;
	
//	@JSONField(serialize = false, name = "region")
//	private String regionId;
	
	@JSONField(serialize = false)
	private transient ChaosUser user;
	
	public ChaosUser getUser() {
		return user;
	}
	
	public String getUserId() {
		return user != null ? user.getUserId() : null;
	}
	
	public static void copy(BaseRequest from, BaseRequest to) {
		to.setNamespace(from.getNamespace());
		to.setUser(from.getUser());
//		to.setRegionId(from.getRegionId());
		to.setRequestId(from.getRequestId());
		to.setLang(from.getLang());
	}
}
