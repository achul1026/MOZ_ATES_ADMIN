package com.moz.ates.traffic.admin.api.ites;

import java.util.HashMap;
import java.util.Map;

import com.moz.ates.traffic.common.entity.api.ItesApiAccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.moz.ates.traffic.common.component.api.ites.ItesApiComponent;
import com.moz.ates.traffic.common.entity.api.ApiCommonResponse;
import com.moz.ates.traffic.common.entity.api.ItesApiResponse.ItesApiResponseData;
import com.moz.ates.traffic.common.entity.common.ApiDriverInfoDTO;


@Controller
@RequestMapping(value = "/api/ites")
public class ItesApiController {
	
	@Value("${ites.api.username}")
	String username;
	
	@Value("${ites.api.password}")
	String password;
	
	@Value("${ites.api.url.baseUrl}")
	String baseUrl;
	
	@Autowired
	ItesApiComponent itesApiComponent;

    @PostMapping("/searchDvrLcenId")
    @ResponseBody
    public ResponseEntity<?> searchDvrLcenId(@RequestBody ApiDriverInfoDTO apiDriverInfoDTO) {
    	try {
//    		ItesApiAccessToken itesApiAccessToken = itesApiComponent.getBearerToken(username,password,baseUrl);
//    		if(itesApiAccessToken != null) {
//    			apiDriverInfoDTO.setItesApiAccessToken(itesApiAccessToken);
//    		} else {
//    			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to get token");
//
//    		}
//    		ApiCommonResponse<?> response = itesApiComponent.searchDvrLcenId(apiDriverInfoDTO, baseUrl);
//    		if(response.isSuccess()) {
//    			return ResponseEntity.ok(response.getData());
//    		} else {
//    			return ResponseEntity.ok(null);
//    		}


			ItesApiResponseData itesApiResponseData = new ItesApiResponseData();
			Map<String, String> classDetails = new HashMap<>();

			classDetails.put("C1", "Veículo Pesado - acima de 3500kg, mas não superior a 16000kg");
			classDetails.put("CE", "2000kg 이상");

			// ItesApiResponseData에 데이터 설정
			itesApiResponseData.setApelido("MANACAZE");
			itesApiResponseData.setCategoria("");
			itesApiResponseData.setClasses(classDetails);
			itesApiResponseData.setCodigo(11080112);
			itesApiResponseData.setDataDeNascimento("1996-07-24");
			itesApiResponseData.setDescricaoCategoria("");
			itesApiResponseData.setDocNumber("1105042267830I");
			itesApiResponseData.setEnderesso1("01");
			itesApiResponseData.setEnderesso2("32");
			itesApiResponseData.setEnderesso3("29 DE SETEMBRO");
			itesApiResponseData.setEnderesso4("MAPUTO PROVINCIA");
			itesApiResponseData.setEstado("01");
			itesApiResponseData.setFimValidadeCategoria(null);
			itesApiResponseData.setFimValidadeClasse("2028-06-28");
			itesApiResponseData.setGenero("Masculino");
			itesApiResponseData.setIncioValidadeClasse("2023-04-30");
			itesApiResponseData.setInicioValidadeCategoria(null);
			itesApiResponseData.setNome("ANDRE JOSE");
			itesApiResponseData.setOutrosNomes(null);
			itesApiResponseData.setPaisOrigem("Mocambique");
			itesApiResponseData.setPhone1("845920918");
			itesApiResponseData.setPhone2("845920918");
			itesApiResponseData.setRestricaoCondutor("Nenhuma");
			itesApiResponseData.setRestricaoVeiculo("Nenhuma");
			itesApiResponseData.setTipoDocumento("Bilhete de Identidade");

			// ApiCommonResponse 생성 및 설정
			ApiCommonResponse<ItesApiResponseData> apiResponse = new ApiCommonResponse<>();
			apiResponse.setData(itesApiResponseData);
			apiResponse.setSuccess(true);
			return ResponseEntity.ok(apiResponse.getData());

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
