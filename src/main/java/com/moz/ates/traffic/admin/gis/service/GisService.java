package com.moz.ates.traffic.admin.gis.service;

import com.moz.ates.traffic.common.entity.common.AccidentDomain;
import com.moz.ates.traffic.common.entity.common.EnforcementDomain;

public interface GisService {
	public AccidentDomain getAcMapInfo(String tfcAcdntId);
	public EnforcementDomain getMapInfo(String tfcEnfId);
}
