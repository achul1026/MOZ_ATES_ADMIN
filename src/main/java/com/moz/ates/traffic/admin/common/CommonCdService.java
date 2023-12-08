package com.moz.ates.traffic.admin.common;

import java.util.List;

import com.moz.ates.traffic.common.entity.common.MozCmCd;

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

}
