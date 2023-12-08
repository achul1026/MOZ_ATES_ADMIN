package com.moz.ates.traffic.admin.trafficequipmentmng;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.moz.ates.traffic.admin.common.DataTableVO;
import com.moz.ates.traffic.common.entity.equipment.MozTfcEnfEqpLog;
import com.moz.ates.traffic.common.entity.equipment.MozTfcEnfEqpMaster;
import com.moz.ates.traffic.common.repository.equipment.MozTfcEnfEqpLogRepository;

@Controller
@RequestMapping(value = "/eqp")
public class TrafficEqpController {
	
	@Value("${file.upload.path}")
	private String applPath;
	
    @Autowired
    private TrafficEqpService trafficEqpService;

    /**
     * @brief : 단속장비 리스트 화면
     * @details : 단속장비 리스트 화면
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : finePymntInfo
     * @return : 
     */
    @GetMapping("/mng/list.do")
    public String mngList(Model model, @ModelAttribute MozTfcEnfEqpMaster tfcEnfEqpMaster){
        model.addAttribute("tfcEnfEqpMaster",tfcEnfEqpMaster);
        model.addAttribute("eqpList", trafficEqpService.getEqpList(tfcEnfEqpMaster));
        model.addAttribute("totalCnt", trafficEqpService.getEqpListCnt(tfcEnfEqpMaster));
        return "views/equipmentmng/eqpMngList";
    }

//    /**
//     * @brief : 단속장비 리스트 조회
//     * @details : 단속장비 리스트 조회
//     * @author : KC.KIM
//     * @date : 2023.08.04
//     * @param : finePymntInfo
//     * @return : DataTableVO
//     */
//    @PostMapping(value = "mngListAjax")
//    @ResponseBody
//    public DataTableVO mngListAjax(@ModelAttribute MozTfcEnfEqpMaster tfcEnfEqpMaster){
//
//        return trafficEqpService.getMngListDatatable(tfcEnfEqpMaster);
//    }

    /**
     * @brief : 단속장비 등록 화면
     * @details : 단속장비 등록 화면
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : 
     * @return : 
     */
    @GetMapping("/mng/save.do")
    public String mngRegist(Model model){

        return "views/equipmentmng/eqpMngRegist";
    }

    /**
     * @brief : 단속장비 등록 
     * @details : 단속장비 등록 
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : 
     * @return : 
     */
    @PostMapping("/mng/save.ajax")
    @ResponseBody
    public Map<String,Object> mngRegistAjax(@ModelAttribute MozTfcEnfEqpMaster tfcEnfEqpMaster,
    		@RequestParam("uploadFile") MultipartFile[] imageFile) throws IOException{
        Map<String, Object> result = new HashMap<>();

        int dupliCnt = trafficEqpService.getEqpDupliCnt(tfcEnfEqpMaster);
        String uuid = "";
        String DBfileName = "";
        String fileName = "";
        String projectPath = System.getProperty("user.dir")+applPath;
        File makeFolder = new File(projectPath);
        String default_Path = projectPath;
        String default_Name = "notimage.png";

        
        if(!makeFolder.exists()) {
        	makeFolder.mkdir();
        	System.out.println("폴더 생성 성공");
        }else {
        	System.out.println("해당 폴더가 존재 합니다");
        }
        for(int i=0;i<imageFile.length;i++) {
        	uuid = UUID.randomUUID().toString();
        	fileName = uuid+"_"+imageFile[i].getOriginalFilename();
        	if(i==imageFile.length-1) {
        		DBfileName += fileName;
        	} else {
        		DBfileName += fileName+",";
        	}
            String file_path = projectPath+fileName;        
            File file = new File(file_path);        
            if(!imageFile[i].isEmpty()) {
                FileOutputStream fo = new FileOutputStream(file);
                byte[] fileBytes = imageFile[i].getBytes();
                fo.write(fileBytes);
                fo.close();
                tfcEnfEqpMaster.setTfcEnfEqpImagepath(file_path);
                tfcEnfEqpMaster.setTfcEnfEqpImageorgn(DBfileName);
            } else {
            	tfcEnfEqpMaster.setTfcEnfEqpImagepath(default_Path);
                tfcEnfEqpMaster.setTfcEnfEqpImageorgn(default_Name);
            }
        }

        if(dupliCnt > 0){
            result.put("code", "-1");
            result.put("msg", "중복된 장비번호 입니다.");
        }else{
            try {
                trafficEqpService.registEqp(tfcEnfEqpMaster);
                result.put("code", "1");
            }catch (Exception e){
                result.put("code", "0");
                result.put("msg", "error");
            }
        }
        


        return result;
    }
    
    /**
     * @brief : 단속장비 상세 조회
     * @details : 단속장비 상세 조회
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : tfcEnfEqpId
     * @return : 
     */
    @GetMapping("/mng/detail.do")
    public String mngDetail(Model model, @RequestParam("tfcEnfEqpId")String tfcEnfEqpId){
    	MozTfcEnfEqpMaster tfcEnfEqpMaster = trafficEqpService.getEqpDetail(tfcEnfEqpId);
        model.addAttribute("tfcEnfEqpMaster",tfcEnfEqpMaster);

        return "views/equipmentmng/eqpMngDetail";
    }
    
    /**
     * @brief : 단속장비 수정 화면
     * @details : 단속장비 수정 화면
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : tfcEnfEqpId
     * @return : 
     */
    @GetMapping("/mng/update.do")
    public String mngModify(Model model, @RequestParam("tfcEnfEqpId")String tfcEnfEqpId){

    	MozTfcEnfEqpMaster tfcEnfEqpMaster = trafficEqpService.getEqpDetail(tfcEnfEqpId);
        model.addAttribute("tfcEnfEqpMaster",tfcEnfEqpMaster);

        return "views/equipmentmng/eqpMngModify";
    }
    
    /**
     * @brief : 단속장비 정보 수정
     * @details : 단속장비 정보 수정
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : tfcEnfEqpMaster
     * @return : 
     */
    @PostMapping("/mng/update.ajax")
    @ResponseBody
    public Map<String, Object> mngModifyAjax(@ModelAttribute MozTfcEnfEqpMaster tfcEnfEqpMaster, 
    		@RequestParam("uploadFile") MultipartFile[] imageFile) throws IOException{
        Map<String, Object> result = new HashMap<>();

        if(!imageFile[0].isEmpty()) {
            String uuid = UUID.randomUUID().toString();
            String DBfileName = "";
            String fileName = "";
            String projectPath = System.getProperty("user.dir")+applPath;
            File makeFolder = new File(projectPath);
            String default_Path = projectPath;
            String default_Name = "notimage.png";

            
            if(!makeFolder.exists()) {
            	makeFolder.mkdir();
            	System.out.println("폴더 생성 성공");
            }else {
            	System.out.println("해당 폴더가 존재 합니다");
            }
            for(int i=0;i<imageFile.length;i++) {
            	fileName = uuid+"_"+imageFile[i].getOriginalFilename();
            	if(i==imageFile.length-1) {
            		DBfileName += fileName;
            	} else {
            		DBfileName += fileName+",";
            	}
                String file_path = projectPath+fileName;        
                File file = new File(file_path);        
                if(!imageFile[i].isEmpty()) {
                    FileOutputStream fo = new FileOutputStream(file);
                    byte[] fileBytes = imageFile[i].getBytes();
                    fo.write(fileBytes);
                    fo.close();
                    tfcEnfEqpMaster.setTfcEnfEqpImagepath(file_path);
                    tfcEnfEqpMaster.setTfcEnfEqpImageorgn(DBfileName);
                } else {
                	tfcEnfEqpMaster.setTfcEnfEqpImagepath(default_Path);
                    tfcEnfEqpMaster.setTfcEnfEqpImageorgn(default_Name);
                }
            }
        } else {
        	System.out.println("사진파일 수정x");
        }
        
        try {
            trafficEqpService.updateEqp(tfcEnfEqpMaster);
            result.put("code", "1");
        }catch (Exception e){
            result.put("code", "0");
        }
        
        return result;
    }
    
    /**
     * @brief : 단속장비 이미지 삭제
     * @details : 단속장비 이미지 삭제
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : tfcEnfEqpMaster
     * @return : 
     */
    @PostMapping(value = "/mng/image/delete.ajax")
    public String imageDelete(Model model, @ModelAttribute MozTfcEnfEqpMaster tfcEnfEqpMaster) {
        String default_Name = "notimage.png";
        String default_Path = System.getProperty("user.dir")+applPath;
        tfcEnfEqpMaster.setTfcEnfEqpImageorgn(default_Name);
        tfcEnfEqpMaster.setTfcEnfEqpImagepath(default_Path);
    	trafficEqpService.updateEqpImage(tfcEnfEqpMaster);    	

    	model.addAttribute("tfcEnfEqpMaster", tfcEnfEqpMaster);
    	return "views/equipmentmng/eqpMngModify";
    }

    /**
     * @brief : 단속장비 로그 리스트 화면
     * @details : 단속장비 로그 리스트 화면
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : tfcEnfEqpMaster
     * @return : 
     */
    @GetMapping("/log/list.do")
    public String logList(Model model,@ModelAttribute MozTfcEnfEqpLog tfcEnfEqpLog){

        model.addAttribute("tfcEnfEqpLog", tfcEnfEqpLog);
        model.addAttribute("eqpLogList", trafficEqpService.getEqpLogList(tfcEnfEqpLog));
        model.addAttribute("totalCnt", trafficEqpService.getEqpLogListCnt(tfcEnfEqpLog));
        return "views/equipmentmng/eqpLogList";
    }

    /**
     * @brief : 교통시설물 관리 리스트 화면
     * @details : 교통시설물 관리 리스트 화면
     * @author : KC.KIM
     * @date : 2023.08.17
     * @param : 
     * @return : 
     */
    @RequestMapping(value = "/facility/mng/list.do")
    public String facilityMngList(Model model){
        return "views/equipmentmng/facilityMngList";
        
    }
    
    /**
     * @brief : 교통시설물 관리 로그 리스트 화면
     * @details : 교통시설물 관리 로그 리스트 화면
     * @author : KC.KIM
     * @date : 2023.08.17
     * @param : 
     * @return : 
     */
    @RequestMapping(value = "/facility/log/list.do")
    public String facilityLogList(Model model){
        return "views/equipmentmng/facilityLogList";
    }
}
