package com.moz.ates.traffic.admin.common.enums;

public enum OprtrSttsCd {
	
	NORMAL("OSC000", "정상"),
	STOP("OSC001", "정지"),
	WAITTING("OSC002", "승인 대기"),
	WITHDRAW("OSC003", "탈퇴")
	;
	
	private String code;
	private String name;
	
	private OprtrSttsCd(String code, String name) {
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
