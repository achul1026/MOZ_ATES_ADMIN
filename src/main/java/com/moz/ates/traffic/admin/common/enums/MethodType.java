package com.moz.ates.traffic.admin.common.enums;

import lombok.Getter;

@Getter
public enum MethodType {

	CREATE("CREATE"),
	READ("READ"),
	UPDATE("UPDATE"),
	DELETE("DELETE")
	;
	
	private String name;
	
	private MethodType(String name) {
		this.name = name;
	}
	
	
	public static String getCodeByName(String name) {
		for(MethodType r : MethodType.values()) {
			if(r.name.equals(name)) {
				return r.name;
			}
		}
		return null;
	}
}
