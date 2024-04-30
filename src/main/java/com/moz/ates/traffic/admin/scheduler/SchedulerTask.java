package com.moz.ates.traffic.admin.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.moz.ates.traffic.admin.scheduler.job.fine.FineJobService;
import com.moz.ates.traffic.common.entity.log.MozTfcSystmErrLog;
import com.moz.ates.traffic.common.enums.LogCateCd;
import com.moz.ates.traffic.common.repository.log.MozTfcSystmErrLogRepository;
import com.moz.ates.traffic.common.support.exception.CommonException;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class SchedulerTask {
	
    @Value("${java.schedule:false}")
    private boolean scheduleEnabled;
    
	@Autowired
	FineJobService fineJobService;
	
	@Autowired
	MozTfcSystmErrLogRepository mozTfcSystmErrLogRepository;
	
	/**
	 * @brief : 고지 스케쥴러
	 * @details : 고지 스케쥴러
	 * @author : NK.KIM
	 * @date : 2024.02.23
	 */
//	@Scheduled(cron = "0 */1 * * * *") //테스트용 1분
	@Scheduled(cron = "0 1 0 * * *") // 매일 00:01분에 실행
    public void fineJobScheduler() throws InterruptedException {
		if(scheduleEnabled) {
			try {
				fineJobService.updateFirstNoticeBatch();
				mozTfcSystmErrLogRepository.saveMozTfcSystmErrLog(new MozTfcSystmErrLog(LogCateCd.BATCH,"First Fine Notice Update Success","Y"));
			} catch(CommonException e) {
				mozTfcSystmErrLogRepository.saveMozTfcSystmErrLog(new MozTfcSystmErrLog(LogCateCd.BATCH,"First Fine Notice Update Failed","N"));
			}
			
			try {
				fineJobService.updateSecondNoticeBatch();
				mozTfcSystmErrLogRepository.saveMozTfcSystmErrLog(new MozTfcSystmErrLog(LogCateCd.BATCH,"Second Fine Notice Update Success","Y"));
			} catch(CommonException e) {
				mozTfcSystmErrLogRepository.saveMozTfcSystmErrLog(new MozTfcSystmErrLog(LogCateCd.BATCH,"Second Fine Notice Update Failed","N"));
			}
			log.debug("Activate scheduler");
		} else {
			log.debug("Disable scheduler");
		}
    }
}
