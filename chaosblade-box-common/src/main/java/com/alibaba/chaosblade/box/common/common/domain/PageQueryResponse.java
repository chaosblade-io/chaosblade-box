package com.alibaba.chaosblade.box.common.common.domain;

import lombok.Data;

import java.util.List;

@Data
public class PageQueryResponse<T> {
	
	private List<T> content;
	
	private Long total;
	
	private long pages;
	
	private long pageSize;
	
	private long currentPage;
	
}
