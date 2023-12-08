package com.moz.ates.traffic.admin.trafficenforcementmng;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class CarVO extends EnfSearchVO{

    private String regDt;
    private String carType;
    private String carDriverName;
    private String carNum;
    private String automotiveUse;//자동차 용도
    private String carClassification; //자동차 구분
    private String competentAuthority; //관할 관청
    private String wtCarIsScrapped; //폐차여부
    private String note; //비고

}
