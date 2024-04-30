package com.moz.ates.traffic.admin.common;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.moz.ates.traffic.common.entity.common.MozCmCd;
import com.moz.ates.traffic.common.entity.operator.MozWebOprtr;
import com.moz.ates.traffic.common.entity.payment.MozPlPymntInfo;
import com.moz.ates.traffic.common.entity.police.MozPolInfo;
import com.moz.ates.traffic.common.repository.common.MozCmCdRepository;
import com.moz.ates.traffic.common.repository.operator.MozWebOprtrRepository;
import com.moz.ates.traffic.common.repository.payment.MozPlPymntInfoRepository;
import com.moz.ates.traffic.common.repository.police.MozPolInfoRepository;

/**
 * className : CommonCdServiceImpl
 * author : Mike Lim
 * description : 공통코드 조회 impl
 */
@Service
public class CommonCdServiceImpl implements CommonCdService {

    @Autowired
    private MozCmCdRepository cmCdRepository;
    
    @Autowired
    private MozWebOprtrRepository webOprtrRepository;
    
    @Autowired
    private MozPolInfoRepository polInfoRepository;
    
    @Autowired
    MozPlPymntInfoRepository plPymntInfoRepository;
    
    @Override
    public List<MozCmCd> getCdList(String cdGroupId) {
    	return cmCdRepository.findAllCdList(cdGroupId);
    }

    /**
	 * @brief : 담당자 정보 개수 조회
	 * @details : 담당자 정보 개수 조회
	 * @author : KC.KIM
	 * @date : 2024.02.29
	 * @param : webOprtr
	 * @return :
	 */
	@Override
	public int getOprtrListCnt(MozWebOprtr webOprtr) {
		return webOprtrRepository.countMozWebOprtr(webOprtr);
	}

	/**
	 * @brief : 담당자 정보 조회
	 * @details : 담당자 정보 조회
	 * @author : KC.KIM
	 * @date : 2024.02.29
	 * @param : webOprtr
	 * @return :
	 */
	@Override
	public List<MozWebOprtr> getOprtrList(MozWebOprtr webOprtr) {
		return webOprtrRepository.findAllMozWebOprtr(webOprtr);
	}

	@Override
	public int getPolListCnt(MozPolInfo polInfo) {
		return polInfoRepository.countMozPolInfo(polInfo);
	}

	@Override
	public List<MozPolInfo> getPolList(MozPolInfo polInfo) {
		return polInfoRepository.findAllMozPolInfo(polInfo);
	}

	@Override
	public List<MozPlPymntInfo> getPlacePaymentList() {
		MozPlPymntInfo plPymntInfo = new MozPlPymntInfo();
		return plPymntInfoRepository.findAllPlacePaymentList(plPymntInfo);
	}
}
