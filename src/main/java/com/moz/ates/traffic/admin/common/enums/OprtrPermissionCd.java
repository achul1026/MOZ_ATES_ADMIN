package com.moz.ates.traffic.admin.common.enums;

public enum OprtrPermissionCd {
	
	SUPER_ADMIN("OPC000", "Super Admin"),
	ADMIN_USER("OPC001", "Admin User"),
	OFFICE_OPERATOR("OPC002", "Office Operator"),
	POLICE_OPERATOR("OPC003", "Police Operator"),
	POLICE_OFFICER("OPC004","Police Officer")
	;
	
	private String code;
	private String name;	
	
	private OprtrPermissionCd(String code, String name) {
		this.code = code;
		this.name = name;
	}
	
	public String getCode() {
		return code;
	}

	public String getName() {
		return name;
	}
	
	public static String getCodeByName(String name) {
		for(OprtrPermissionCd r : OprtrPermissionCd.values()) {
			if(r.name.equals(name )) {
				return r.code;
			}
		}
		return null;
	}
	
	public static boolean getCodeByNameValidateSuperAdmin(String name) {
		for(OprtrPermissionCd r : OprtrPermissionCd.values()) {
			if(OprtrPermissionCd.SUPER_ADMIN.name.equals(name)) {
				return false;
			}
			
			if(r.name.equals(name)) {
				return true;
			}
		}
		return false;
	}
}
