package com.moz.ates.traffic.admin.common;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.moz.ates.traffic.common.entity.common.MozCmCd;
import com.moz.ates.traffic.common.repository.common.MozCmCdRepository;

/**
 * className : CommonCdServiceImpl
 * author : Mike Lim
 * description : 공통코드 조회 impl
 */
@Service
public class CommonCdServiceImpl implements CommonCdService {

    @Autowired
    private MozCmCdRepository cmCdRepository;

    @Override
    public List<MozCmCd> getCdList(String cdGroupId) {
    	return cmCdRepository.selectCdList(cdGroupId);
    }
}
