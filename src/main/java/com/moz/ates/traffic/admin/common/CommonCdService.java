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
 * className : CommonCdService
 * author : Mike Lim
 * description : 공통코드 관련 인터페이스
 */
@Service
public class CommonCdService {
	
	@Autowired
    private MozCmCdRepository cmCdRepository;
    
    @Autowired
    private MozWebOprtrRepository webOprtrRepository;
    
    @Autowired
    private MozPolInfoRepository polInfoRepository;
    
    @Autowired
    MozPlPymntInfoRepository plPymntInfoRepository;

    /**
     * methodName : getCdList
     * author : Mike Lim
     * description : 공통코드 리스트 조회
     * @param cdGroupId
     * @return list
     */
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
	public List<MozWebOprtr> getOprtrList(MozWebOprtr webOprtr) {
		return webOprtrRepository.findAllMozWebOprtr(webOprtr);
	}

	/**
	 * @brief : 경찰관 정보 개수 조회
	 * @details : 경찰관 정보 개수 조회
	 * @author : KC.KIM
	 * @date : 2024.03.05
	 * @param : polInfo
	 * @return :
	 */
	public int getPolListCnt(MozPolInfo polInfo) {
		return polInfoRepository.countMozPolInfo(polInfo);
	}

	/**
	 * @brief : 경찰관 정보 조회
	 * @details : 경찰관 정보 조회
	 * @author : KC.KIM
	 * @date : 2024.03.05
	 * @param : polInfo
	 * @return :
	 */
	public List<MozPolInfo> getPolList(MozPolInfo polInfo) {
		return polInfoRepository.findAllMozPolInfo(polInfo);
	}

	/**
	 * @brief 결제 지역 목록
	 * @author KY.LEE
	 * @date 2024. 5. 31.
	 * @method getPlacePaymentList
	 */
	public List<MozPlPymntInfo> getPlacePaymentList() {
		MozPlPymntInfo plPymntInfo = new MozPlPymntInfo();
		return plPymntInfoRepository.findAllPlacePaymentList(plPymntInfo);
	}

	/**
	  * @Method Name : getDvrLcenTyCdList
	  * @Date : 2024. 5. 9.
	  * @Author : KY.LEE
	  * @Method Brief : 면허 타입 정류
	  * @param tfcEnfId
	  * @return
	  */
	public List<MozCmCd> getCmCdByCdGroupId(String cdId) {
		return cmCdRepository.findAllSubCmcd(cdId);
	}
}
