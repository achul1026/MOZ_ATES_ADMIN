package com.moz.ates.traffic.admin.common.enums;

import lombok.Getter;

@Getter
public enum UrlType {

	SAVE("save.do","SAVE","SAVE_POR"),
	DETAIL("detail.do","DETAIL","DETAIL_POR"),
	UPDATE("update.do","UPDATE","UPDATE_POR")
	;
	
	private String urlPattern;
	private String engName;
	private String porName;
	
	private UrlType(String urlPattern, String engName, String porName) {
		this.urlPattern = urlPattern;
		this.engName = engName;
		this.porName = porName;
	}
	
	
	public static String getCodeByName(String lang,String urlType) {
		for(UrlType r : UrlType.values()) {
			if(r.urlPattern.equals(urlType)) {
				String urlName = r.getEngName();
				if("por".equals(lang)) {
					urlName = r.getPorName();
				}
				return urlName;
			}
		}
		return null;
	}
}
