package com.moz.ates.traffic.admin.common.enums;

public enum PolSttsCd {
	
	NORMAL("PSC000", "정상"),
	STOP("PSC001", "정지"),
	WAITTING("PSC002", "승인 대기"),
	WITHDRAW("PSC003", "탈퇴")
	;
	
	private String code;
	private String name;
	
	private PolSttsCd(String code, String name) {
		this.code = code;
		this.name = name;
	}
	
	public String getCode() {
		return code;
	}

	public String getName() {
		return name;
	}
	
}
