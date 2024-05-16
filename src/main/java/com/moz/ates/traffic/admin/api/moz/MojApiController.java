package com.moz.ates.traffic.admin.api.moz;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.moz.ates.traffic.common.component.api.moj.MojApiComponent;
import com.moz.ates.traffic.common.entity.api.MojApiAccessToken;
import com.moz.ates.traffic.common.entity.common.ApiDriverInfoDTO;

@Controller
@RequestMapping(value = "/api")
public class MojApiController {
	
	@Value("${moj.api.username}")
	String username;
	
	@Value("${moj.api.password}")
	String password;

	@Value("${moj.api.grantType}")
	String grantType;
	
	@Value("${moj.api.url.accessToken}")
	String getTokenUrl;
	
	@Autowired
	MojApiComponent mojApiComponent;

    @PostMapping("/searchVehicleNo")
    @ResponseBody
    public ResponseEntity<?> searchVehicleNo(@RequestBody ApiDriverInfoDTO apiDriverInfoDTO) {
    	try {
    		MojApiAccessToken mojApiAccessToken = mojApiComponent.getBearerToken(username,password,grantType,getTokenUrl);
    		
    		if(mojApiAccessToken != null) {
    			apiDriverInfoDTO.setApiToken(mojApiAccessToken.getAccessToken());
    		} else {
    			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to get token");
    			
    		}
    		String result  = mojApiComponent.searchVehicleNo(apiDriverInfoDTO);
    		
    		return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
