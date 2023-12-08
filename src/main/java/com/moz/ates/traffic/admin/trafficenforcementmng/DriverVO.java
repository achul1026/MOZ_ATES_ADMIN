package com.moz.ates.traffic.admin.trafficenforcementmng;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class DriverVO extends EnfSearchVO{
	
	private String name;
	private String phoneNum;
    private String birth;
    private String sex;
    private String driverLicense; //면허번호
    private String email;
    private String address; //주소
    private String nationality; //국적
    private String drivetLicenseDate; //면허취득일
    private String licenseRenewal; //면허갱신횟수
    private String nid; //NID
    private String LicenseCheck;
//    private boolean is_LicenseCheck; //면허상태
    
}
