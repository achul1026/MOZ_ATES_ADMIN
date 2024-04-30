package com.moz.ates.traffic.admin.common;

import java.util.List;

import com.moz.ates.traffic.common.entity.common.MozCmCd;
import com.moz.ates.traffic.common.entity.operator.MozWebOprtr;
import com.moz.ates.traffic.common.entity.payment.MozPlPymntInfo;
import com.moz.ates.traffic.common.entity.police.MozPolInfo;

/**
 * className : CommonCdService
 * author : Mike Lim
 * description : 공통코드 관련 인터페이스
 */
public interface CommonCdService {

    /**
     * methodName : getCdList
     * author : Mike Lim
     * description : 공통코드 리스트 조회
     * @param cdGroupId
     * @return list
     */
    List<MozCmCd> getCdList(String cdGroupId);

    /**
	 * @brief : 담당자 정보 개수 조회
	 * @details : 담당자 정보 개수 조회
	 * @author : KC.KIM
	 * @date : 2024.02.29
	 * @param : webOprtr
	 * @return :
	 */
	int getOprtrListCnt(MozWebOprtr webOprtr);

	/**
	 * @brief : 담당자 정보 조회
	 * @details : 담당자 정보 조회
	 * @author : KC.KIM
	 * @date : 2024.02.29
	 * @param : webOprtr
	 * @return :
	 */
	List<MozWebOprtr> getOprtrList(MozWebOprtr webOprtr);

	/**
	 * @brief : 경찰관 정보 개수 조회
	 * @details : 경찰관 정보 개수 조회
	 * @author : KC.KIM
	 * @date : 2024.03.05
	 * @param : polInfo
	 * @return :
	 */
	int getPolListCnt(MozPolInfo polInfo);

	/**
	 * @brief : 경찰관 정보 조회
	 * @details : 경찰관 정보 조회
	 * @author : KC.KIM
	 * @date : 2024.03.05
	 * @param : polInfo
	 * @return :
	 */
	List<MozPolInfo> getPolList(MozPolInfo polInfo);

	public List<MozPlPymntInfo> getPlacePaymentList();
}
