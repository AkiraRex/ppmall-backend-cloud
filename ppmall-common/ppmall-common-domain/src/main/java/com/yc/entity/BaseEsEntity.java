package com.yc.entity;

import java.util.HashMap;

import org.springframework.data.annotation.Id;

public class BaseEsEntity extends HashMap<String, Object> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -429924571515235368L;

	@Id
	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
