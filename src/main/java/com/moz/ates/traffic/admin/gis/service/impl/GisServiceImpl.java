package com.moz.ates.traffic.admin.gis.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.moz.ates.traffic.admin.gis.service.GisService;
import com.moz.ates.traffic.common.entity.common.AccidentDomain;
import com.moz.ates.traffic.common.entity.common.EnforcementDomain;
import com.moz.ates.traffic.common.repository.common.GisRepository;
@Service
public class GisServiceImpl implements GisService{
	
	@Autowired
	GisRepository gisRepository;

	@Override
	public EnforcementDomain getMapInfo(String tfcEnfId) {
		EnforcementDomain enfDomain = new EnforcementDomain();
		enfDomain = gisRepository.findOneEnforcementDomainBytfcEnfId(tfcEnfId);
		
		return enfDomain;
	}

	@Override
	public AccidentDomain getAcMapInfo(String tfcAcdntId) {
		AccidentDomain acdntDomain = new AccidentDomain();
		acdntDomain = gisRepository.selectAccidentList(tfcAcdntId);
		
		return acdntDomain;
	}
	
	
}
